package com.kay.example;


import java.io.File;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.security.CodeSource;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

public class Main {

    public static void premain(String args, Instrumentation instrumentation){
        try {
            CodeSource codeSource = Main.class.getProtectionDomain().getCodeSource();
            String location = codeSource.getLocation().toURI().getSchemeSpecificPart();

            System.out.println("location:" + location);

            JarFile jarFile = new JarFile(location);
            System.out.println(Main.class.getClassLoader());

            URL url = new File(location).toURI().toURL();
            URL[] urls = new URL[]{url};
            AgentURLClassLoader agentURLClassLoader = new AgentURLClassLoader(urls);

            Attributes attributes = jarFile.getManifest().getMainAttributes();
            String bootstrap = attributes.getValue("Bootstrap-Class");

            agentURLClassLoader.loadClass(bootstrap)
                               .getMethod("premain", String.class, Instrumentation.class)
                               .invoke(null, args, instrumentation);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
