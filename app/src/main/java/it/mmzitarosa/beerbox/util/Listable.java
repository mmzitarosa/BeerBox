package it.mmzitarosa.beerbox.util;

import androidx.annotation.NonNull;

import com.punkapi.api2pojo.beers.BeersItem;

import java.util.List;

public interface Listable {

    void fillListView(@NonNull List<BeersItem> beers, boolean clean);

    void onListViewLastItemReached(int lastPage);

    void onListViewError(String message, Exception e);

}
