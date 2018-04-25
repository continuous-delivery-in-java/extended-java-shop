package com.github.quiram.shopping.acceptancetests.steps;

import com.github.quiram.shopping.acceptancetests.pages.ShopfrontHomePage;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Step;

import java.util.LinkedList;
import java.util.List;

import static com.github.quiram.utils.Collections.transpose;
import static net.serenitybdd.core.Serenity.sessionVariableCalled;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

public class ShopfrontSteps extends StepsBase {
    private static final String PRICES_COLLECTION_KEY = "prices_collection";
    private List<String> productNames;

    @SuppressWarnings("unused")
    private ShopfrontHomePage page;

    @Step
    public void shopfront_service_is_ready() {
        page.load();
    }

    @Step
    public void user_obtains_the_list_of_products() {
        productNames = page.getProductNames();
    }

    @Step("There are {0} products in the list")
    public void product_list_has_size(int size) {
        assertThat(productNames, hasSize(size));
    }

    @Step("Product with name '{0} is in the list")
    public void includes_product_name(String name) {
        assertThat(productNames, hasItem(name));
    }

    @Step
    public void check_all_prices() {
        checkPrices();
    }

    @Step
    public void check_all_prices_again() {
        checkPrices();
    }

    @Step
    public void prices_have_not_changed() {
        final long numberOfDifferentSetsOfPrices = getPricesCollection().stream().distinct().count();
        assertThat(numberOfDifferentSetsOfPrices, is(1L));
    }

    @Step
    public void all_prices_have_changed() {
        List<List<String>> pricesLists = transpose(getPricesCollection());
        pricesLists.forEach(pricesList -> {
            final long numberOfDifferentSetsOfPrices = pricesList.stream().distinct().count();
            assertThat(numberOfDifferentSetsOfPrices, is(2L));
        });
    }

    @SuppressWarnings("unchecked")
    private List<List<String>> getPricesCollection() {
        List<List<String>> pricesCollection = (List<List<String>>) sessionVariableCalled(PRICES_COLLECTION_KEY);
        return pricesCollection == null ? new LinkedList<>() : pricesCollection;
    }

    private void checkPrices() {
        page.load();
        addToPrices(page.getPrices());
    }

    private void addToPrices(List<String> prices) {
        final List<List<String>> pricesCollection = getPricesCollection();
        pricesCollection.add(prices);
        Serenity.setSessionVariable(PRICES_COLLECTION_KEY).to(pricesCollection);
    }
}
