package uk.co.danielbryant.shopping.shopfront.services.dto;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class SmartThrottleFlag {
    private int initialPortionIn;
    private LocalDate startDate;
    private int dailyIncrement;

    public SmartThrottleFlag(int initialPortionIn, LocalDate startDate, int dailyIncrement) {
        this.initialPortionIn = initialPortionIn;
        this.startDate = startDate;
        this.dailyIncrement = dailyIncrement;
    }

    public int getPortionIn() {
        final LocalDate now = LocalDate.now();
        if (startDate.isAfter(now)) {
            return 0;
        }

        long daysPast = DAYS.between(startDate, now);
        long totalIncrement = daysPast * dailyIncrement;
        long currentPortionIn = initialPortionIn + totalIncrement;
        return Math.min((int) currentPortionIn, 100);
    }
}
