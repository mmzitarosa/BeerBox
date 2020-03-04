package it.mmzitarosa.beerbox.network;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import it.mmzitarosa.beerbox.util.Logger;
import it.mmzitarosa.beerbox.util.Util;

import static it.mmzitarosa.beerbox.network.Network.ContentType;

public class NetworkService extends AsyncTask<Void, String, Pair<Integer, Object>> {

    private URL url;
    private String targetUrl;
    private Map<String, String> parameters;
    private NetworkListener networkListener;
    private ContentType contentType;
    private File file;
    private int pid;

    NetworkService(@NonNull String url, @NonNull ContentType contentType, @Nullable Map<String, String> parameters, @Nullable File file, @NonNull NetworkListener callback) {
        this.targetUrl = url;
        this.parameters = parameters != null ? parameters : new HashMap<String, String>();
        this.networkListener = callback;
        this.contentType = contentType;
        this.file = file;
        this.pid = new Random().nextInt();
    }

    @Override
    protected void onPreExecute() {
        this.url = null;

        try {
            if (contentType == ContentType.MEDIA_IMAGE && file != null && file.exists()) {
                Util.fileToInputStream(file);
                Bitmap bitmap = Util.inputStreamToBitmap(Util.fileToInputStream(file));
                if (bitmap != null) {
                    Logger.i(pid + ": Bitmap retrieved from file.");
                    networkListener.onSuccess(bitmap);
                    return;
                }
            }
        } catch (IOException e) {
            Logger.e(pid + ": File found but not valid. Continue... ", e);
        }

        String url = targetUrl;
        if (parameters != null && !parameters.isEmpty()) {
            StringBuilder params = new StringBuilder("?");
            for (String paramKey : parameters.keySet()) {
                params.append("&")
                        .append(paramKey)
                        .append("=")
                        .append(parameters.get(paramKey));
            }
            url += params.toString();
        }
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            networkListener.onError(pid + ": URL not valid error.", e);
        }
    }

    @Override
    protected Pair<Integer, Object> doInBackground(Void... voids) {
        if (url == null)
            return null;

        Object response;
        int status;
        try {
            Logger.i(pid + ": Request to: " + url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            InputStream inputStream;
            status = connection.getResponseCode();
            Logger.i("HTTP response status: " + status);
            if (status == 200)
                inputStream = new BufferedInputStream(connection.getInputStream());
            else
                inputStream = connection.getErrorStream();

            switch (contentType) {
                case MEDIA_IMAGE:
                    Logger.i(pid + ": Bitmap generation from InputStream...");
                    if (file != null) {
                        Util.inputStreamToFile(inputStream, file);
                        Logger.i(pid + ": Bitmap saved to file.");
                        inputStream = Util.fileToInputStream(file);
                    }
                    response = Util.inputStreamToBitmap(inputStream);
                    Logger.i(pid + ": Bitmap generated successfully!");
                    break;
                case TEXT_STRING:
                default:
                    Logger.i(pid + ": String generation from InputStream...");
                    response = Util.inputStreamToString(inputStream);
                    Logger.i(pid + ": Bitmap generated successfully: " + response);
            }
            connection.disconnect();
            return new Pair<>(status, response);

        } catch (IOException e) {
            networkListener.onError(pid + ": Error opening connection.", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Pair<Integer, Object> response) {
        if (url == null || response == null)
            super.onPostExecute(response);
        else if (response.first != 200)
            networkListener.onError((String) response.second, null);
        else
            networkListener.onSuccess(response.second);
    }
}
