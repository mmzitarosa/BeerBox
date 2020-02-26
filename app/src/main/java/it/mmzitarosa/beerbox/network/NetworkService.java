package it.mmzitarosa.beerbox.network;

import android.os.AsyncTask;
import android.util.Pair;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import it.mmzitarosa.beerbox.util.Util;

public class NetworkService extends AsyncTask<Void, String, Pair<Integer, String>> {

    private URL url;
    private String targetUrl;
    private Map<String, String> parameters;
    private BeerBoxCallback beerBoxCallback;

    public NetworkService(String url, BeerBoxCallback callback) {
        this.url = null;
        this.targetUrl = url;
        this.parameters = new HashMap<>();
        this.beerBoxCallback = callback;
    }

    public NetworkService(String url, Map<String, String> parameters, BeerBoxCallback callback) {
        this.url = null;
        this.targetUrl = url;
        this.parameters = parameters;
        this.beerBoxCallback = callback;
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
    protected Pair<Integer, String> doInBackground(Void... voids) {
        if (url == null)
            return null;

        String response;
        int status;
        try {
            InputStream inputStream;
            URLConnection connection = url.openConnection();
            switch (url.getProtocol()) {
                case "https":
                    ((HttpsURLConnection) connection).setRequestMethod("GET");
                    status = ((HttpsURLConnection) connection).getResponseCode();
                    if (status == 200)
                        inputStream = new BufferedInputStream(connection.getInputStream());
                    else
                        inputStream = ((HttpsURLConnection) connection).getErrorStream();
                    response = Util.inputStreamToString(inputStream);
                    ((HttpsURLConnection) connection).disconnect();
                    break;
                case "http":
                    ((HttpURLConnection) connection).setRequestMethod("GET");
                    status = ((HttpURLConnection) connection).getResponseCode();
                    if (status == 200)
                        inputStream = new BufferedInputStream(connection.getInputStream());
                    else
                        inputStream = ((HttpURLConnection) connection).getErrorStream();
                    response = Util.inputStreamToString(inputStream);
                    ((HttpURLConnection) connection).disconnect();
                    break;
                default:
                    beerBoxCallback.onError("URL protocol not yet implemented or not supported.");
                    return null;
            }

            return new Pair<>(status, response);

        } catch (IOException e) {
            beerBoxCallback.onError("Error opening connection.", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Pair<Integer, String> response) {
        if (url == null || response == null)
            super.onPostExecute(response);
        else if (response.first != 200)
            beerBoxCallback.onError(response.second);
        else
            beerBoxCallback.onSuccess(response.second);
    }
}
