package com.github.quiram.shopping.featureflags;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.quiram.shopping.featureflags.model.Flag;
import com.github.quiram.shopping.featureflags.repositories.FlagRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.github.quiram.utils.Random.*;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = FeatureFlagsApplication.class)
public class FeatureFlagsApplicationCT {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FlagRepository flagRepository;

    @Before
    public void dataSetup() {
        flagRepository.save(new Flag(null, "offers", 20, true));
    }

    @Test
    public void getAllFlags() {
        final List<Flag> flags = asList(get("/flags", Flag[].class));
        assertTrue(flags.size() > 0);
    }

    @Test
    public void getSpecificFlag() {
        final Flag flag = get("/flags/1", Flag.class);
        assertThat(flag.getFlagId(), is(1L));
        assertThat(flag.getName(), is("offers"));
        assertThat(flag.getPortionIn(), is(20));
        assertThat(flag.isSticky(), is(true));
    }

    @Test
    public void get404ForInexistentFlag() {
        final ResponseEntity<Flag> response = rawGet("/flags/9999", Flag.class);
        assertThat(response.getStatusCode(), is(NOT_FOUND));
    }

    @Test
    public void canCreateAndRetrieveFlag() {
        final long flagCount = flagRepository.count();

        final Flag newFlag = new Flag(null, randomString(), 20, true);
        final ResponseEntity<String> response = rawPost("/flags", newFlag, String.class);
        assertThat(flagRepository.count(), is(flagCount + 1));
        assertThat(response.getStatusCode(), is(CREATED));
        assertTrue(response.getHeaders().containsKey(LOCATION));
        final List<String> locations = response.getHeaders().get(LOCATION);
        assertThat(locations, hasSize(1));
        final String flagLocator = locations.get(0);

        final Flag savedFlag = get(flagLocator, Flag.class);
        assertThat(savedFlag.getName(), is(newFlag.getName()));
        assertThat(savedFlag.getPortionIn(), is(newFlag.getPortionIn()));
        assertNotNull(savedFlag.getFlagId());
        assertTrue(savedFlag.isSticky());
    }

    @Test
    public void canCreateFlagWithoutNewStickyField() {
        final long flagCount = flagRepository.count();

        final SmallFlag smallFlag = new SmallFlag(randomString(), 20);
        final ResponseEntity<String> response = rawPost("/flags", smallFlag, String.class);
        assertThat(response.getStatusCode(), is(CREATED));
        assertTrue(response.getHeaders().containsKey(LOCATION));
        assertThat(flagRepository.count(), is(flagCount + 1));
        final List<String> locations = response.getHeaders().get(LOCATION);
        assertThat(locations, hasSize(1));
        final String flagLocator = locations.get(0);

        final Flag savedFlag = get(flagLocator, Flag.class);
        assertThat(savedFlag.getName(), is(smallFlag.getName()));
        assertThat(savedFlag.getPortionIn(), is(smallFlag.getPortionIn()));
        assertNotNull(savedFlag.getFlagId());
        assertFalse(savedFlag.isSticky());
    }

    @Test
    public void cannotCreateFlagIndicatingTheId() {
        final ResponseEntity<String> response = rawPost("/flags", new Flag(randomLong(), randomString(), 20, false), String.class);

        assertThat(response.getStatusCode(), is(BAD_REQUEST));
    }

    @Test
    public void cannotCreateFlagWithNameThatAlreadyExists() {
        final String name = randomString();
        flagRepository.save(new Flag(null, name, 20, false));

        final ResponseEntity<String> response = rawPost("/flags", new Flag(null, name, 35, false), String.class);

        assertThat(response.getStatusCode(), is(BAD_REQUEST));
    }

    @Test
    public void canDeleteFlagThatExists() {
        final Long flagId = flagRepository.save(new Flag(null, randomString(), 20, false)).getFlagId();
        final long flagCount = flagRepository.count();

        final ResponseEntity<String> response = rawDelete("/flags/" + flagId, String.class);
        assertThat(response.getStatusCode(), is(OK));
        assertThat(flagRepository.count(), is(flagCount - 1));
    }

    @Test
    public void canUpdateFlagThatExists() {
        final Long flagId = flagRepository.save(new Flag(null, randomString(), 20, false)).getFlagId();

        final Flag newFlag = new Flag(flagId, randomString(), randomInt(100), true);
        final ResponseEntity<String> response = rawPut("/flags/" + flagId, newFlag);
        assertThat(response.getStatusCode(), is(OK));

        final Flag actualFlag = get("/flags/" + flagId, Flag.class);
        assertThat(actualFlag, is(newFlag));
    }

    @Test
    public void flagIdInPathOverridesIdInBody() {
        final Long flagId = flagRepository.save(new Flag(null, randomString(), 20, true)).getFlagId();

        final Flag updatedFlag = new Flag(randomLong(), randomString(), randomInt(100), false);
        rawPut("/flags/" + flagId, updatedFlag);
        final Flag actualFlag = get("/flags/" + flagId, Flag.class);
        assertThat(actualFlag.getFlagId(), is(flagId));
        assertThat(actualFlag.getName(), is(updatedFlag.getName()));
        assertThat(actualFlag.getPortionIn(), is(updatedFlag.getPortionIn()));
        assertThat(actualFlag.isSticky(), is(updatedFlag.isSticky()));
    }

    private <T> T get(String path, Class<T> responseType) {
        return restTemplate.getForObject(path, responseType);
    }

    private <T> ResponseEntity<T> rawGet(String path, Class<T> responseType) {
        return restTemplate.exchange(path, GET, null, responseType);
    }

    private <I, O> ResponseEntity<O> rawPost(String path, I requestBody, Class<O> responseType) {
        return restTemplate.exchange(path, POST, new HttpEntity<>(requestBody), responseType);
    }

    private <T> ResponseEntity<T> rawDelete(String path, Class<T> responseType) {
        return restTemplate.exchange(path, DELETE, null, responseType);
    }

    private <T> ResponseEntity<String> rawPut(String path, T requestBody) {
        return restTemplate.exchange(path, PUT, new HttpEntity<>(requestBody), String.class);
    }

    private class SmallFlag {
        @JsonProperty
        private String name;
        @JsonProperty
        private int portionIn;

        private SmallFlag(String name, int portionIn) {
            this.name = name;
            this.portionIn = portionIn;
        }

        public String getName() {
            return name;
        }

        public int getPortionIn() {
            return portionIn;
        }
    }

}
