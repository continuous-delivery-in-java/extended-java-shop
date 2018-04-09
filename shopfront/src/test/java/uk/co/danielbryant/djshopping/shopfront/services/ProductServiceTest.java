package uk.co.danielbryant.djshopping.shopfront.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import uk.co.danielbryant.djshopping.shopfront.model.Product;
import uk.co.danielbryant.djshopping.shopfront.repo.ProductRepo;
import uk.co.danielbryant.djshopping.shopfront.repo.StockRepo;
import uk.co.danielbryant.djshopping.shopfront.services.dto.ProductDTO;
import uk.co.danielbryant.djshopping.shopfront.services.dto.StockDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.amarinperez.utils.Collections.toMap;
import static com.amarinperez.utils.Random.randomString;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class ProductServiceTest {
    private StockRepo stockRepo = mock(StockRepo.class);
    private ProductRepo productRepo = mock(ProductRepo.class);
    private ProductService productService = new ProductService(stockRepo, productRepo);

    private List<ProductDTO> productDTOS;
    private List<StockDTO> stockDTOS;
    private List<Product> expectedProducts;

    public ProductServiceTest(String testName, List<ProductDTO> productDTOS, List<StockDTO> stockDTOS, List<Product> expectedProducts) {
        this.productDTOS = productDTOS;
        this.stockDTOS = stockDTOS;
        this.expectedProducts = expectedProducts;
    }

    @Test
    public void testScenario() {
        final Map<String, ProductDTO> productDTOMap = toMap(productDTOS, ProductDTO::getId);
        final Map<String, StockDTO> stockDTOMap = toMap(stockDTOS, StockDTO::getProductId);
        when(stockRepo.getStockDTOs()).thenReturn(stockDTOMap);
        when(productRepo.getProductDTOs()).thenReturn(productDTOMap);

        final List<Product> actualProducts = productService.getProducts();
        assertThat(actualProducts, containsInAnyOrder(expectedProducts.toArray(new Product[0])));
    }

    @Parameters(name = "{0}")
    public static List<Object[]> data() {
        return asList(new Object[][]{
                {"no products if both repositories returns empty list",
                        emptyList(),
                        emptyList(),
                        emptyList()},
                {"no products if product repo returns empty list, even if stock provides elements",
                        emptyList(),
                        singletonList(new StockDTO("1", randomString(), 10)),
                        emptyList()},
                {"products will contain default sku and quantity if stock provides no elements",
                        singletonList(new ProductDTO("1", "name-1", "description-1", new BigDecimal(10))),
                        emptyList(),
                        singletonList(new Product("1", "default", "name-1", "description-1", new BigDecimal(10), 999))},
                {"products will contain default sku and quantity if provided stock doesn't match",
                        singletonList(new ProductDTO("2", "name-2", "description-2", new BigDecimal(20))),
                        singletonList(new StockDTO("1", randomString(), 10)),
                        singletonList(new Product("2", "default", "name-2", "description-2", new BigDecimal(20), 999))},
                {"products will contain appropriate sku and quantity if provided stock matches",
                        singletonList(new ProductDTO("3", "name-3", "description-3", new BigDecimal(30))),
                        singletonList(new StockDTO("3", "sku-3", 10)),
                        singletonList(new Product("3", "sku-3", "name-3", "description-3", new BigDecimal(30), 10))},
                {"products can be matched even if provided in different order",
                        asList(new ProductDTO("4", "name-4", "description-4", new BigDecimal(40)), new ProductDTO("5", "name-5", "description-5", new BigDecimal(50))),
                        asList(new StockDTO("5", "sku-5", 5), new StockDTO("4", "sku-4", 4)),
                        asList(new Product("4", "sku-4", "name-4", "description-4", new BigDecimal(40), 4), new Product("5", "sku-5", "name-5", "description-5", new BigDecimal(50), 5))}
        });
    }
}