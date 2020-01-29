package com.neata.gastos;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neata.gastos.modelo.Categoria;
import com.neata.gastos.modelo.Item;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {


    private List<Item> items;
    private int layout;
    private OnItemClickListener itemClickListener;


    public ItemAdapter(List<Item> items, int layout, OnItemClickListener itemClickListener) {
        this.items = items;
        this.layout = layout;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(items.get(position), itemClickListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView nombre;
        public TextView precio;
        public TextView categoria;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.itemName);
            precio = itemView.findViewById(R.id.itemPrice);
            categoria = itemView.findViewById(R.id.itemCat);
        }

        public void bind(final Item item, final OnItemClickListener itemListener){

            nombre.setText(item.getNombre());
            precio.setText(item.getPrecio()+"");
            categoria.setText(item.getCategoria().getNombre());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemListener.onItemClick(item, getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Item item, int position);
    }


}
