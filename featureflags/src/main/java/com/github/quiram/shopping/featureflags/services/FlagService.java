package com.github.quiram.shopping.featureflags.services;

import com.github.quiram.shopping.featureflags.exceptions.FlagCreatedWithIdException;
import com.github.quiram.shopping.featureflags.exceptions.FlagNameAlreadyExistsException;
import com.github.quiram.shopping.featureflags.exceptions.FlagNotFoundException;
import com.github.quiram.shopping.featureflags.exceptions.FlagWithoutIdException;
import com.github.quiram.shopping.featureflags.model.Flag;
import com.github.quiram.shopping.featureflags.repositories.FlagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

@Service
public class FlagService {

    private FlagRepository flagRepository;

    @Autowired
    FlagService(FlagRepository flagRepository) {
        this.flagRepository = flagRepository;
    }

    public List<Flag> getFlags() {
        return stream(flagRepository.findAll().spliterator(), false).collect(toList());
    }

    public Flag getFlag(Long id) throws FlagNotFoundException {
        return Optional.ofNullable(flagRepository.findOne(id))
                .orElseThrow(() -> new FlagNotFoundException("Flag not found with id: " + id));
    }

    public Flag addFlag(Flag flag) throws FlagCreatedWithIdException, FlagNameAlreadyExistsException {
        if (flag.getFlagId() != null) {
            throw new FlagCreatedWithIdException("flag includes the id " + flag.getFlagId());
        }

        if (flagRepository.findByName(flag.getName()) != null) {
            throw new FlagNameAlreadyExistsException("there is already a flag with name " + flag.getName());
        }

        return flagRepository.save(flag);
    }

    public void removeFlag(Long id) throws FlagNotFoundException {
        getFlag(id);
        flagRepository.delete(id);
    }

    public void updateFlag(Flag flag) throws FlagNotFoundException, FlagWithoutIdException {
        if (flag.getFlagId() == null) {
            throw new FlagWithoutIdException(flag.getName());
        }

        getFlag(flag.getFlagId());
        flagRepository.save(flag);
    }
}
