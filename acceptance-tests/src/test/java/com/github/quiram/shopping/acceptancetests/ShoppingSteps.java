package com.github.quiram.shopping.acceptancetests;

import com.github.quiram.shopping.acceptancetests.entities.Product;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.thucydides.core.annotations.Step;
import org.hamcrest.Matchers;

import java.util.List;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.apache.http.HttpStatus.SC_OK;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertThat;

class ShoppingSteps {
    private static final ContentType CONTENT_TYPE = ContentType.JSON;
    private static final String SHOPFRONT_IP = System.getenv("shopfront_ip");
    private static final String SUT_BASE_URI = format("http://%s:8010/", SHOPFRONT_IP);
    private List<Product> productList;

    @Step
    void shopfront_service_is_ready() {
        await().atMost(3, MINUTES).until(ShoppingSteps::serviceIsReady);
    }

    private static boolean serviceIsReady() {
        final Response response;

        try {
            response = given().contentType(CONTENT_TYPE)
                    .when()
                    .get(SUT_BASE_URI)
                    .thenReturn();
        } catch (Exception e) {
            return false;
        }

        return response.statusCode() == SC_OK;
    }

    @Step
    void user_obtains_the_list_of_products() {
        productList = asList(
                given().contentType(CONTENT_TYPE)
                        .when().get(SUT_BASE_URI + "products")
                        .andReturn().body().as(Product[].class)
        );
    }

    @Step("There are {0} products in the list")
    void product_list_has_size(int size) {
        assertThat(productList, Matchers.hasSize(size));
    }
}
