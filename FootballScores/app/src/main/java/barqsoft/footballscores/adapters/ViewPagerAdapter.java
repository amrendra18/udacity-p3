package barqsoft.footballscores.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.format.Time;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import barqsoft.footballscores.R;
import barqsoft.footballscores.app.fragments.MainScreenFragment;

/**
 * Created by Amrendra Kumar on 27/01/16.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<MainScreenFragment> mFragmentList = new ArrayList<>();
    Context mContext;

    public ViewPagerAdapter(FragmentManager manager, Context context) {
        super(manager);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(MainScreenFragment fragment) {
        mFragmentList.add(fragment);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getDayName(System.currentTimeMillis() + ((position - 2) * 86400000));
    }

    public String getDayName(long dateInMillis) {
        // If the date is today, return the localized version of "Today" instead of the actual
        // day name.

        Time t = new Time();
        t.setToNow();
        int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
        int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);
        if (julianDay == currentJulianDay) {
            return mContext.getString(R.string.today);
        } else if (julianDay == currentJulianDay + 1) {
            return mContext.getString(R.string.tomorrow);
        } else if (julianDay == currentJulianDay - 1) {
            return mContext.getString(R.string.yesterday);
        } else {
            Time time = new Time();
            time.setToNow();
            // Otherwise, the format is just the day of the week (e.g "Wednesday".
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            return dayFormat.format(dateInMillis);
        }
    }
}
