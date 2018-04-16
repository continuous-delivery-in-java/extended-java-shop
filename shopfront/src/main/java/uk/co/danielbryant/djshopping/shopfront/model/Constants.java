package uk.co.danielbryant.djshopping.shopfront.model;

import java.util.List;

import static java.util.Collections.singletonList;

public class Constants {
    public static final long ADAPTIVE_PRICING_FLAG_ID = 1L;
    public static final String ADAPTIVE_PRICING_COMMAND_KEY = "adaptivePricing";
    public static final List<String> COMMAND_KEYS = singletonList(ADAPTIVE_PRICING_COMMAND_KEY);
}
