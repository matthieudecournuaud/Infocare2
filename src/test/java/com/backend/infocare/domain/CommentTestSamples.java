package com.backend.infocare.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CommentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Comment getCommentSample1() {
        return new Comment()
            .id(1L)
            .title("title1")
            .type("type1")
            .visibility("visibility1")
            .description("description1")
            .editedBy("editedBy1")
            .attachments("attachments1")
            .responseToCommentId(1L);
    }

    public static Comment getCommentSample2() {
        return new Comment()
            .id(2L)
            .title("title2")
            .type("type2")
            .visibility("visibility2")
            .description("description2")
            .editedBy("editedBy2")
            .attachments("attachments2")
            .responseToCommentId(2L);
    }

    public static Comment getCommentRandomSampleGenerator() {
        return new Comment()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .visibility(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .editedBy(UUID.randomUUID().toString())
            .attachments(UUID.randomUUID().toString())
            .responseToCommentId(longCount.incrementAndGet());
    }
}
