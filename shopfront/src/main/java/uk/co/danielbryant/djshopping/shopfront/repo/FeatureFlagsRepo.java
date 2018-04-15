package uk.co.danielbryant.djshopping.shopfront.repo;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uk.co.danielbryant.djshopping.shopfront.services.dto.FlagDTO;

import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class FeatureFlagsRepo {
    private static final Logger LOGGER = getLogger(FeatureFlagsRepo.class);

    @Value("${featureFlagsUri}")
    private String featureFlagsUri;

    @Autowired
    @Qualifier(value = "stdRestTemplate")
    private RestTemplate restTemplate;

    public Optional<FlagDTO> getFlag(long flagId) {
        final String flagUrl = featureFlagsUri + "/flags/" + flagId;
        LOGGER.info("Fetching flag from {}", flagUrl);
        return Optional.ofNullable(restTemplate.getForObject(flagUrl, FlagDTO.class));
    }
}
