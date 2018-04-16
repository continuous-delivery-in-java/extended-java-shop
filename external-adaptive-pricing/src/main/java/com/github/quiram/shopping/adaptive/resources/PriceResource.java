package com.github.quiram.shopping.adaptive.resources;

import com.github.quiram.shopping.adaptive.model.Price;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static com.github.quiram.utils.ArgumentChecks.ensureNotBlank;
import static com.github.quiram.utils.Random.randomDouble;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/price")
public class PriceResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(PriceResource.class);

    @RequestMapping(method = GET)
    public Price getPrice(@RequestParam(name = "productName") String productName) {
        LOGGER.info("getPriceFor '{}'", productName);
        ensureNotBlank(productName, "productName");
        return new Price(new BigDecimal(Double.toString(randomDouble(100, 2))));
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public void handleMissingParameter(IllegalArgumentException e) {
    }


}
