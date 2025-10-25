package com.turling.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class QuestionOptionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static QuestionOption getQuestionOptionSample1() {
        return new QuestionOption().id(1L).name("name1").type(1).imageUrl("imageUrl1").isAnswer(true);
    }

    public static QuestionOption getQuestionOptionSample2() {
        return new QuestionOption().id(2L).name("name2").type(2).imageUrl("imageUrl2").isAnswer(false);
    }

    public static QuestionOption getQuestionOptionRandomSampleGenerator() {
        return new QuestionOption()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .type(intCount.incrementAndGet())
            .imageUrl(UUID.randomUUID().toString())
            .isAnswer(random.nextBoolean());
    }
}
