package com.neata.gastos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.neata.gastos.modelo.Categoria;
import com.neata.gastos.modelo.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemsListActivity  extends AppCompatActivity  implements ItemDialog.DialogListener {

    private FloatingActionButton fab;
    private RecyclerView recycler;

    private RecyclerView.LayoutManager mLayoutManager;

    private ItemAdapter adapter;

    private List<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        recycler = findViewById(R.id.recyclerView);
        recycler.setHasFixedSize(true);
        recycler.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(mLayoutManager);

        fab = findViewById(R.id.FABAddCity);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoticeDialog();
            }
        });
        fab = findViewById(R.id.FABAddCity);
        fillItems();
        adapter = new ItemAdapter(items, R.layout.list_view_item, new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Item item, int position) {
                //TODO: abrir dialog para editar valores del item
            }

        });

        recycler.setAdapter(adapter);
    }

    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new ItemDialog();
        dialog.show(getSupportFragmentManager(), "itemDialog");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    void fillItems(){
        items = new ArrayList<>();
        items.add(new Item("pan", 2.0, new Categoria("1", "comida")));
        items.add(new Item("pan1", 4.0, new Categoria("1", "comida")));
    }
}
