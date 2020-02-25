package it.mmzitarosa.beerbox;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import it.mmzitarosa.beerbox.adapter.BeerAdapter;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private void loadViews() {
        recyclerView = (RecyclerView) findViewById(R.id.main_beers_list);
        loadRecylerView();
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

        loadViews();

    }


}
