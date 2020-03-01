package it.mmzitarosa.beerbox;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.punkapi.api2pojo.beers.BeersItem;

import java.util.List;

import it.mmzitarosa.beerbox.adapter.BeerAdapter;
import it.mmzitarosa.beerbox.util.Beerable;
import it.mmzitarosa.beerbox.util.Listable;
import it.mmzitarosa.beerbox.util.Logger;

public class MainActivity extends AppCompatActivity implements Listable, Beerable {

    private static final int MY_INTERNET_PERMISSION = 777;

    private Context context;
    private RecyclerView recyclerView;
    private BeerAdapter beerAdapter;
    private PunkapiController punkapiController;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;

        requestForPermissions();
        loadViews();

        punkapiController = new PunkapiController(context);

        punkapiController.getAllBeers();

    }

    @Override
    public void onMoreInfoClick(BeersItem beer) {
        InfoBottomSheetDialog infoBottomSheetDialog = new InfoBottomSheetDialog(beer);
        infoBottomSheetDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), infoBottomSheetDialog.getTag());
    }

    @Override
    public void fillListView(List<BeersItem> beers) {
        if (beerAdapter == null) {
            beerAdapter = new BeerAdapter(beers, context);
            recyclerView.setAdapter(beerAdapter);
        } else {
            beerAdapter.addBeers(beers);
        }
    }

    @Override
    public void onListViewLastItemReached(int lastPage) {
        punkapiController.getAllBeers(lastPage + 1);
    }

    @Override
    public void onListViewError(String message, Exception e) {
        Logger.e(message);
        if (e != null) {
            e.printStackTrace();
        }
    }
}
