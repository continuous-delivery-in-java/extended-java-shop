package uk.co.danielbryant.djshopping.shopfront.model;

public class CircuitBreaker {
    private String name;
    private boolean isOpen;

    public CircuitBreaker() {
        // Needed by Spring
    }

    public CircuitBreaker(String name, boolean isOpen) {
        this.name = name;
        this.isOpen = isOpen;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
