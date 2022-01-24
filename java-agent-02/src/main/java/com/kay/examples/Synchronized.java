package com.kay.examples;

public class Synchronized implements Runnable {
    @Override
    public synchronized void run()
    {
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            //
        }
    }
}
