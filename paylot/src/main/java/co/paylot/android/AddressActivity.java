package co.paylot.android;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import co.paylot.android.models.Merchant;
import co.paylot.android.models.ProcessorItem;
import co.paylot.android.models.Transaction;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static co.paylot.android.PaylotConstants.RESULT_FAIL;
import static co.paylot.android.PaylotConstants.SOCKET_URL;
import static co.paylot.android.PaylotConstants.STATUS_REQUEST_CODE;

public class AddressActivity extends AppCompatActivity implements View.OnClickListener {
    Merchant merchant;
    Transaction transaction;
    ProcessorItem processorItem;
    Button openWalletButton;
    PayCountDownTimer countDownTimer;
    ProgressBar progressBar;
    LinearLayout paymentLayout;
    RestService restService;
    WebSocket webSocket;
    boolean isDestroyed;
    private ImageView logoView, qrView;
    private TextView nameView, rateView,
            timerView, addressView, amountView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        countDownTimer = new PayCountDownTimer();
        restService = RestClient.getService();
        merchant = SharedData.getMerchant();
        processorItem = SharedData.getTxData();

        if (merchant == null || processorItem == null) {
            finish();
        }

        transaction = SharedData.getCurrentTx();

        initializeViews();
        createTransaction();
        setUpMerchant();
    }

    private void initiateWebSocket() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
        Request request = new Request.Builder()
                .url(SOCKET_URL)
                .build();

        webSocket = client.newWebSocket(request, new TransactionUpdateListener());
    }

    private void createTransaction() {
        startAnimating();
        Call<Transaction> transactionRequest = transaction == null ?
                restService.createTransaction(processorItem) :
                restService.updateTransaction(transaction.getId(), processorItem);

        transactionRequest.enqueue(new Callback<Transaction>() {
            @Override
            public void onResponse(@NonNull Call<Transaction> call, @NonNull Response<Transaction> response) {
                if (response.isSuccessful()) {
                    transaction = response.body();
                    SharedData.setCurrentTx(transaction);
                    setUpTxDetails();
                    initiateWebSocket();
                    stopAnimating();
                } else {
                    Helper.logResponseError(response);
                    onError("Error " + response.code() + ": Could not initiate transaction");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Transaction> call, @NonNull Throwable t) {
                onError("Could not initiate transaction");
            }
        });
    }

    private void setUpTxDetails() {
        openWalletButton.setVisibility(hasWallet() ? View.VISIBLE : View.GONE);

        rateView.setText(processorItem.getConversion());
        amountView.setText(processorItem.getAmountData());

        String address = transaction.getDestination().getAddress();

        addressView.setText(address);

        String qrUrl = String.format("https://chart.googleapis.com/chart?chs=512x512&cht=qr&chl=%s:%s?amount=%s",
                processorItem.getCurrencyData()
                        .getName()
                        .toLowerCase(),
                address,
                processorItem.getAmount());

        RequestOptions options = new RequestOptions()
                .placeholder(R.color.paylot_lightGrey);

        Glide.with(this)
                .setDefaultRequestOptions(options)
                .load(qrUrl)
                .into(qrView);
    }

    private void setUpMerchant() {
        nameView.setText(merchant.getName());

        RequestOptions options = new RequestOptions()
                .fallback(R.drawable.paylot_sample_logo);

        Glide.with(this)
                .setDefaultRequestOptions(options)
                .load(merchant.getLogo().getThumb())
                .into(logoView);
    }

    private void initializeViews() {
        logoView = findViewById(R.id.merchant_logo);
        qrView = findViewById(R.id.qr_view);
        nameView = findViewById(R.id.merchant_name);
        rateView = findViewById(R.id.tx_rate);
        timerView = findViewById(R.id.timer_view);
        addressView = findViewById(R.id.address_view);
        amountView = findViewById(R.id.amount_view);
        progressBar = findViewById(R.id.progressBar);
        paymentLayout = findViewById(R.id.payLayout);

        qrView.setOnClickListener(this);
        addressView.setOnClickListener(this);
        amountView.setOnClickListener(this);

        openWalletButton = findViewById(R.id.open_wallet_view);
    }

    private boolean hasWallet() {
        Intent walletIntent = getWalletIntent();

        return walletIntent.resolveActivity(getPackageManager()) != null;
    }

    private void stopAnimating() {
        paymentLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        countDownTimer.start();
    }

    private void startAnimating() {
        paymentLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void onError(String message) {
        setResult(RESULT_CANCELED);
        Helper.showToast(this, message);
        finish();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.amount_view)
            copy("amount", String.valueOf(processorItem.getAmount()));
        else if (id == R.id.address_view || id == R.id.qr_view)
            copy("wallet address", transaction.getDestination().getAddress());
    }

    private void copy(String label, String message) {
        ClipboardManager clipboard = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clip = ClipData.newPlainText(label, message);

        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            Helper.showToast(this, String.format("Copied %s to clipboard", label));
        }
    }

    public void changeCurrency(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void openInWallet(View view) {
        Intent i = getWalletIntent();
        startActivity(i);
    }

    @NonNull
    private Intent getWalletIntent() {
        String url = String.format(Locale.getDefault(), "%s:%s?amount=%f",
                processorItem.getCurrencyData().getName().toLowerCase(),
                transaction.getDestination().getAddress(),
                processorItem.getAmount());

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        return i;
    }

    private void onTransactionSuccess() {
        webSocket.cancel();
        countDownTimer.cancel();
        if(!isDestroyed) {
            Intent intent = new Intent(this, StatusActivity.class);
            intent.putExtra("success", true);
            startActivityForResult(intent, STATUS_REQUEST_CODE);
        }
    }

    private void onTransactionFailed() {
        webSocket.cancel();
        countDownTimer.cancel();
        Intent intent = new Intent(this, StatusActivity.class);
        intent.putExtra("success", false);

        startActivityForResult(intent, STATUS_REQUEST_CODE);
    }

    @Override
    protected void onDestroy() {
        isDestroyed = true;
        countDownTimer.cancel();
        webSocket.cancel();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        changeCurrency(null);
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == STATUS_REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_CANCELED:
                case RESULT_OK:
                    setResult(resultCode, data);
                    finish();
                    break;
                case RESULT_FAIL:
                    handleError(data);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleError(Intent data) {
        boolean retry = data.getBooleanExtra("retry", false);

        if (retry) {
            setResult(RESULT_CANCELED);
            finish();
        } else {
            timerView.setVisibility(View.GONE);
            tryConfirmTransaction(false);
            initiateWebSocket();
        }
    }

    private void tryConfirmTransaction(final boolean closeOnFail) {
        restService.getTransaction(transaction.getId())
                .enqueue(new Callback<Transaction>() {
                    @Override
                    public void onResponse(@NonNull Call<Transaction> call,
                                           @NonNull Response<Transaction> response) {
                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().isSent()) {
                            onTransactionSuccess();
                        }else if(closeOnFail){
                            Helper.logResponseError(response);
                            onTransactionFailed();
                        } else {
                            Helper.logResponseError(response);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Transaction> call,
                                          @NonNull Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    class PayCountDownTimer extends CountDownTimer {

        PayCountDownTimer() {
//            super(30000, PaylotConstants.COUNTDOWN_INTERVAL);
            super(PaylotConstants.REFRESH_TIME, PaylotConstants.COUNTDOWN_INTERVAL);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int secondsUntilFinished = (int) (millisUntilFinished / 1000);
            int minutesRemaining = secondsUntilFinished / 60;
            int secondsRemaining = secondsUntilFinished - minutesRemaining * 60;

            String timerText;
            if (minutesRemaining > 0) {
                timerText = String.format(Locale.getDefault(), "%dm: %ds remaining", minutesRemaining, secondsRemaining);
            } else {
                timerText = String.format(Locale.getDefault(), "%ds remaining", secondsRemaining);
            }
            timerView.setText(timerText);
        }

        @Override
        public void onFinish() {
            tryConfirmTransaction(true);
        }
    }

    class TransactionUpdateListener extends WebSocketListener {
        @Override
        public void onMessage(WebSocket webSocket, String text) {
            try {
                JSONObject data = new JSONObject(text);
                JSONObject txData = data.optJSONObject("update")
                        .optJSONObject("transaction");
                Helper.log("Update " + txData.toString());
                boolean sent = txData.optBoolean("sent");
                String id = txData.optString("_id");

                if (transaction.getId().equals(id)
                        && sent
                        && !isDestroyed) {
                    onTransactionSuccess();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onMessage(webSocket, text);
        }

        @Override
        public void onOpen(WebSocket webSocket, okhttp3.Response response) {
            Helper.log("Socket connected successfully.");
            super.onOpen(webSocket, response);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            Helper.log("Socket disconnected successfully.");
            super.onClosed(webSocket, code, reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, @Nullable okhttp3.Response response) {
            Helper.log("Socket connection failed.");
            super.onFailure(webSocket, t, response);
        }
    }
}
