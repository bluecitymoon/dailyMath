package com.turling.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class QuestionCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static QuestionCategory getQuestionCategorySample1() {
        return new QuestionCategory().id(1L).name("name1");
    }

    public static QuestionCategory getQuestionCategorySample2() {
        return new QuestionCategory().id(2L).name("name2");
    }

    public static QuestionCategory getQuestionCategoryRandomSampleGenerator() {
        return new QuestionCategory().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
