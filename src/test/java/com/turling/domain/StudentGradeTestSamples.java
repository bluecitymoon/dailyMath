package com.turling.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class StudentGradeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static StudentGrade getStudentGradeSample1() {
        return new StudentGrade().id(1L).name("name1").index(1).term(1);
    }

    public static StudentGrade getStudentGradeSample2() {
        return new StudentGrade().id(2L).name("name2").index(2).term(2);
    }

    public static StudentGrade getStudentGradeRandomSampleGenerator() {
        return new StudentGrade()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .index(intCount.incrementAndGet())
            .term(intCount.incrementAndGet());
    }
}
