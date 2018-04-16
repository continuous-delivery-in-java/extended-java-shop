package com.github.quiram.shopping.featureflags;

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

import static com.amarinperez.utils.Random.randomLong;
import static com.amarinperez.utils.Random.randomString;
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
        flagRepository.save(new Flag(null, "offers", 20));
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
    }

    @Test
    public void get404ForInexistentFlag() {
        final ResponseEntity<Flag> response = rawGet("/flags/9999", Flag.class);
        assertThat(response.getStatusCode(), is(NOT_FOUND));
    }

    @Test
    public void canCreateAndRetrieveFlag() {
        final long flagCount = flagRepository.count();

        final Flag newFlag = new Flag(null, randomString(), 20);
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
    }

    @Test
    public void cannotCreateFlagIndicatingTheId() {
        final ResponseEntity<String> response = rawPost("/flags", new Flag(randomLong(), randomString(), 20), String.class);

        assertThat(response.getStatusCode(), is(BAD_REQUEST));
    }

    @Test
    public void cannotCreateFlagWithNameThatAlreadyExists() {
        final String name = randomString();
        flagRepository.save(new Flag(null, name, 20));

        final ResponseEntity<String> response = rawPost("/flags", new Flag(null, name, 35), String.class);

        assertThat(response.getStatusCode(), is(BAD_REQUEST));
    }

    @Test
    public void canDeleteFlagThatExists() {
        final Long flagId = flagRepository.save(new Flag(null, randomString(), 20)).getFlagId();
        final long flagCount = flagRepository.count();

        final ResponseEntity<String> response = rawDelete("/flags/" + flagId, String.class);
        assertThat(response.getStatusCode(), is(OK));
        assertThat(flagRepository.count(), is(flagCount - 1));
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
}
