package barqsoft.footballscores.utils;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.TimeZone;

import barqsoft.footballscores.BuildConfig;
import barqsoft.footballscores.logger.Debug;

/**
 * Created by Amrendra Kumar on 30/01/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 21)
public class DateUtilsTest {

    @Before
    public void setUp() throws Exception {
        //for testing purpose, setting default timezone IST
        TimeZone.setDefault(TimeZone.getTimeZone("IST"));
    }

    @After
    public void tornDown() throws Exception {
    }

    @Test
    public void testGetLocalTime() throws Exception {
        String utcTime = "2016-01-31T19:30:00Z";
        String expectedLocalTime = "2016-02-01t01:00z";
        String result = DateUtils.getLocalTime(utcTime);
        Debug.e("result : " + result, false);
        Assert.assertEquals("Local time doesnot match", expectedLocalTime, result);
    }

    @Test
    public void testGetLocalDateTimeWithNoTime() throws Exception {
        String utcTime = "2016-01-31";
        String expectedLocalTime = "2016-01-31t05:30z";
        String result = DateUtils.getLocalTime(utcTime);
        Debug.e("result : " + result, false);
        Assert.assertEquals("Local time doesnot match", expectedLocalTime, result);
    }

    @Test
    public void test24to12HoursConversion() throws Exception {
        String time24[] = {
                "00:00",
                "00:01",
                "00:59",
                "01:00",
                "01:01",
                "01:59",
                "09:30",
                "11:59",
                "12:00",
                "12:01",
                "18:18",
                "23:59"
        };
        String time12[] = {
                "12:00 AM",
                "12:01 AM",
                "12:59 AM",
                "01:00 AM",
                "01:01 AM",
                "01:59 AM",
                "09:30 AM",
                "11:59 AM",
                "12:00 PM",
                "12:01 PM",
                "06:18 PM",
                "11:59 PM"
        };
        String result;
        for (int i = 0; i < time24.length; i++) {
            result = DateUtils.get12HoursTime(time24[i]);
            Assert.assertEquals("24 time doesnot match", time12[i], result);
        }
    }
}
