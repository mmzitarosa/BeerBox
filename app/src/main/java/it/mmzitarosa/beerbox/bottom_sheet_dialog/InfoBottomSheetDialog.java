package it.mmzitarosa.beerbox.bottom_sheet_dialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.punkapi.api2pojo.beers.BeersItem;

import it.mmzitarosa.beerbox.R;
import it.mmzitarosa.beerbox.network.BitmapNetworkListener;
import it.mmzitarosa.beerbox.network.PunkApiNetworkController;

public class InfoBottomSheetDialog extends BottomSheetDialogFragment {

    private BeersItem beer;
    private ImageView image;
    private PunkApiNetworkController networkController;
    private SharedPreferences sharedPreferences;

    private OnCheckedBookmark onCheckedBookmark;

    public InfoBottomSheetDialog(BeersItem beer, Context context) {
        this.beer = beer;
        this.networkController = new PunkApiNetworkController(context);
        this.sharedPreferences = context.getSharedPreferences("bookmarks", Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_dialog_info, container, false);
        image = v.findViewById(R.id.more_info_beer_image);
        TextView title = v.findViewById(R.id.more_info_beer_title);
        TextView tagline = v.findViewById(R.id.more_info_beer_tagline);
        TextView description = v.findViewById(R.id.more_info_beer_description);
        ImageButton bookmark = v.findViewById(R.id.more_info_bookmark);

        title.setText(beer.getName());
        tagline.setText(beer.getTagline());
        description.setText(beer.getDescription());

        networkController.getBitmapFromUrl(beer.getImage_url(), new BitmapNetworkListener() {
            @Override
            public void onReady(@NonNull Bitmap bitmap) {
                image.setImageBitmap(bitmap);
            }
        });

        if (sharedPreferences.getBoolean(String.valueOf(beer.getId()), false)) {
            bookmark.setActivated(true);
        }

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setActivated(!v.isActivated());
                sharedPreferences.edit().putBoolean(String.valueOf(beer.getId()), v.isActivated()).apply();
                if (onCheckedBookmark != null) {
                    onCheckedBookmark.onCheched();
                }
            }
        });

        return v;
    }

    public void setOnCheckedBookmarkListener(OnCheckedBookmark onCheckedBookmarkListener) {
        this.onCheckedBookmark = onCheckedBookmarkListener;
    }

}
