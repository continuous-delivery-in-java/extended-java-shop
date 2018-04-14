package com.github.quiram.featureflags.services;

import com.github.quiram.featureflags.exceptions.FlagCreatedWithIdException;
import com.github.quiram.featureflags.exceptions.FlagNameAlreadyExistsException;
import com.github.quiram.featureflags.model.Flag;
import com.github.quiram.featureflags.repositories.FlagRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.amarinperez.test_utils.Exceptions.expectException;
import static com.amarinperez.utils.Random.randomString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
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
        final Flag newFlag = new Flag(null, randomString(), 10);
        final String generatedId = randomString();
        when(repository.save(newFlag))
                .thenReturn(new Flag(generatedId, newFlag.getName(), newFlag.getPortionIn()));

        final Flag savedFlag = flagService.addFlag(newFlag);
        assertThat(savedFlag.getFlagId(), is(generatedId));
    }

    @Test
    public void flagMustComeWithoutId() {
        final Flag flag = new Flag(randomString(), randomString(), 10);
        expectException(() -> flagService.addFlag(flag), FlagCreatedWithIdException.class, "flag includes the id",
                "flag was created successfully");
    }

    @Test
    public void flagNamesMustBeUnique() {
        final String name = randomString();
        final Flag newFlag = new Flag(null, name, 10);
        final Flag existingFlag = new Flag(randomString(), name, 20);
        when(repository.findByName(name)).thenReturn(existingFlag);

        expectException(() -> flagService.addFlag(newFlag), FlagNameAlreadyExistsException.class, "name",
                "flag was created successfully");
    }
}