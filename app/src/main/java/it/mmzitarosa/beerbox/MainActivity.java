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
import it.mmzitarosa.beerbox.network.BeerBoxCallback;
import it.mmzitarosa.beerbox.network.PunkapiNetwork;
import it.mmzitarosa.beerbox.util.Logger;

public class MainActivity extends AppCompatActivity {

    private static final int MY_INTERNET_PERMISSION = 777;

    private Context context;
    private RecyclerView recyclerView;
    private PunkapiNetwork punkapiNetwork;

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

    }

    private void requestBeersAndFillList() {
        punkapiNetwork.getAllBeers(new BeerBoxCallback() {
            @Override
            public void onSuccess(Object object) {
                List<BeersItem> beers = (List<BeersItem>) object;
                fillRecyclerView(beers);
            }

            @Override
            public void onError(String response) {
                //TODO to manage
                Logger.e(response);
            }
        });
    }

    private void fillRecyclerView(List<BeersItem> beers) {
        BeerAdapter beerAdapter = new BeerAdapter(beers, context);
        recyclerView.setAdapter(beerAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;

        requestForPermissions();
        loadViews();

        punkapiNetwork = new PunkapiNetwork(context);

        requestBeersAndFillList();

    }



}
