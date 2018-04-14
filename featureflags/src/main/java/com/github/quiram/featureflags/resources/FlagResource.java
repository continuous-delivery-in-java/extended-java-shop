package com.github.quiram.featureflags.resources;

import com.github.quiram.featureflags.exceptions.FlagCreatedWithIdException;
import com.github.quiram.featureflags.exceptions.FlagNameAlreadyExistsException;
import com.github.quiram.featureflags.exceptions.FlagNotFoundException;
import com.github.quiram.featureflags.model.Flag;
import com.github.quiram.featureflags.services.FlagService;
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
    public Flag getFlag(@PathVariable("flagId") String flagId) throws FlagNotFoundException {
        LOGGER.info("getFlag with flagId: {}", flagId);
        return flagService.getFlag(flagId);
    }

    @RequestMapping(value = "{flagId}", method = DELETE)
    public ResponseEntity<?> deleteFlag(@PathVariable("flagId") String flagId) throws FlagNotFoundException {
        LOGGER.info("deleteFlag with flagId: {}", flagId);
        flagService.removeFlag(flagId);
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
