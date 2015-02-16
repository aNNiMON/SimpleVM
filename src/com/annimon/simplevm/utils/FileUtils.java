package com.annimon.simplevm.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author aNNiMON
 */
public class FileUtils {
    
    public static String readContents(InputStream is) throws IOException {
        final StringBuilder sb = new StringBuilder();
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ( (line = reader.readLine()) != null ) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
        }
        return sb.toString();
    }
}
