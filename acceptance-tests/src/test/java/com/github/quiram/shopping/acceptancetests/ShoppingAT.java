package com.github.quiram.shopping.acceptancetests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.apache.http.HttpStatus.SC_OK;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.core.Is.is;

public class ShoppingAT {
    private static final ContentType CONTENT_TYPE = ContentType.JSON;
    private static final String SUT_BASE_URI = "http://localhost:8010/";

    @BeforeClass
    public static void checkServiceIsUp() {
        await().atMost(2, MINUTES).until(ShoppingAT::serviceIsReady);
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

    @Test
    public void correctNumberOfProductsReturned() {
        given().contentType(CONTENT_TYPE)
                .when()
                .get(SUT_BASE_URI + "products")
                .then()
                .body("size()", is(5));
    }

    @Test
    public void productOneHasCorrectProductInfo() {
        given().contentType(CONTENT_TYPE)
                .when()
                .get(SUT_BASE_URI + "products")
                .then()
                .body("[0].id", is("1"))
                .body("[0].sku", is("12345678"));
    }

    @Test
    public void productOneHasCorrectStockInfo() {
        given()
                .contentType(CONTENT_TYPE)
                .when()
                .get(SUT_BASE_URI + "products")
                .then()
                .body("[0].amountAvailable", is(5));
    }
}
