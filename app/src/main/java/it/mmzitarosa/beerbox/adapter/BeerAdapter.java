package it.mmzitarosa.beerbox.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.punkapi.api2pojo.beers.BeersItem;

import java.util.ArrayList;
import java.util.List;

import it.mmzitarosa.beerbox.InfoBottomSheetDialog;
import it.mmzitarosa.beerbox.R;
import it.mmzitarosa.beerbox.network.BeerBoxCallback;
import it.mmzitarosa.beerbox.network.Network;

public class BeerAdapter extends RecyclerView.Adapter<BeerAdapter.BeerViewHolder> {

    private List<BeersItem> beers;
    private Context context;
    private Network network;
    private List<Bitmap> bitmaps;

    public BeerAdapter(List<BeersItem> beers, Context context) {
        this.context = context;
        this.network = new Network();
        this.beers = beers;
        bitmaps = new ArrayList<>();
    }

    @NonNull
    @Override
    public BeerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.beer_item, parent, false);
        return new BeerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final BeerViewHolder holder, final int position) {
        final BeersItem beer = beers.get(position);
        holder.title.setText(beer.getName());
        holder.tagline.setText(beer.getTagline());
        holder.description.setText(beer.getDescription());

        Bitmap bitmap;
        if (position < bitmaps.size() && (bitmap = bitmaps.get(position)) != null) {
            holder.image.setImageBitmap((Bitmap) bitmap);
        } else {
            network.getRequest(beer.getImage_url(), Network.Content.MEDIA_IMAGE, new BeerBoxCallback() {
                @Override
                public void onSuccess(Object object) {
                    bitmaps.add(position, (Bitmap) object);
                    holder.image.setImageBitmap((Bitmap) object);
                }

                @Override
                public void onError(String response) {

                }
            });
        }

        holder.moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoBottomSheetDialog infoBottomSheetDialog = new InfoBottomSheetDialog(beer);
                infoBottomSheetDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), infoBottomSheetDialog.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return beers.size();
    }

    public static class BeerViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView title;
        private TextView tagline;
        private TextView description;
        private TextView moreInfo;

        public BeerViewHolder(@NonNull View v) {
            super(v);
            image = v.findViewById(R.id.beer_image);
            title = v.findViewById(R.id.beer_title);
            tagline = v.findViewById(R.id.beer_tagline);
            description = v.findViewById(R.id.beer_description);
            moreInfo = v.findViewById(R.id.beer_more_info);
        }
    }

}
