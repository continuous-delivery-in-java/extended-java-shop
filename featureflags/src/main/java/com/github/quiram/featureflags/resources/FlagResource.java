package com.github.quiram.featureflags.resources;

import com.github.quiram.featureflags.exceptions.FlagNotFoundException;
import com.github.quiram.featureflags.model.Flag;
import com.github.quiram.featureflags.services.FlagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flags")
public class FlagResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlagResource.class);

    @Autowired
    private FlagService flagService;

    @RequestMapping()
    public List<Flag> getFlags() {
        LOGGER.info("getFlags (All flags)");
        return flagService.getFlags();
    }

    @RequestMapping("{flagId}")
    public Flag getFlag(@PathVariable("flagId") String flagId) throws FlagNotFoundException {
        LOGGER.info("getFlag with flagId: {}", flagId);
        return flagService.getFlag(flagId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleFlagNotFound(FlagNotFoundException e) {
    }
}
