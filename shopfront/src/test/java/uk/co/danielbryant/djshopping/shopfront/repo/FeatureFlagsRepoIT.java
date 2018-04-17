package uk.co.danielbryant.djshopping.shopfront.repo;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import uk.co.danielbryant.djshopping.shopfront.services.dto.FlagDTO;

import java.util.Optional;

import static com.github.quiram.utils.Random.randomLong;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.junit.rules.Timeout.seconds;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
public class FeatureFlagsRepoIT {
    @Rule
    public Timeout globalTimeout = seconds(3); // 3 seconds max per method tested

    @Rule
    public WireMockRule mockFeatureFlagsRepo = new WireMockRule(options().port(8040), false);

    @Autowired
    private FeatureFlagsRepo featureFlagsRepo;
    private long flagId;

    @Before
    public void setup() {
        flagId = randomLong();
    }

    @Test
    public void canGetFlagThatExists() {
        setResponse(aResponse()
                .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                .withBody("{\"flagId\":" + flagId + ", \"name\":\"a-flag\", \"portionIn\":100}")
        );

        final Optional<FlagDTO> flag = getFlag();
        assertTrue(flag.isPresent());
        assertThat(flag.get().getName(), is("a-flag"));
    }

    @Test
    public void returnsEmptyForFlagDoesNotExist() {
        setResponse(aResponse().withStatus(404));

        final Optional<FlagDTO> flag = getFlag();
        assertFalse(flag.isPresent());
    }

    @Test
    public void returnsEmptyWhenFlagServiceFails() {
        setResponse(aResponse().withStatus(500));

        final Optional<FlagDTO> flag = getFlag();
        assertFalse(flag.isPresent());
    }

    @Test
    public void returnsEmptyWhenRequestTimesOut() {
        // Client should timeout after 1 second, see configuration below
        setResponse(aResponse().withFixedDelay(30 * 1000));

        final Optional<FlagDTO> flag = getFlag();
        assertFalse(flag.isPresent());
    }

    @Test
    public void returnsEmptyWithEmptyBodyInResponse() {
        setResponse(aResponse()
                .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                .withBody("")
        );

        final Optional<FlagDTO> flag = getFlag();
        assertFalse(flag.isPresent());
    }

    @Test
    public void returnsEmptyWithRubbishResponse() {
        setResponse(aResponse()
                .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                .withBody("aldshfpaeha;oihfoasd;bg'alkr")
        );

        final Optional<FlagDTO> flag = getFlag();
        assertFalse(flag.isPresent());
    }

    private void setResponse(ResponseDefinitionBuilder response) {
        mockFeatureFlagsRepo.stubFor(WireMock
                .get(urlEqualTo("/flags/" + flagId))
                .willReturn(response));
    }

    private Optional<FlagDTO> getFlag() {
        return featureFlagsRepo.getFlag(flagId);
    }

    @Configuration
    public static class MyConfiguration {
        @Bean
        @Qualifier(value = "stdRestTemplate")
        public RestTemplate restTemplate() {
            HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
            httpRequestFactory.setConnectionRequestTimeout(1000);
            httpRequestFactory.setConnectTimeout(1000);
            httpRequestFactory.setReadTimeout(1000);
            return new RestTemplate(httpRequestFactory);
        }

        @Bean
        public FeatureFlagsRepo featureFlagsRepo(RestTemplate restTemplate) {
            return new FeatureFlagsRepo("http://localhost:8040", restTemplate);
        }
    }
}