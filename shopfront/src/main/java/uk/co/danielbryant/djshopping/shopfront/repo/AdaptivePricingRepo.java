package uk.co.danielbryant.djshopping.shopfront.repo;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uk.co.danielbryant.djshopping.shopfront.services.dto.PriceDTO;

import java.math.BigDecimal;
import java.util.Optional;

import static uk.co.danielbryant.djshopping.shopfront.model.Constants.ADAPTIVE_PRICING_COMMAND_KEY;

@Component
public class AdaptivePricingRepo {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdaptivePricingRepo.class);

    @Value("${adaptivePricingUri}")
    private String adaptivePricingUri;

    @Autowired
    @Qualifier(value = "stdRestTemplate")
    private RestTemplate restTemplate;

    @HystrixCommand(commandKey = ADAPTIVE_PRICING_COMMAND_KEY,
            fallbackMethod = "getPriceFallback")
    public Optional<BigDecimal> getPriceFor(String productName) {
        return Optional.of(restTemplate.getForObject(adaptivePricingUri + "/price?productName=" + productName, PriceDTO.class).getPrice());
    }

    public Optional<BigDecimal> getPriceFallback(String productName) {
        LOGGER.info("FALLBACK used when contacting Adaptive Pricing Service for {}", productName);
        return Optional.empty();
    }
}
