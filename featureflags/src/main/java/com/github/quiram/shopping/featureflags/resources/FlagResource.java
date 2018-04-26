package com.github.quiram.shopping.featureflags.resources;

import com.github.quiram.shopping.featureflags.exceptions.FlagCreatedWithIdException;
import com.github.quiram.shopping.featureflags.exceptions.FlagNameAlreadyExistsException;
import com.github.quiram.shopping.featureflags.exceptions.FlagNotFoundException;
import com.github.quiram.shopping.featureflags.exceptions.FlagWithoutIdException;
import com.github.quiram.shopping.featureflags.model.Flag;
import com.github.quiram.shopping.featureflags.services.FlagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/flags")
public class FlagResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlagResource.class);

    @Autowired
    private FlagService flagService;

    @RequestMapping(method = GET)
    public List<Flag> getFlags() {
        LOGGER.info("getFlags (All flags)");
        return flagService.getFlags();
    }

    @RequestMapping(method = POST)
    public ResponseEntity<?> createFlag(@RequestBody Flag flag) throws FlagCreatedWithIdException, FlagNameAlreadyExistsException {
        LOGGER.info("createFlag: {}", flag);
        final Flag savedFlag = flagService.addFlag(flag);
        return ResponseEntity.created(URI.create("/flags/" + savedFlag.getFlagId())).build();
    }

    @RequestMapping(value = "{flagId}", method = GET)
    public Flag getFlag(@PathVariable("flagId") Long flagId) throws FlagNotFoundException {
        LOGGER.info("getFlag with flagId: {}", flagId);
        return flagService.getFlag(flagId);
    }

    @RequestMapping(value = "{flagId}", method = DELETE)
    public ResponseEntity<?> deleteFlag(@PathVariable("flagId") Long flagId) throws FlagNotFoundException {
        LOGGER.info("deleteFlag with flagId: {}", flagId);
        flagService.removeFlag(flagId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "{flagId}", method = PUT)
    public ResponseEntity<?> updateFlag(@PathVariable("flagId") Long flagId, @RequestBody Flag flag) throws FlagNotFoundException,
            FlagWithoutIdException {
        LOGGER.info("updating with flagId: {}", flagId);
        flagService.updateFlag(new Flag(flagId, flag.getName(), flag.getPortionIn(), flag.isSticky()));
        return ResponseEntity.ok().build();
    }


    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public void handleFlagNotFound(FlagNotFoundException e) {
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public void handleFlagWithId(FlagCreatedWithIdException e) {
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public void handleFlagNameAlreadyExists(FlagNameAlreadyExistsException e) {
    }
}
