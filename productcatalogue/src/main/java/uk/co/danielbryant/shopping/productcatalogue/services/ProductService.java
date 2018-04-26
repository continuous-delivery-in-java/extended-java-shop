package uk.co.danielbryant.shopping.productcatalogue.services;

import uk.co.danielbryant.shopping.productcatalogue.model.v2.Product;

import java.util.*;

import static uk.co.danielbryant.shopping.productcatalogue.model.v2.Price.complexPrice;
import static uk.co.danielbryant.shopping.productcatalogue.model.v2.Price.singlePrice;

public class ProductService {

    //{productId, Product}
    private Map<String, Product> fakeProductDAO = new HashMap<>();

    public ProductService() {
        fakeProductDAO.put("1", new Product("1", "Widget", "Premium ACME Widgets", complexPrice("1.20", "1.00", 5)));
        fakeProductDAO.put("2", new Product("2", "Sprocket", "Grade B sprockets", singlePrice("4.10")));
        fakeProductDAO.put("3", new Product("3", "Anvil", "Large Anvils", complexPrice("45.50", "45.00", 10)));
        fakeProductDAO.put("4", new Product("4", "Cogs", "Grade Y cogs", singlePrice("1.80")));
        fakeProductDAO.put("5", new Product("5", "Multitool", "Multitools", singlePrice("154.10")));
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(fakeProductDAO.values());
    }

    public Optional<Product> getProduct(String id) {
        return Optional.ofNullable(fakeProductDAO.get(id));
    }
}
