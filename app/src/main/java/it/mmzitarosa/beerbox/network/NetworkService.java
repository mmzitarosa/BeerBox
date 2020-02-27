package it.mmzitarosa.beerbox.network;

import android.os.AsyncTask;
import android.util.Pair;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import it.mmzitarosa.beerbox.util.Logger;
import it.mmzitarosa.beerbox.util.Util;

import static it.mmzitarosa.beerbox.network.Network.Content;

public class NetworkService extends AsyncTask<Void, String, Pair<Integer, Object>> {


    private URL url;
    private String targetUrl;
    private Map<String, String> parameters;
    private BeerBoxCallback beerBoxCallback;
    private Content content;

    public NetworkService(String url, Content content, BeerBoxCallback callback) {
        this.url = null;
        this.targetUrl = url;
        this.parameters = new HashMap<>();
        this.beerBoxCallback = callback;
        this.content = content;
    }

    public NetworkService(String url, Map<String, String> parameters, BeerBoxCallback callback) {
        this.url = null;
        this.targetUrl = url;
        this.parameters = parameters;
        this.beerBoxCallback = callback;
        this.content = Content.TEXT_STRING;
    }

    @Override
    protected void onPreExecute() {
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
            beerBoxCallback.onError("URL not valid error.", e);
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

            switch (content) {
                case MEDIA_IMAGE:
                    Logger.i("Bitmap generation from InputStream...");
                    response = Util.inputStreamToBitmap(inputStream);
                    Logger.i("Bitmap generated successfully!");
                    break;
                case TEXT_STRING:
                default:
                    Logger.i("String generation from InputStream...");
                    response = Util.inputStreamToString(inputStream);
                    Logger.i("Bitmap generated successfully: " + response);
            }
            connection.disconnect();
            return new Pair<>(status, response);

        } catch (IOException e) {
            beerBoxCallback.onError("Error opening connection.", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Pair<Integer, Object> response) {
        if (url == null || response == null)
            super.onPostExecute(response);
        else if (response.first != 200)
            beerBoxCallback.onError((String) response.second);
        else
            beerBoxCallback.onSuccess(response.second);
    }
}
