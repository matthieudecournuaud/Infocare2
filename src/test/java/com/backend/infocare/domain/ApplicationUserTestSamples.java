package com.backend.infocare.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ApplicationUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ApplicationUser getApplicationUserSample1() {
        return new ApplicationUser().id(1L).phoneNumber("phoneNumber1").location("location1").avatar("avatar1").notes("notes1");
    }

    public static ApplicationUser getApplicationUserSample2() {
        return new ApplicationUser().id(2L).phoneNumber("phoneNumber2").location("location2").avatar("avatar2").notes("notes2");
    }

    public static ApplicationUser getApplicationUserRandomSampleGenerator() {
        return new ApplicationUser()
            .id(longCount.incrementAndGet())
            .phoneNumber(UUID.randomUUID().toString())
            .location(UUID.randomUUID().toString())
            .avatar(UUID.randomUUID().toString())
            .notes(UUID.randomUUID().toString());
    }
}
