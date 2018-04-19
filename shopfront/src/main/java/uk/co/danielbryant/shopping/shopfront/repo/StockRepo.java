package uk.co.danielbryant.shopping.shopfront.repo;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uk.co.danielbryant.shopping.shopfront.services.dto.StockDTO;

import java.util.List;
import java.util.Map;

import static com.github.quiram.utils.Collections.toMap;
import static java.util.Collections.emptyMap;

@Component
public class StockRepo {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockRepo.class);

    @Value("${stockManagerUri}")
    private String stockManagerUri;

    @Autowired
    @Qualifier(value = "stdRestTemplate")
    private RestTemplate restTemplate;

    public StockRepo() {
        // Needed by Spring
    }

    public StockRepo(String stockManagerUri, RestTemplate restTemplate) {
        this.stockManagerUri = stockManagerUri;
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(fallbackMethod = "stocksNotFound") // Hystrix circuit breaker for fault-tolerance demo
    public Map<String, StockDTO> getStockDTOs() {
        LOGGER.info("getStocksDTOs");
        ResponseEntity<List<StockDTO>> stockManagerResponse =
                restTemplate.exchange(stockManagerUri + "/stocks",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<StockDTO>>() {
                        });
        List<StockDTO> stockDTOs = stockManagerResponse.getBody();

        return toMap(stockDTOs, StockDTO::getProductId);
    }

    public StockDTO getStockDTO(String id) {
        return restTemplate.getForObject(stockManagerUri + "/stocks/" + id, StockDTO.class);
    }

    public Map<String, StockDTO> stocksNotFound() {
        LOGGER.info("stocksNotFound *** FALLBACK ***");
        return emptyMap();
    }
}
