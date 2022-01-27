package com.kay.agent.monitor.agent;

import com.kay.agent.monitor.expose.Trace;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

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
