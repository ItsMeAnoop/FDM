package com.field.datamatics;


import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@SmallTest
public class ApplicationTest {

    @Test
    public void testPercentage_calculation() {
        assertEquals(calculatePercentage(6, 12), 50);
        assertEquals(calculatePercentage(1,25),4);
        assertEquals(calculatePercentage(3,23),13);
        assertEquals(calculatePercentage(3,12),25);
    }

    private int calculatePercentage(int a, int b) {

        float per = a * 100f/ b;
        return Math.round(per);
    }
}