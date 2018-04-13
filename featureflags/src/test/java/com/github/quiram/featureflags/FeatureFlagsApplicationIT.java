package com.github.quiram.featureflags;

import com.github.quiram.featureflags.model.Flag;
import com.github.quiram.featureflags.repositories.FlagRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = FeatureFlagsApplication.class)
public class FeatureFlagsApplicationIT {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FlagRepository flagRepository;

    @Before
    public void dataSetup() {
        flagRepository.save(new Flag("1", "offers", 20));
    }

    @Test
    public void getAllFlags() {
        final List<Flag> flags = asList(get("/flags", Flag[].class));
        assertThat(flags, hasSize(1));
    }

    @Test
    public void getSpecificFlag() {
        final Flag flag = get("/flags/1", Flag.class);
        assertThat(flag.getFlagId(), is("1"));
        assertThat(flag.getName(), is("offers"));
        assertThat(flag.getPortionIn(), is(20));
    }

    @Test
    public void get404ForInexistentFlag() {
        final ResponseEntity<Flag> response = rawGet("/flags/9999", Flag.class);
        assertThat(response.getStatusCode(), is(NOT_FOUND));
    }

    private <T> T get(String path, Class<T> responseType) {
        return restTemplate.getForObject(path, responseType);
    }

    private <T> ResponseEntity<T> rawGet(String path, Class<T> responseType) {
        return restTemplate.exchange(path, GET, null, responseType);
    }

}
