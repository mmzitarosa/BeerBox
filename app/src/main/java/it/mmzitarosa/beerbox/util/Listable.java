package it.mmzitarosa.beerbox.util;

import com.punkapi.api2pojo.beers.BeersItem;

import java.util.List;

public interface Listable {

    public void fillListView(List<BeersItem> beers);

    public void onListViewLastItemReached(int lastPage);

    public void onListViewError(String message, Exception e);

}