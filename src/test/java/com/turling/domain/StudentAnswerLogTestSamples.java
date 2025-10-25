package com.turling.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class StudentAnswerLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static StudentAnswerLog getStudentAnswerLogSample1() {
        return new StudentAnswerLog().id(1L).studentId(1L).questionId(1L).answer("answer1").correct(1);
    }

    public static StudentAnswerLog getStudentAnswerLogSample2() {
        return new StudentAnswerLog().id(2L).studentId(2L).questionId(2L).answer("answer2").correct(2);
    }

    public static StudentAnswerLog getStudentAnswerLogRandomSampleGenerator() {
        return new StudentAnswerLog()
            .id(longCount.incrementAndGet())
            .studentId(longCount.incrementAndGet())
            .questionId(longCount.incrementAndGet())
            .answer(UUID.randomUUID().toString())
            .correct(intCount.incrementAndGet());
    }
}
