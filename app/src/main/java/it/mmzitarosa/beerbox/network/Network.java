package it.mmzitarosa.beerbox.network;

import java.io.File;
import java.util.Map;

public class Network {

    public void getRequest(String url, NetworkListener callback) {
        getRequest(url, ContentType.TEXT_STRING, callback);
    }

    public void getRequest(String url, ContentType contentType, NetworkListener callback) {
        new NetworkService(url, contentType, null, null, callback).execute();
    }

    public void getRequest(String url, Map<String, String> parameters, NetworkListener callback) {
        new NetworkService(url, ContentType.TEXT_STRING, parameters, null, callback).execute();
    }

    public void getRequest(String url, File file, NetworkListener callback) {
        new NetworkService(url, ContentType.MEDIA_IMAGE, null, file, callback).execute();
    }


    public enum ContentType {TEXT_STRING, MEDIA_IMAGE}

}
