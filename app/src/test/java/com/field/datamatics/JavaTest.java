package com.field.datamatics;

import android.test.suitebuilder.annotation.SmallTest;

import com.field.datamatics.utils.Utilities;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.junit.Assert.assertNull;

/**
 * Created by Jithz on 12/20/2015.
 */

@SmallTest
public class JavaTest {

    @Test
    public void test_float() {
        float[] f1 = null, f2 = null;

        assertNull(f1);
        assertNull(f2);

        int thisDay = 4;
        int lastDay = 1;
        for (int i = lastDay; i <= thisDay; i++) {
            System.out.println(i+"..");
        }
    }

    @Test
    public void date_test(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        String date = formatter.format(cal.getTime());
        System.out.println("date : "+date);
    }
}
