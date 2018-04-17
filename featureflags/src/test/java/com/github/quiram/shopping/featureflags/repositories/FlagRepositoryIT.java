package com.github.quiram.shopping.featureflags.repositories;

import com.github.quiram.shopping.featureflags.model.Flag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.StreamSupport;

import static com.github.quiram.utils.Random.randomInt;
import static com.github.quiram.utils.Random.randomString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url= jdbc:postgresql://localhost:5432/featureflags",
        "spring.datasource.platform=postgresql",
        "spring.datasource.username=testuser",
        "spring.datasource.password=test-password",
        "spring.jpa.hibernate.ddl-auto=create"
})
public class FlagRepositoryIT {
    @Autowired
    private FlagRepository flagRepository;

    @Test
    public void getExistingFlags() {
        final Iterable<Flag> all = flagRepository.findAll();
        assertEquals(3, StreamSupport.stream(all.spliterator(), false).count());
    }

    @Test
    public void canSeeFlagAfterInserting() {
        final Flag newFlag = new Flag(null, randomString(), randomInt(100));
        final Flag savedFlag = flagRepository.save(newFlag);
        final Flag retrievedFlag = flagRepository.findOne(savedFlag.getFlagId());
        assertThat(retrievedFlag.getName(), is(newFlag.getName()));
        assertThat(retrievedFlag.getPortionIn(), is(newFlag.getPortionIn()));
        assertNotNull(retrievedFlag.getFlagId());
    }

    @Test
    public void canFindFlagByName() {
        final Flag flag = flagRepository.findByName("disabled-feature");
        assertNotNull(flag);
        assertThat(flag.getPortionIn(), is(0));
    }

    @Test
    public void canDeleteFlags() {
        flagRepository.delete(3L);
        final Flag notFound = flagRepository.findOne(3L);
        assertNull(notFound);
    }
}