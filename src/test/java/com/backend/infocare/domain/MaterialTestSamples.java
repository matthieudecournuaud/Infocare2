package com.backend.infocare.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MaterialTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Material getMaterialSample1() {
        return new Material()
            .id(1L)
            .name("name1")
            .type("type1")
            .manufacturer("manufacturer1")
            .model("model1")
            .statusMaterial("statusMaterial1")
            .note("note1")
            .serialNumber(1L);
    }

    public static Material getMaterialSample2() {
        return new Material()
            .id(2L)
            .name("name2")
            .type("type2")
            .manufacturer("manufacturer2")
            .model("model2")
            .statusMaterial("statusMaterial2")
            .note("note2")
            .serialNumber(2L);
    }

    public static Material getMaterialRandomSampleGenerator() {
        return new Material()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .manufacturer(UUID.randomUUID().toString())
            .model(UUID.randomUUID().toString())
            .statusMaterial(UUID.randomUUID().toString())
            .note(UUID.randomUUID().toString())
            .serialNumber(longCount.incrementAndGet());
    }
}
