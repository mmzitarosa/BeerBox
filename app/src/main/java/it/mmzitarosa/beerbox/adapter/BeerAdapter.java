package it.mmzitarosa.beerbox.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.punkapi.api2pojo.beers.BeersItem;

import java.util.ArrayList;
import java.util.List;

import it.mmzitarosa.beerbox.MainActivity;
import it.mmzitarosa.beerbox.R;
import it.mmzitarosa.beerbox.network.BitmapNetworkListener;
import it.mmzitarosa.beerbox.util.Beerable;
import it.mmzitarosa.beerbox.util.Listable;

public class BeerAdapter extends RecyclerView.Adapter<BeerAdapter.BeerViewHolder> {

    private List<BeersItem> beers;
    private Context context;
    private int lastPage;
    private boolean alreadyDone;

    public BeerAdapter(List<BeersItem> beers, Context context) {
        this.context = context;
        this.beers = beers;
        lastPage = 0;
        alreadyDone = false;
    }

    public void clean() {
        beers = new ArrayList<>();
        lastPage = -1;
        alreadyDone = false;
        notifyDataSetChanged();
    }

    public void addBeers(List<BeersItem> beers) {
        //block to avoid unnecessary calls (onListViewLastItemReached)
        if (!beers.isEmpty()) {
            this.beers.addAll(beers);
            lastPage++;
            alreadyDone = false;
            notifyDataSetChanged();
        }
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

        ((MainActivity) context).getNetworkController().getBitmapFromUrl(beer.getImage_url(), new BitmapNetworkListener() {
            @Override
            public void onReady(@NonNull Bitmap bitmap) {
                holder.image.setImageBitmap(bitmap);
            }
        });

        holder.moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Beerable) context).onMoreInfoClick(beer);
            }
        });

        if (position == 0) {
            holder.itemView.setPadding(holder.itemView.getPaddingLeft(), (int) context.getResources().getDimension(R.dimen.beer_first_item_padding_top), holder.itemView.getPaddingRight(), holder.itemView.getPaddingBottom());
        }

        if (position == beers.size() - 1 && !alreadyDone) {
            alreadyDone = true;
            ((Listable) context).onListViewLastItemReached(lastPage + 1);
        }
    }

    @Override
    public int getItemCount() {
        return beers.size();
    }

    static class BeerViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView title;
        private TextView tagline;
        private TextView description;
        private TextView moreInfo;

        BeerViewHolder(@NonNull View v) {
            super(v);
            image = v.findViewById(R.id.beer_image);
            title = v.findViewById(R.id.beer_title);
            tagline = v.findViewById(R.id.beer_tagline);
            description = v.findViewById(R.id.beer_description);
            moreInfo = v.findViewById(R.id.beer_more_info);
        }
    }

}
