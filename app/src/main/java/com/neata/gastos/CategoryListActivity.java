package com.neata.gastos;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        fillCategories();

        recycler = findViewById(R.id.recyclerView);
        recycler.setHasFixedSize(true);
        recycler.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(mLayoutManager);

        fab = findViewById(R.id.FABAddCity);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: gestionar click en el fab con un dialog para añadir categoria
            }
        });
        fab = findViewById(R.id.FABAddCity);

        progressBar = findViewById(R.id.progressBar);


    }

    private void fillCategories(){
        categorias = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("categoria")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println("good: " + document.getId() + " => " + document.getData());

                                categorias.add(new Categoria(document.getId(), (String) document.getData().get("nombre")));
                                System.out.println(document.getData().get("nombre"));
                            }
                            showResults();
                        } else {
                            throw new Resources.NotFoundException("No se ha podido acceder a las categorías");
                        }
                    }
                });

//                Map<String, Object> user = new HashMap<>();
//                user.put("nombre", "almuerzo");
//
//                db.collection("categoria")
//                .add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        System.out.println("HEHEHHE: "+ documentReference.getId());
//                        //Log.d("Good", "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w("Bad", "Error adding document", e);
//                    }
//                });
    }

    private void showResults() {

        adapter = new CategoryAdapter(categorias, R.layout.category_item, new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Categoria categoria, int position) {
                //TODO: abrir dialog para borrar categoria
            }

        });

        recycler.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
    }
}
