package it.mmzitarosa.beerbox.util;

import androidx.annotation.NonNull;

import com.punkapi.api2pojo.beers.BeersItem;

import java.util.List;

public interface Listable {

    public void fillListView(@NonNull List<BeersItem> beers, boolean clean);

    public void onListViewLastItemReached(int lastPage);

    public void onListViewError(String message, Exception e);

}
