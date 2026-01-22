// File: java-cache/src/cacheflow/StockData.java
package cacheflow;

public class StockData {
    private String symbol;
    private double price;
    private double change;
    private double changePercent;
    private long timestamp;
    
    public StockData(String symbol, double price, double change, double changePercent) {
        this.symbol = symbol;
        this.price = price;
        this.change = change;
        this.changePercent = changePercent;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Getters
    public String getSymbol() { return symbol; }
    public double getPrice() { return price; }
    public double getChange() { return change; }
    public double getChangePercent() { return changePercent; }
    public long getTimestamp() { return timestamp; }
    
    @Override
    public String toString() {
        return String.format("%s: $%.2f (%.2f%%)", symbol, price, changePercent);
    }
}