package com.github.quiram.shopping.featureflags.services;

import com.github.quiram.shopping.featureflags.exceptions.FlagCreatedWithIdException;
import com.github.quiram.shopping.featureflags.exceptions.FlagNameAlreadyExistsException;
import com.github.quiram.shopping.featureflags.exceptions.FlagNotFoundException;
import com.github.quiram.shopping.featureflags.exceptions.FlagWithoutIdException;
import com.github.quiram.shopping.featureflags.model.Flag;
import com.github.quiram.shopping.featureflags.repositories.FlagRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Random;

import static com.github.quiram.test_utils.Exceptions.expectException;
import static com.github.quiram.test_utils.MockitoVerifications.once;
import static com.github.quiram.utils.Random.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FlagServiceTest {
    @Mock
    private FlagRepository repository;
    private FlagService flagService;

    @Before
    public void setup() {
        flagService = new FlagService(repository);
    }

    @Test
    public void canAddFlag() throws FlagCreatedWithIdException, FlagNameAlreadyExistsException {
        final boolean sticky = randomBoolean();
        final Flag newFlag = new Flag(null, randomString(), 10, sticky);
        final Long generatedId = randomLong();
        when(repository.save(newFlag))
                .thenReturn(new Flag(generatedId, newFlag.getName(), newFlag.getPortionIn(), sticky));

        final Flag savedFlag = flagService.addFlag(newFlag);
        assertThat(savedFlag.getFlagId(), is(generatedId));
    }

    @Test
    public void flagMustComeWithoutId() {
        final Flag flag = new Flag(randomLong(), randomString(), 10, false);
        expectException(() -> flagService.addFlag(flag), FlagCreatedWithIdException.class, "flag includes the id",
                "flag was created successfully");
    }

    @Test
    public void flagNamesMustBeUnique() {
        final String name = randomString();
        final Flag newFlag = new Flag(null, name, 10, false);
        final Flag existingFlag = new Flag(randomLong(), name, 20, false);
        when(repository.findByName(name)).thenReturn(existingFlag);

        expectException(() -> flagService.addFlag(newFlag), FlagNameAlreadyExistsException.class, "name",
                "flag was created successfully");
    }

    @Test
    public void canDeleteFlag() throws FlagNotFoundException {
        final Long id = randomLong();
        when(repository.findOne(id)).thenReturn(new Flag(id, randomString(), 10, false));

        flagService.removeFlag(id);
        verify(repository).delete(id);
    }

    @Test
    public void cannotDeleteFlagThatDoesNotExist() {
        final Long id = randomLong();
        when(repository.findOne(id)).thenReturn(null);

        expectException(() -> flagService.removeFlag(id), FlagNotFoundException.class, id.toString(), "successfully deleted");
    }

    @Test
    public void cannotUpdateFlagThatDoesNotExist() {
        final Long id = randomLong();
        when(repository.findOne(id)).thenReturn(null);
        final Flag updatedFlag = new Flag(id, randomString(), randomInt(100), false);

        expectException(() -> flagService.updateFlag(updatedFlag), FlagNotFoundException.class, id.toString(), "successfully updated");
    }

    @Test
    public void cannotUpdateFlagWithoutId() {
        final String flagName = randomString();
        final Flag updatedFlag = new Flag(null, flagName, randomInt(100), false);

        expectException(() -> flagService.updateFlag(updatedFlag), FlagWithoutIdException.class, flagName, "successfully updated");
    }

    @Test
    public void canUpdateFlagIfDoneCorrectly() throws FlagWithoutIdException, FlagNotFoundException {
        final long flagId = randomLong();
        final Flag originalFlag = new Flag(flagId, randomString(), randomInt(100), false);
        when(repository.findOne(flagId)).thenReturn(originalFlag);

        final Flag newFlag = new Flag(flagId, randomString(), randomInt(100), randomBoolean());
        flagService.updateFlag(newFlag);
        verify(repository, once()).save(newFlag);
    }

    public static boolean randomBoolean() {
        return new Random().nextBoolean();
    }
}
