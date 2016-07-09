package com.field.datamatics;

import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;

import java.util.Calendar;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Jithz on 12/19/2015.
 */

@SmallTest
public class CalendarTest {

    @Test
    public void testCalendar() {

        Calendar lastWeek = Calendar.getInstance();
        lastWeek.set(2015, 12, 9);
        System.out.println("this day : " + lastWeek.get(Calendar.DAY_OF_MONTH));
        if (lastWeek.get(Calendar.DAY_OF_MONTH) < 8) {
            lastWeek.add(Calendar.DAY_OF_MONTH, 1 - lastWeek.get(Calendar.DAY_OF_MONTH));
        } else
            lastWeek.add(Calendar.DAY_OF_MONTH, -7);
        System.out.println("Last week day is " + lastWeek.get(Calendar.DAY_OF_MONTH));
        assertEquals(lastWeek.get(Calendar.DAY_OF_MONTH), 2);
    }
}
