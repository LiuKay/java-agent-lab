package com.kay.agent;

/**
 * -javaagent:D:\Code\bytecode-lab\agent01\build\libs\agent01-1.0-SNAPSHOT.jar=agentArgs
 */
public class AgentTest {

    public static void main(String[] args) {
        AgentTest agentTest = new AgentTest();
        agentTest.sayHi();
    }

    @TimeMonitor(note = "time recode note")
    private void sayHi(){
        System.out.println("hi Agent!");
    }
}