package it.mmzitarosa.beerbox.util;

import androidx.annotation.NonNull;

import com.punkapi.api2pojo.beers.BeersItem;

public interface Beerable {

    public void onMoreInfoClick(@NonNull BeersItem beer);

}

