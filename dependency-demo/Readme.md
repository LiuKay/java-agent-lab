# README

This project is dependency used in agent to show "dependency conflicts" in agent and user application.

## Problem:

The agent is using "com.kay:dependency-demo:2.0",
and the application in 'test-demo-01' is using "com.kay:dependency-demo:1.0",
the two versions are incompatible.

How to resolve dependencies conflicts?


### How to run

1. publish the version 1.0 to local maven, and then add the method "version2()" to "DependencyClass" class:
```java
    // This method is only in "com.kay:dependency-demo:2.0"
    public void version2(){
        System.out.println("version2:" + DependencyClass.class.getClassLoader());
    }
```

2. update the version in build.gradle to "2.0" and publish to the local maven again.

3. package the 'java-agent-05' project to build the agent jar

4. add the VM options "-XX:+TraceClassLoading -javaagent:/path-to/agent-05-1.0.jar" to run the 'test-demo-01' to, using the agent from step3.

- The agent is using "com.kay:dependency-demo:2.0"
- The application in 'test-demo-01' is using "com.kay:dependency-demo:1.0"

**The two versions of DependencyClass is loaded from different class loader.**