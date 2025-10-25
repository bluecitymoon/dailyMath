package com.turling.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SchoolTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static School getSchoolSample1() {
        return new School().id(1L).name("name1").registeredStudentsCount(1).pinyin("pinyin1");
    }

    public static School getSchoolSample2() {
        return new School().id(2L).name("name2").registeredStudentsCount(2).pinyin("pinyin2");
    }

    public static School getSchoolRandomSampleGenerator() {
        return new School()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .registeredStudentsCount(intCount.incrementAndGet())
            .pinyin(UUID.randomUUID().toString());
    }
}
