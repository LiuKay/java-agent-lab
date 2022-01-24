package com.kay.examples;

import java.util.ArrayList;
import java.util.List;

//-javaagent:C:\kaybee\code\bytecode-lab\java-agent-02\build\libs\java-agent-02-1.0-SNAPSHOT.jar=debug
public class SynchronizedThreads {

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<Thread>();
        Synchronized aSynchronized = new Synchronized();
        for (int i=0; i<1000; i++)
        {
            Thread thread = new Thread(new Starter(aSynchronized));
            threads.add(thread);
            thread.start();
        }
        for (Thread thread: threads)
        {
            thread.join();
        }
    }

}
