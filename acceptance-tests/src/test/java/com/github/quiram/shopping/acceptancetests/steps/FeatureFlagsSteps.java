package com.github.quiram.shopping.acceptancetests.steps;

import com.github.quiram.shopping.acceptancetests.entities.Flag;
import net.thucydides.core.annotations.Step;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static java.lang.String.format;

public class FeatureFlagsSteps extends StepsBase {
    private static final String FEATURE_FLAGS_IP = System.getenv("feature_flags_ip");
    private static final String FEATURE_FLAGS_URL = format("http://%s:8040/flags/", FEATURE_FLAGS_IP);

    @Step
    public void feature_flags_service_is_ready() {
        waitForService(FEATURE_FLAGS_URL);
    }

    @Step("Admin sets the adaptive pricing feature flag to {0}%")
    public void admin_sets_the_adaptive_pricing_feature_flag_to(int portionIn) {
        final Flag currentFlag = given().contentType(JSON).get(FEATURE_FLAGS_URL + "1").body().as(Flag.class);
        final Flag newFlag = new Flag(currentFlag.getFlagId(), currentFlag.getName(), portionIn);

        given()
                .contentType(JSON)
                .body(newFlag)
                .when()
                .put(FEATURE_FLAGS_URL + "1")
                .thenReturn();
    }
}
