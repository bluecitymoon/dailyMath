package com.turling.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class QuestionBaseGroupTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static QuestionBaseGroup getQuestionBaseGroupSample1() {
        return new QuestionBaseGroup().id(1L).title("title1").questionIds("questionIds1");
    }

    public static QuestionBaseGroup getQuestionBaseGroupSample2() {
        return new QuestionBaseGroup().id(2L).title("title2").questionIds("questionIds2");
    }

    public static QuestionBaseGroup getQuestionBaseGroupRandomSampleGenerator() {
        return new QuestionBaseGroup()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .questionIds(UUID.randomUUID().toString());
    }
}
