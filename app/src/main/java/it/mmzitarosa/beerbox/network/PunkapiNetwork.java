package it.mmzitarosa.beerbox.network;

import android.content.Context;

import com.google.gson.Gson;
import com.punkapi.api2pojo.beers.BeersItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.mmzitarosa.beerbox.R;

public class PunkapiNetwork extends Network {

    private Context context;
    private String url;
    private Gson gson;

    public PunkapiNetwork(Context context) {
        this.context = context;
        this.gson = new Gson();
    }

    public void getAllBeers(final BeerBoxCallback callback) {
        String getAllBeersUrl = context.getResources().getString(R.string.get_beers_url);

        getRequest(getAllBeersUrl, new BeerBoxCallback() {
            @Override
            public void onSuccess(Object response) {
                try {
                    List<BeersItem> beers = jsonToList((String) response, BeersItem.class);
                    callback.onSuccess(beers);
                } catch (JSONException e) {
                    callback.onError("Error parsing response.", e);
                }
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    private <T> T jsonToObject(String response, Class<T> classType) throws JSONException {
        JSONObject object = new JSONObject(response);
        return gson.fromJson(object.toString(), classType);
    }

    private <T> List<T> jsonToList(String response, Class<T> classType) throws JSONException {
        JSONArray jsonArray = new JSONArray(response);
        ArrayList<T> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), classType));
        }
        return list;
    }

}
