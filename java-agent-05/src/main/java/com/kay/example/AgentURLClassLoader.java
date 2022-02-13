package com.kay.example;

import java.net.URL;
import java.net.URLClassLoader;

public class AgentURLClassLoader extends URLClassLoader {

    public AgentURLClassLoader(URL[] urls) {
        this(urls, null);
    }

    public AgentURLClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }


}
