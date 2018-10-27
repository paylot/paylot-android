package co.paylot.android.models;

/**
 * Created with love by Dozie on 10/24/2018.
 */

public class HashedTxData extends TxData {
    private String hash;

    public String getHash() {
        return hash;
    }

    public HashedTxData setHash(String hash) {
        this.hash = hash;
        return this;
    }
}
