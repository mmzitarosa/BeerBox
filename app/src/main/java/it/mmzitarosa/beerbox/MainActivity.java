package it.mmzitarosa.beerbox;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.punkapi.api2pojo.beers.BeersItem;

import java.util.List;

import it.mmzitarosa.beerbox.adapter.BeerAdapter;
import it.mmzitarosa.beerbox.network.PunkApiNetworkController;
import it.mmzitarosa.beerbox.util.Beerable;
import it.mmzitarosa.beerbox.util.Listable;
import it.mmzitarosa.beerbox.util.Logger;

public class MainActivity extends AppCompatActivity implements Listable, Beerable {

    private static final int MY_INTERNET_PERMISSION = 777;

    private Context context;
    private RecyclerView recyclerView;
    private BeerAdapter beerAdapter;
    private PunkApiNetworkController networkController;

    private void requestForPermissions() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, MY_INTERNET_PERMISSION);
        }
    }

    private void loadViews() {
        recyclerView = (RecyclerView) findViewById(R.id.main_beers_list);
        configureRecyclerView();
    }

    private void configureRecyclerView() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager beerLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(beerLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        beerAdapter = null;
    }

    public PunkApiNetworkController getNetworkController() {
        return networkController;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;

        requestForPermissions();
        loadViews();

        networkController = new PunkApiNetworkController(context);

        networkController.getAllBeers();

    }

    @Override
    public void onMoreInfoClick(BeersItem beer) {
        InfoBottomSheetDialog infoBottomSheetDialog = new InfoBottomSheetDialog(beer, context);
        infoBottomSheetDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), infoBottomSheetDialog.getTag());
    }

    @Override
    public void fillListView(@NonNull List<BeersItem> beers) {
        if (beerAdapter == null) {
            beerAdapter = new BeerAdapter(beers, context);
            recyclerView.setAdapter(beerAdapter);
        } else {
            beerAdapter.addBeers(beers);
        }
    }

    @Override
    public void onListViewLastItemReached(@NonNull int lastPage) {
        networkController.getAllBeers(lastPage + 1);
    }

    @Override
    public void onListViewError(String message, @Nullable Exception e) {
        Logger.e(message, e);
        Toast.makeText(context, "An error occurred while processing the request.", Toast.LENGTH_SHORT).show();
    }
}
