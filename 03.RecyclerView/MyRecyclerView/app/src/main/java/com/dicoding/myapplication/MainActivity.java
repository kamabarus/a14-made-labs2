package com.dicoding.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dicoding.myapplication.adapter.CardViewPresidentAdapter;
import com.dicoding.myapplication.adapter.GridPresidentAdapter;
import com.dicoding.myapplication.adapter.ListPresidentAdapter;
import com.dicoding.myapplication.listener.ItemClickSupport;
import com.dicoding.myapplication.model.President;
import com.dicoding.myapplication.model.PresidentData;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView rvCategory;
    private ArrayList<President> list;
    final String STATE_TITLE = "state_string";
    final String STATE_LIST = "state_list";
    final String STATE_MODE = "state_mode";
    int mode;
    String title = "Mode List";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvCategory = findViewById(R.id.rv_category);
        rvCategory.setHasFixedSize(true);

        list = new ArrayList<>();

        /*
        Gunakanlah savedinstancestate untuk menjaga data ketika terjadi config changes
         */
        if (savedInstanceState == null) {
            /*
            Pada saat pertama kali activity dijalankan,
            Ambil data dari method getListData, kemudian tampilkan recyclerviewlist
             */
            setActionBarTitle(title);
            list.addAll(PresidentData.getListData());
            showRecyclerList();
            mode = R.id.action_list;

        } else {
            /*
            Jika terjadi config changes maka ambil data yang dikirimkan dari saveinstancestate
             */
            title = savedInstanceState.getString(STATE_TITLE);
            ArrayList<President> stateList = savedInstanceState.getParcelableArrayList(STATE_LIST);
            int stateMode = savedInstanceState.getInt(STATE_MODE);

            /*
            Set data untuk title, list, dan mode yang dipilih
             */
            setActionBarTitle(title);
            list.addAll(stateList);
            setMode(stateMode);
        }

    }

    private void showSelectedPresident(President president) {
        Toast.makeText(this, "Kamu memilih " + president.getName(), Toast.LENGTH_SHORT).show();
    }

    private void showRecyclerList() {
        rvCategory.setLayoutManager(new LinearLayoutManager(this));
        ListPresidentAdapter listPresidentAdapter = new ListPresidentAdapter(this);
        listPresidentAdapter.setListPresident(list);
        rvCategory.setAdapter(listPresidentAdapter);

        ItemClickSupport.addTo(rvCategory).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showSelectedPresident(list.get(position));
            }
        });
    }

    private void showRecyclerGrid() {
        rvCategory.setLayoutManager(new GridLayoutManager(this, 2));
        GridPresidentAdapter gridPresidentAdapter = new GridPresidentAdapter(this);
        gridPresidentAdapter.setListPresident(list);
        rvCategory.setAdapter(gridPresidentAdapter);

        ItemClickSupport.addTo(rvCategory).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showSelectedPresident(list.get(position));
            }
        });
    }

    private void showRecyclerCardView() {
        rvCategory.setLayoutManager(new LinearLayoutManager(this));
        CardViewPresidentAdapter cardViewPresidentAdapter = new CardViewPresidentAdapter(this);
        cardViewPresidentAdapter.setListPresident(list);
        rvCategory.setAdapter(cardViewPresidentAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        setMode(item.getItemId());

        return super.onOptionsItemSelected(item);
    }

    /*
    Method ini digunakan untuk seleksi mode yang dipilih
     */
    public void setMode(int selectedMode) {
        switch (selectedMode) {
            case R.id.action_list:
                title = "Mode List";
                showRecyclerList();
                break;

            case R.id.action_grid:
                title = "Mode Grid";
                showRecyclerGrid();
                break;

            case R.id.action_cardview:
                title = "Mode CardView";
                showRecyclerCardView();
                break;
        }
        /*
        Simpan jenis recyclerview yang sudah dipilih ke dalam variable mode
         */
        mode = selectedMode;
        setActionBarTitle(title);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_TITLE, title);
        outState.putParcelableArrayList(STATE_LIST, list);
        outState.putInt(STATE_MODE, mode);
    }
}
