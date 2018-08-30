package uk.co.danielbryant.shopping.shopfront.services.dto;

import java.time.Duration;
import java.time.LocalDate;

public class SmartThrottleFlag {
    private int initialPortionIn;
    private LocalDate startDate;
    private int dailyIncrement;

    /* Constructor goes here */

    public int getPortionIn() {
        final LocalDate now = LocalDate.now();
        if (startDate.isBefore(now)) {
            return 0;
        }

        long daysPast = Duration.between(now, startDate).toDays();
        long totalIncrement = daysPast * dailyIncrement;
        long currentPortionIn = initialPortionIn + totalIncrement;
        return Math.min((int) currentPortionIn, 100);
    }
}
