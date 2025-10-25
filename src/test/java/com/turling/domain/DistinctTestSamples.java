package com.turling.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DistinctTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Distinct getDistinctSample1() {
        return new Distinct().id(1L).name("name1").pinyin("pinyin1");
    }

    public static Distinct getDistinctSample2() {
        return new Distinct().id(2L).name("name2").pinyin("pinyin2");
    }

    public static Distinct getDistinctRandomSampleGenerator() {
        return new Distinct().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).pinyin(UUID.randomUUID().toString());
    }
}
