package uk.co.danielbryant.shopping.productcatalogue;

import io.dropwizard.testing.junit.DropwizardAppRule;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import uk.co.danielbryant.shopping.productcatalogue.configuration.ProductServiceConfiguration;
import uk.co.danielbryant.shopping.productcatalogue.model.v2.BulkPrice;
import uk.co.danielbryant.shopping.productcatalogue.model.v2.Product;
import uk.co.danielbryant.shopping.productcatalogue.model.v2.UnitPrice;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;

import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;
import static org.eclipse.jetty.http.HttpStatus.NOT_FOUND_404;
import static org.eclipse.jetty.http.HttpStatus.OK_200;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

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
        final List<uk.co.danielbryant.shopping.productcatalogue.model.v1.Product> products = response.readEntity(new GenericType<List<uk.co.danielbryant
                .shopping.productcatalogue.model.v1.Product>>() {
        });
        assertEquals(5, products.size());
    }

    @Test
    public void getV2Products() {
        final Response response = client.target(baseUrl + "/v2/products").request().get();
        assertEquals(OK_200, response.getStatus());
        final List<Product> products = response.readEntity(new GenericType<List<Product>>() {
        });
        assertEquals(5, products.size());
        assertThat(products.get(0).getPrice().getBulkPrice(), is(new BulkPrice(new UnitPrice(new BigDecimal("1.00")), 5)));
    }

    @Test
    public void canGetSpecificProduct() {
        final Response response = client.target(baseUrl + "/products/1").request().get();
        assertEquals(OK_200, response.getStatus());
        final uk.co.danielbryant.shopping.productcatalogue.model.v1.Product product = response.readEntity(uk.co.danielbryant.shopping.productcatalogue.model.v1.Product.class);
        assertEquals("Widget", product.getName());
        assertEquals(new BigDecimal("1.20"), product.getPrice());
    }

    @Test
    public void canGetSpecificV2Product() {
        final Response response = client.target(baseUrl + "/v2/products/3").request().get();
        assertEquals(OK_200, response.getStatus());
        final Product product = response.readEntity(Product.class);
        assertEquals("Anvil", product.getName());
        assertThat(product.getPrice().getSingle().getValue(), is(new BigDecimal("45.50")));
        assertThat(product.getPrice().getBulkPrice(), is(new BulkPrice(new UnitPrice(new BigDecimal("45.00")), 10)));
    }

    @Test
    public void canHandleProductThatDoesNotExist() {
        final Response response = client.target(baseUrl + "/products/987897987").request().get();
        assertEquals(NOT_FOUND_404, response.getStatus());
    }
}
