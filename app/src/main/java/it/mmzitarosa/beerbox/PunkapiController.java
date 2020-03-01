package it.mmzitarosa.beerbox;

import android.content.Context;

import com.google.gson.Gson;
import com.punkapi.api2pojo.beers.BeersItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.mmzitarosa.beerbox.network.BeerBoxCallback;
import it.mmzitarosa.beerbox.network.Network;
import it.mmzitarosa.beerbox.util.Listable;

public class PunkapiController extends Network {

    private Context context;
    private Gson gson;

    public PunkapiController(Context context) {
        this.context = context;
        this.gson = new Gson();
    }

    public void getAllBeers() {
        getAllBeers(1);
    }

    public void getAllBeers(int page) {
        String getAllBeersUrl = urlSpecificPage(context.getResources().getString(R.string.get_beers_url), page);

        getRequest(getAllBeersUrl, new BeerBoxCallback() {
            @Override
            public void onSuccess(Object response) {
                try {
                    List<BeersItem> beers = jsonToList((String) response, BeersItem.class);
                    ((Listable) context).fillListView(beers);
                } catch (JSONException e) {
                    ((Listable) context).onListViewError("Error parsing response.", e);
                }
            }

            @Override
            public void onError(String error) {
                ((Listable) context).onListViewError(error, null);
            }
        });
    }

    private String urlSpecificPage(String url, int page) {
        return url + "?page=" + page;
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
