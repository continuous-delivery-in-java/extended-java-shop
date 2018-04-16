package com.github.quiram.shopping.adaptive;

import com.github.quiram.shopping.adaptive.model.Price;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = AdaptivePricingApplication.class)
public class AdaptivePricingApplicationCT {
    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void getAllFlags() {
        final Price price = restTemplate.getForObject("/price?productName=lala", Price.class);
        assertNotNull(price);
        assertNotNull(price.getPrice());
    }
}
