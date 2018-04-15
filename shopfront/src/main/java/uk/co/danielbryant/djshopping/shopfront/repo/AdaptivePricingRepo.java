package uk.co.danielbryant.djshopping.shopfront.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uk.co.danielbryant.djshopping.shopfront.services.dto.PriceDTO;

import java.math.BigDecimal;

@Component
public class AdaptivePricingRepo {

    @Value("${adaptivePricingUri}")
    private String adaptivePricingUri;

    @Autowired
    @Qualifier(value = "stdRestTemplate")
    private RestTemplate restTemplate;


    public BigDecimal getPriceFor(String productName) {
        return restTemplate.getForObject(adaptivePricingUri + "/price?productName=" + productName, PriceDTO.class).getPrice();
    }
}
