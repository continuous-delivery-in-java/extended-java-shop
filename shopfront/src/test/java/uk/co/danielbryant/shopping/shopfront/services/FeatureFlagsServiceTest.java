package uk.co.danielbryant.shopping.shopfront.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.co.danielbryant.shopping.shopfront.repo.FeatureFlagsRepo;
import uk.co.danielbryant.shopping.shopfront.services.dto.FlagDTO;

import java.util.Optional;
import java.util.Random;

import static com.github.quiram.utils.Random.randomLong;
import static com.github.quiram.utils.Random.randomString;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FeatureFlagsServiceTest {
    @Mock
    private FeatureFlagsRepo featureFlagsRepo;

    @Mock
    private Random random;
    private FeatureFlagsService featureFlagsService;

    @Before
    public void setUp() {
        featureFlagsService = new FeatureFlagsService(featureFlagsRepo, random);
    }

    @Test
    public void neverApplyIfFlagNotFound() {
        when(featureFlagsRepo.getFlag(anyLong())).thenReturn(Optional.empty());
        assertFalse(featureFlagsService.shouldApplyFeatureWithFlag(randomLong()));
    }

    @Test
    public void neverApplyIfFlagSetToZero() {
        when(featureFlagsRepo.getFlag(anyLong())).thenReturn(Optional.of(new FlagDTO(randomLong(), randomString(), 0)));
        assertFalse(featureFlagsService.shouldApplyFeatureWithFlag(randomLong()));
    }

    @Test
    public void alwaysApplyIfFlagSetToHundred() {
        when(featureFlagsRepo.getFlag(anyLong())).thenReturn(Optional.of(new FlagDTO(randomLong(), randomString(), 100)));
        assertTrue(featureFlagsService.shouldApplyFeatureWithFlag(randomLong()));
    }

    @Test
    public void applyIfRandomIsLowerThanFlag() {
        when(featureFlagsRepo.getFlag(anyLong())).thenReturn(Optional.of(new FlagDTO(randomLong(), randomString(), 50)));
        when(random.nextInt(100)).thenReturn(20);
        assertTrue(featureFlagsService.shouldApplyFeatureWithFlag(randomLong()));
    }

    @Test
    public void notApplyIfRandomIsHigherThanFlag() {
        when(featureFlagsRepo.getFlag(anyLong())).thenReturn(Optional.of(new FlagDTO(randomLong(), randomString(), 50)));
        when(random.nextInt(100)).thenReturn(70);
        assertFalse(featureFlagsService.shouldApplyFeatureWithFlag(randomLong()));
    }

}