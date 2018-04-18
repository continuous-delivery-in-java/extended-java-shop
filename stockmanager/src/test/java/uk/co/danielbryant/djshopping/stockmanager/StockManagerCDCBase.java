package uk.co.danielbryant.djshopping.stockmanager;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import uk.co.danielbryant.djshopping.stockmanager.exceptions.StockNotFoundException;
import uk.co.danielbryant.djshopping.stockmanager.model.Stock;
import uk.co.danielbryant.djshopping.stockmanager.resources.StockResource;
import uk.co.danielbryant.djshopping.stockmanager.services.StockService;

import static org.mockito.MockitoAnnotations.initMocks;

public class StockManagerCDCBase {

    @Before
    public void setup() {
        initMocks(this);
        RestAssuredMockMvc.standaloneSetup(new StockResource(new FakeStockService()));
    }

    private class FakeStockService extends StockService {
        @Override
        public Stock getStock(String productId) throws StockNotFoundException {
            return new Stock(productId, "sku-" + productId, 10);
        }
    }
}
