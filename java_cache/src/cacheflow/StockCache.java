// File: java-cache/src/cacheflow/StockCache.java
package cacheflow;

import java.util.HashMap;
import java.util.Map;

public class StockCache {
    private Map<String, StockData> cache;
    private int hitCount = 0;
    private int missCount = 0;
    
    public StockCache() {
        cache = new HashMap<>(16, 0.75f);
        System.out.println("✅ Cache initialized with HashMap (16 buckets, 0.75 load factor)");
    }
    
    public void put(String symbol, double price, double change, double changePercent) {
        StockData stock = new StockData(symbol, price, change, changePercent);
        cache.put(symbol, stock);
        System.out.println("💾 Cached: " + stock);
    }
    
    public StockData get(String symbol) {
        if (cache.containsKey(symbol)) {
            hitCount++;
            System.out.println("⚡ HIT: Retrieved from cache: " + cache.get(symbol));
            return cache.get(symbol);
        } else {
            missCount++;
            System.out.println("❌ MISS: " + symbol + " not in cache");
            return null;
        }
    }
    
    public void remove(String symbol) {
        if (cache.remove(symbol) != null) {
            System.out.println("🗑️ Removed: " + symbol);
        }
    }
    
    public boolean contains(String symbol) {
        return cache.containsKey(symbol);
    }
    
    public int size() {
        return cache.size();
    }
    
    public void clear() {
        cache.clear();
        System.out.println("🧹 Cache cleared");
    }
    
    public void printAll() {
        System.out.println("\n📊 CACHE CONTENTS (" + size() + " stocks):");
        System.out.println("=".repeat(40));
        cache.values().forEach(System.out::println);
        System.out.println("=".repeat(40));
    }
    
    public void printStats() {
        int total = hitCount + missCount;
        double hitRate = total > 0 ? (hitCount * 100.0 / total) : 0;
        System.out.println("\n📈 CACHE STATISTICS:");
        System.out.println("Total requests: " + total);
        System.out.println("Cache hits: " + hitCount + " (HIT RATE: " + String.format("%.2f", hitRate) + "%)");
        System.out.println("Cache misses: " + missCount);
        System.out.println("Current size: " + size() + " stocks");
    }
}