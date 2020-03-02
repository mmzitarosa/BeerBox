package it.mmzitarosa.beerbox.network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface NetworkListener {

    public abstract void onSuccess(@NonNull Object object);

    public abstract void onError(@Nullable String response, @Nullable Exception e);

}
