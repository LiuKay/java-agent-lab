package com.kay.agent.security.agent;

import com.kay.agent.security.expose.Secured;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

public class AgentMain {

    //rebase the class annotated with Secured annotation
    public static void premain(String arg, Instrumentation inst){
        new AgentBuilder.Default()
                .type(ElementMatchers.any())
                .transform(((builder, typeDescription, classLoader, module) ->
                        builder.method(ElementMatchers.isAnnotatedWith(Secured.class))
                               .intercept(MethodDelegation.to(SecurityInterceptor.class)
                                                          .andThen(SuperMethodCall.INSTANCE))))
                .installOn(inst);

    }

}
