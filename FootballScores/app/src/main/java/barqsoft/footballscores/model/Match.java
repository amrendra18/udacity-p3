package barqsoft.footballscores.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Amrendra Kumar on 28/01/16.
 */
public class Match {

    @SerializedName("href")
    String match;

    public int getMatchId() {
        String matchId = match.substring(match.lastIndexOf("/") + 1);
        return Integer.parseInt(matchId);
    }


    @Override
    public String toString() {
        return "Match[" + match + "]\n";
    }
}
