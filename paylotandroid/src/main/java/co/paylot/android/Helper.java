package co.paylot.android;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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
}
