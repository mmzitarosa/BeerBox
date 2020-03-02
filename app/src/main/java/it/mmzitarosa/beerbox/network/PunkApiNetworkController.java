package it.mmzitarosa.beerbox.network;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.punkapi.api2pojo.beers.BeersItem;

import org.json.JSONException;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.mmzitarosa.beerbox.R;
import it.mmzitarosa.beerbox.util.Listable;
import it.mmzitarosa.beerbox.util.Logger;
import it.mmzitarosa.beerbox.util.Util;

public class PunkApiNetworkController extends Network {

    private Context context;

    public PunkApiNetworkController(Context context) {
        this.context = context;
    }

    public void getAllBeers() {
        getAllBeers(1);
    }

    public void getAllBeers(final int page) {
        String getAllBeersUrl = context.getResources().getString(R.string.get_beers_url);

        Map<String, String> params = new HashMap<>();
        params.put("page", String.valueOf(page));

        getRequest(getAllBeersUrl, params, new NetworkListener() {
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

    public void getBitmapFromUrl(@NonNull String url, final BitmapNetworkListener bitmapNetworkListener) {
        String urlSHA1 = Util.SHA1(url);
        File file = null;
        if (urlSHA1 != null) {
            file = new File(context.getCacheDir(), urlSHA1);
        }
        getRequest(url, file, new NetworkListener() {
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

    public void getSelectedBeers(String string) {
        getSelectedBeers(string, 1);
    }

    public void getSelectedBeers(String string, final int page) {
        String getBeersUrl = context.getResources().getString(R.string.get_beers_url);

        Map<String, String> params = new HashMap<>();
        params.put("beer_name", string.trim().replace(" ", "_"));
        params.put("page", String.valueOf(page));

        getRequest(getBeersUrl, params, new NetworkListener() {
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
