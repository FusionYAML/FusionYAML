package io.github.fusionyaml.utils;

import java.io.*;

public class FileUtils {


    public static String readToString(File file) throws IOException {
        return readToString(new FileReader(file));
    }

    public static String readToString(Reader reader) throws IOException {
        BufferedReader r = new BufferedReader(reader);
        StringBuilder builder = new StringBuilder();
        String line;
        boolean firstRun = true;
        while ((line = r.readLine()) != null) {
            if (!firstRun) builder.append("\n");
            else firstRun = false;
            builder.append(line);
        }
        r.close();
        return builder.toString();
    }

}
