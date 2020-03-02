package it.mmzitarosa.beerbox.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import it.mmzitarosa.beerbox.BuildConfig;

public class Util {

    final private static char[] hexArray = "0123456789abcdef".toUpperCase().toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    @Nullable
    public static String SHA1(@Nullable String string) {
        if (string == null)
            return null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(string.getBytes());
            byte[] messageDigest = digest.digest();

            return bytesToHex(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            Logger.e(e);
            return null;
        }
    }

    public static boolean isLoggable() {
        return BuildConfig.BUILD_TYPE.equalsIgnoreCase("debug") || BuildConfig.DEBUG;
    }

    public static void inputStreamToFile(InputStream inputStream, File file) throws IOException {
        byte[] buffer = new byte[1024];

        FileOutputStream outputStream = new FileOutputStream(file);
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.write(buffer);
        outputStream.close();
    }

    public static InputStream fileToInputStream(File file) throws IOException {
        return new FileInputStream(file);
    }

    @Nullable
    public static String inputStreamToString(InputStream inputStream) throws IOException {
        if (inputStream == null)
            return null;

        byte[] buffer = new byte[1024];

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        String result = outputStream.toString(StandardCharsets.UTF_8.name());
        outputStream.close();
        return result;
    }

    public static Bitmap inputStreamToBitmap(InputStream inputStream) throws IOException {
        if (inputStream == null)
            return null;

        Bitmap result = BitmapFactory.decodeStream(inputStream);
        return result;
    }

    public static <T> T jsonToObject(String response, Class<T> classType) throws JSONException {
        Gson gson = new Gson();
        JSONObject object = new JSONObject(response);
        return gson.fromJson(object.toString(), classType);
    }

    public static <T> List<T> jsonToList(String response, Class<T> classType) throws JSONException {
        Gson gson = new Gson();
        JSONArray jsonArray = new JSONArray(response);
        ArrayList<T> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), classType));
        }
        return list;
    }


}
