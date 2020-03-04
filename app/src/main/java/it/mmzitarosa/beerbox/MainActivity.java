package it.mmzitarosa.beerbox;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
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

public class MainActivity extends AppCompatActivity implements Listable, Beerable {

    private static final int MY_INTERNET_PERMISSION = 777;

    private RecyclerView recyclerView;
    private SearchView searchView;
    private ChipGroup chipGroup;

    private Context context;
    private BeerAdapter beerAdapter;
    private PunkApiNetworkController networkController;
    private boolean ignoreSearch;

    private void requestForPermissions() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, MY_INTERNET_PERMISSION);
        }
    }

    private void loadViews() {
        //searchEditText = (EditText) findViewById(R.id.main_search_edit_text);
        searchView = findViewById(R.id.main_search_edit_text);
        configureSearchView();
        chipGroup = findViewById(R.id.main_chip_group);
        configureChipGroup();
        recyclerView = findViewById(R.id.main_beers_list);
        configureRecyclerView();
    }

    private void configureSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (ignoreSearch) {
                    ignoreSearch = false;
                    return false;
                }

                if (chipGroup.getCheckedChipId() != View.NO_ID) {
                    ignoreSearch = true;
                    chipGroup.clearCheck();
                }

                if (!newText.isEmpty())
                    networkController.getSelectedBeers(newText);
                else
                    networkController.getAllBeers();
                return false;
            }
        });
    }


    private void configureRecyclerView() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager beerLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(beerLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        beerAdapter = null;
    }

    private void configureChipGroup() {
        String[] categories = getResources().getStringArray(R.array.categories);
        CompoundButton.OnCheckedChangeListener changeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ((Chip) buttonView).setChipBackgroundColorResource(R.color.colorOrange);
                    buttonView.setTextColor(getResources().getColor(R.color.colorText));
                } else {
                    ((Chip) buttonView).setChipBackgroundColorResource(R.color.colorItems);
                    buttonView.setTextColor(getResources().getColor(R.color.colorDarkText));
                }
            }
        };
        for (final String category : categories) {
            Chip chip = new Chip(context);
            chip.setText(category);
            chip.setOnCheckedChangeListener(changeListener);
            chipGroup.addView(chip);
        }
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                if (ignoreSearch) {
                    ignoreSearch = false;
                    return;
                }

                Chip chip = (Chip) group.getChildAt(checkedId - 1);
                if (chip == null)
                    networkController.getAllBeers();
                else {
                    networkController.getSelectedBeers(chip.getText().toString());
                    if (!searchView.getQuery().toString().isEmpty()) {
                        ignoreSearch = true;
                        searchView.clearFocus();
                        searchView.setQuery("", false);
                    }
                }
            }
        });
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
    public void onMoreInfoClick(@NonNull BeersItem beer) {
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
    public void onListViewLastItemReached(int lastPage) {
        networkController.getMoreBeers(lastPage + 1);
    }

    @Override
    public void onListViewError(String message, @Nullable Exception e) {
        Logger.e(message, e);
        Toast.makeText(context, "An error occurred while processing the request.", Toast.LENGTH_SHORT).show();
    }


}
