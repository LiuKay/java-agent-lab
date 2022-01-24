package com.kay.examples;

public class Nested implements Runnable {

    Runnable child;

    public Nested(int counter, Synchronized aSynchronized)
    {
        counter--;
        if (counter > 0)
        {
            child = new Nested(counter, aSynchronized);
        }
        else
        {
            child = aSynchronized;
        }
    }

    @Override
    public void run() {
        child.run();
    }
}
