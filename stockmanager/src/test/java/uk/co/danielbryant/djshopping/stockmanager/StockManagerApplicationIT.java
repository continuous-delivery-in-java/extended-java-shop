package uk.co.danielbryant.djshopping.stockmanager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.danielbryant.djshopping.stockmanager.model.Stock;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = StockManagerApplication.class)
public class StockManagerApplicationIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void listOfStocksProvidesFiveElements() {
        final List<Stock> stocks = getStocks();
        assertThat(stocks, hasSize(5));
    }

    @Test
    public void skuForFirstStockIsAsExpected() {
        final Stock firstStock = getStocks().get(0);
        assertThat(firstStock.getSku(), is("12345678"));
    }

    private List<Stock> getStocks() {
        return asList(restTemplate.getForObject("/stocks", Stock[].class));
    }
}
