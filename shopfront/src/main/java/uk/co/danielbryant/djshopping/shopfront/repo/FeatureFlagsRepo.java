package uk.co.danielbryant.djshopping.shopfront.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uk.co.danielbryant.djshopping.shopfront.services.dto.FlagDTO;

import java.util.Optional;

@Component
public class FeatureFlagsRepo {

    @Value("${featureFlagsUri}")
    private String featureFlagsUri;

    @Autowired
    @Qualifier(value = "stdRestTemplate")
    private RestTemplate restTemplate;

    public Optional<FlagDTO> getFlag(long flagId) {
        return Optional.ofNullable(restTemplate.getForObject("/flags/" + flagId, FlagDTO.class));
    }
}
