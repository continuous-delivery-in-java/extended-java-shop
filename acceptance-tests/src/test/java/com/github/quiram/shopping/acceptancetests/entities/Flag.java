package com.github.quiram.shopping.acceptancetests.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Flag {
    private Long flagId;
    private String name;
    private int portionIn;

    public Flag() {

    }

    public Flag(Long flagId, String name, int portionIn) {
        this.flagId = flagId;
        this.name = name;
        this.portionIn = portionIn;
    }

    public Long getFlagId() {
        return flagId;
    }

    public void setFlagId(Long flagId) {
        this.flagId = flagId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPortionIn() {
        return portionIn;
    }

    public void setPortionIn(int portionIn) {
        this.portionIn = portionIn;
    }
}
