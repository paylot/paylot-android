package co.paylot.android.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Created with love by Dozie on 10/25/2018.
 */

public class Transaction implements Parcelable {
    public static final Parcelable.Creator<Transaction> CREATOR = new Parcelable.Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel source) {
            return new Transaction(source);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };
    @SerializedName("_id")
    private String id;
    private TxDestination destination;
    private String reference;
    private boolean sent;
    private boolean confirmed;
    private double amount;
    private double amountSent;
    private transient String currency;

    public Transaction() {
    }

    protected Transaction(Parcel in) {
        this.id = in.readString();
        this.destination = in.readParcelable(TxDestination.class.getClassLoader());
        this.reference = in.readString();
        this.sent = in.readByte() != 0;
        this.confirmed = in.readByte() != 0;
        this.amount = in.readDouble();
        this.amountSent = in.readDouble();
        this.currency = in.readString();
    }

    public String getId() {
        return id;
    }

    public Transaction setId(String id) {
        this.id = id;
        return this;
    }

    public TxDestination getDestination() {
        return destination;
    }

    public Transaction setDestination(TxDestination destination) {
        this.destination = destination;
        return this;
    }

    public String getReference() {
        return reference;
    }

    public Transaction setReference(String reference) {
        this.reference = reference;
        return this;
    }

    public boolean isSent() {
        return sent;
    }

    public Transaction setSent(boolean sent) {
        this.sent = sent;
        return this;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Transaction setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
        return this;
    }

    public double getAmount() {
        return amount;
    }

    public Transaction setAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public double getAmountSent() {
        return amountSent;
    }

    public Transaction setAmountSent(double amountSent) {
        this.amountSent = amountSent;
        return this;
    }

    public String getCurrency() {
        return TextUtils.isEmpty(currency) ? "" : currency;
    }

    public Transaction setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public String getAmountData() {
        return String.format(Locale.getDefault(),"%f %s", getAmount(), getCurrency().toUpperCase());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeParcelable(this.destination, flags);
        dest.writeString(this.reference);
        dest.writeByte(this.sent ? (byte) 1 : (byte) 0);
        dest.writeByte(this.confirmed ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.amount);
        dest.writeDouble(this.amountSent);
        dest.writeString(this.currency);
    }
}
