package barqsoft.footballscores.model;

import barqsoft.footballscores.logger.Debug;

/**
 * Created by Amrendra Kumar on 30/01/16.
 */
public class Team {

    String name;
    String code;
    String shortName;
    String crestUrl;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCrestUrl() {
        return crestUrl;
    }

    public void setCrestUrl(String crestUrl) {
        this.crestUrl = crestUrl;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Team:" + name + "\n");
        sb.append("shortName : " + shortName + "\n");
        sb.append("crestUrl : " + crestUrl + "\n");
        String ret = sb.toString();
        Debug.i(ret, false);
        return ret;
    }
}
