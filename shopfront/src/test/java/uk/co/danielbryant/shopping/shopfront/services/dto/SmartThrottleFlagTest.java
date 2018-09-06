package uk.co.danielbryant.shopping.shopfront.services.dto;

import org.junit.Test;

import java.time.LocalDate;

import static java.time.LocalDate.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.YEARS;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SmartThrottleFlagTest {
    private static final LocalDate tomorrow = now().plus(1, DAYS);
    private static final LocalDate today = now();
    private static final LocalDate yesterday = now().minus(1, DAYS);
    private static final LocalDate lastYear = now().minus(1, YEARS);

    @Test
    public void flagIsZeroBeforeStartDate() {
        SmartThrottleFlag smartThrottleFlag = new SmartThrottleFlag(5, tomorrow, 10);
        assertThat(smartThrottleFlag.getPortionIn(), is(0));
    }

    @Test
    public void flagIsInitialPortionAtStartDate() {
        SmartThrottleFlag smartThrottleFlag = new SmartThrottleFlag(5, today, 10);
        assertThat(smartThrottleFlag.getPortionIn(), is(5));
    }

    @Test
    public void flagIsInitialPlusOneIncrementOnDayAfterStartDate() {
        SmartThrottleFlag smartThrottleFlag = new SmartThrottleFlag(5, yesterday, 10);
        assertThat(smartThrottleFlag.getPortionIn(), is(15));
    }

    @Test
    public void flagMaxesOutAt100AfterEnoughDaysHavePassed() {
        SmartThrottleFlag smartThrottleFlag = new SmartThrottleFlag(5, lastYear, 10);
        assertThat(smartThrottleFlag.getPortionIn(), is(100));
    }
}