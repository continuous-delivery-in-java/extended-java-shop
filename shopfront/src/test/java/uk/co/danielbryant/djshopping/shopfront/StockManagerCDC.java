package uk.co.danielbryant.djshopping.shopfront;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import uk.co.danielbryant.djshopping.shopfront.repo.FeatureFlagsRepo;
import uk.co.danielbryant.djshopping.shopfront.repo.StockRepo;
import uk.co.danielbryant.djshopping.shopfront.services.dto.StockDTO;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureStubRunner(ids = {"uk.co.danielbryant.djshopping:shopfront:+:stubs:8030"}, workOffline = true)
public class StockManagerCDC {
    @Autowired
    private StockRepo stockRepo;

    @Test
    public void stockCanBeReadFromProducerResponse() {
        final StockDTO expected = new StockDTO("123", "sku-123", 10);
        final StockDTO actual = stockRepo.getStockDTO("123");
        assertEquals(expected, actual);
    }
}
