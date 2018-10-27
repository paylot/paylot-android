package co.paylot.android;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import co.paylot.android.models.PaylotParam;

/**
 * Created with love by Dozie on 10/24/2018.
 */

public class PaylotManager {
    private String email;
    private double amount = 0;
    private String publicKey;
    private String secretKey;
    private String reference;
    private String currency = "NGN";
    private String name = "";
    private String meta = "";
    private String subAccount = "";
    private boolean sendMail = true;
    private Activity activity;

    public PaylotManager(@NonNull Activity activity) {
        this.activity = activity;
    }

    public PaylotManager setEmail(String email) {
        this.email = email;
        return this;
    }

    public PaylotManager setAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public PaylotManager setPublicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public PaylotManager setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public PaylotManager setReference(String reference) {
        this.reference = reference;
        return this;
    }

    public PaylotManager setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public PaylotManager setName(String name) {
        this.name = name;
        return this;
    }

    public PaylotManager setMeta(String meta) {
        this.meta = meta;
        return this;
    }

    public PaylotManager setSubAccount(String subAccount) {
        this.subAccount = subAccount;
        return this;
    }

    public PaylotManager setShouldSendMail(boolean sendMail) {
        this.sendMail = sendMail;
        return this;
    }

    public void initialize() {
        if (activity != null) {
            Intent intent = new Intent(activity, PayActivity.class);
            intent.putExtra(PaylotConstants.PAY_REQUEST_PARAMS, getParam());
            activity.startActivityForResult(intent, PaylotConstants.PAY_REQUEST_CODE);
        } else {
            Log.d(PaylotConstants.PAYLOT_LOG_TAG, "Context is required!");
        }
    }

    private PaylotParam getParam() {
        PaylotParam param = new PaylotParam();
        param.setAmount(amount)
                .setCurrency(currency)
                .setEmail(email)
                .setName(name)
                .setPublicKey(publicKey)
                .setSecretKey(secretKey)
                .setSendMail(sendMail)
                .setReference(reference)
                .setSubAccount(subAccount);

        return param;
    }
}
