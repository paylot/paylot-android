package co.paylot.android;

/**
 * Created with love by Dozie on 10/24/2018.
 */

public class PaylotConstants {
    public static final int PAY_REQUEST_CODE = 15030;
    public static final int ADDRESS_REQUEST_CODE = 15040;
    public static final int STATUS_REQUEST_CODE = 15050;
    public static final String PAY_REQUEST_PARAMS = "Pay Request";
    public static final String PAYLOT_LOG_TAG = "PaylotLogger";

//    public static final String API_URL = "10.0.2.2:9000";
    public static final String API_URL = "api.paylot.co";
    public static final String API_BASE_URL = "https://" + API_URL;
    public static final String SOCKET_URL = "wss://" + API_URL;

    public static final long REFRESH_TIME = 5 * 60 * 1000;
    public static final long CLOSE_TIME = 5000;
    public static final long COUNTDOWN_INTERVAL = 1000;

    public static final int RESULT_FAIL = 4;
    public static final int RESULT_SUCCESS = 5;
}
