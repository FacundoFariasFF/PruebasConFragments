package com.example.fragmentosapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class MonedasAdapter extends RecyclerView.Adapter<MonedasAdapter.ViewHolder>{

    Context context;
    List<Monedas> monedasList;

    public MonedasAdapter(Context context, List<Monedas>monedasList){
        setHasStableIds(true);
        this.context = context;
        this.monedasList = monedasList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_monedas,parent,false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Monedas monedas = monedasList.get(position);

        holder.nombre.setText(monedas.getNombre());
        holder.compra.setText("Precio de Compra: " +  monedas.getCompra());
        holder.venta.setText("Precio de Venta: " + monedas.getVenta());
        holder.variacion.setText("Variacion: " + monedas.getVariacion()+"%");

        switch (monedas.nombre)
        {
            case "Dolar Oficial": holder.imageView.setImageResource(R.drawable.dolar);
                break;
            case "Dolar Blue": holder.imageView.setImageResource(R.drawable.blue);
                break;
            case "Dolar Soja": holder.imageView.setImageResource(R.drawable.soja);
                break;
            case "Dolar Contado con Liqui": holder.imageView.setImageResource(R.drawable.liqui);
                break;
            case "Dolar Bolsa": holder.imageView.setImageResource(R.drawable.bolsa);
                break;
            case "Bitcoin": holder.imageView.setImageResource(R.drawable.bitcoin);
                break;
            case "Dolar turista": holder.imageView.setImageResource(R.drawable.turista);
                break;
            default:holder.imageView.setImageResource(R.drawable.dolar);

        }
    }

    @Override
    public int getItemCount() {
        return monedasList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombre,compra,venta,variacion;
        CardView cardView;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.txt_nombre);
            compra = itemView.findViewById(R.id.txt_compra);
            venta = itemView.findViewById(R.id.txt_venta);
            variacion = itemView.findViewById(R.id.txt_variacion);
            imageView = itemView.findViewById(R.id.imagen_nombre);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }

}
