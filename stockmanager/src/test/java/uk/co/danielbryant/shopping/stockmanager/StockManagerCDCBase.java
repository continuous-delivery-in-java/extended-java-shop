package uk.co.danielbryant.shopping.stockmanager;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import uk.co.danielbryant.shopping.stockmanager.exceptions.StockNotFoundException;
import uk.co.danielbryant.shopping.stockmanager.model.v1.Stock;
import uk.co.danielbryant.shopping.stockmanager.resources.StockResource;
import uk.co.danielbryant.shopping.stockmanager.services.StockService;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
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
            return newStock(productId, 10);
        }

        @Override
        public List<Stock> getStocks() {
            return IntStream.rangeClosed(1, 100).mapToObj(this::newStock).collect(toList());
        }

        private Stock newStock(int i) {
            return newStock(Integer.toString(i), i * 10);
        }

        private Stock newStock(String productId, int amountAvailable) {
            return new Stock(productId, "sku-" + productId, amountAvailable);
        }
    }
}
