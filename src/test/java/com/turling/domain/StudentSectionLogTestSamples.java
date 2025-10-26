package com.turling.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class StudentSectionLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static StudentSectionLog getStudentSectionLogSample1() {
        return new StudentSectionLog().id(1L).studentId(1L).sectionId(1L).totalCount(1).finishedCount(1);
    }

    public static StudentSectionLog getStudentSectionLogSample2() {
        return new StudentSectionLog().id(2L).studentId(2L).sectionId(2L).totalCount(2).finishedCount(2);
    }

    public static StudentSectionLog getStudentSectionLogRandomSampleGenerator() {
        return new StudentSectionLog()
            .id(longCount.incrementAndGet())
            .studentId(longCount.incrementAndGet())
            .sectionId(longCount.incrementAndGet())
            .totalCount(intCount.incrementAndGet())
            .finishedCount(intCount.incrementAndGet());
    }
}
