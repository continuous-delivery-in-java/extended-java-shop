package com.github.quiram.featureflags.model;

import com.amarinperez.utils.ReflectiveToStringCompareEquals;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static com.amarinperez.utils.ArgumentChecks.*;

@Entity
public class Flag extends ReflectiveToStringCompareEquals<Flag> {

    @Id
    @GeneratedValue
    private Long flagId;
    private String name;
    private int portionIn;

    private Flag() {
        // Needed by Spring
    }

    public Flag(Long flagId, String name, int portionIn) {
        ensurePortionIsValid(portionIn, "portionIn");
        ensureNotBlank(name, "name");

        this.flagId = flagId;
        this.name = name;
        this.portionIn = portionIn;
    }

    private static void ensurePortionIsValid(int portionIn, @SuppressWarnings("SameParameterValue") final String fieldName) {
        ensureNotNegative(portionIn, fieldName);
        ensure(portionIn, fieldName, i -> i > 100, fieldName + " cannot be higher than 100%");
    }

    public Long getFlagId() {
        return flagId;
    }

    public String getName() {
        return name;
    }

    public int getPortionIn() {
        return portionIn;
    }
}
