package it.mmzitarosa.beerbox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import it.mmzitarosa.beerbox.R;

public class BeerAdapter extends RecyclerView.Adapter<BeerAdapter.BeerViewHolder> {

    @NonNull
    @Override
    public BeerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.beer_item, parent, false);
        return new BeerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BeerViewHolder holder, int position) {
        //TODO set content nell'item della lista
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public static class BeerViewHolder extends RecyclerView.ViewHolder {

        //TODO definire qui le view

        public BeerViewHolder(@NonNull View itemView) {
            super(itemView);
            //TODO definire qui le view
        }
    }

}
