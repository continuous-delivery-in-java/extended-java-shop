package uk.co.danielbryant.shopping.shopfront.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mock;
import uk.co.danielbryant.shopping.shopfront.model.Product;
import uk.co.danielbryant.shopping.shopfront.repo.AdaptivePricingRepo;
import uk.co.danielbryant.shopping.shopfront.repo.ProductRepo;
import uk.co.danielbryant.shopping.shopfront.repo.StockRepo;
import uk.co.danielbryant.shopping.shopfront.services.dto.ProductDTO;
import uk.co.danielbryant.shopping.shopfront.services.dto.StockDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.github.quiram.utils.Collections.toMap;
import static com.github.quiram.utils.Random.randomDouble;
import static com.github.quiram.utils.Random.randomString;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(Parameterized.class)
public class ProductServiceTest {
    @Mock
    private StockRepo stockRepo;
    @Mock
    private ProductRepo productRepo;
    @Mock
    private FeatureFlagsService featureFlagsService;
    @Mock
    private AdaptivePricingRepo adaptivePricingRepo;

    private ProductService productService;

    private List<Product> expectedProducts;

    public ProductServiceTest(String testName, List<ProductDTO> productDTOS, List<StockDTO> stockDTOS, List<Product> expectedProducts) {
        final Map<String, ProductDTO> productDTOMap = toMap(productDTOS, ProductDTO::getId);
        final Map<String, StockDTO> stockDTOMap = toMap(stockDTOS, StockDTO::getProductId);
        this.expectedProducts = expectedProducts;

        initMocks(this);
        when(stockRepo.getStockDTOs()).thenReturn(stockDTOMap);
        when(productRepo.getProductDTOs()).thenReturn(productDTOMap);

        productService = new ProductService(stockRepo, productRepo, featureFlagsService, adaptivePricingRepo);
    }

    @Test
    public void originalPricesAreKeptWhenFlagIsOff() {
        when(featureFlagsService.shouldApplyFeatureWithFlag(anyLong())).thenReturn(false);

        final List<Product> actualProducts = productService.getProducts();
        assertThat(actualProducts, containsInAnyOrder(expectedProducts.toArray(new Product[0])));
    }

    @Test
    public void noPriceMatchesWhenFlagIsFullyOn() {
        // Technically some prices could actually match since they are all random, but this is very unlikely
        when(featureFlagsService.shouldApplyFeatureWithFlag(anyLong())).thenReturn(true);
        when(adaptivePricingRepo.getPriceFor(anyString())).thenReturn(Optional.of(new BigDecimal(Double.toString(randomDouble(100, 2)))));

        final List<Product> actualProducts = productService.getProducts();
        actualProducts.forEach(actualProduct -> {
            final Product originalProduct = findOriginalProduct(actualProduct.getId());
            assertNotEquals(originalProduct.getPrice(), actualProduct.getPrice());
        });
    }

    @Test
    public void originalPriceIsUsedWhenAdaptivePricingFailsEvenIfFlagIsOn() {
        when(featureFlagsService.shouldApplyFeatureWithFlag(anyLong())).thenReturn(true);
        when(adaptivePricingRepo.getPriceFor(anyString())).thenReturn(Optional.empty());

        final List<Product> actualProducts = productService.getProducts();
        assertThat(actualProducts, containsInAnyOrder(expectedProducts.toArray(new Product[0])));
    }

    private Product findOriginalProduct(String productId) {
        return expectedProducts.stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Original product not found for id " + productId));
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
                        asList(new ProductDTO("4", "name-4", "description-4", new BigDecimal(40)), new ProductDTO("5", "name-5", "description-5",
                                new BigDecimal(50))),
                        asList(new StockDTO("5", "sku-5", 5), new StockDTO("4", "sku-4", 4)),
                        asList(new Product("4", "sku-4", "name-4", "description-4", new BigDecimal(40), 4), new Product("5", "sku-5", "name-5",
                                "description-5", new BigDecimal(50), 5))}
        });
    }
}