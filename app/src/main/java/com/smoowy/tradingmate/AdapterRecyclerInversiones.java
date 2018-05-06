package com.smoowy.tradingmate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterRecyclerInversiones extends RecyclerView.Adapter<AdapterRecyclerInversiones.Holder> {


    LayoutInflater inflater;
    int datos;
    ArrayList<DBOpInversiones> lista;
    ArrayList<DBOpInversiones> listaRespaldo;

    public AdapterRecyclerInversiones(Context context) {

        inflater = LayoutInflater.from(context);
        datos = 0;
        lista = new ArrayList<>();
        listaRespaldo = new ArrayList<>();

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.recycler_view_inversiones, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        if (datos == 0)
            return;

        DBOpInversiones op = lista.get(position);

        holder.inversion.setText(op.getInversion());
        holder.cantidad.setText(op.getCantidad());
        holder.precio.setText(op.getPrecio());
    }

    @Override
    public int getItemCount() {
        return datos;
    }


    public void agregarDatos(String precio, String inversion, String cantidad) {
        datos += 1;
        DBOpInversiones op = new DBOpInversiones();
        op.setCantidad(cantidad);
        op.setPrecio(precio);
        op.setInversion(inversion);
        lista.add(op);
        notifyDataSetChanged();
    }


    public void quitarDatos(Integer position) {
        listaRespaldo = new ArrayList<>(lista);
        datos -= 1;
        DBOpInversiones op = lista.get(position);
        lista.remove(op);
        notifyDataSetChanged();
    }


    public void recuperarDatos() {
        lista = new ArrayList<>(listaRespaldo);
        datos +=1;
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView precio, inversion, cantidad;

        public Holder(View itemView) {
            super(itemView);
            precio = itemView.findViewById(R.id.precioRV);
            inversion = itemView.findViewById(R.id.inversionRV);
            cantidad = itemView.findViewById(R.id.cantidadRV);
        }
    }


}


