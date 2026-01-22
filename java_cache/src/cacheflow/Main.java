// File: java-cache/src/cacheflow/Main.java
package cacheflow;

public class Main {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║           CACHEFLOW PRO v1.0             ║");
        System.out.println("║      Java Caching Engine - Day 1         ║");
        System.out.println("╚══════════════════════════════════════════╝");
        
        // Create cache
        StockCache cache = new StockCache();
        
        // Add stocks
        System.out.println("\n📥 POPULATING CACHE:");
        cache.put("TSLA", 235.42, 2.84, 1.22);
        cache.put("AAPL", 182.30, -0.92, -0.50);
        cache.put("GOOGL", 145.60, 1.15, 0.80);
        cache.put("NVDA", 890.12, 43.20, 5.10);
        
        // Test operations
        System.out.println("\n🔍 TESTING CACHE OPERATIONS:");
        cache.get("TSLA");      // Hit
        cache.get("AAPL");      // Hit
        cache.get("AMZN");      // Miss
        
        // Display
        cache.printAll();
        cache.printStats();
        
        // More tests
        System.out.println("\n🧪 ADDITIONAL TESTS:");
        System.out.println("Contains TSLA? " + cache.contains("TSLA"));
        System.out.println("Cache size: " + cache.size());
        
        cache.remove("GOOGL");
        System.out.println("After removal, contains GOOGL? " + cache.contains("GOOGL"));
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("✅ DAY 1: Basic HashMap cache implemented!");
        System.out.println("📁 Files created: StockData.java, StockCache.java, Main.java");
        System.out.println("🎯 Next: Add expiry (TTL) and thread safety");
    }
}