package it.mmzitarosa.beerbox;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.punkapi.api2pojo.beers.BeersItem;

import it.mmzitarosa.beerbox.network.BeerBoxCallback;
import it.mmzitarosa.beerbox.network.Network;

public class InfoBottomSheetDialog extends BottomSheetDialogFragment {

    BeersItem beer;
    Network network;
    private ImageView image;
    private TextView title;
    private TextView tagline;
    private TextView description;

    public InfoBottomSheetDialog(BeersItem beer) {
        this.beer = beer;
        this.network = new Network();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_dialog_info, container, false);
        image = v.findViewById(R.id.more_info_beer_image);
        title = v.findViewById(R.id.more_info_beer_title);
        tagline = v.findViewById(R.id.more_info_beer_tagline);
        description = v.findViewById(R.id.more_info_beer_description);

        title.setText(beer.getName());
        tagline.setText(beer.getTagline());
        description.setText(beer.getDescription());

        Bitmap bitmap;
        network.getRequest(beer.getImage_url(), Network.Content.MEDIA_IMAGE, new BeerBoxCallback() {
            @Override
            public void onSuccess(Object object) {
                image.setImageBitmap((Bitmap) object);
            }

            @Override
            public void onError(String response) {

            }
        });

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
