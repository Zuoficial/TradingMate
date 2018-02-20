package com.smoowy.tradingmate;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

public class AdapterRecyclerPorcentajes extends
        RecyclerView.Adapter<AdapterRecyclerPorcentajes.Holder> {

    private LayoutInflater mInflater;

    ArrayList<Double> tablaPorcentajes,
            tablaPorcentajesInvertida;
    TreeMap<Integer, String> tablaPrecioFinal,
            tablaPorcentajeFinal, tablaGananciasFinal, tablaActualFinal, tablaLiquidezFinal;
    TreeMap<Integer, Integer> tablaColoresFinal;
    int iPositivo, ajustadorPorcentajes, ajustador;
    double invertido, invertidoFinal, ganancia, invertidoDestino,
            precio, precioFinal, comision, porcentajeMostrar, invertidoActual,
            porcentaje, referenciaLiquidezOrigen, referenciaLiquidezDestino, liquidez,
            comisionEntrada, comisionSalida;
    boolean modoComprar;
    String monedaOrigenNombre, monedaDestinoNombre;
    Context context;
    String precisionOrigen, precisionDestino, precisionPrecio, precisionInversion;


    public AdapterRecyclerPorcentajes(Context context, Bundle bundle) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        comisionEntrada = .04;
        comisionSalida = .04;
        invertido = bundle.getDouble("invertido");
        invertidoFinal = invertido * (1 - comisionEntrada);
        precio = bundle.getDouble("precio");
        comision = bundle.getDouble("comision");
        invertidoDestino = (invertido / precio) * (1 - comisionEntrada);
        referenciaLiquidezDestino = 2;
        referenciaLiquidezOrigen = 1;
        monedaOrigenNombre = bundle.getString("monedaOrigenNombre");
        monedaDestinoNombre = bundle.getString("monedaDestinoNombre");
        precisionOrigen = bundle.getString("precisionOrigen");
        precisionDestino = bundle.getString("precisionDestino");
        precisionPrecio = bundle.getString("precisionPrecio");
        precisionInversion = bundle.getString("precisionInversion");
        porcentaje = .01;
        modoComprar = bundle.getBoolean("modoComprar");
        hacerListas(modoComprar, porcentaje);
        ajustadorPorcentajes = 1;

    }

    public void cambioPorcentaje(String porcentaje, Integer multiplicador) {

        this.porcentaje = Double.parseDouble(porcentaje.substring(0, porcentaje.length() - 1));
        this.porcentaje /= 100;
        this.ajustadorPorcentajes = multiplicador;
        hacerListas(modoComprar, this.porcentaje);
        notifyDataSetChanged();
    }

    public void cambioMoneda(boolean monedaOrigen) {

        hacerListas(monedaOrigen, porcentaje);
        notifyDataSetChanged();
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = mInflater.inflate(R.layout.recycler_view_porcentaje, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(Holder holder, int position) {

        holder.fondo.setBackgroundResource(tablaColoresFinal.get(position));
        holder.textoGanancia.setText(tablaGananciasFinal.get(position));
        holder.textoPorcentaje.setText(tablaPorcentajeFinal.get(position));
        holder.textoPrecio.setText(tablaPrecioFinal.get(position));
        holder.textoActual.setText(tablaActualFinal.get(position));
        holder.textoLiquidez.setText(tablaLiquidezFinal.get(position));


        if (tablaGananciasFinal.get(position).contains("+"))
            holder.textoGanadoLetra.setText("Ganado");
        else
            holder.textoGanadoLetra.setText("Perdido");


        if (modoComprar) {
            holder.textoInvertido.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
            holder.textoUsando.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);
        } else {
            holder.textoInvertido.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);
            holder.textoUsando.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
        }
    }

    @Override
    public int getItemCount() {
        return 199 * ajustadorPorcentajes;
    }


    void hacerListas(boolean monedaOrigen, double porcentaje) {

        tablaPorcentajes = new ArrayList<>();
        tablaPrecioFinal = new TreeMap<>();
        tablaPorcentajeFinal = new TreeMap<>();
        tablaGananciasFinal = new TreeMap<>();
        tablaColoresFinal = new TreeMap<>();
        tablaActualFinal = new TreeMap<>();
        tablaLiquidezFinal = new TreeMap<>();
        this.modoComprar = monedaOrigen;

        for (int i = 0; i < 100 * ajustadorPorcentajes; i++) {
            tablaPorcentajes.add(porcentaje * i);
        }

        tablaPorcentajesInvertida = new ArrayList<>(tablaPorcentajes);
        Collections.sort(tablaPorcentajesInvertida, Collections.<Double>reverseOrder());


        for (int i = 0; i < 199 * ajustadorPorcentajes; i++) {

            if (i < 100 * ajustadorPorcentajes) {

                tablaColoresFinal.put(i, R.drawable.fondo_marcador_positivo);
                porcentajeMostrar = tablaPorcentajesInvertida.get(i) * 100;
                precioFinal = positivo(precio, tablaPorcentajesInvertida.get(i));
                tablaPrecioFinal.put(i, String.format(precisionPrecio, precioFinal) + " " + monedaOrigenNombre);

                if (this.modoComprar) {

                    ganancia = invertido * tablaPorcentajesInvertida.get(i);
                    tablaGananciasFinal.put(i, "+" + String.format(precisionOrigen, ganancia) + " " + monedaOrigenNombre);
                    invertidoActual = invertido * (1 + tablaPorcentajesInvertida.get(i));
                    tablaActualFinal.put(i, String.format(precisionOrigen, invertidoActual) + " " + monedaOrigenNombre);
                    liquidez = invertidoActual * referenciaLiquidezOrigen;
                    tablaLiquidezFinal.put(i, String.format("%.2f", liquidez) + " USD");
                    tablaPorcentajeFinal.put(i, "+" + String.format("%.2f", porcentajeMostrar) + "%");

                } else {

                    ganancia = (invertidoFinal / precioFinal) * tablaPorcentajesInvertida.get(i);
                    tablaGananciasFinal.put(i, "+" + String.format(precisionDestino, ganancia) + " " + monedaDestinoNombre);
                    invertidoActual = invertidoFinal / precioFinal;
                    tablaActualFinal.put(i, String.format(precisionDestino, invertidoActual) + " " + monedaDestinoNombre);
                    liquidez = invertidoActual * referenciaLiquidezDestino;
                    tablaLiquidezFinal.put(i, String.format("%.2f", liquidez) + " USD");
                    tablaPorcentajeFinal.put(i, "+" + String.format("%.2f", porcentajeMostrar) + "%");
                }


            } else {

                tablaColoresFinal.put(i, R.drawable.fondo_marcador_negativo);

                iPositivo = i - 99 * ajustadorPorcentajes;

                ajustador = 0;

                if (ajustadorPorcentajes == 2)
                    ajustador = 1;
                else if (ajustadorPorcentajes == 4)
                    ajustador = 3;
                else if (ajustadorPorcentajes == 10)
                    ajustador = 9;

                iPositivo -= ajustador;


                porcentajeMostrar = tablaPorcentajes.get(iPositivo) * 100;
                porcentajeMostrar *= -1;
                precioFinal = negativo(
                        precio, tablaPorcentajes.get(iPositivo));
                tablaPrecioFinal.put(i, String.format(precisionPrecio, precioFinal) + " " + monedaOrigenNombre);

                if (this.modoComprar) {

                    ganancia = invertido * tablaPorcentajes.get(iPositivo);
                    ganancia *= -1;
                    tablaGananciasFinal.put(i, String.format(precisionOrigen, ganancia) + " " + monedaOrigenNombre);
                    invertidoActual = invertido * (1-tablaPorcentajes.get(iPositivo));
                    tablaActualFinal.put(i, String.format(precisionOrigen, invertidoActual) + " " + monedaOrigenNombre);
                    liquidez = invertidoActual * referenciaLiquidezOrigen;
                    tablaLiquidezFinal.put(i, String.format("%.2f", liquidez) + " USD");

                } else {
                    ganancia = (invertidoFinal / precioFinal) * tablaPorcentajes.get(iPositivo);
                    ganancia *= -1;
                    tablaGananciasFinal.put(i, String.format(precisionDestino, ganancia) + " " + monedaDestinoNombre);
                    invertidoActual = invertidoFinal / precioFinal;
                    tablaActualFinal.put(i, String.format(precisionDestino, invertidoActual) + " " + monedaDestinoNombre);
                    liquidez = invertidoActual * referenciaLiquidezDestino;
                    tablaLiquidezFinal.put(i, String.format("%.2f", liquidez) + " USD");
                }


                tablaPorcentajeFinal.put(i, String.format("%.2f", porcentajeMostrar) + "%");


            }
        }

        tablaColoresFinal.put((99 * ajustadorPorcentajes) + ajustador, R.drawable.fondo_marcador_neutral);

    }

    double a, b;

    double positivo(double precio, double porcentaje) {

        // Incluye el pago de la comision


        if (modoComprar) {

            a = invertidoFinal;
            a /= precio;

            b = invertido * (1 + porcentaje);
            b *= (1 + comisionSalida);
            b += (invertido * comisionEntrada);

            precio = b / a;

        } else {

            precio *= 1 - porcentaje;
        }


        return precio;
    }

    double negativo(double precio, double porcentaje) {

        //Incluye el pago de la comision

        if (modoComprar) {

            a = invertidoFinal;
            a /= precio;

            b = invertido * (1 - porcentaje);
            b *= (1 + comisionSalida);
            b += (invertido * comisionEntrada);

            precio = b / a;

        } else {
            {
                precio *= 1 + porcentaje;
            }
        }


        return precio;
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView textoPorcentaje, textoGanancia, textoPrecio, textoInvertido, textoInvertidoLetra,
                textoActual, textoUsando, textoGanadoLetra, textoLiquidez, textoLiquidezLetra,
                textoBase;
        ImageView fondo;


        Holder(View itemView) {
            super(itemView);
            textoPorcentaje = itemView.findViewById(R.id.textoPorcentaje);
            textoGanancia = itemView.findViewById(R.id.textoGanancia);
            textoPrecio = itemView.findViewById(R.id.textoPrecioMod);
            textoInvertido = itemView.findViewById(R.id.textoInvertidoRV);
            textoInvertidoLetra = itemView.findViewById(R.id.textoInvertidoLetra);
            textoActual = itemView.findViewById(R.id.textoActualRV);
            textoUsando = itemView.findViewById(R.id.textoUsandoRV);
            textoGanadoLetra = itemView.findViewById(R.id.textoGanadoLetraRV);
            textoLiquidez = itemView.findViewById(R.id.textoLiquidez);
            textoLiquidezLetra = itemView.findViewById(R.id.textoLiquidezLetra);
            textoBase = itemView.findViewById(R.id.textoBase);
            textoPrecio.setOnClickListener(onClickListener);
            textoInvertido.setOnClickListener(onClickListener);
            textoActual.setOnClickListener(onClickListener);
            textoUsando.setOnClickListener(onClickListener);
            textoLiquidez.setOnClickListener(onClickListener);
            textoBase.setOnClickListener(onClickListener);
            textoBase.setText(String.format(precisionPrecio, precio) + " " + monedaOrigenNombre);
            fondo = itemView.findViewById(R.id.fondo);
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        }


        Vibrator vibrator;
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(500);
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);


                switch (view.getId()) {

                    case R.id.textoPrecioMod: {
                        ClipData clip = ClipData.newPlainText("Precio", textoPrecio.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(context, "Precio grabado: " + textoPrecio.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.textoActualRV: {
                        ClipData clip = ClipData.newPlainText("Precio", textoActual.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(context, "Precio grabado: " + textoActual.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.textoInvertidoRV: {
                        ClipData clip = ClipData.newPlainText("Precio", textoInvertido.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(context, "Precio grabado: " + textoInvertido.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.textoUsandoRV: {
                        ClipData clip = ClipData.newPlainText("Precio", textoUsando.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(context, "Precio grabado: " + textoUsando.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;
                    }

                    case R.id.textoLiquidez: {
                        ClipData clip = ClipData.newPlainText("Precio", textoLiquidez.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(context, "Precio grabado: " + textoLiquidez.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;
                    }

                    case R.id.textoBase: {
                        ClipData clip = ClipData.newPlainText("Precio", textoBase.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(context, "Precio grabado: " + textoBase.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;
                    }
                }


            }
        };

    }


}
