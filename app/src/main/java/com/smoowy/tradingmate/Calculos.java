package com.smoowy.tradingmate;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Calculos extends AppCompatActivity implements Comunicador {

    RecyclerView recyclerBotonesPorcentajes, recyclerPorcentajes;
    AdapterRecyclerBotonesPorcentajes adapterRecyclerBotonesPorcentajes;
    AdapterRecyclerPorcentajes adapterRecyclerPorcentajes;
    Button botonComprar, botonCazar, botonClear, botonPorcentajes,
            botonCerrar, botonPorcentajeCalculador,
            botonPorcentajeCalculadorMenos, botonPorcentajeCalculadorMas;
    TextView encabezado, textoGanancia, textoInvertido, textoInvertidoActual, textoUsado, textoGananciaLetra,
            textoPorcentaje, textoPrecio, textoBase, textoLiquidez;
    String monedaOrigenNombre, monedaDestinoNombre, monedaLiquidezNombre;
    EditText textoPrecioMod, textoPorcentajeMod;
    double invertido, precio, comision, invertidoDestino, precioFinal,
            precioIngresado, porcentajeFinal, gananciaFinal, invertidoActual, porcentajeIngresado,
            liquidez, referenciaLiquidezOrigen, referenciaLiquidezDestino,comisionEntrada,comisionSalida,invertidoFinal;
    int ajustadorPorcentajes;
    boolean modoComprar, botonPorcentajesAplanado,
            botonModoComprar, botonporcentajeCalculadorAplanado, botonPorcentajeCalculadorMasAplanado;
    Vibrator vibrator;
    String precisionOrigen, precisionDestino, precisionPrecio, precisionInversion, precisionLiquidez,
            precisionOrigenNumero, precisionDestinoNumero, precisionPrecioNumero, precisionInversionNumero;
    RelativeLayout calculador;
    boolean hayComisionEntrada,hayComisionSalida;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculos);
        encabezado = findViewById(R.id.encabezado);
        calculador = findViewById(R.id.calculador);
        monedaOrigenNombre = getIntent().getExtras().getString("monedaOrigenNombre");
        monedaDestinoNombre = getIntent().getExtras().getString("monedaDestinoNombre");
        monedaLiquidezNombre = "USD";
        invertido = getIntent().getExtras().getDouble("invertido");
        precio = getIntent().getExtras().getDouble("precio");
        comision = (getIntent().getExtras().getDouble("comision")) / 100;
        comisionEntrada = comision;
        comisionSalida = comision;
        hayComisionEntrada = true;
        hayComisionSalida = true;
        modoComprar = getIntent().getExtras().getBoolean("modoComprar");
        botonModoComprar = modoComprar;
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
        referenciaLiquidezOrigen = 1;
        referenciaLiquidezDestino = 10455;
        precisionLiquidez = "%.2f";

        invertidoFinal = invertido * (1 - comisionEntrada);
        invertidoDestino = (invertido / precio) * (1 - comisionEntrada);
        setRecyclerViewRecyclerBotonesPorcentajes();
        setRecyclerViewRecyclerPorcentajes();
        botonComprar = findViewById(R.id.botonComprar);
        botonCazar = findViewById(R.id.botonCazar);
        botonClear = findViewById(R.id.botonClear);
        botonPorcentajes = findViewById(R.id.botonPorcentajes);
        botonCerrar = findViewById(R.id.botonCerrar);
        botonPorcentajeCalculador = findViewById(R.id.botonPorcentajeCalculador);
        botonPorcentajeCalculadorMas = findViewById(R.id.botonPorcentajeCalculadorMas);
        botonPorcentajeCalculadorMenos = findViewById(R.id.botonPorcentajeCalculadorMenos);
        botonComprar.setOnTouchListener(onTouchListener);
        botonCazar.setOnTouchListener(onTouchListener);
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
        textoLiquidez.setText("0.00 " + monedaLiquidezNombre);
        textoLiquidez.setOnTouchListener(onTouchListener);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (botonModoComprar)
            setBotonComprar();
        else
            setBotonCazar();

        setBotonPorcentajesAplanado();
        ajustadorPosicion(1);
        textoPrecioMod.clearFocus();

    }

    @Override
    protected void onDestroy() {
        SharedPreferences sharedPreferences = getSharedPreferences("TradingMatePreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("monedaOrigenNombre", monedaOrigenNombre);
        editor.putString("monedaDestinoNombre", monedaDestinoNombre);
        editor.putString("invertido", String.valueOf(invertido));
        editor.putString("precio", String.valueOf(precio));
        editor.putString("comision", String.valueOf((comision * 100)));
        editor.putString("precisionOrigenNumero", precisionOrigenNumero);
        editor.putString("precisionDestinoNumero", precisionDestinoNumero);
        editor.putString("precisionPrecioNumero", precisionPrecioNumero);
        editor.putString("precisionInversionNumero", precisionInversionNumero);


        editor.apply();

        super.onDestroy();

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

                    if (modoComprar)
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
                    if (modoComprar)
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


            if (modoComprar) {

                calculoModoComprar();

                textoLiquidez.setText(String.format(precisionLiquidez, liquidez) + " " + monedaLiquidezNombre);


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

                textoLiquidez.setText(String.format(precisionLiquidez, liquidez) + " " + monedaLiquidezNombre);


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


    private void calculoModoComprar() {


        if (botonporcentajeCalculadorAplanado) {


            if (!botonPorcentajeCalculadorMasAplanado)
                porcentajeIngresado *= -1;

            porcentajeIngresado /= 100;


            double a, b;

            a = invertido * (1 + porcentajeIngresado);
            a *= (1 + comisionSalida);
            a += (invertido * comisionEntrada);

            b = invertidoFinal;
            b /= precio;



            precioFinal = a / b;
            gananciaFinal = invertido * porcentajeIngresado;
            invertidoActual = invertido * (1 + porcentajeIngresado);
            porcentajeIngresado *= 100;


        } else {

            double a = invertidoDestino;
            a *= precioIngresado;
            a -= invertido * comisionSalida;
            a /= 1 + comisionEntrada;

            gananciaFinal = a - invertido;
            porcentajeFinal = a / invertido;
            porcentajeFinal -= 1;
            invertidoActual = invertido * (1 + (porcentajeFinal));
            porcentajeFinal *= 100;


            positivo = gananciaFinal > 0;



        }
        liquidez = invertidoActual * referenciaLiquidezOrigen;

    }

    private void calculoModoCazar() {


        if (botonporcentajeCalculadorAplanado) {

            porcentajeFinal = porcentajeIngresado / 100;

            if (botonPorcentajeCalculadorMasAplanado)
                precioIngresado = precio * (1 + (porcentajeFinal * -1));
            else {
                precioIngresado = precio * (1 + porcentajeFinal);
                porcentajeFinal *= -1;
            }

            gananciaFinal = (invertidoFinal / precioIngresado) * porcentajeFinal;

            invertidoActual = (invertidoFinal / precioIngresado);

        } else {
            porcentajeFinal = precioIngresado / precio;
            porcentajeFinal -= 1;
            porcentajeFinal *= -1;
            gananciaFinal = (invertidoFinal / precioIngresado) * porcentajeFinal;
            invertidoActual = (invertidoFinal / precioIngresado);
            porcentajeFinal *= 100;
        }

        liquidez = invertidoActual * referenciaLiquidezDestino;
        positivo = gananciaFinal > 0;


    }


    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                switch (view.getId()) {

                    case R.id.botonComprar: {
                        if (botonModoComprar)
                            break;
                        else {

                            setBotonComprar();
                            if (botonPorcentajesAplanado) {
                                ajustadorPosicion(ajustadorPorcentajes);
                            }

                            break;
                        }

                    }
                    case R.id.botonCazar: {
                        if (!botonModoComprar)
                            break;
                        else {
                            setBotonCazar();

                            if (botonPorcentajesAplanado) {
                                ajustadorPosicion(ajustadorPorcentajes);
                            }
                        }

                        break;

                    }

                    case R.id.botonPorcentajes: {

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
                            if (modoComprar)
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

                            if (modoComprar)
                                textoInvertidoActual.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);

                            else
                                textoInvertidoActual.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);

                        }
                        textoLiquidez.setText("0.00 " + monedaLiquidezNombre);

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
                            textoLiquidez.setText("0.00 " + monedaLiquidezNombre);
                            if (modoComprar)

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
                            textoLiquidez.setText("0.00 " + monedaLiquidezNombre);
                            if (modoComprar)

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
        textoLiquidez.setText("0.00 " + monedaLiquidezNombre);

        if (modoComprar) {

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
        botonComprar.setBackgroundResource(R.drawable.fondo_botones);
        botonCazar.setBackgroundResource(R.drawable.fondo_botones_presionado);
        botonModoComprar = false;

        modoComprar = false;
        adapterRecyclerPorcentajes.cambioMoneda(modoComprar);
        encabezado.setText("cazar " + monedaDestinoNombre + " con " + monedaOrigenNombre);
        if (modoComprar) {

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
        textoLiquidez.setText("0.00 " + monedaLiquidezNombre);
        ajustadorPosicion(ajustadorPorcentajes);
        vibrator.vibrate(50);
    }

    private void setBotonComprar() {
        botonComprar.setBackgroundResource(R.drawable.fondo_botones_presionado);
        botonCazar.setBackgroundResource(R.drawable.fondo_botones);
        botonModoComprar = true;
        modoComprar = true;
        adapterRecyclerPorcentajes.cambioMoneda(modoComprar);
        encabezado.setText(monedaDestinoNombre + " comprado con " + monedaOrigenNombre);
        if (modoComprar) {

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
        textoLiquidez.setText("0.00 " + monedaLiquidezNombre);
        ajustadorPosicion(ajustadorPorcentajes);
        vibrator.vibrate(50);
    }

    private void setBotonPorcentajesAplanado() {
        if (botonPorcentajesAplanado) {
            TransitionManager.beginDelayedTransition(calculador);
            recyclerPorcentajes.setVisibility(View.VISIBLE);
            recyclerBotonesPorcentajes.setVisibility(View.VISIBLE);
            botonPorcentajesAplanado = false;
            botonPorcentajes.setBackgroundResource(R.drawable.fondo_botones_presionado);

        } else {
            TransitionManager.beginDelayedTransition(calculador);
            recyclerPorcentajes.setVisibility(View.GONE);
            recyclerBotonesPorcentajes.setVisibility(View.GONE);
            botonPorcentajesAplanado = true;
            botonPorcentajes.setBackgroundResource(R.drawable.fondo_botones);
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
        bundle.putDouble("comision", comision);
        bundle.putBoolean("modoComprar", modoComprar);
        bundle.putString("precisionOrigen", precisionOrigen);
        bundle.putString("precisionDestino", precisionDestino);
        bundle.putString("precisionPrecio", precisionPrecio);
        bundle.putString("precisionInversion", precisionInversion);

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
                layoutManagerPorcentajes.scrollToPositionWithOffset(99, 100);
                break;
            }
            case 2: {
                layoutManagerPorcentajes.scrollToPositionWithOffset(199, 200);
                break;
            }
            case 4: {
                layoutManagerPorcentajes.scrollToPositionWithOffset(399, 400);
                break;
            }
            case 10: {
                layoutManagerPorcentajes.scrollToPositionWithOffset(1000, 1000);
                break;
            }
        }
    }
}
