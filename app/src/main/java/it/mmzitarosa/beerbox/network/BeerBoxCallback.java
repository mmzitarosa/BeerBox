package it.mmzitarosa.beerbox.network;

import it.mmzitarosa.beerbox.util.Logger;

public abstract class BeerBoxCallback {

    public abstract void onSuccess(Object object);

    public abstract void onError(String response);

    public void onError(String response, Exception e) {
        Logger.e(response, e);
        onError(response);
    }
}
