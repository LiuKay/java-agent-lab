package com.kay.monitor00;

public class UserService {

    //method to be monitored
    public String getUserInfo(String id) {
        System.out.println("get user info");
        System.out.println("get user info");
        System.out.println("get user info");
//        throw new RuntimeException("eeeeexxx");
        return id;
    }
}
