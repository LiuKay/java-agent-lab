package com.kay.examples;

public class Starter implements Runnable {

    Runnable nested;

    public Starter(Synchronized aSynchronized)
    {
        nested = new Nested(100, aSynchronized);
    }

    @Override
    public void run() {
        nested.run();
    }

}
