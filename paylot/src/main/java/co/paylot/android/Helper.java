package co.paylot.android;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created with love by Dozie on 10/24/2018.
 */

public class Helper {
    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void log(String message) {
        Log.d(PaylotConstants.PAYLOT_LOG_TAG, message);
    }

    public static <T> void logResponseError(Response<T> response) {
        try {
            ResponseBody error = response.errorBody();

            if (error != null) {
                Helper.log(error.string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
