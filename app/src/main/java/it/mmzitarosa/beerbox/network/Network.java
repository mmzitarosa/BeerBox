package it.mmzitarosa.beerbox.network;

import java.util.Map;

public class Network {

    public void getRequest(String url, BeerBoxCallback callback) {
        new NetworkService(url, Content.TEXT_STRING, callback).execute();
    }

    public void getRequest(String url, Content content, BeerBoxCallback callback) {
        new NetworkService(url, content, callback).execute();
    }

    public void getRequest(String url, Map<String, String> parameters, BeerBoxCallback callback) {
        new NetworkService(url, parameters, callback).execute();
    }

    public enum Content {TEXT_STRING, MEDIA_IMAGE}

}
