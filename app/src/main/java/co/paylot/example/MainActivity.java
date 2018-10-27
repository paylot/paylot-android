package co.paylot.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import co.paylot.android.PaylotConstants;
import co.paylot.android.PaylotManager;
import co.paylot.android.models.Transaction;

public class MainActivity extends AppCompatActivity {

    public static final String DEFAULT_EMAIL = "dosky.ogbo91@gmail.com";
    private TextInputEditText amountInput, emailInput;
    private TextView amountText, referenceText;
    private LinearLayout paySuccessLayout;
    private String email;
    private double amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        amountInput = findViewById(R.id.amount_input);
        emailInput = findViewById(R.id.email_input);
        amountText = findViewById(R.id.amount_text);
        referenceText = findViewById(R.id.reference_text);
        paySuccessLayout = findViewById(R.id.pay_success_layout);
    }

    public void pay(View view) {
        if(inputValid()) {
            PaylotManager paylotManager = new PaylotManager(this)
                    .setAmount(amount)
                    .setCurrency("NGN")
                    .setEmail(email)
                    .setPublicKey(BuildConfig.PaylotPublicKey)
                    .setSecretKey(BuildConfig.PaylotSecretKey)
                    .setReference(String.valueOf(new Date().getTime()));

            paylotManager.initialize();
        }
    }

    private boolean inputValid() {
        String email = emailInput.getText().toString();
        String amount = amountInput.getText().toString();

        if(TextUtils.isEmpty(amount) || Double.parseDouble(amount) < 100) {
            showAlert("Invalid amount. Please, enter a minimum of 100.");
            return false;
        }

        if(!TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showAlert("Invalid email. Please, check and try again.");
            return false;
        }

        this.email = TextUtils.isEmpty(email) ? DEFAULT_EMAIL : email;
        this.amount = Double.parseDouble(amount);
        return true;
    }

    private void showAlert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PaylotConstants.PAY_REQUEST_CODE && resultCode == RESULT_OK){
            Transaction transaction = data.getParcelableExtra("transaction");
            paySuccessLayout.setVisibility(View.VISIBLE);
            referenceText.setText(String.format("Reference: %s", transaction.getReference()));
            amountText.setText(String.format("Amount: %s", transaction.getAmountData()));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void clear(View view) {
        paySuccessLayout.setVisibility(View.GONE);
    }
}
