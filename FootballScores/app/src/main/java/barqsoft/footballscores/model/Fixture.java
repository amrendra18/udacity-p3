package barqsoft.footballscores.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import barqsoft.footballscores.logger.Debug;

/**
 * Created by Amrendra Kumar on 27/01/16.
 */
public class Fixture {

    String date;
    String status;
    int matchday;
    String homeTeamName;
    String awayTeamName;
    Result result;


    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(homeTeamName + " vs " + awayTeamName + "\n");
        sb.append("date : " + date + "\n");
        sb.append("status : " + status + "\n");
        sb.append(result.toString());
        String ret = sb.toString();
        Debug.i(ret, false);
        return ret;
    }

    public static final class Response {

        @Expose
        public int count;

        @Expose
        public String timeFrameStart;

        @Expose
        public String timeFrameEnd;

        @Expose
        @SerializedName("fixtures")
        public List<Fixture> fixtures = new ArrayList<>();
    }

}
