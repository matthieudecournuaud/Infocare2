package com.backend.infocare.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProcedureTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Procedure getProcedureSample1() {
        return new Procedure()
            .id(1L)
            .name("name1")
            .description("description1")
            .category("category1")
            .procedureId(1L)
            .stepByStepGuide("stepByStepGuide1")
            .estimatedTime(1)
            .requiredTools("requiredTools1")
            .skillsRequired("skillsRequired1")
            .safetyInstructions("safetyInstructions1")
            .reviewedBy("reviewedBy1")
            .attachments("attachments1");
    }

    public static Procedure getProcedureSample2() {
        return new Procedure()
            .id(2L)
            .name("name2")
            .description("description2")
            .category("category2")
            .procedureId(2L)
            .stepByStepGuide("stepByStepGuide2")
            .estimatedTime(2)
            .requiredTools("requiredTools2")
            .skillsRequired("skillsRequired2")
            .safetyInstructions("safetyInstructions2")
            .reviewedBy("reviewedBy2")
            .attachments("attachments2");
    }

    public static Procedure getProcedureRandomSampleGenerator() {
        return new Procedure()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .category(UUID.randomUUID().toString())
            .procedureId(longCount.incrementAndGet())
            .stepByStepGuide(UUID.randomUUID().toString())
            .estimatedTime(intCount.incrementAndGet())
            .requiredTools(UUID.randomUUID().toString())
            .skillsRequired(UUID.randomUUID().toString())
            .safetyInstructions(UUID.randomUUID().toString())
            .reviewedBy(UUID.randomUUID().toString())
            .attachments(UUID.randomUUID().toString());
    }
}
