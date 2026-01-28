// File: src/cacheflow/Main.java
package cacheflow;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║           CACHEFLOW PRO v2.0             ║");
        System.out.println("║     Day 2: Expiry + Thread Safety        ║");
        System.out.println("╚══════════════════════════════════════════╝");
        
        // Create cache with new features
        StockCache cache = new StockCache();
        
        // Test 1: Add stocks with different TTLs
        System.out.println("\n📥 POPULATING CACHE WITH DIFFERENT TTLS:");
        cache.put("TSLA", 235.42, 2.84, 1.22, 10000);      // 10 seconds
        cache.put("AAPL", 182.30, -0.92, -0.50, 20000);    // 20 seconds
        cache.put("GOOGL", 145.60, 1.15, 0.80);            // Default 60 seconds
        cache.put("NVDA", 890.12, 43.20, 5.10, 5000);      // 5 seconds (quick expiry!)
        
        // Display initial state
        cache.printAll();
        
        // Test 2: Simulate user requests
        System.out.println("\n👥 SIMULATING MULTIPLE USERS:");
        
        // User 1
        System.out.println("\n👤 User 1 requests:");
        cache.get("TSLA");
        cache.get("AAPL");
        
        // User 2 (simultaneous - thread-safe!)
        System.out.println("\n👤 User 2 requests:");
        cache.get("TSLA");  // Should still be there
        cache.get("AMZN");  // Not in cache
        
        // Test 3: Wait for expiry
        System.out.println("\n⏰ WAITING FOR EXPIRY (7 seconds)...");
        Thread.sleep(7000); // Wait 7 seconds
        
        System.out.println("\n🔍 AFTER 7 SECONDS:");
        cache.get("NVDA");  // Should be expired (5s TTL)
        cache.get("TSLA");  // Should still be valid (10s TTL)
        
        // Test 4: Check contains() with expiry
        System.out.println("\n✅ CHECKING CONTAINS:");
        System.out.println("Contains TSLA? " + cache.contains("TSLA"));
        System.out.println("Contains NVDA? " + cache.contains("NVDA")); // False (expired)
        
        // Test 5: Manual cleanup
        System.out.println("\n🧹 MANUAL CLEANUP:");
        int removed = cache.cleanupExpired();
        System.out.println("Manually removed " + removed + " expired stocks");
        
        // Final stats
        cache.printAll();
        cache.printStats();
        
        // Test 6: Concurrent access simulation
        System.out.println("\n🚀 SIMULATING 5 CONCURRENT USERS:");
        Thread[] users = new Thread[5];
        
        for (int i = 0; i < 5; i++) {
            final int userId = i + 1;
            users[i] = new Thread(() -> {
                cache.get("TSLA");
                cache.get("AAPL");
                System.out.println("User " + userId + " completed");
            });
        }
        
        // Start all users at same time
        for (Thread user : users) {
            user.start();
        }
        
        // Wait for all to finish
        for (Thread user : users) {
            user.join();
        }
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("✅ DAY 2 COMPLETE!");
        System.out.println("🎯 Features added:");
        System.out.println("   1. Expiry (TTL) - Auto-delete old data");
        System.out.println("   2. Thread safety - Multiple users supported");
        System.out.println("   3. Auto-cleanup thread");
        System.out.println("   4. ConcurrentHashMap implementation");
        
        // Clean shutdown
        cache.shutdown();
    }
}