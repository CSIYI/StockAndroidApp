package com.example.stockproject.ui.home;

public class StockModel {
    private String ticker;
    private String last_price;
    private String change;

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getLast_price() {
        return last_price;
    }

    public void setLast_price(String last_price) {
        this.last_price = last_price;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }
}
