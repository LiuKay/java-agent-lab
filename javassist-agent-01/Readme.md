### 遇到的坑

1. 请一定使用 JDK1.8, 不要因为使用与项目不一样的 JDK 1.8 导致一些奇怪的错误，当入门之后再研究不同 JDK 的使用差异。
   我一开始使用的是 JDK11，然后 javassist 一直报找不到对应的 TargetClass, 排查的人都要没了。
2. 在 test 目录里面运行可能会当作 单元测试来运行，导致 transform 2次。
    我在 test 目录里面写了一个普通类来运行 javaagent，结果目标类被 transform 了2次，第二次就报 class freeze了。
3. tools.jar 也要一起打包

    参考 `implementation files("${System.properties['java.home']}/../lib/tools.jar")`


### Agent 执行方式

1. 第一种方式是添加 VM 参数 -javaagent:agent.jar, 这样就会在 JVM 启动的时候执行 main 方法之前，执行 agent 的 premain 方法，达到增强类的目的
2. 第二种方式是通过 VirtualMachine.loadAgent() 的方式在 JVM 运行的过程种去加载 agent，此时会执行 agentmain 方法，在 JVM 运行的过程种动态的来增强类


### JVM启动记载 Agent

1. 在 TargetClass 启动时，添加 VM 参数 -javaagent:agent.jar 即可

### JVM运行中加载 Agent

1. 打包得到 agent jar
2. 运行 TargetClass main 方法，此时运行的是还未被替换的方法
3. 将 Attach main 里面的pid 替换成 TargetClass 输出的 pid，agent 路径替换成打包好的路径，运行 main 方法
4. 热部署成功，TargetClass 现在运行的是已替换后的方法