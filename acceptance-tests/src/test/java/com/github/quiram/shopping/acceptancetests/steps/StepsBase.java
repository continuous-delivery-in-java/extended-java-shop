package com.github.quiram.shopping.acceptancetests.steps;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.apache.http.HttpStatus.SC_OK;
import static org.awaitility.Awaitility.await;

abstract class StepsBase {
    private static boolean serviceIsReady(String serviceUrl) {
        final Response response;

        try {
            response = given()
                    .when()
                    .get(serviceUrl)
                    .thenReturn();
        } catch (Exception e) {
            return false;
        }

        return response.statusCode() == SC_OK;
    }

    void waitForService(String serviceUrl) {
        await().atMost(3, MINUTES).until(() -> serviceIsReady(serviceUrl));
    }
}
