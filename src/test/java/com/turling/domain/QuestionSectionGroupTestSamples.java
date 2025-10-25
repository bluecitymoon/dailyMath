package com.turling.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class QuestionSectionGroupTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static QuestionSectionGroup getQuestionSectionGroupSample1() {
        return new QuestionSectionGroup().id(1L).title("title1").baseGroupIds("baseGroupIds1");
    }

    public static QuestionSectionGroup getQuestionSectionGroupSample2() {
        return new QuestionSectionGroup().id(2L).title("title2").baseGroupIds("baseGroupIds2");
    }

    public static QuestionSectionGroup getQuestionSectionGroupRandomSampleGenerator() {
        return new QuestionSectionGroup()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .baseGroupIds(UUID.randomUUID().toString());
    }
}
