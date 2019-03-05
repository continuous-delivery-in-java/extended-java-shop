package uk.co.danielbryant.shopping.shopfront.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.co.danielbryant.shopping.shopfront.repo.FeatureFlagsRepo;
import uk.co.danielbryant.shopping.shopfront.services.dto.FlagDTO;

import java.util.Optional;
import java.util.Random;

@Service
public class FeatureFlagsService {
    private final FeatureFlagsRepo featureFlagsRepo;
    private final Random random;

    @Autowired
    public FeatureFlagsService(FeatureFlagsRepo featureFlagsRepo, @Value("#{new java.util.Random()}") Random random) {
        this.featureFlagsRepo = featureFlagsRepo;
        this.random = random;
    }

    // Get value of flag and check if a randomly generated value falls within
    public boolean shouldApplyFeatureWithFlag(long flagId) {
        final Optional<FlagDTO> flag = featureFlagsRepo.getFlag(flagId);
        return flag.map(FlagDTO::getPortionIn).map(this::randomWithinPortion)
                .orElse(false);
    }

    private boolean randomWithinPortion(int portionIn) {
        return random.nextInt(100) < portionIn;
    }
}
