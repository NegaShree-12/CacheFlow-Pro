// File: src/cacheflow/StockCache.java
package cacheflow;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class StockCache {
    // CHANGE 1: Use ConcurrentHashMap instead of HashMap
    private ConcurrentHashMap<String, StockData> cache;
    
    // CHANGE 2: Atomic counters for thread safety
    private AtomicInteger hitCount = new AtomicInteger(0);
    private AtomicInteger missCount = new AtomicInteger(0);
    
    // NEW: Expiry manager
    private ExpiryManager expiryManager;
    
    // Constants
    private static final long DEFAULT_TTL = 60000; // 60 seconds
    
    public StockCache() {
        // Thread-safe map with 32 buckets (more for concurrency)
        cache = new ConcurrentHashMap<>(32, 0.75f, 16);
        
        // Start expiry manager
        expiryManager = new ExpiryManager(this);
        expiryManager.start();
        
        System.out.println("✅ Cache initialized with ConcurrentHashMap");
        System.out.println("   Thread-safe for multiple users!");
        System.out.println("   Default TTL: 60 seconds");
    }
    
    // NEW: Overloaded put with TTL parameter
    public void put(String symbol, double price, double change, 
                    double changePercent, long ttlMillis) {
        StockData stock = new StockData(symbol, price, change, 
                                        changePercent, ttlMillis);
        cache.put(symbol, stock);
        System.out.println("💾 Cached: " + stock);
    }
    
    // Original put with default TTL
    public void put(String symbol, double price, double change, 
                    double changePercent) {
        put(symbol, price, change, changePercent, DEFAULT_TTL);
    }
    
    // UPDATED get(): Check expiry before returning
    public StockData get(String symbol) {
        StockData stock = cache.get(symbol);
        
        if (stock != null) {
            if (stock.isExpired()) {
                // Auto-remove expired data
                cache.remove(symbol);
                missCount.incrementAndGet();
                System.out.println("⏰ EXPIRED: " + symbol + " was removed");
                return null;
            } else {
                hitCount.incrementAndGet();
                System.out.println("⚡ HIT: " + stock);
                return stock;
            }
        } else {
            missCount.incrementAndGet();
            System.out.println("❌ MISS: " + symbol + " not in cache");
            return null;
        }
    }
    
    // NEW: Cleanup method for ExpiryManager
    public int cleanupExpired() {
        int count = 0;
        for (String symbol : cache.keySet()) {
            StockData stock = cache.get(symbol);
            if (stock != null && stock.isExpired()) {
                cache.remove(symbol);
                count++;
            }
        }
        return count;
    }
    
    // UPDATED: Thread-safe size
    public int size() {
        return cache.size();
    }
    
    // UPDATED: Thread-safe contains (checks expiry)
    public boolean contains(String symbol) {
        StockData stock = cache.get(symbol);
        return stock != null && !stock.isExpired();
    }
    
    // NEW: Get cache with expiry info
    public void printAll() {
        System.out.println("\n📊 CACHE CONTENTS (" + size() + " stocks):");
        System.out.println("=".repeat(60));
        cache.values().forEach(stock -> {
            String status = stock.isExpired() ? "🕰️ EXPIRED" : "✅ VALID";
            System.out.println(stock + " " + status);
        });
        System.out.println("=".repeat(60));
    }
    
    // NEW: Get cache stats with thread-safe counters
    public void printStats() {
        int total = hitCount.get() + missCount.get();
        double hitRate = total > 0 ? (hitCount.get() * 100.0 / total) : 0;
        
        System.out.println("\n📈 CACHE STATISTICS:");
        System.out.println("Total requests: " + total);
        System.out.println("Cache hits: " + hitCount.get() + 
                         " (HIT RATE: " + String.format("%.2f", hitRate) + "%)");
        System.out.println("Cache misses: " + missCount.get());
        System.out.println("Current size: " + size() + " stocks");
        System.out.println("Thread-safe: ✅ Yes (ConcurrentHashMap)");
        System.out.println("Auto-expiry: ✅ Yes (60s TTL)");
    }
    
    // NEW: Graceful shutdown
    public void shutdown() {
        expiryManager.stop();
        System.out.println("🔌 Cache shutdown complete");
    }
}