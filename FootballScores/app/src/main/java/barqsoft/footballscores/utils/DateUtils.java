package barqsoft.footballscores.utils;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import barqsoft.footballscores.logger.Debug;

/**
 * Created by Amrendra Kumar on 30/01/16.
 */
public class DateUtils {

    public static String getLocalTime(String dateTime) {
        //"2016-01-31T19:30:00Z"
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-ddXHH:mm:ssY", Locale.getDefault());
        //simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String myDate = "";
        try {
            myDate = simpleDateFormat.parse(dateTime).toString();
        } catch (ParseException e) {
            Debug.e("Cannot parse date[" + dateTime + "] : " + e.getLocalizedMessage(), false);
        }
        return myDate;
    }

    public static String getDateFromDateTime(@NonNull String dateTime) {
        //"2016-01-26T17:30:00Z"
        return dateTime.substring(0, dateTime.indexOf("T"));
    }

    public static String getTimeFromDateTime(@NonNull String dateTime) {
        //"2016-01-26T17:30:00Z"
        return dateTime.substring(dateTime.indexOf("T") + 1, dateTime.indexOf("Z"));
    }
}
