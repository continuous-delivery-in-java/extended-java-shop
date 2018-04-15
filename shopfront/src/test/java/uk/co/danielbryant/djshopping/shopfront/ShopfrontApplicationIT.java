package uk.co.danielbryant.djshopping.shopfront;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.danielbryant.djshopping.shopfront.model.Product;

import java.math.BigDecimal;
import java.util.List;

import static com.amarinperez.utils.Random.randomInt;
import static com.amarinperez.utils.Random.randomString;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.lang.Math.abs;
import static java.util.Arrays.asList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_HTML;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;
import static uk.co.danielbryant.djshopping.shopfront.model.Constants.ADAPTIVE_PRICING_FLAG_ID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ShopfrontApplication.class)
public class ShopfrontApplicationIT {
    @Autowired
    private TestRestTemplate restTemplate;

    @Rule
    public WireMockRule mockProductRepo = new WireMockRule(8020);

    @Rule
    public WireMockRule mockStockRepo = new WireMockRule(8030);

    @Rule
    public WireMockRule mockFeatureFlagsRepo = new WireMockRule(8040);

    @Rule
    public WireMockRule mockAdaptivePricingRepo = new WireMockRule(8050);

    private final String description1 = randomString();
    private final int amount1 = abs(randomInt());
    private final String amountString1 = "" + amount1;

    @Before
    public void setup() {
        mockProductRepo.stubFor(WireMock
                .get(urlEqualTo("/products"))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withBody("[{\"id\":\"1\",\"name\":\"Object 1\",\"description\":\"" + description1 + "\",\"price\":1.20},{\"id\":\"2\"," +
                                "\"name\":\"Object 2\",\"description\":\"The second object\",\"price\":4.10}]")
                ));

        mockStockRepo.stubFor(WireMock
                .get(urlEqualTo("/stocks"))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withBody("[{\"productId\":\"1\",\"sku\":\"sku-1\",\"amountAvailable\":" + amountString1 + "},{\"productId\":\"2\"," +
                                "\"sku\":\"sku-2\",\"amountAvailable\":2}]")
                ));

        mockFeatureFlagsRepo.stubFor(WireMock
                .get(urlEqualTo("/flags/" + ADAPTIVE_PRICING_FLAG_ID))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withBody("{\"flagId\":" + ADAPTIVE_PRICING_FLAG_ID + ", \"name\":\"adaptive-pricing\", \"portionIn\":100}")
                ));

        mockAdaptivePricingRepo.stubFor(WireMock
                .get(urlPathEqualTo("/price"))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withBody("{\"price\":12.34}")
                ));
    }

    @Test
    public void getHomePage() {
        final ResponseEntity<String> response = rawGet("/", String.class);

        assertEquals(OK, response.getStatusCode());
        final String contentType = response.getHeaders().get(CONTENT_TYPE).get(0);
        assertThat(contentType, containsString(TEXT_HTML));
        final String body = response.getBody();
        assertThat(body, allOf(containsString(description1), containsString(amountString1)));
    }

    @Test
    public void getProducts() {
        final List<Product> products = asList(get("/products", Product[].class));
        assertThat(products, hasSize(2));
        final Product product = products.get(0);
        assertThat(product.getDescription(), is(description1));
        assertThat(product.getAmountAvailable(), is(amount1));
        assertThat(product.getPrice(), is(new BigDecimal("12.34")));
    }

    private <T> T get(String path, Class<T> responseType) {
        return restTemplate.getForObject(path, responseType);
    }

    private <T> ResponseEntity<T> rawGet(String path, Class<T> responseType) {
        return restTemplate.exchange(path, GET, null, responseType);
    }

}
