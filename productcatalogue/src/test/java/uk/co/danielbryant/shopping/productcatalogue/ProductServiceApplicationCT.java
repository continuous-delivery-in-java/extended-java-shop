package uk.co.danielbryant.shopping.productcatalogue;

import io.dropwizard.testing.junit.DropwizardAppRule;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import uk.co.danielbryant.shopping.productcatalogue.configuration.ProductServiceConfiguration;
import uk.co.danielbryant.shopping.productcatalogue.model.v1.Product;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;

import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;
import static org.eclipse.jetty.http.HttpStatus.NOT_FOUND_404;
import static org.eclipse.jetty.http.HttpStatus.OK_200;
import static org.junit.Assert.assertEquals;

public class ProductServiceApplicationCT {
    @ClassRule
    public static final DropwizardAppRule<ProductServiceConfiguration> RULE = new DropwizardAppRule<>(
            ProductServiceApplication.class,
            resourceFilePath("product-catalogue.yml"));
    private Client client;
    private String baseUrl;


    @Before
    public void setUp() {
        client = new JerseyClientBuilder().build();
        baseUrl = "http://localhost:" + RULE.getLocalPort();
    }

    @Test
    public void canGetAllProducts() {
        final Response response = client.target(baseUrl + "/products").request().get();
        assertEquals(OK_200, response.getStatus());
        final List<Product> products = response.readEntity(new GenericType<List<Product>>() {
        });
        assertEquals(5, products.size());
    }

    @Test
    public void canGetSpecificProduct() {
        final Response response = client.target(baseUrl + "/products/1").request().get();
        assertEquals(OK_200, response.getStatus());
        final Product product = response.readEntity(Product.class);
        assertEquals("Widget", product.getName());
        assertEquals(new BigDecimal("1.20"), product.getPrice());
    }

    @Test
    public void canHandleProductThatDoesNotExist() {
        final Response response = client.target(baseUrl + "/products/987897987").request().get();
        assertEquals(NOT_FOUND_404, response.getStatus());
    }
}
