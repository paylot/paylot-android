package co.paylot.android;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.Locale;

import co.paylot.android.models.Merchant;
import co.paylot.android.models.ProcessorItem;

public class StatusActivity extends AppCompatActivity {

    private TextView statusTitle, statusDesc;
    private ImageView logoView, statusImage;
    private TextView nameView, rateView, timerView;
    private LinearLayout errorBtnLayout;

    private CountDownTimer countDownTimer;
    private boolean isSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        initializeViews();
        setUpMerchant();
        setUpTxDetails();
        setUpStatusView();
    }

    private void setUpStatusView() {
        isSuccess = getIntent().getBooleanExtra("success", false);
        String message = getIntent().getStringExtra("message");

        if (isSuccess)
            setUpSuccessView();
        else
            setUpErrorView(message);
    }

    private void setUpSuccessView() {
        String message = String.format("Your payment of %s was received successfully.",
                SharedData.getTxData().getAmountData());

        statusDesc.setText(message);
        statusTitle.setText(R.string.paylot_payment_success);
        statusImage.setImageResource(R.drawable.paylot_ic_success);
        timerView.setVisibility(View.VISIBLE);

        countDownTimer = new CloseCountDownTimer();
        countDownTimer.start();
    }

    private void setUpErrorView(String message) {
        message = TextUtils.isEmpty(message) ?
                "Transaction could not be confirmed on time. " +
                        "Click wait if you have already paid or retry the transaction" :
                message;

        statusDesc.setText(message);
        statusTitle.setText(R.string.paylot_payment_fail);
        statusImage.setImageResource(R.drawable.paylot_ic_error);
        errorBtnLayout.setVisibility(View.VISIBLE);
    }

    private void initializeViews() {
        logoView = findViewById(R.id.merchant_logo);
        nameView = findViewById(R.id.merchant_name);
        rateView = findViewById(R.id.tx_rate);
        statusDesc = findViewById(R.id.status_desc);
        statusTitle = findViewById(R.id.status_title);
        statusImage = findViewById(R.id.status_image);
        timerView = findViewById(R.id.timer_view);
        errorBtnLayout = findViewById(R.id.error_btn_layout);
    }

    private void setUpMerchant() {
        Merchant merchant = SharedData.getMerchant();

        if (merchant == null) {
            onError("Could not get merchant details");
            return;
        }

        nameView.setText(merchant.getName());

        RequestOptions options = new RequestOptions()
                .fallback(R.drawable.paylot_sample_logo);

        Glide.with(this)
                .setDefaultRequestOptions(options)
                .load(merchant.getLogo().getThumb())
                .into(logoView);
    }

    private void setUpTxDetails() {
        ProcessorItem processorItem = SharedData.getTxData();
        if (processorItem == null) {
            onError("Could not get transaction data");
            return;
        }
        rateView.setText(processorItem.getConversion());
    }

    private void onError(String message) {
        setResult(RESULT_CANCELED);
        Helper.showToast(this, message);
        finish();
    }

    public void retry(View view) {
        Intent intent = new Intent();
        intent.putExtra("retry", true);
        setResult(PaylotConstants.RESULT_FAIL, intent);
        finish();
    }

    public void wait(View view) {
        Intent intent = new Intent();
        intent.putExtra("retry", false);
        setResult(PaylotConstants.RESULT_FAIL, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.onDestroy();
    }

    class CloseCountDownTimer extends CountDownTimer {

        CloseCountDownTimer() {
            super(PaylotConstants.CLOSE_TIME, PaylotConstants.COUNTDOWN_INTERVAL);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int secondsUntilFinished = (int) (millisUntilFinished / 1000);

            String timerText = String.format(Locale.getDefault(), "Closing in %ds", secondsUntilFinished);

            timerView.setText(timerText);
        }

        @Override
        public void onFinish() {
            Intent intent = new Intent();
            String currency = SharedData.getTxData()
                    .getCurrencyData().getSymbol();
            SharedData.getCurrentTx().setCurrency(currency);
            intent.putExtra("transaction", SharedData.getCurrentTx());
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
