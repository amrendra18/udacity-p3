package barqsoft.footballscores.api;

import barqsoft.footballscores.utils.AppConstants;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Amrendra Kumar on 27/01/16.
 */
public class FootballApiClientService {

    private static FootballApiClientService footballEndPointInterface = null;

    private FootballApiClientService() {
    }

    public static FootballApiClientService getInstance() {
        if (footballEndPointInterface == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(AppConstants.API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            footballEndPointInterface = retrofit.create(FootballApiClientService.class);
        }
        return footballEndPointInterface;
    }
}
