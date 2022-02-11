# bytecode-instrument-lab

JVM bytecode instrument learning 

### ASM

[ASM 学习参考](./docs/asm.md)

### Javassist

[入门的一个 javassist agent demo](./java-agent-01/Readme.md)

### Byte-buddy


## Java Agent 

### Demos
- [monitor jvm thread status](./java-agent-02/Readme.md)
- [monitor jvm metrics](./java-agent-03/Readme.md)
- [export jvm metrics to prometheus](./java-agent-04/Readme.md)

### Agent Basics

Instrument API: [java.lang.instrument (Java 2 Platform SE 5.0) (oracle.com)](https://docs.oracle.com/javase/1.5.0/docs/api/java/lang/instrument/package-summary.html)

Java Agent 必须实现一个 `premain` 方法（一个比 main 方法更靠前执行的方法），作为 agent 执行的入口：

```shell
public static void premain(String agentArgs, Instrumentation inst);
public static void premain(String agentArgs);
```

JVM 启动的时候会按参数顺序执行所有 Agent 的 `premain` 方法，然后再执行 `main`方法，每一个 premain 方法都应该能够正常返回，否则 JVM 将无法启动。

Java Agent 需要打包成 JAR，并且添加 `MANIFEST.MF `文件到 JAR 包中，manifest 是一个描述文件(MANIFEST.MF 文件一般在 META-INF 文件夹下)

| **Manifest Attribute**           | Description                                                  |
| -------------------------------- | ------------------------------------------------------------ |
| **Premain-Class**                | 必须. premain 方法所在的 agent class name , com.kay.MyAgent  |
| **Agent-Class**                  | 可选，如果需要在运行时加入 agent，该参数必须填，需要Agent实现 agentmain 方法作为 attach 到 JVM 的启动方法 |
| **Boot-Class-Path**              | 可选.bootstrap class loader 搜索的路径                       |
| **Can-Redefine-Classes**         | 可选.默认 false，表示该 agent 是否可以 redefine class        |
| **Can-Retransform-Classes**      | 可选，默认false，是否可以修改 class                          |
| **Can-Set-Native-Method-Prefix** | 可选，默认false，是否设置 native method 方法前缀             |

一个最简单的 manifest 如下：

```tex
Premain-Class:com.kay.MyAgent
Manifest-Version: 1.0
```

Java Agent 需要作为 VM 参数添加到执行命令中：

```shell
java -javaagent:myagent.jar=agentArgs -jar myapp.jar
```

### Debug Agent

#### 1 Remote Debug
Java Agent 实际上与用户应用程序运行在同一个 JVM 实例中，所以同样可以通过 Remote Debug 的方式来进行 Debug。
首先在应用程序运行的时候打开 remote debug 的监听端口：（IDE 中运行的时候就在 main 方法添加相应的 VM 参数就 OK 了）
```shell
java -agentlib:jdwp=transport=dt_socket,address=5005,server=y,suspend=n \
-javaagent:myagent.jar=agentArgs \
-jar myapp.jar
```
然后在 Agent 的代码中去运行 Remote Debug 连到运行的程序中，就可以进行 debug agent 了。
（在 IDEA 中的话就是直接新建一个 Run Configuration 选择 Remote JVM debug 就可以了）

#### 2 IDE Debug

在 IDE 中将 Java Agent 的代码放到项目的依赖项里面，打好断点，启动项目可以直接进行 Debug， IDE 能够智能的找到 Agent 的代码并在断点处停住