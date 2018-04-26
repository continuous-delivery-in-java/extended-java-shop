package uk.co.danielbryant.shopping.productcatalogue.resources.v1;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import uk.co.danielbryant.shopping.productcatalogue.model.v2.Product;
import uk.co.danielbryant.shopping.productcatalogue.services.ProductService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

import static com.github.quiram.utils.Collections.map;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {

    private ProductService productService;

    @Inject
    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @GET
    @Timed
    public Response getAllProducts() {
        return Response.status(200)
                .entity(map(productService.getAllProducts(), Product::asV1Product))
                .build();
    }

    @GET
    @Timed
    @Path("{id}")
    public Response getProduct(@PathParam("id") String id) {
        Optional<uk.co.danielbryant.shopping.productcatalogue.model.v1.Product> result = productService.getProduct(id).map(Product::asV1Product);

        if (result.isPresent()) {
            return Response.status(Response.Status.OK)
                    .entity(result.get())
                    .build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .build();
        }
    }
}
