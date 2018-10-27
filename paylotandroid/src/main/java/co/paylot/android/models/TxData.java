package co.paylot.android.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with love by Dozie on 10/24/2018.
 */

public class TxData {

    private double amount;
    private boolean deposit;
    private String key;
    private String reference;
    private String currency;
    private TxPayload payload;

    public TxData() {
        deposit = true;
    }

    public boolean isDeposit() {
        return deposit;
    }

    public TxData setDeposit(boolean deposit) {
        this.deposit = deposit;
        return this;
    }

    public double getAmount() {
        return amount;
    }

    public TxData setAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public String getKey() {
        return key;
    }

    public TxData setKey(String key) {
        this.key = key;
        return this;
    }

    public String getReference() {
        return reference;
    }

    public TxData setReference(String reference) {
        this.reference = reference;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public TxData setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public TxPayload getPayload() {
        return payload;
    }

    public TxData setPayload(TxPayload payload) {
        this.payload = payload;
        return this;
    }

    public JSONObject toJSON() {
        JSONObject data = new JSONObject();

        try {
            data.putOpt("payload", getPayload().toJSON())
                    .putOpt("currency", getCurrency())
                    .putOpt("key", getKey())
                    .putOpt("reference", getReference())
                    .putOpt("amount", getAmount());
        } catch (JSONException bug) {
            bug.printStackTrace();
        }

        return data;
    }


}
