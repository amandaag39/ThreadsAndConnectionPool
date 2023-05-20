package org.example;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionPool {
    private static ConnectionPool instance;
    private BlockingQueue<MockConnection> pool;

    private ConnectionPool () {
        pool = new ArrayBlockingQueue<>(5);
        for (int i = 0; i < 5; i++) {
            pool.add(new MockConnection());
        }
    }

    // Implement double-checked locking idiom for lazy initialization
    public static ConnectionPool getInstance() {
        if (instance == null) {
            synchronized (ConnectionPool.class) {
                if (instance == null) {
                    instance = new ConnectionPool();
                }
            }
        }
        return instance;
    }

    public MockConnection getConnection() throws InterruptedException {
        return pool.take();
    }

    public void releaseConnection(MockConnection connection) {
        pool.add(connection);
    }
}

