package co.paylot.android.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with love by Dozie on 10/26/2018.
 */

public class TxDestination implements Parcelable {
    public static final Creator<TxDestination> CREATOR = new Creator<TxDestination>() {
        @Override
        public TxDestination createFromParcel(Parcel source) {
            return new TxDestination(source);
        }

        @Override
        public TxDestination[] newArray(int size) {
            return new TxDestination[size];
        }
    };
    private String address;

    private TxDestination(Parcel in) {
        this.address = in.readString();
    }

    public String getAddress() {
        return address;
    }

    public TxDestination setAddress(String address) {
        this.address = address;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.address);
    }
}
