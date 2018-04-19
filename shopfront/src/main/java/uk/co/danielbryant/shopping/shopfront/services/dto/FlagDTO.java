package uk.co.danielbryant.shopping.shopfront.services.dto;

public class FlagDTO {

    private Long flagId;
    private String name;
    private int portionIn;

    private FlagDTO() {
        // Needed by Spring
    }

    public FlagDTO(Long flagId, String name, int portionIn) {
        this.flagId = flagId;
        this.name = name;
        this.portionIn = portionIn;
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
