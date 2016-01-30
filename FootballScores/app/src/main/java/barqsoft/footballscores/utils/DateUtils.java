package barqsoft.footballscores.utils;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import barqsoft.footballscores.logger.Debug;

/**
 * Created by Amrendra Kumar on 30/01/16.
 */
public class DateUtils {

    public static String get12HoursTime(String time24){
        DateFormat f1 = new SimpleDateFormat("HH:mm");
        DateFormat f2 = new SimpleDateFormat("hh:mm aa");
        String time12;
        try {
            time12 = f2.format(f1.parse(time24)).toString();
        } catch (ParseException e) {
            e.printStackTrace();
            time12 = time24;
        }
        return time12;
    }
    public static String getLocalTime(String dateTime) {
        //"2016-01-31T19:30:00Z"
        if (!dateTime.contains("T")) {
            dateTime += "T00:00:00Z";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat localFormat = new SimpleDateFormat("yyyy-MM-dd't'HH:mm'z'");
        localFormat.setTimeZone(TimeZone.getDefault());
        String myDate = "";
        try {
            myDate = localFormat.format(dateFormat.parse(dateTime)).toString();
        } catch (ParseException e) {
            Debug.e("Cannot parse date[" + dateTime + "] : " + e.getLocalizedMessage(), false);
            myDate = dateTime;
            myDate = myDate.replace("T", "t");
            myDate = myDate.replace("Z", "z");
        }
        return myDate;
    }

    public static String getDateFromDateTime(@NonNull String dateTime) {
        //"2016-01-26T17:30:00Z"
        dateTime = getLocalTime(dateTime);
        return dateTime.substring(0, dateTime.indexOf("t"));
    }

    public static String getTimeFromDateTime(@NonNull String dateTime) {
        //"2016-01-26T17:30:00Z"
        dateTime = getLocalTime(dateTime);
        return dateTime.substring(dateTime.indexOf("t") + 1, dateTime.indexOf("z"));
    }
}
