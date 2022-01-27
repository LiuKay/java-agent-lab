package com.kay.agent.security;

import com.kay.agent.security.expose.Secured;

public class UserService {

    @Secured(user = "ADMIN")
    public void doSensitiveAction(){
        System.out.println("doSensitiveAction ");
    }

}
