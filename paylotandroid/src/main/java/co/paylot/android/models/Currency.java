package co.paylot.android.models;

/**
 * Created with love by Dozie on 10/24/2018.
 */

public class Currency {
    private String id;
    private String name;
    private String symbol;

    public String getId() {
        return id;
    }

    public Currency setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Currency setName(String name) {
        this.name = name;
        return this;
    }

    public String getSymbol() {
        return symbol;
    }

    public Currency setSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }
}
