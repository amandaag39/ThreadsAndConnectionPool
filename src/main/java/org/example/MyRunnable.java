package org.example;

public class MyRunnable implements Runnable {
    @Override
    public void run() {
        //Code to be executed by the thread
        for (int i = 0; i < 5; i++) {
            System.out.println("Thread: "+ Thread.currentThread().getId() + ", Count: " + i);
            try {
                Thread.sleep(1000); //Pause the thread for 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
