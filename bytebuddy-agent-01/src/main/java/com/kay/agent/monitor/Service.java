package com.kay.agent.monitor;

import com.kay.agent.monitor.expose.Trace;

public class Service {

    @Trace
    public String getUserInfo(String username, String address) {
        return String.format("this is user info:%s,%s", username, address);
//        throw new IllegalArgumentException("illegal username:" + username);
    }

}
