package it.mmzitarosa.beerbox.network;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

public interface BitmapNetworkListener {

    public void onReady(@NonNull Bitmap bitmap);

}
