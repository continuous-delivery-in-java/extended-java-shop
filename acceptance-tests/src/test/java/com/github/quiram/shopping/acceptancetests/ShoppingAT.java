package com.github.quiram.shopping.acceptancetests;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SerenityRunner.class)
public class ShoppingAT {
    @Steps
    private
    ShoppingSteps steps;

    @Test
    public void numberOfProductsAsExpected() {
        // GIVEN
        steps.shopfront_service_is_ready();

        // WHEN
        steps.user_obtains_the_list_of_products();

        // THEN
        steps.product_list_has_size(5);
    }
}
