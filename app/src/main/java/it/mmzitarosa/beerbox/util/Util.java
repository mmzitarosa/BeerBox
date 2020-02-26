package it.mmzitarosa.beerbox.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import it.mmzitarosa.beerbox.BuildConfig;

public class Util {

    static boolean isLoggable() {
        return BuildConfig.BUILD_TYPE.equalsIgnoreCase("debug") || BuildConfig.DEBUG;
    }

    public static String inputStreamToString(InputStream inputStream) throws IOException {
        if (inputStream == null)
            return null;
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8.name());
    }

}
