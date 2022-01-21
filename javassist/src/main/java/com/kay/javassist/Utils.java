package com.kay.javassist;

import java.io.File;
import java.io.FileOutputStream;

public final class Utils {

    public static void outputClass(byte[] bytes, String className) {
        final String currentPath = System.getProperty("user.dir");
        String path = currentPath + File.separator + className + ".class";
        try (FileOutputStream out = new FileOutputStream(path)) {
            System.out.println("ASM output path:" + path);
            out.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
