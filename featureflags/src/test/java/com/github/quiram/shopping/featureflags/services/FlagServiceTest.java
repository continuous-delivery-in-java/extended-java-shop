package com.github.quiram.shopping.featureflags.services;

import com.github.quiram.shopping.featureflags.exceptions.FlagCreatedWithIdException;
import com.github.quiram.shopping.featureflags.exceptions.FlagNameAlreadyExistsException;
import com.github.quiram.shopping.featureflags.exceptions.FlagNotFoundException;
import com.github.quiram.shopping.featureflags.model.Flag;
import com.github.quiram.shopping.featureflags.repositories.FlagRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.github.quiram.test_utils.Exceptions.expectException;
import static com.github.quiram.utils.Random.randomLong;
import static com.github.quiram.utils.Random.randomString;
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
        final Flag newFlag = new Flag(null, randomString(), 10);
        final Long generatedId = randomLong();
        when(repository.save(newFlag))
                .thenReturn(new Flag(generatedId, newFlag.getName(), newFlag.getPortionIn()));

        final Flag savedFlag = flagService.addFlag(newFlag);
        assertThat(savedFlag.getFlagId(), is(generatedId));
    }

    @Test
    public void flagMustComeWithoutId() {
        final Flag flag = new Flag(randomLong(), randomString(), 10);
        expectException(() -> flagService.addFlag(flag), FlagCreatedWithIdException.class, "flag includes the id",
                "flag was created successfully");
    }

    @Test
    public void flagNamesMustBeUnique() {
        final String name = randomString();
        final Flag newFlag = new Flag(null, name, 10);
        final Flag existingFlag = new Flag(randomLong(), name, 20);
        when(repository.findByName(name)).thenReturn(existingFlag);

        expectException(() -> flagService.addFlag(newFlag), FlagNameAlreadyExistsException.class, "name",
                "flag was created successfully");
    }

    @Test
    public void canDeleteFlag() throws FlagNotFoundException {
        final Long id = randomLong();
        when(repository.findOne(id)).thenReturn(new Flag(id, randomString(), 10));

        flagService.removeFlag(id);
        verify(repository).delete(id);
    }

    @Test
    public void cannotDeleteFlagThatDoesNotExist() {
        final Long id = randomLong();
        when(repository.findOne(id)).thenReturn(null);

        expectException(() -> flagService.removeFlag(id), FlagNotFoundException.class, id.toString(), "successfully deleted");
    }
}
