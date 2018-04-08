package uk.co.danielbryant.djshopping.stockmanager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.danielbryant.djshopping.stockmanager.model.Stock;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = StockManagerApplication.class)
public class StockManagerApplicationIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void listOfStocksProvidesFiveElements() {
        final List<Stock> stocks = asList(get("/stocks", Stock[].class));
        assertThat(stocks, hasSize(5));
    }

    @Test
    public void canGetSpecificStock() {
        final Stock stock = get("/stocks/1", Stock.class);
        assertThat(stock.getSku(), is("12345678"));
        assertThat(stock.getAmountAvailable(), is(5));
    }

    @Test
    public void canHandleNotFoundStock() {
        final ResponseEntity<Stock> response = rawGet("/stocks/99999999", Stock.class);
        assertThat(response.getStatusCode(), is(NOT_FOUND));
    }

    private <T> T get(String path, Class<T> responseType) {
        return restTemplate.getForObject(path, responseType);
    }

    private <T> ResponseEntity<T> rawGet(String path, Class<T> responseType) {
        return restTemplate.exchange(path, GET, null, responseType);
    }
}
