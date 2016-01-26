package barqsoft.footballscores.api;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by Amrendra Kumar on 27/01/16.
 */
public class FootballApiClientService {

    public static final String API_BASE_URL = "http://api.football-data.org/";

    private static FootballEndpointServiceInterface footballEndPointInterface = null;

    private FootballApiClientService() {
    }

    public static FootballEndpointServiceInterface getInstance() {
        if (footballEndPointInterface == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            footballEndPointInterface = retrofit.create(FootballEndpointServiceInterface.class);
        }
        return footballEndPointInterface;
    }
}
