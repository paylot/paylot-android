package co.paylot.android.models;

import java.text.DecimalFormat;
import java.util.Locale;

/**
 * Created with love by Dozie on 10/24/2018.
 */

public class ProcessorItem extends HashedTxData {
    private Currency currencyData;
    private String storeAs;
    private double nairaValue;
    private double storedValue;
    private boolean selected;
    private RealAmount realValue;
    private TransactionSubAccount subAccount;

    public ProcessorItem() {

    }

    public boolean isSelected() {
        return selected;
    }

    public ProcessorItem setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }

    public Currency getCurrencyData() {
        return currencyData;
    }

    public String getStoreAs() {
        return storeAs;
    }

    public String getAmountData() {
        return String.format(Locale.getDefault(), "%f %s", getAmount(), currencyData.getSymbol());
    }

    public double getNairaValue() {
        return nairaValue;
    }

    public double getStoredValue() {
        return storedValue;
    }

    public RealAmount getRealValue() {
        return realValue;
    }

    public TransactionSubAccount getSubAccount() {
        return subAccount;
    }

    public String getCurrency() {
        return getCurrency();
    }

    public String getConversion() {
        double toAmount = getAmount();
        String toCurrency = getCurrencyData().getSymbol();
        String fromAmount = getRealValue().toString();

        return String.format(Locale.getDefault(), "%s = %f %s",
                fromAmount, toAmount, toCurrency);
    }

    public class RealAmount {
        private String currency;
        private double amount;

        public RealAmount(String currency, double amount) {
            this.currency = currency;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return String.format("%s %s", new DecimalFormat("#.#######").format(amount), currency);
        }
    }

    private class TransactionSubAccount {
        private String id;
        private double amount;

        public TransactionSubAccount(String id, double amount) {
            this.id = id;
            this.amount = amount;
        }
    }
}


