package uk.co.danielbryant.shopping.stockmanager.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import uk.co.danielbryant.shopping.stockmanager.exceptions.StockNotFoundException;
import uk.co.danielbryant.shopping.stockmanager.model.v2.Stock;
import uk.co.danielbryant.shopping.stockmanager.services.StockService;

import java.util.List;

import static com.github.quiram.utils.Collections.map;

@RestController
@RequestMapping("/stocks")
public class StockResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockResource.class);
    private static final String STOCK_V2_CONTENT_TYPE = "application/vnd.stock.v2+json";

    @Autowired
    private StockService stockService;

    public StockResource() {
    }

    public StockResource(StockService stockService) {
        this.stockService = stockService;
    }

    @RequestMapping()
    public List<uk.co.danielbryant.shopping.stockmanager.model.v1.Stock> getStocksV1() {
        LOGGER.info("getStocks (v1, All stocks)");
        return map(stockService.getStocks(), Stock::asV1Stock);
    }

    @RequestMapping(produces = STOCK_V2_CONTENT_TYPE)
    public List<Stock> getStocksV2() {
        LOGGER.info("getStocks (v2, All stocks)");
        return stockService.getStocks();
    }

    @RequestMapping("{productId}")
    public uk.co.danielbryant.shopping.stockmanager.model.v1.Stock getStockV1(@PathVariable("productId") String productId) throws StockNotFoundException {
        LOGGER.info("getStock (v1) with productId: {}", productId);
        return stockService.getStock(productId).asV1Stock();
    }

    @RequestMapping(path = "{productId}", produces = STOCK_V2_CONTENT_TYPE)
    public Stock getStockV2(@PathVariable("productId") String productId) throws StockNotFoundException {
        LOGGER.info("getStock (v2) with productId: {}", productId);
        return stockService.getStock(productId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleStockNotFound(StockNotFoundException snfe) {
    }
}
