// File: src/cacheflow/StockData.java
package cacheflow;

public class StockData {
    private String symbol;
    private double price;
    private double change;
    private double changePercent;
    private long timestamp;
    private long expiryTime;  // NEW FIELD: When this data expires
    
    public StockData(String symbol, double price, double change, 
                     double changePercent, long ttlMillis) {
        this.symbol = symbol;
        this.price = price;
        this.change = change;
        this.changePercent = changePercent;
        this.timestamp = System.currentTimeMillis();
        this.expiryTime = this.timestamp + ttlMillis; // Calculate expiry
    }
    
    // NEW METHOD: Check if data is expired
    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
    
    // NEW METHOD: How many seconds until expiry
    public long secondsUntilExpiry() {
        long remaining = (expiryTime - System.currentTimeMillis()) / 1000;
        return Math.max(0, remaining);
    }
    
    // Getters (unchanged)
    public String getSymbol() { return symbol; }
    public double getPrice() { return price; }
    public double getChange() { return change; }
    public double getChangePercent() { return changePercent; }
    public long getTimestamp() { return timestamp; }
    public long getExpiryTime() { return expiryTime; } // New getter
    
    @Override
    public String toString() {
        String status = isExpired() ? "EXPIRED" : "VALID";
        return String.format("%s: $%.2f (%.2f%%) [%s, expires in %ds]", 
                             symbol, price, changePercent, 
                             status, secondsUntilExpiry());
    }
}