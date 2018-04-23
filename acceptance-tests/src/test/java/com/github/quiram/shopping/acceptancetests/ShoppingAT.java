package com.github.quiram.shopping.acceptancetests;

import com.github.quiram.shopping.acceptancetests.steps.FeatureFlagsSteps;
import com.github.quiram.shopping.acceptancetests.steps.ShopfrontSteps;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SerenityRunner.class)
public class ShoppingAT {
    @Steps
    private ShopfrontSteps shopfrontSteps;

    @Steps
    private FeatureFlagsSteps featureFlagsSteps;

    @Test
    public void numberOfProductsAsExpected() {
        // GIVEN
        shopfrontSteps.shopfront_service_is_ready();

        // WHEN
        shopfrontSteps.user_obtains_the_list_of_products();

        // THEN
        shopfrontSteps.product_list_has_size(5);
    }

    @Test
    public void disableAdaptivePricingFeature() {
        // GIVEN
        featureFlagsSteps.feature_flags_service_is_ready();

        // AND
        shopfrontSteps.shopfront_service_is_ready();

        // WHEN
        featureFlagsSteps.admin_sets_the_adaptive_pricing_feature_flag_to(0);

        // AND
        shopfrontSteps.check_all_prices();

        // AND
        shopfrontSteps.check_all_prices_again();

        // THEN
        shopfrontSteps.prices_have_not_changed();
    }
}
