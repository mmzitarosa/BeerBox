package it.mmzitarosa.beerbox;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.punkapi.api2pojo.beers.BeersItem;

import java.util.List;

import it.mmzitarosa.beerbox.adapter.BeerAdapter;
import it.mmzitarosa.beerbox.network.PunkApiNetworkController;
import it.mmzitarosa.beerbox.util.Beerable;
import it.mmzitarosa.beerbox.util.Listable;
import it.mmzitarosa.beerbox.util.Logger;

public class MainActivity extends AppCompatActivity implements Listable, Beerable, CompoundButton.OnCheckedChangeListener {

    private static final int MY_INTERNET_PERMISSION = 777;

    private RecyclerView recyclerView;
    private SearchView searchView;
    private ChipGroup chipGroup;

    private Context context;
    private BeerAdapter beerAdapter;
    private PunkApiNetworkController networkController;

    private void requestForPermissions() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, MY_INTERNET_PERMISSION);
        }
    }

    private void loadViews() {
        //searchEditText = (EditText) findViewById(R.id.main_search_edit_text);
        searchView = (SearchView) findViewById(R.id.main_search_edit_text);
        chipGroup = (ChipGroup) findViewById(R.id.main_chip_group);
        configureChipGroup();
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

    public void configureChipGroup() {
        String[] categories = {"Blonde", "Lager", "Malts", "Ipa", "Cipolla", "Blonde", "Lager", "Malts", "Ipa", "Cipolla"};
        for (final String category : categories) {
            Chip chip = new Chip(context);

            chip.setText(category);
            chip.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) context);
            chipGroup.addView(chip);
        }
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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty())
                    networkController.getSelectedBeers(newText);
                else
                    networkController.getAllBeers();
                return false;
            }
        });
    }

    @Override
    public void onMoreInfoClick(BeersItem beer) {
        InfoBottomSheetDialog infoBottomSheetDialog = new InfoBottomSheetDialog(beer, context);
        infoBottomSheetDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), infoBottomSheetDialog.getTag());
    }

    @Override
    public void fillListView(@NonNull List<BeersItem> beers, boolean clean) {
        if (beerAdapter == null) {
            beerAdapter = new BeerAdapter(beers, context);
            recyclerView.setAdapter(beerAdapter);
        } else {
            if (clean) {
                beerAdapter.clean();
            }
            beerAdapter.addBeers(beers);
        }
    }

    @Override
    public void onListViewLastItemReached(@NonNull int lastPage) {
        String searchText;
        if (!(searchText = searchView.getQuery().toString()).isEmpty())
            networkController.getSelectedBeers(searchText, lastPage + 1);
        else
            networkController.getAllBeers(lastPage + 1);
    }

    @Override
    public void onListViewError(String message, @Nullable Exception e) {
        Logger.e(message, e);
        Toast.makeText(context, "An error occurred while processing the request.", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            ((Chip) buttonView).setChipBackgroundColorResource(R.color.colorOrange);
            ((Chip) buttonView).setTextColor(getResources().getColor(R.color.colorText));
            networkController.getSelectedBeers(buttonView.getText().toString());
        } else {
            ((Chip) buttonView).setChipBackgroundColorResource(R.color.colorItems);
            ((Chip) buttonView).setTextColor(getResources().getColor(R.color.colorDarkText));
        }
    }

}
