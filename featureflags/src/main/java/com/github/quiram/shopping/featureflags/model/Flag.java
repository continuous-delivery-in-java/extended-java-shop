package com.github.quiram.shopping.featureflags.model;

import com.github.quiram.utils.ReflectiveToStringCompareEquals;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static com.github.quiram.utils.ArgumentChecks.*;

@Entity
public class Flag extends ReflectiveToStringCompareEquals<Flag> {

    @Id
    @GeneratedValue
    private Long flagId;
    private String name;
    private int portionIn;
    private boolean sticky;

    private Flag() {
        // Needed by Spring
    }

    public Flag(Long flagId, String name, int portionIn, boolean sticky) {
        ensureInRange(0, 100, portionIn, "portionIn");
        ensureNotBlank(name, "name");

        this.flagId = flagId;
        this.name = name;
        this.portionIn = portionIn;
        this.sticky = sticky;
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

    public boolean isSticky() {
        return sticky;
    }

    public void setSticky(boolean sticky) {
        this.sticky = sticky;
    }
}
