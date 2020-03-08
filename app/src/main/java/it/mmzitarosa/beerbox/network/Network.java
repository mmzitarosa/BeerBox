package it.mmzitarosa.beerbox.network;

import java.io.File;
import java.util.Map;

class Network {

    void getRequest(String url, ContentType contentType, NetworkListener callback) {
        new NetworkService(url, contentType, null, null, callback).execute();
    }

    void getRequest(String url, File file, NetworkListener callback) {
        new NetworkService(url, ContentType.MEDIA_IMAGE, null, file, callback).execute();
    }

    void getRequest(String url, Map<String, String> parameters, File file, NetworkListener callback) {
        new NetworkService(url, ContentType.TEXT_STRING, parameters, file, callback).execute();
    }

    public enum ContentType {TEXT_STRING, MEDIA_IMAGE}

}
