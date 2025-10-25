package com.turling.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class QuestionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Question getQuestionSample1() {
        return new Question()
            .id(1L)
            .description("description1")
            .solution("solution1")
            .solutionExternalLink("solutionExternalLink1")
            .createBy("createBy1")
            .createByUserId(1L);
    }

    public static Question getQuestionSample2() {
        return new Question()
            .id(2L)
            .description("description2")
            .solution("solution2")
            .solutionExternalLink("solutionExternalLink2")
            .createBy("createBy2")
            .createByUserId(2L);
    }

    public static Question getQuestionRandomSampleGenerator() {
        return new Question()
            .id(longCount.incrementAndGet())
            .description(UUID.randomUUID().toString())
            .solution(UUID.randomUUID().toString())
            .solutionExternalLink(UUID.randomUUID().toString())
            .createBy(UUID.randomUUID().toString())
            .createByUserId(longCount.incrementAndGet());
    }
}
