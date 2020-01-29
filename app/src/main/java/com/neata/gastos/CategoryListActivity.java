package com.neata.gastos;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.neata.gastos.modelo.Categoria;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryListActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView recycler;
    private ProgressBar progressBar;

    private RecyclerView.LayoutManager mLayoutManager;

    private CategoryAdapter adapter;

    private List<Categoria> categorias;

    private AlertDialog addCategoryDialog;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        setTitle("Categorías");
        recycler = findViewById(R.id.recyclerView);
        recycler.setHasFixedSize(true);
        recycler.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(mLayoutManager);

        categorias = new ArrayList<>();

        adapter = new CategoryAdapter(categorias, R.layout.category_item, new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final Categoria categoria, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CategoryListActivity.this);
// Add the buttons
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        db.collection("categoria").document(categoria.getId())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        categorias.remove(position);
                                        recycler.removeViewAt(position);
                                        adapter.notifyItemRemoved(position);
                                        adapter.notifyItemRangeChanged(position, categorias.size());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        throw new Resources.NotFoundException("No se ha podido borrar la categoría");
                                    }
                                });
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                // Set other dialog properties
                builder.setMessage("¿Quieres borrar esta categoría?")
                        .setTitle("Borrar categoría");


                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();


            }

        });

        recycler.setAdapter(adapter);
        fab = findViewById(R.id.FABAddCity);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addCategoryDialog.show();
            }
        });
        fab = findViewById(R.id.FABAddCity);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        fillCategories();
        createDialog();

    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CategoryListActivity.this);
        final EditText input = new EditText(CategoryListActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        // Add the buttons
        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                final String editText = input.getText().toString();
                Map<String, Object> categoria = new HashMap<>();
                categoria.put("nombre", editText);

                db.collection("categoria")
                .add(categoria)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Log.d("Good", "DocumentSnapshot added with ID: " + documentReference.getId());
                        categorias.add(new Categoria(documentReference.getId(), editText));
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        try {
                            throw new IOException("No se ha podido guardar la categoría");
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        // Set other dialog properties
        builder.setMessage("¿Quieres añadir una nueva categoría?")
                .setTitle("Añadir nueva categoría");

        input.setLayoutParams(lp);
        builder.setView(input);

        // Create the AlertDialog
       addCategoryDialog = builder.create();

    }

    private void fillCategories(){
        db.collection("categoria")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                categorias.add(new Categoria(document.getId(), (String) document.getData().get("nombre")));
                            }
                            showResults();
                        } else {
                            throw new Resources.NotFoundException("No se ha podido acceder a las categorías");
                        }
                    }
                });

    }

    private void showResults() {

        adapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }
}
