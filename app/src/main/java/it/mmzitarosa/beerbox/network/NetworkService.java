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

    NetworkService(@NonNull String url, @NonNull ContentType contentType, @Nullable Map<String, String> parameters, @Nullable File file, @NonNull NetworkListener callback) {
        this.targetUrl = url;
        this.parameters = parameters != null ? parameters : new HashMap<String, String>();
        this.networkListener = callback;
        this.contentType = contentType;
        this.file = file;
    }

    @Override
    protected void onPreExecute() {
        this.url = null;
        try {
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
                networkListener.onError("URL not valid error.", e);
            }

            if (file != null && this.url != null) {
                String urlSHA1 = Util.SHA1(this.url.toString());
                if (urlSHA1 != null) {
                    file = new File(file, urlSHA1);
                }
                if (contentType == ContentType.MEDIA_IMAGE && file.exists()) {
                    Util.fileToInputStream(file);
                    Bitmap bitmap = Util.inputStreamToBitmap(Util.fileToInputStream(file));
                    if (bitmap != null) {
                        Logger.i("Bitmap retrieved from file.");
                        networkListener.onSuccess(bitmap);
                        this.url = null;
                    }
                }
            }
        } catch (IOException e) {
            Logger.e("File found but not valid. Continue... ", e);
        }

    }

    @Override
    protected Pair<Integer, Object> doInBackground(Void... voids) {
        if (url == null)
            return null;

        Object response;
        int status;
        try {
            Logger.i("Request to: " + url);
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
                    Logger.i("Bitmap generation from InputStream...");
                    if (file != null) {
                        inputStream = Util.inputStreamToFile(inputStream, file);
                        Logger.i("Bitmap saved to file.");
                    }
                    response = Util.inputStreamToBitmap(inputStream);
                    Logger.i("Bitmap generated successfully!");
                    break;
                case TEXT_STRING:
                default:
                    Logger.i("String generation from InputStream...");
                    if (file != null) {
                        inputStream = Util.inputStreamToFile(inputStream, file);
                        Logger.i("String saved to file.");
                    }
                    response = Util.inputStreamToString(inputStream);
                    Logger.i("String generated successfully: " + response);
            }
            connection.disconnect();
            return new Pair<>(status, response);

        } catch (Exception e) {
            try {
                if (file != null && file.exists()) {
                    Logger.i("Try to retrieving string from file...");
                    response = Util.inputStreamToString(Util.fileToInputStream(file));
                    Logger.i("String retrieved successfully: " + response);
                    return new Pair<>(200, response);
                }
                throw e;
            } catch (Exception ex) {
                networkListener.onError("Error opening connection.", e);
                return null;
            }
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
