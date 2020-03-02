package it.mmzitarosa.beerbox;

import android.content.Context;
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

import it.mmzitarosa.beerbox.network.BitmapNetworkListener;
import it.mmzitarosa.beerbox.network.PunkApiNetworkController;

public class InfoBottomSheetDialog extends BottomSheetDialogFragment {

    private BeersItem beer;
    private ImageView image;
    private TextView title;
    private TextView tagline;
    private TextView description;
    private PunkApiNetworkController networkController;
    private Context context;

    public InfoBottomSheetDialog(BeersItem beer, Context context) {
        this.beer = beer;
        this.context = context;
        this.networkController = new PunkApiNetworkController(context);
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

        networkController.getBitmapFromUrl(beer.getImage_url(), new BitmapNetworkListener() {
            @Override
            public void onReady(@NonNull Bitmap bitmap) {
                image.setImageBitmap(bitmap);
            }
        });

        return v;
    }

}
