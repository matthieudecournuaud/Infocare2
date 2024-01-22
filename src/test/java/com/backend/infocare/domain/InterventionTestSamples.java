package com.backend.infocare.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InterventionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Intervention getInterventionSample1() {
        return new Intervention()
            .id(1L)
            .title("title1")
            .description("description1")
            .createdBy("createdBy1")
            .attachments("attachments1")
            .notes("notes1");
    }

    public static Intervention getInterventionSample2() {
        return new Intervention()
            .id(2L)
            .title("title2")
            .description("description2")
            .createdBy("createdBy2")
            .attachments("attachments2")
            .notes("notes2");
    }

    public static Intervention getInterventionRandomSampleGenerator() {
        return new Intervention()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .attachments(UUID.randomUUID().toString())
            .notes(UUID.randomUUID().toString());
    }
}
