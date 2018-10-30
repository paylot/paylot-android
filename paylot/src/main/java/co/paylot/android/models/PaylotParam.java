package co.paylot.android.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with love by Dozie on 10/24/2018.
 */

public class PaylotParam implements Parcelable {
    public static final Creator<PaylotParam> CREATOR = new Creator<PaylotParam>() {
        @Override
        public PaylotParam createFromParcel(Parcel source) {
            return new PaylotParam(source);
        }

        @Override
        public PaylotParam[] newArray(int size) {
            return new PaylotParam[size];
        }
    };
    private String email;
    private double amount;
    private String publicKey;
    private String secretKey;
    private String reference;
    private String currency;
    private String name;
    private String subAccount;
    private boolean sendMail;

    public PaylotParam() {
    }

    protected PaylotParam(Parcel in) {
        this.email = in.readString();
        this.amount = in.readDouble();
        this.publicKey = in.readString();
        this.secretKey = in.readString();
        this.reference = in.readString();
        this.currency = in.readString();
        this.name = in.readString();
        this.subAccount = in.readString();
        this.sendMail = in.readByte() != 0;
    }

    public String getEmail() {
        return email;
    }

    public PaylotParam setEmail(String email) {
        this.email = email;
        return this;
    }

    public double getAmount() {
        return amount;
    }

    public PaylotParam setAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public PaylotParam setPublicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public PaylotParam setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public String getReference() {
        return reference;
    }

    public PaylotParam setReference(String reference) {
        this.reference = reference;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public PaylotParam setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public String getName() {
        return name;
    }

    public PaylotParam setName(String name) {
        this.name = name;
        return this;
    }

    public String getSubAccount() {
        return subAccount;
    }
    
    public PaylotParam setSubAccount(String subAccount) {
        this.subAccount = subAccount;
        return this;
    }

    public boolean isSendMail() {
        return sendMail;
    }

    public PaylotParam setSendMail(boolean sendMail) {
        this.sendMail = sendMail;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeDouble(this.amount);
        dest.writeString(this.publicKey);
        dest.writeString(this.secretKey);
        dest.writeString(this.reference);
        dest.writeString(this.currency);
        dest.writeString(this.name);
        dest.writeString(this.subAccount);
        dest.writeByte(this.sendMail ? (byte) 1 : (byte) 0);
    }
}
