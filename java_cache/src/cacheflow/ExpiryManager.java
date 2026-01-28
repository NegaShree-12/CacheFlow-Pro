// File: src/cacheflow/ExpiryManager.java
package cacheflow;

import java.util.concurrent.*;

public class ExpiryManager implements Runnable {
    private StockCache cache;
    private ScheduledExecutorService scheduler;
    private boolean running = false;
    
    public ExpiryManager(StockCache cache) {
        this.cache = cache;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }
    
    public void start() {
        if (!running) {
            running = true;
            // Run cleanup every 30 seconds
            scheduler.scheduleAtFixedRate(this, 0, 30, TimeUnit.SECONDS);
            System.out.println("🔄 Expiry Manager started (runs every 30s)");
        }
    }
    
    public void stop() {
        running = false;
        scheduler.shutdown();
        System.out.println("🛑 Expiry Manager stopped");
    }
    
    @Override
    public void run() {
        int removed = cache.cleanupExpired();
        if (removed > 0) {
            System.out.println("🧹 Cleaned up " + removed + " expired stocks");
        }
    }
}