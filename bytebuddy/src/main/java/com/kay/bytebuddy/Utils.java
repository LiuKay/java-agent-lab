package com.kay.bytebuddy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public final class Utils {

    private static final String currentPath = System.getProperty("user.dir");

    public static void outputClass(byte[] bytes, String className) {
        try {
            Files.createDirectories(Paths.get(currentPath, "classes"));
            String path = currentPath + File.separator + "classes" + File.separator + className + ".class";
            System.out.println("output path:" + path);

            Files.write(Paths.get(path), bytes, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
