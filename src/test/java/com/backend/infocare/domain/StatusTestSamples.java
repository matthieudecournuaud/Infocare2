package com.backend.infocare.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StatusTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Status getStatusSample1() {
        return new Status()
            .id(1L)
            .name("name1")
            .statusCode("statusCode1")
            .description("description1")
            .colorCode("colorCode1")
            .nextPossibleStatus("nextPossibleStatus1");
    }

    public static Status getStatusSample2() {
        return new Status()
            .id(2L)
            .name("name2")
            .statusCode("statusCode2")
            .description("description2")
            .colorCode("colorCode2")
            .nextPossibleStatus("nextPossibleStatus2");
    }

    public static Status getStatusRandomSampleGenerator() {
        return new Status()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .statusCode(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .colorCode(UUID.randomUUID().toString())
            .nextPossibleStatus(UUID.randomUUID().toString());
    }
}
