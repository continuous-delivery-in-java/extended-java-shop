package uk.co.danielbryant.djshopping.shopfront.services;

import org.springframework.beans.factory.annotation.Autowired;
import uk.co.danielbryant.djshopping.shopfront.repo.FeatureFlagsRepo;
import uk.co.danielbryant.djshopping.shopfront.services.dto.FlagDTO;

import java.util.Optional;
import java.util.Random;

public class FeatureFlagsService {
    @Autowired
    private final FeatureFlagsRepo featureFlagsRepo;
    @Autowired
    private final Random random;

    public FeatureFlagsService(FeatureFlagsRepo featureFlagsRepo, Random random) {
        this.featureFlagsRepo = featureFlagsRepo;
        this.random = random;
    }

    public boolean shouldApplyFeatureWithFlag(long flagId) {
        final Optional<FlagDTO> flag = featureFlagsRepo.getFlag(flagId);
        return flag.map(FlagDTO::getPortionIn).map(this::randomWithinPortion).orElse(false);
    }

    private boolean randomWithinPortion(int i) {
        return i > random.nextInt(100);
    }
}
