package com.smoowy.tradingmate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.TreeMap;

public class AdapterRecyclerBotonesPorcentajes extends RecyclerView.Adapter<AdapterRecyclerBotonesPorcentajes.Holder> {
    private LayoutInflater mInflater;
    Comunicador comunicador;


    public AdapterRecyclerBotonesPorcentajes(Context context) {
        mInflater = LayoutInflater.from(context);
        comunicador = (Comunicador) context;
        crearListas();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = mInflater.inflate(R.layout.recycler_view_botones, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    TreeMap<Integer, String> listaBotones;
    TreeMap<Integer, Boolean> listaBotonesChecador;
    TreeMap<Integer, Integer> listaMultiplicador;

    void crearListas() {

        String[] nombres = {"10%", "5%", "2.5%", "1%", "0.50%", "0.25%", "0.10%"};
        Integer[] multi = {1, 1, 1, 1, 2, 4, 10};
        listaBotones = new TreeMap<>();
        listaBotonesChecador = new TreeMap<>();
        listaMultiplicador = new TreeMap<>();
        for (int i = 0; i < 7; i++) {
            listaBotones.put(i, nombres[i]);
            listaBotonesChecador.put(i, false);
            listaMultiplicador.put(i, multi[i]);
        }


    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        if (listaBotonesChecador.get(position)) {
            holder.boton.setBackgroundResource(R.drawable.fondo_botones_presionado);
        } else
            holder.boton.setBackgroundResource(R.drawable.fondo_botones);


        holder.boton.setText(listaBotones.get(position));

    }

    @Override
    public int getItemCount() {
        return 7;
    }


    class Holder extends RecyclerView.ViewHolder {

        Button boton;

        Holder(final View itemView) {
            super(itemView);
            boton = itemView.findViewById(R.id.botonPorcentaje);
            boton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boton.setBackgroundResource(R.drawable.fondo_botones_presionado);
                    comunicador.cambioPorcentaje(boton.getText().toString(), listaMultiplicador.get(getAdapterPosition()));
                    for (int i = 0; i < 7; i++) {
                        listaBotonesChecador.put(i, false);
                    }
                    listaBotonesChecador.put(getAdapterPosition(), true);
                    notifyDataSetChanged();

                }
            });

        }
    }


}
