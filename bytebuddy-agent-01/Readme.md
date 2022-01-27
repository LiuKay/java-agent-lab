# bytebuddy-agent-01

本项目实现了这样一个 Java Agent:

> 通过 Instrumentation API 去 tranform 所有加了 @Trace 注解的方法，在方法执行的过程种会打印方法的签名，参数，返回值以及方法执行的时间。

Interceptor 实现如下，通过 ByteBuddy 的注解可以很方便的拿到方法运行时的各种信息，通过 Advice 将切入逻辑植入其中：
```java
public class MethodAdvice {

    @Advice.OnMethodEnter
    public static long enter(@Advice.Origin String origin,
                             @Advice.Origin("#d") String descriptor,
                             @Advice.AllArguments Object[] args) {
        StringBuilder stringBuilder = new StringBuilder("Args:[");
        StringJoiner joiner = new StringJoiner(",");
        if (args != null) {
            for (Object arg : args) {
                joiner.add(arg == null ? "null" : arg.toString());
            }
        }
        stringBuilder.append(joiner).append("]");

        System.out.println(">>Method:" + origin + "," + stringBuilder);

        return System.nanoTime();
    }

    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void exit(@Advice.Enter long startTime,
                            @Advice.Thrown Throwable throwable,
                            @Advice.Return Object returnValue) {
        System.out.println(
                String.format("<<Method execution time:%dns, return=%s, exception=%s", System.nanoTime() - startTime,
                              returnValue, throwable));
    }

}
```
然后通过 ByteBuddy 创建 Java Agent:

```java
public class AgentMain {

    public static void premain(String arg, Instrumentation inst){
        new AgentBuilder.Default()
                .type(ElementMatchers.any())
                .transform(((builder, typeDescription, classLoader, module) ->
                        builder.method(ElementMatchers.isAnnotatedWith(Trace.class))
                               .intercept(Advice.to(MethodAdvice.class))))
                .installOn(inst);

    }

}
```