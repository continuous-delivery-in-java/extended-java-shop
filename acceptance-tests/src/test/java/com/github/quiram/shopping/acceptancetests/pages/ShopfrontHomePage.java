package com.github.quiram.shopping.acceptancetests.pages;

import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static com.github.quiram.utils.Collections.map;
import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.awaitility.Awaitility.await;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleContains;

public class ShopfrontHomePage extends PageObject {
    private static final String SHOPFRONT_IP = System.getenv("shopfront_ip");
    private static final String SHOPFRONT_URL = format("http://%s:8010/", SHOPFRONT_IP);

    @FindBy(xpath = "//*[@id=\"product-table\"]/tbody/tr[*]/td[3]")
    private List<WebElement> productNames;

    @FindBy(xpath = "//*[@id=\"product-table\"]/tbody/tr[*]/td[5]")
    private List<WebElement> productPrices;

    public void load() {
        await().atMost(3, MINUTES).until(this::pageIsReady);
    }

    private boolean pageIsReady() {
        try {
            openAt(SHOPFRONT_URL);
            waitFor(titleContains("Java Shopfront"));
            return true;
        } catch (TimeoutException | UnsupportedOperationException e) {
            return false;
        }
    }


    public List<String> getProductNames() {
        return map(productNames, WebElement::getText);
    }

    public List<String> getPrices() {
        return map(productPrices, WebElement::getText);
    }
}
