package co.paylot.android;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.paylot.android.models.HashedTxData;
import co.paylot.android.models.Merchant;
import co.paylot.android.models.PaylotParam;
import co.paylot.android.models.ProcessorItem;
import co.paylot.android.models.TxData;
import co.paylot.android.models.TxPayload;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayActivity extends AppCompatActivity implements ProcessorAdapter.OnItemClickListener {
    PaylotParam param;
    PayCountDownTimer countDownTimer;
    private RestService restService;
    private boolean merchantLoaded;
    private boolean processorsLoaded;
    private List<ProcessorItem> processors = new ArrayList<>();
    private ProcessorItem selectedProcessor;
    private Merchant merchant;
    private ProgressBar progressBar;
    private ImageView logoView;
    private TextView nameView, rateView, timerView;
    private LinearLayout paymentLayout;
    private ProcessorAdapter processorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        param = getIntent()
                .getParcelableExtra(PaylotConstants.PAY_REQUEST_PARAMS);

        restService = RestClient.getService();
        countDownTimer = new PayCountDownTimer();

        if (param == null) {
            onError("An error occurred while processing data");
            return;
        }

        SharedData.reset();
        initializeViews();
        signData();
        getMerchant();
        startAnimating();
    }

    private void initializeViews() {
        RecyclerView processorView = findViewById(R.id.processorView);
        processorAdapter = new ProcessorAdapter(this, processors);
        processorAdapter.setOnItemClickListener(this);
        processorView.setAdapter(processorAdapter);
        processorView.setLayoutManager(new GridLayoutManager(this, 2));

        paymentLayout = findViewById(R.id.payLayout);
        progressBar = findViewById(R.id.progressBar);
        logoView = findViewById(R.id.merchant_logo);
        nameView = findViewById(R.id.merchant_name);
        rateView = findViewById(R.id.tx_rate);
        timerView = findViewById(R.id.timer_view);
    }

    private void signData() {
        TxPayload payload = new TxPayload()
                .setEmail(param.getEmail())
                .setSendMail(param.isSendMail())
                .setName(param.getName());

        TxData body = new TxData()
                .setAmount(param.getAmount())
                .setCurrency(param.getCurrency())
                .setPayload(payload)
                .setKey(param.getPublicKey())
                .setReference(param.getReference());


        restService.signTransaction(body)
                .enqueue(new Callback<HashedTxData>() {
                    @Override
                    public void onResponse(@NonNull Call<HashedTxData> call,
                                           @NonNull Response<HashedTxData> response) {
                        if (response.isSuccessful()) {
                            processTransaction(response.body());
                        } else {
                            try {
                                Helper.log(response.errorBody().string());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            onError("Transaction not signed successfully.");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<HashedTxData> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        onError("Could not sign transaction");
                    }
                });
    }

    private void getMerchant() {
        restService.getMerchantByKey(param.getPublicKey())
                .enqueue(new Callback<Merchant>() {
                    @Override
                    public void onResponse(@NonNull Call<Merchant> call,
                                           @NonNull Response<Merchant> response) {
                        if (response.isSuccessful()) {
                            merchant = response.body();
                            merchantLoaded = true;

                            displayMerchant(merchant);

                            if (processorsLoaded)
                                stopAnimating();
                        } else {
                            try {
                                Helper.log(response.errorBody().string());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            onError(response.code() + ": Could not fetch merchant.");
                        }
                    }

                    @Override
                    public void onFailure(Call<Merchant> call, Throwable t) {
                        t.printStackTrace();
                        onError("Could not get merchant.");
                    }
                });
    }

    private void displayMerchant(Merchant merchant) {
        nameView.setText(merchant.getName());
        Glide.with(PayActivity.this)
                .load(merchant.getLogo().getThumb())
                .into(logoView);
    }

    private void processTransaction(HashedTxData data) {
        restService.processTransaction(data)
                .enqueue(new Callback<List<ProcessorItem>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<ProcessorItem>> call,
                                           @NonNull Response<List<ProcessorItem>> response) {
                        if (response.isSuccessful()) {
                            processorsLoaded = true;

                            if (response.body() != null) {
                                processors.addAll(response.body());
                                processorAdapter.notifyDataSetChanged();
                            }
                            if (merchantLoaded)
                                stopAnimating();
                        } else {
                            try {
                                Helper.log(response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            onError("Transaction not processed successfully.");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<ProcessorItem>> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        onError("Could not process transaction");
                    }
                });
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
    public void onItemClick(ProcessorItem item) {
        selectedProcessor = item;
        refreshRate();
    }

    private void refreshRate() {
        String rateText = selectedProcessor.getConversion();
        rateView.setText(rateText);
        rateView.setVisibility(View.VISIBLE);
    }

    public void getPaymentAddress(View view) {
        SharedData.setMerchant(merchant);
        SharedData.setTxData(selectedProcessor);

        Intent intent = new Intent(this, AddressActivity.class);
        startActivityForResult(intent, PaylotConstants.ADDRESS_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            setResult(resultCode, data);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        countDownTimer.cancel();
        super.onDestroy();
    }

    private void reset() {
        processors.clear();
        selectedProcessor = null;
        signData();
        startAnimating();
    }

    class PayCountDownTimer extends CountDownTimer {

        PayCountDownTimer() {
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
            reset();
        }
    }
}
