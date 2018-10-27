package co.paylot.android;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static co.paylot.android.PaylotConstants.API_BASE_URL;

/**
 * Created with love by Dozie on 10/24/2018.
 */

public class RestClient {
    private static RestService restService;

    public synchronized static RestService getService(){
        if(restService == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(API_BASE_URL)
                    .build();

            restService = retrofit.create(RestService.class);
        }

        return restService;
    }
}
