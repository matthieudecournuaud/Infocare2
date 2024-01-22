package com.backend.infocare.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ApplicationUserTicketTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ApplicationUserTicket getApplicationUserTicketSample1() {
        return new ApplicationUserTicket().id(1L);
    }

    public static ApplicationUserTicket getApplicationUserTicketSample2() {
        return new ApplicationUserTicket().id(2L);
    }

    public static ApplicationUserTicket getApplicationUserTicketRandomSampleGenerator() {
        return new ApplicationUserTicket().id(longCount.incrementAndGet());
    }
}
