package com.kay.agent.security.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.MethodDelegation;

import java.lang.instrument.Instrumentation;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import static net.bytebuddy.matcher.ElementMatchers.isSubTypeOf;
import static net.bytebuddy.matcher.ElementMatchers.named;

public class AgentMain {

    //rebase the class annotated with Secured annotation
    public static void premain(String arg, Instrumentation inst) throws ClassNotFoundException {
        new AgentBuilder.Default()
                .type(isSubTypeOf(Class.forName("javax.servlet.Servlet")))
                .transform(((builder, typeDescription, classLoader, module) ->
                        builder.method(named("service"))
                               .intercept(MethodDelegation.to(Interceptor.class))))
                .installOn(inst);

    }

    public static class Interceptor {
        public static void intercept(ServletRequest request, ServletResponse response) {
            try {
                String contextPath = request.getServletContext().getContextPath();
                System.err.println("request path:" + contextPath);

                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.println("Hello, transformed world!");
                outputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
