package com.turling.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CommunityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Community getCommunitySample1() {
        return new Community().id(1L).name("name1").studentsCount(1);
    }

    public static Community getCommunitySample2() {
        return new Community().id(2L).name("name2").studentsCount(2);
    }

    public static Community getCommunityRandomSampleGenerator() {
        return new Community().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).studentsCount(intCount.incrementAndGet());
    }
}
