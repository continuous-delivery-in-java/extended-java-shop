package com.github.quiram.shopping.adaptive.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Random;

import static com.amarinperez.utils.ArgumentChecks.ensureNotBlank;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/price")
public class PriceResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(PriceResource.class);

    @RequestMapping(method = GET)
    public BigDecimal getPrice(@RequestParam(name = "productName") String productName) {
        LOGGER.info("getPriceFor '{}'", productName);
        ensureNotBlank(productName, "productName");
        double priceInPence = new Random().nextInt(10000);
        Double priceInPounds = priceInPence / 100;
        return new BigDecimal(priceInPounds.toString());
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public void handleMissingParameter(IllegalArgumentException e) {
    }


}
