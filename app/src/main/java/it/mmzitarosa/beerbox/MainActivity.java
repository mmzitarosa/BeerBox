package it.mmzitarosa.beerbox;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

    private RecyclerView recyclerView;
    private PunkapiNetwork punkapiNetwork;

    private void load() {
        requestForPermissions();
        punkapiNetwork = new PunkapiNetwork(MainActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.main_beers_list);
        loadRecylerView();
    }

    private void requestForPermissions() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, MY_INTERNET_PERMISSION);
        }
    }

    private void loadRecylerView() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager beerLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(beerLayoutManager);
        RecyclerView.Adapter beerAdapter = new BeerAdapter();
        recyclerView.setAdapter(beerAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        load();

        punkapiNetwork.getAllBeers(new BeerBoxCallback() {
            @Override
            public void onSuccess(Object object) {
                List<BeersItem> list = (List<BeersItem>) object;
                for (BeersItem beersItem : list) {
                    Logger.i(beersItem.getName());
                }
            }

            @Override
            public void onError(String response) {
                Logger.e(response);
            }
        });

    }


}
