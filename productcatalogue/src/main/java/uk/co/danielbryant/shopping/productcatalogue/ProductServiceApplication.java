package uk.co.danielbryant.shopping.productcatalogue;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import uk.co.danielbryant.shopping.productcatalogue.configuration.ProductServiceConfiguration;
import uk.co.danielbryant.shopping.productcatalogue.healthchecks.BasicHealthCheck;
import uk.co.danielbryant.shopping.productcatalogue.resources.v2.ProductResource;

public class ProductServiceApplication extends Application<ProductServiceConfiguration> {
    public static void main(String[] args) throws Exception {
        new ProductServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "product-list-service";
    }

    @Override
    public void initialize(Bootstrap<ProductServiceConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(ProductServiceConfiguration config,
                    Environment environment) {
        final BasicHealthCheck healthCheck = new BasicHealthCheck(config.getVersion());
        environment.healthChecks().register("template", healthCheck);

        Injector injector = Guice.createInjector();
        environment.jersey().register(injector.getInstance(uk.co.danielbryant.shopping.productcatalogue.resources.v1.ProductResource.class));
        environment.jersey().register(injector.getInstance(ProductResource.class));
    }
}