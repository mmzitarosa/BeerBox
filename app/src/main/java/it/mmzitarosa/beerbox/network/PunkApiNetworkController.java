package it.mmzitarosa.beerbox.network;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.punkapi.api2pojo.beers.BeersItem;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.mmzitarosa.beerbox.R;
import it.mmzitarosa.beerbox.util.Listable;
import it.mmzitarosa.beerbox.util.Logger;
import it.mmzitarosa.beerbox.util.Util;

public class PunkApiNetworkController extends Network {

    private Context context;
    private Map<String, String> params;

    public PunkApiNetworkController(Context context) {
        this.context = context;
        this.params = new HashMap<>();
    }

    public void getAllBeers() {
        getAllBeers(1);
    }

    public void getAllBeers(int page) {
        params.clear();
        request(page);
    }

    public void getSelectedBeers(String string) {
        getSelectedBeers(string, 1);
    }

    public void getSelectedBeers(String string, int page) {

        params.put("beer_name", string.trim().replace(" ", "_"));

        request(page);
    }

    public void getMoreBeers(int page) {
        request(page);
    }

    public void getBitmapFromUrl(@NonNull String url, final BitmapNetworkListener bitmapNetworkListener) {
        getRequest(url, context.getCacheDir(), new NetworkListener() {
            @Override
            public void onSuccess(@NonNull Object response) {
                bitmapNetworkListener.onReady((Bitmap) response);
            }

            @Override
            public void onError(@Nullable String response, @Nullable Exception e) {
                // TODO default image
                Logger.e(response, e);
            }

        });

    }

    private void request(final int page) {
        String getBeersUrl = context.getResources().getString(R.string.get_beers_url);

        params.put("page", String.valueOf(page));
        getRequest(getBeersUrl, params, context.getCacheDir(), new NetworkListener() {
            @Override
            public void onSuccess(@NonNull Object response) {
                try {
                    List<BeersItem> beers = Util.jsonToList((String) response, BeersItem.class);
                    ((Listable) context).fillListView(beers, page == 1);
                } catch (JSONException e) {
                    ((Listable) context).onListViewError("Error parsing response.", e);
                }
            }

            @Override
            public void onError(@Nullable String response, @Nullable Exception e) {
                ((Listable) context).onListViewError(response, e);
            }
        });
    }


}
