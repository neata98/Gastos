package com.neata.gastos;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.neata.gastos.modelo.Categoria;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CardView gastosCardView;
    private CardView catCardView;
    public static List<Categoria> categorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gastosCardView = findViewById(R.id.gastosCardView);
        gastosCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ItemsListActivity.class);
                startActivity(intent);
            }
        });

        catCardView = findViewById(R.id.catCardview);
        catCardView.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               Intent intent = new Intent(MainActivity.this, CategoryListActivity.class);
                                               startActivity(intent);
                                           }
                                       }

        );
        //fillCategories();
    }

    private void fillCategories() {
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
                        } else {
                            throw new Resources.NotFoundException("No se ha podido acceder a las categorías");
                        }
                    }
                });
    }
}
