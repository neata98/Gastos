package com.neata.gastos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neata.gastos.modelo.Categoria;


import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    private List<Categoria> categorias;
    private int layout;
    private OnItemClickListener itemClickListener;


    public CategoryAdapter(List<Categoria> categorias, int layout, OnItemClickListener itemClickListener) {
        this.categorias = categorias;
        this.layout = layout;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(categorias.get(position), itemClickListener);
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView nombre;


        public ViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.categoryName);

        }

        public void bind(final Categoria categoria, final OnItemClickListener itemListener){

            nombre.setText(categoria.getNombre());


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemListener.onItemClick(categoria, getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Categoria categoria, int position);
    }
}
