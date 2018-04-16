package uk.co.danielbryant.djshopping.stockmanager;

import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.core.Is.is;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class StockManagerApplicationCT {

    private RequestSpecification when;
    private final String baseUrl = "http://localhost:8030";

    @Before
    public void testSetup() {
        when = given().contentType(JSON).when();
    }

    @Test
    public void listOfStocksProvidesFiveElements() {
        when.get(baseUrl + "/stocks")
                .then().assertThat()
                .body("size()", is(5));
    }

    @Test
    public void canGetSpecificStock() {
        when.get(baseUrl + "/stocks/1")
                .then().assertThat()
                .body("sku", is("12345678"), "amountAvailable", is(5));
    }

    @Test
    public void canHandleNotFoundStock() {
        when.get(baseUrl + "/stocks/99999999")
                .then().assertThat()
                .statusCode(is(NOT_FOUND.value()));
    }
}
