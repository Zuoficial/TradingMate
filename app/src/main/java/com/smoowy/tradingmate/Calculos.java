package com.smoowy.tradingmate;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class Calculos extends AppCompatActivity implements Comunicador {

    RecyclerView recyclerBotonesPorcentajes, recyclerPorcentajes;
    AdapterRecyclerBotonesPorcentajes adapterRecyclerBotonesPorcentajes;
    AdapterRecyclerPorcentajes adapterRecyclerPorcentajes;
    Button botonCazar, botonCorta, botonLarga, botonClear, botonPorcentajes,
            botonCerrar, botonPorcentajeCalculador,
            botonPorcentajeCalculadorMenos, botonPorcentajeCalculadorMas;
    TextView encabezado, textoGanancia, textoInvertido, textoInvertidoActual, textoUsado, textoGananciaLetra,
            textoPorcentaje, textoPrecio, textoBase, textoLiquidez;
    String monedaOrigenNombre, monedaDestinoNombre;
    EditText textoPrecioMod, textoPorcentajeMod;
    double invertido, precio, invertidoDestino, precioFinal,
            precioIngresado, porcentajeFinal, gananciaFinal, invertidoActual, porcentajeIngresado,
            liquidezOrigen, liquidezDestino, comisionEntrada, comisionSalida, invertidoFinal, liquidez,
            resultadoComisionEntrada, resultadoComisionSalida;
    int ajustadorPorcentajes, modo;
    final int modoCazar = 0, modoCorta = 1, modoLarga = 2;
    boolean botonPorcentajesAplanado,
            botonporcentajeCalculadorAplanado, botonPorcentajeCalculadorMasAplanado;
    Vibrator vibrator;
    String precisionOrigen, precisionDestino, precisionPrecio, precisionInversion, precisionLiquidez,
            precisionOrigenNumero, precisionDestinoNumero, precisionPrecioNumero, precisionInversionNumero, liquidezNombre;
    RelativeLayout calculador;
    DrawerLayout drawer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_calculos);
        drawer = findViewById(R.id.drawer_layout);
        encabezado = findViewById(R.id.encabezado);
        encabezado.setOnTouchListener(onTouchListener);
        calculador = findViewById(R.id.calculador);
        monedaOrigenNombre = getIntent().getExtras().getString("monedaOrigenNombre");
        monedaDestinoNombre = getIntent().getExtras().getString("monedaDestinoNombre");
        liquidezNombre = getIntent().getExtras().getString("liquidezNombre");
        invertido = getIntent().getExtras().getDouble("invertido");
        precio = getIntent().getExtras().getDouble("precio");
        comisionEntrada = (getIntent().getExtras().getDouble("comisionEntrada")) / 100;
        comisionSalida = (getIntent().getExtras().getDouble("comisionSalida")) / 100;
        modo = getIntent().getExtras().getInt("modo");
        botonPorcentajesAplanado = getIntent().getExtras().getBoolean("botonPorcentajesAplanado");
        botonporcentajeCalculadorAplanado = false;
        botonPorcentajeCalculadorMasAplanado = true;
        precisionOrigen = getIntent().getExtras().getString("precisionOrigen");
        precisionDestino = getIntent().getExtras().getString("precisionDestino");
        precisionPrecio = getIntent().getExtras().getString("precisionPrecio");
        precisionInversion = getIntent().getExtras().getString("precisionInversion");
        precisionOrigenNumero = getIntent().getExtras().getString("precisionOrigenNumero");
        precisionDestinoNumero = getIntent().getExtras().getString("precisionDestinoNumero");
        precisionPrecioNumero = getIntent().getExtras().getString("precisionPrecioNumero");
        precisionInversionNumero = getIntent().getExtras().getString("precisionInversionNumero");
        liquidezOrigen = getIntent().getExtras().getDouble("liquidezOrigen");
        liquidezDestino = getIntent().getExtras().getDouble("liquidezDestino");
        precisionLiquidez = "%.2f";
        invertidoFinal = invertido;
        invertidoDestino = (invertido / precio);
        setRecyclerViewRecyclerBotonesPorcentajes();
        setRecyclerViewRecyclerPorcentajes();
        botonCazar = findViewById(R.id.botonCazar);
        botonCorta = findViewById(R.id.botonCorta);
        botonLarga = findViewById(R.id.botonLarga);
        botonClear = findViewById(R.id.botonClear);
        botonPorcentajes = findViewById(R.id.botonPorcentajes);
        botonCerrar = findViewById(R.id.botonCerrar);
        botonPorcentajeCalculador = findViewById(R.id.botonPorcentajeCalculador);
        botonPorcentajeCalculadorMas = findViewById(R.id.botonPorcentajeCalculadorMas);
        botonPorcentajeCalculadorMenos = findViewById(R.id.botonPorcentajeCalculadorMenos);
        botonCazar.setOnTouchListener(onTouchListener);
        botonCorta.setOnTouchListener(onTouchListener);
        botonLarga.setOnTouchListener(onTouchListener);
        botonCerrar.setOnTouchListener(onTouchListener);
        botonPorcentajes.setOnTouchListener(onTouchListener);
        botonClear.setOnTouchListener(onTouchListener);
        botonPorcentajeCalculador.setOnTouchListener(onTouchListener);
        botonPorcentajeCalculadorMas.setOnTouchListener(onTouchListener);
        botonPorcentajeCalculadorMenos.setOnTouchListener(onTouchListener);
        textoPorcentaje = findViewById(R.id.textoPorcentaje);
        textoPorcentajeMod = findViewById(R.id.textoPorcentajeMod);
        textoPorcentajeMod.addTextChangedListener(textWatcher);
        textoGanancia = findViewById(R.id.textoGanancia);
        textoPrecio = findViewById(R.id.textoPrecio);
        textoPrecio.setOnTouchListener(onTouchListener);
        textoPrecioMod = findViewById(R.id.textoPrecioMod);
        textoPrecioMod.addTextChangedListener(textWatcher);
        textoInvertido = findViewById(R.id.textoInvertido);
        textoInvertido.setOnTouchListener(onTouchListener);
        textoInvertidoActual = findViewById(R.id.textoInvertidoActual);
        textoInvertidoActual.setOnTouchListener(onTouchListener);
        textoUsado = findViewById(R.id.textoUsado);
        textoUsado.setOnTouchListener(onTouchListener);
        textoGananciaLetra = findViewById(R.id.textoGanadoLetra);
        textoBase = findViewById(R.id.textoBase);
        textoBase.setText(String.format(precisionOrigen, precio) + " " + monedaOrigenNombre);
        textoBase.setOnTouchListener(onTouchListener);
        textoLiquidez = findViewById(R.id.textoLiquidez);
        textoLiquidez.setText("0.00 " + liquidezNombre);
        textoLiquidez.setOnTouchListener(onTouchListener);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (modo == modoCazar) {
            setBotonCazar();
        } else if (modo == modoCorta) {
            setBotonCorta();
        } else if (modo == modoLarga) {
            setBotonLarga();
        }


        setBotonPorcentajesAplanado();
        ajustadorPorcentajes = 1;
        ajustadorPosicion(ajustadorPorcentajes);
        textoPrecioMod.clearFocus();

    }


    boolean positivo;
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if (botonporcentajeCalculadorAplanado) {

                if (textoPorcentajeMod.getText().toString().equals(".") ||
                        textoPorcentajeMod.getText().toString().isEmpty()
                        ) {

                    if (modo == modoCorta || modo == modoLarga)
                        textoGanancia.setText("+0.00 " + monedaOrigenNombre);
                    else
                        textoGanancia.setText("+0.00 " + monedaDestinoNombre);
                    return;
                }

            } else {
                if (textoPrecioMod.getText().toString().equals(".") ||
                        textoPrecioMod.getText().toString().isEmpty()
                        ) {

                    textoPorcentaje.setText("+0.00%");
                    if (modo == modoCorta || modo == modoLarga)
                        textoGanancia.setText("+0.00 " + monedaOrigenNombre);
                    else
                        textoGanancia.setText("+0.00 " + monedaDestinoNombre);
                    return;
                }
            }

            if (botonporcentajeCalculadorAplanado)
                porcentajeIngresado = (Double.parseDouble(textoPorcentajeMod.getText().toString()));
            else
                precioIngresado = (Double.parseDouble(textoPrecioMod.getText().toString()));


            if (modo == modoCorta || modo == modoLarga) {

                if (modo == modoCorta)
                    calculoModoCorta();
                else
                    calculoModoLarga();

                textoLiquidez.setText(String.format(precisionLiquidez, liquidez) + " " + liquidezNombre);


                if (botonporcentajeCalculadorAplanado)
                    textoPrecio.setText(String.format(precisionOrigen, precioFinal));

                if (positivo) {
                    textoGanancia.setText("+" + String.format(precisionOrigen, gananciaFinal) + " " + monedaOrigenNombre);
                    textoPorcentaje.setText("+" + String.format("%.2f", porcentajeFinal) + "%");
                    textoGananciaLetra.setText("Ganado");
                } else {
                    textoGanancia.setText(String.format(precisionOrigen, gananciaFinal) + " " + monedaOrigenNombre);
                    textoPorcentaje.setText(String.format("%.2f", porcentajeFinal) + "%");
                    textoGananciaLetra.setText("Perdido");

                }

                textoInvertidoActual.setText(String.format(precisionOrigen, invertidoActual) + " " + monedaOrigenNombre);

            } else {

                calculoModoCazar();

                textoLiquidez.setText(String.format(precisionLiquidez, liquidez) + " " + liquidezNombre);


                if (botonporcentajeCalculadorAplanado) {
                    textoPrecio.setText(String.format(precisionOrigen, precioIngresado));

                }

                if (positivo) {
                    textoGanancia.setText("+" + String.format(precisionDestino, gananciaFinal) + " " + monedaDestinoNombre);
                    textoPorcentaje.setText("+" + String.format("%.2f", porcentajeFinal) + "%");
                    textoGananciaLetra.setText("Ganacia");

                } else {

                    textoGanancia.setText(String.format(precisionDestino, gananciaFinal) + " " + monedaDestinoNombre);
                    textoPorcentaje.setText(String.format("%.2f", porcentajeFinal) + "%");
                    textoGananciaLetra.setText("Perdido");
                }


                textoInvertidoActual.setText(String.format(precisionDestino, invertidoActual) + " " + monedaDestinoNombre);

            }

        }


        @Override
        public void afterTextChanged(Editable editable) {
        }
    };


    private void calculoModoCazar() {


        if (botonporcentajeCalculadorAplanado) {

            porcentajeFinal = porcentajeIngresado / 100;

            if (botonPorcentajeCalculadorMasAplanado) {

                precioIngresado = invertidoDestino * (1 + porcentajeFinal);
                precioIngresado = invertidoFinal / precioIngresado;
            } else {
                precioIngresado = invertidoDestino * (1 - porcentajeFinal);
                precioIngresado = invertidoFinal / precioIngresado;
                porcentajeFinal *= -1;
            }


            invertidoActual = invertidoFinal / precioIngresado;
            gananciaFinal = invertidoActual - invertidoDestino;


        } else

        {

            invertidoActual = invertidoFinal / precioIngresado;
            gananciaFinal = invertidoActual - invertidoDestino;
            porcentajeFinal = invertidoActual / invertidoDestino;
            porcentajeFinal -= 1;
            porcentajeFinal *= 100;
        }


        liquidez = invertidoActual * liquidezDestino;
        positivo = gananciaFinal > 0;


    }


    private void calculoModoCorta() {


        if (botonporcentajeCalculadorAplanado) {


            if (!botonPorcentajeCalculadorMasAplanado)
                porcentajeIngresado *= -1;

            porcentajeIngresado /= 100;


            double a;

            a = invertidoDestino * (1 + porcentajeIngresado);
            resultadoComisionSalida = a * comisionSalida;
            resultadoComisionEntrada = invertidoDestino * comisionEntrada;
            a += resultadoComisionSalida + resultadoComisionEntrada;

            precioFinal = invertido / a;
            invertidoActual = invertido * (1 + porcentajeIngresado);
            gananciaFinal = invertidoActual - invertidoFinal;


        } else {


            double a;

            a = invertido / precioIngresado;
            resultadoComisionEntrada = invertidoDestino * comisionEntrada;
            a -= resultadoComisionEntrada;
            a /= 1 + comisionSalida;
            resultadoComisionSalida = a * comisionSalida;


            porcentajeFinal = a / invertidoDestino;
            porcentajeFinal -= 1;
            invertidoActual = invertido * (1 + porcentajeFinal);
            gananciaFinal = invertidoActual - invertidoFinal;
            porcentajeFinal *= 100;
            positivo = gananciaFinal > 0;

        }
        liquidez = invertidoActual * liquidezOrigen;

    }


    private void calculoModoLarga() {


        if (botonporcentajeCalculadorAplanado) {


            if (!botonPorcentajeCalculadorMasAplanado)
                porcentajeIngresado *= -1;

            porcentajeIngresado /= 100;


            double a;

            a = invertido * (1 + porcentajeIngresado);
            a *= (1 + comisionSalida);
            a += (invertido * comisionEntrada);

            precioFinal = a / invertidoDestino;
            invertidoActual = invertido * (1 + porcentajeIngresado);
            gananciaFinal = invertidoActual - invertidoFinal;


        } else {


            double a = invertidoDestino;
            a *= precioIngresado;
            a -= invertido * comisionEntrada;
            a /= 1 + comisionSalida;

            porcentajeFinal = a / invertido;
            porcentajeFinal -= 1;
            invertidoActual = invertido * (1 + (porcentajeFinal));
            gananciaFinal = invertidoActual - invertidoFinal;
            porcentajeFinal *= 100;


            positivo = gananciaFinal > 0;


        }
        liquidez = invertidoActual * liquidezOrigen;

    }


    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                switch (view.getId()) {


                    case R.id.encabezado: {
                        drawer.openDrawer(Gravity.START);
                        break;
                    }


                    case R.id.botonCazar: {
                        drawer.closeDrawer(Gravity.START);
                        if (modo == modoCazar) {
                            ajustadorPosicion(ajustadorPorcentajes);
                            break;

                        } else {
                            setBotonCazar();
                            ajustadorPosicion(ajustadorPorcentajes);
                        }
                        break;

                    }

                    case R.id.botonCorta: {
                        drawer.closeDrawer(Gravity.START);
                        if (modo == modoCorta) {
                            ajustadorPosicion(ajustadorPorcentajes);
                            break;
                        } else {
                            setBotonCorta();
                            ajustadorPosicion(ajustadorPorcentajes);
                        }
                        break;

                    }

                    case R.id.botonLarga: {
                        drawer.closeDrawer(Gravity.START);
                        if (modo == modoLarga) {
                            ajustadorPosicion(ajustadorPorcentajes);
                            break;
                        } else {
                            setBotonLarga();
                            ajustadorPosicion(ajustadorPorcentajes);
                        }
                        break;

                    }


                    case R.id.botonPorcentajes: {
                        drawer.closeDrawer(Gravity.START);
                        setBotonPorcentajesAplanado();


                        break;
                    }

                    case R.id.botonClear: {
                        vibrator.vibrate(50);
                        limpiarCalculador();
                        break;
                    }

                    case R.id.textoInvertido: {
                        exportarPrecio(textoInvertido);
                        break;
                    }

                    case R.id.textoInvertidoActual: {
                        exportarPrecio(textoInvertidoActual);
                        break;
                    }

                    case R.id.textoUsado: {
                        exportarPrecio(textoUsado);
                        break;
                    }

                    case R.id.textoPrecio: {
                        exportarPrecio(textoPrecio);
                        break;
                    }

                    case R.id.textoBase: {
                        exportarPrecio(textoBase);
                        break;
                    }

                    case R.id.textoLiquidez: {
                        exportarPrecio(textoLiquidez);
                        break;
                    }

                    case R.id.botonCerrar: {

                        Intent data = new Intent();
                        data.putExtra("salir", true);

                        setResult(1, data);
                        vibrator.vibrate(500);
                        finish();

                        break;
                    }

                    case R.id.botonPorcentajeCalculador: {
                        vibrator.vibrate(50);

                        if (!botonporcentajeCalculadorAplanado) {

                            botonporcentajeCalculadorAplanado = true;
                            botonPorcentajeCalculador.setBackgroundResource(R.drawable.fondo_botones_presionado);
                            textoPorcentaje.setVisibility(View.GONE);
                            textoPorcentajeMod.setVisibility(View.VISIBLE);
                            textoPorcentajeMod.setText("");
                            textoPrecio.setText("Precio");
                            textoPrecio.setVisibility(View.VISIBLE);
                            textoPrecioMod.setVisibility(View.GONE);
                            botonPorcentajeCalculadorMas.setVisibility(View.VISIBLE);
                            botonPorcentajeCalculadorMenos.setVisibility(View.VISIBLE);
                            if (modo == modoCorta || modo == modoLarga)
                                textoInvertidoActual.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);

                            else
                                textoInvertidoActual.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);


                        } else {
                            botonporcentajeCalculadorAplanado = false;
                            botonPorcentajeCalculador.setBackgroundResource(R.drawable.fondo_botones);
                            textoPorcentaje.setVisibility(View.VISIBLE);
                            textoPorcentajeMod.setVisibility(View.GONE);
                            textoPrecio.setVisibility(View.GONE);
                            textoPrecioMod.setVisibility(View.VISIBLE);
                            textoPrecioMod.setText("");
                            botonPorcentajeCalculadorMas.setVisibility(View.GONE);
                            botonPorcentajeCalculadorMenos.setVisibility(View.GONE);

                            if (modo == modoCorta || modo == modoLarga)
                                textoInvertidoActual.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);

                            else
                                textoInvertidoActual.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);

                        }
                        textoLiquidez.setText("0.00 " + liquidezNombre);

                        break;
                    }

                    case R.id.botonPorcentajeCalculadorMas: {
                        vibrator.vibrate(50);

                        if (!botonPorcentajeCalculadorMasAplanado) {
                            botonPorcentajeCalculadorMasAplanado = true;
                            botonPorcentajeCalculadorMas.setBackgroundResource(R.drawable.fondo_botones_presionado);
                            botonPorcentajeCalculadorMenos.setBackgroundResource(R.drawable.fondo_botones);
                            textoPorcentajeMod.setText("");
                            textoPrecio.setText("Precio");
                            textoLiquidez.setText("0.00 " + liquidezNombre);
                            if (modo == modoCorta || modo == modoLarga)

                                textoInvertidoActual.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
                            else
                                textoInvertidoActual.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);
                            positivo = true;
                        }

                        break;
                    }
                    case R.id.botonPorcentajeCalculadorMenos: {

                        vibrator.vibrate(50);

                        if (botonPorcentajeCalculadorMasAplanado) {
                            botonPorcentajeCalculadorMasAplanado = false;
                            botonPorcentajeCalculadorMas.setBackgroundResource(R.drawable.fondo_botones);
                            botonPorcentajeCalculadorMenos.setBackgroundResource(R.drawable.fondo_botones_presionado);
                            textoPorcentajeMod.setText("");
                            textoPrecio.setText("Precio");
                            textoLiquidez.setText("0.00 " + liquidezNombre);
                            if (modo == modoCorta || modo == modoLarga)

                                textoInvertidoActual.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
                            else
                                textoInvertidoActual.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);
                            positivo = false;
                        }

                        break;
                    }

                }

            }
            return true;
        }
    };

    private void exportarPrecio(TextView text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Precio", text.getText().toString());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), "Precio grabado: " + text.getText().toString(), Toast.LENGTH_SHORT).show();
        vibrator.vibrate(500);
    }

    private void limpiarCalculador() {


        textoPrecioMod.setText("");
        textoPorcentajeMod.setText("");
        textoPorcentaje.setText("+0.00%");
        textoPrecio.setText("Precio");
        textoLiquidez.setText("0.00 " + liquidezNombre);

        if (modo == modoCorta || modo == modoLarga) {

            textoGanancia.setText("+0.00 " + monedaOrigenNombre);
            textoInvertido.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
            textoInvertidoActual.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);

        } else {

            textoGanancia.setText("+0.00 " + monedaDestinoNombre);
            textoInvertido.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);
            textoInvertidoActual.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);

        }
    }


    private void setBotonCazar() {
        botonCazar.setBackgroundResource(R.drawable.fondo_botones_superior_presionado);
        botonCorta.setBackgroundResource(R.drawable.fondo_botones_superior);
        botonLarga.setBackgroundResource(R.drawable.fondo_botones_superior);

        modo = modoCazar;
        adapterRecyclerPorcentajes.cambioModo(modo);
        encabezado.setText("cazar " + monedaDestinoNombre + " con " + monedaOrigenNombre);


        if (modo == modoCorta || modo == modoLarga) {

            textoGanancia.setText("+0.00 " + monedaOrigenNombre);
            textoInvertido.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
            textoInvertidoActual.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
            textoUsado.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);


        } else {

            textoGanancia.setText("+0.00 " + monedaDestinoNombre);
            textoInvertido.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);
            textoInvertidoActual.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);
            textoUsado.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);

        }
        textoPrecioMod.setText("");
        textoPrecioMod.clearFocus();
        textoPorcentaje.setText("+0.00%");
        textoLiquidez.setText("0.00 " + liquidezNombre);
        ajustadorPosicion(ajustadorPorcentajes);
        vibrator.vibrate(50);
    }

    private void setBotonCorta() {
        botonCazar.setBackgroundResource(R.drawable.fondo_botones_superior);
        botonCorta.setBackgroundResource(R.drawable.fondo_botones_superior_presionado);
        botonLarga.setBackgroundResource(R.drawable.fondo_botones_superior);
        modo = modoCorta;
        adapterRecyclerPorcentajes.cambioModo(modo);
        encabezado.setText("corta " + monedaDestinoNombre + " con " + monedaOrigenNombre);
        if (modo == modoCorta || modo == modoLarga) {

            textoGanancia.setText("+0.00 " + monedaOrigenNombre);
            textoInvertido.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
            textoInvertidoActual.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
            textoUsado.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);

        } else {

            textoGanancia.setText("+0.00 " + monedaDestinoNombre);
            textoInvertido.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);
            textoInvertidoActual.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);
            textoUsado.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);

        }

        textoPrecioMod.setText("");
        textoPrecioMod.clearFocus();
        textoPorcentaje.setText("+0.00%");
        textoLiquidez.setText("0.00 " + liquidezNombre);
        ajustadorPosicion(ajustadorPorcentajes);
        vibrator.vibrate(50);
    }


    private void setBotonLarga() {
        botonCazar.setBackgroundResource(R.drawable.fondo_botones_superior);
        botonCorta.setBackgroundResource(R.drawable.fondo_botones_superior);
        botonLarga.setBackgroundResource(R.drawable.fondo_botones_superior_presionado);
        modo = modoLarga;
        adapterRecyclerPorcentajes.cambioModo(modo);
        encabezado.setText("larga " + monedaDestinoNombre + " con " + monedaOrigenNombre);
        if (modo == modoCorta || modo == modoLarga) {

            textoGanancia.setText("+0.00 " + monedaOrigenNombre);
            textoInvertido.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
            textoInvertidoActual.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
            textoUsado.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);

        } else {

            textoGanancia.setText("+0.00 " + monedaDestinoNombre);
            textoInvertido.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);
            textoInvertidoActual.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);
            textoUsado.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);

        }

        textoPrecioMod.setText("");
        textoPrecioMod.clearFocus();
        textoPorcentaje.setText("+0.00%");
        textoLiquidez.setText("0.00 " + liquidezNombre);
        ajustadorPosicion(ajustadorPorcentajes);
        vibrator.vibrate(50);
    }

    private void setBotonPorcentajesAplanado() {
        if (botonPorcentajesAplanado) {
            TransitionManager.beginDelayedTransition(calculador);
            recyclerPorcentajes.setVisibility(View.VISIBLE);
            recyclerBotonesPorcentajes.setVisibility(View.VISIBLE);
            botonPorcentajesAplanado = false;
            botonPorcentajes.setBackgroundResource(R.drawable.fondo_botones_superior_presionado);

        } else {
            TransitionManager.beginDelayedTransition(calculador);
            recyclerPorcentajes.setVisibility(View.GONE);
            recyclerBotonesPorcentajes.setVisibility(View.GONE);
            botonPorcentajesAplanado = true;
            botonPorcentajes.setBackgroundResource(R.drawable.fondo_botones_superior);
        }
        vibrator.vibrate(50);
    }


    LinearLayoutManager layoutManagerPorcentajes;

    private void setRecyclerViewRecyclerPorcentajes() {
        recyclerPorcentajes = findViewById(R.id.recyclerCambioDePorcentajes);

        Bundle bundle = new Bundle();
        bundle.putString("monedaOrigenNombre", monedaOrigenNombre);
        bundle.putString("monedaDestinoNombre", monedaDestinoNombre);
        bundle.putDouble("invertido", invertido);
        bundle.putDouble("precio", precio);
        bundle.putDouble("comisionEntrada", comisionEntrada);
        bundle.putDouble("comisionSalida", comisionSalida);
        bundle.putDouble("liquidezOrigen", liquidezOrigen);
        bundle.putDouble("liquidezDestino", liquidezDestino);
        bundle.putInt("modo", modo);
        bundle.putString("precisionOrigen", precisionOrigen);
        bundle.putString("precisionDestino", precisionDestino);
        bundle.putString("precisionPrecio", precisionPrecio);
        bundle.putString("precisionInversion", precisionInversion);
        bundle.putString("liquidezNombre", liquidezNombre);

        adapterRecyclerPorcentajes = new AdapterRecyclerPorcentajes(this, bundle);
        recyclerPorcentajes.setAdapter(adapterRecyclerPorcentajes);
        layoutManagerPorcentajes = new LinearLayoutManager(this);
        recyclerPorcentajes.setLayoutManager(layoutManagerPorcentajes);
        layoutManagerPorcentajes.scrollToPosition(97);
    }

    LinearLayoutManager layoutManagerBotonesPorcentajes;

    private void setRecyclerViewRecyclerBotonesPorcentajes() {
        recyclerBotonesPorcentajes = findViewById(R.id.recyclerBotonesPorcentaje);
        adapterRecyclerBotonesPorcentajes = new AdapterRecyclerBotonesPorcentajes(this);
        layoutManagerBotonesPorcentajes = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerBotonesPorcentajes.setAdapter(adapterRecyclerBotonesPorcentajes);
        recyclerBotonesPorcentajes.setLayoutManager(layoutManagerBotonesPorcentajes);
        layoutManagerBotonesPorcentajes.scrollToPosition(2);
    }


    @Override
    public void cambioPorcentaje(String porcentaje, Integer ajustadorPorcentajes) {
        adapterRecyclerPorcentajes.cambioPorcentaje(porcentaje, ajustadorPorcentajes);

        this.ajustadorPorcentajes = ajustadorPorcentajes;

        ajustadorPosicion(ajustadorPorcentajes);
        vibrator.vibrate(50);

    }

    private void ajustadorPosicion(Integer ajustadorPorcentajes) {
        switch (ajustadorPorcentajes) {
            case 1: {
                layoutManagerPorcentajes.scrollToPositionWithOffset(99, 99);
                break;
            }
            case 2: {
                layoutManagerPorcentajes.scrollToPositionWithOffset(199, 200);
                break;
            }
            case 4: {
                layoutManagerPorcentajes.scrollToPositionWithOffset(399, 399);
                break;
            }
            case 10: {
                layoutManagerPorcentajes.scrollToPositionWithOffset(999, 999);
                break;
            }
        }
    }
}
