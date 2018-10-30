package co.paylot.android.models;

/**
 * Created with love by Dozie on 10/24/2018.
 */

public class TxData {

    private double amount;
    private boolean deposit;
    private String key;
    private String reference;
    private String currency;
    private String subaccount;
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

    public String getSubaccount() {
        return subaccount;
    }

    public TxData setSubaccount(String subaccount) {
        this.subaccount = subaccount;
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


}
