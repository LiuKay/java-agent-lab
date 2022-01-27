package com.kay.agent.security.agent;

import com.kay.agent.security.expose.Secured;
import net.bytebuddy.implementation.bind.annotation.Origin;

import java.lang.reflect.Method;

public class SecurityInterceptor {

    static String user = "ANONYMOUS";

    public static void intercept(@Origin Method method){
        if (!method.getAnnotation(Secured.class).user().equals(user)) {
            throw new IllegalStateException("Not allowed user.");
        }
    }

}
