package org.example;

import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        MyRunnable runnable1 = new MyRunnable();
        MyRunnable runnable2 = new MyRunnable();

        Thread thread1 = new Thread(runnable1);
        Thread thread2 = new Thread(runnable2);

        thread1.start();
        thread2.start();

        //Testing Mock Connection
        MockConnection connection = new MockConnection();
        connection.connect();

        //Perform testing operations
        connection.executeQuery("SELECT * FROM users");

        connection.close();

        //Mock Connection using ConnectionPool/Initialize Connection Pool object of size 5
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        //Load Connection Pool using single threads and Java Thread Pool
        ExecutorService executorService = Executors.newFixedThreadPool(7);

        //Create tasks to get connections from the pool
        CompletableFuture<Void>[] aquireTasks = new CompletableFuture[5];
        for (int i = 0; i < 5; i++) {
            aquireTasks[i] = CompletableFuture.runAsync(() -> {
                try {
                    MockConnection connection1 = connectionPool.getConnection();
                    System.out.println("Thread " + Thread.currentThread().getId() + " acquired connection: " + connection1);
                    //Perform simulations using acquired connection
                    Thread.sleep(2000); //sim some work
                    connectionPool.releaseConnection(connection1);
                    System.out.println("Thread " + Thread.currentThread().getId() + " released connection: " + connection1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, executorService);
        }

        //Create tasks to wait for the next available connection
        CompletableFuture<Void>[] waitTasks = new CompletableFuture[2];
        for (int i = 0; i < 2; i++) {
            waitTasks[i] = CompletableFuture.runAsync(() -> {
                try {
                    System.out.println("Thread " + Thread.currentThread().getId() + " waiting for the next available connection...");
                    MockConnection connection1 = connectionPool.getConnection();
                    System.out.println("Thread " + Thread.currentThread().getId() + " acquired connection " + connection1);
                    // Perform simulations with acquired connection
                    Thread.sleep(2000); // sim some work
                    connectionPool.releaseConnection(connection1);
                    System.out.println("Thread " + Thread.currentThread().getId() + " released connection: " + connection1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, executorService);
        }

        //Combine all tasks and wait for their completion
        CompletableFuture<Void> allTasks = CompletableFuture.allOf(aquireTasks).thenCompose((Void) ->
                CompletableFuture.allOf(waitTasks));
        allTasks.get();

        //Shutdown executor service
        executorService.shutdown();

//        //Wait for all tasks to complete
//        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        //Program waits for all threads to finish
        thread1.join();
        thread2.join();

    }
}
