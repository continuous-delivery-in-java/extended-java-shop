package uk.co.danielbryant.shopping.stockmanager;

import io.restassured.specification.RequestSpecification;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.core.Is.is;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class StockManagerApplicationCT {

    private static final String STOCK_V2_CONTENT_TYPE = "application/vnd.stock.v2+json";
    private final String baseUrl = "http://localhost:8030";

    @Test
    public void listOfStocksProvidesFiveElements() {
        when().get(baseUrl + "/stocks")
                .then().assertThat()
                .body("size()", is(5));
    }

    @Test
    public void listOfStocksProvidesFiveElementsInV2() {
        when(STOCK_V2_CONTENT_TYPE).get(baseUrl + "/stocks")
                .then().assertThat()
                .body("size()", is(5), "[0].amountAvailable.total", is(5));
    }

    @Test
    public void canGetSpecificStock() {
        when().get(baseUrl + "/stocks/1")
                .then().assertThat()
                .body("sku", is("12345678"), "amountAvailable", is(5));
    }

    @Test
    public void canGetSpecificStockInVersion2() {
        when(STOCK_V2_CONTENT_TYPE).get(baseUrl + "/stocks/1")
                .then().assertThat()
                .body("sku", is("12345678"),
                        "amountAvailable.total", is(5),
                        "amountAvailable.perPurchase", is(2));
    }

    @Test
    public void canHandleNotFoundStock() {
        when().get(baseUrl + "/stocks/99999999")
                .then().assertThat()
                .statusCode(is(NOT_FOUND.value()));
    }

    private RequestSpecification when() {
        return when(JSON.toString());
    }

    private RequestSpecification when(String acceptType) {
        return given().accept(acceptType).when();
    }

}
