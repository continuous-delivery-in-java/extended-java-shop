package uk.co.danielbryant.djshopping.shopfront;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.danielbryant.djshopping.shopfront.repo.StockRepo;
import uk.co.danielbryant.djshopping.shopfront.services.dto.StockDTO;

import java.util.Map;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureStubRunner(ids = {"uk.co.danielbryant.djshopping:stockmanager:+:stubs:8030"}, workOffline = true)
@DirtiesContext
public class StockManagerCDC {
    @Autowired
    private StockRepo stockRepo;

    @Test
    public void canReadResponseWithIndividualStock() {
        final StockDTO expected = new StockDTO("123", "sku-123", 10);
        final StockDTO actual = stockRepo.getStockDTO("123");
        assertEquals(expected, actual);
    }

    @Test
    public void canReadResponseWithAllStock() {
        final Map<String, StockDTO> stockDTOs = stockRepo.getStockDTOs();
        assertThat(stockDTOs.values(), hasSize(3));
        assertThat(stockDTOs.keySet(), hasItems("1", "2", "3"));
    }
}
