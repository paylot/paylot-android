package co.paylot.android;

import co.paylot.android.models.Merchant;
import co.paylot.android.models.ProcessorItem;
import co.paylot.android.models.Transaction;

/**
 * Created with love by Dozie on 10/25/2018.
 */

public class SharedData {
    private static Merchant merchant;
    private static ProcessorItem txData;
    private static Transaction currentTx;

    public static Merchant getMerchant() {
        return merchant;
    }

    public static void setMerchant(Merchant merchant) {
        SharedData.merchant = merchant;
    }

    public static ProcessorItem getTxData() {
        return txData;
    }

    public static void setTxData(ProcessorItem txData) {
        SharedData.txData = txData;
    }

    public static Transaction getCurrentTx() {
        return currentTx;
    }

    public static void setCurrentTx(Transaction currentTx) {
        SharedData.currentTx = currentTx;
    }

    public static void reset(){
        currentTx = null;
        txData = null;
        merchant = null;
    }
}
