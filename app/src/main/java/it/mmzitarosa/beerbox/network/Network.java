package it.mmzitarosa.beerbox.network;

import java.util.Map;

class Network {

    protected void getRequest(String url, BeerBoxCallback callback) {
        new NetworkService(url, callback).execute();
    }

    protected void getRequest(String url, Map<String, String> parameters, BeerBoxCallback callback) {
        new NetworkService(url, parameters, callback).execute();
    }

}
