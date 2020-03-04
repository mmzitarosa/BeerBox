package it.mmzitarosa.beerbox.network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface NetworkListener {

    void onSuccess(@NonNull Object object);

    void onError(@Nullable String response, @Nullable Exception e);

}
