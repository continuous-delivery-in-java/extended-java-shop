package uk.co.danielbryant.shopping.shopfront.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static uk.co.danielbryant.shopping.shopfront.model.Constants.ADAPTIVE_PRICING_FLAG_ID;
import static uk.co.danielbryant.shopping.shopfront.services.dto.StockDTO.DEFAULT_STOCK_DTO;

@Service
public class ProductService {

    @Autowired
    private StockRepo stockRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private final FeatureFlagsService featureFlagsService;

    @Autowired
    private final AdaptivePricingRepo adaptivePricingRepo;

    ProductService(StockRepo stockRepo, ProductRepo productRepo, FeatureFlagsService featureFlagsService, AdaptivePricingRepo adaptivePricingRepo) {
        this.stockRepo = stockRepo;
        this.productRepo = productRepo;
        this.featureFlagsService = featureFlagsService;
        this.adaptivePricingRepo = adaptivePricingRepo;
    }

    public List<Product> getProducts() {
        Map<String, ProductDTO> productDTOs = productRepo.getProductDTOs();
        Map<String, StockDTO> stockDTOMap = stockRepo.getStockDTOs();

        // Merge productDTOs and stockDTOs to a List of Products
        return productDTOs.values().stream()
                .map(productDTO -> {
                    StockDTO stockDTO = stockDTOMap.getOrDefault(productDTO.getId(), DEFAULT_STOCK_DTO);
                    return new Product(productDTO.getId(), stockDTO.getSku(), productDTO.getName(), productDTO.getDescription(),
                            getPrice(productDTO), stockDTO.getAmountAvailable());
                })
                .collect(toList());
    }

    private BigDecimal getPrice(ProductDTO productDTO) {
        Optional<BigDecimal> maybeAdaptivePrice = Optional.empty();

        if (featureFlagsService.shouldApplyFeatureWithFlag(ADAPTIVE_PRICING_FLAG_ID))
            maybeAdaptivePrice = adaptivePricingRepo.getPriceFor(productDTO.getName());

        return maybeAdaptivePrice.orElse(productDTO.getPrice());
    }

    public List<Product> productsNotFound() {
        return emptyList();
    }
}
