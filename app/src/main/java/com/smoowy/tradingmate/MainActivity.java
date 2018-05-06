package com.smoowy.tradingmate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button botonCazar, botonCorta, botonLarga, botonEmpezar,
            botonPorcentajes, botonAgregarInversion, botonCambioInversion;
    EditText invertido, precio, comisionEntrada, comisionSalida, monedaOrigen, monedaDestino,
            precisionOrigen, precisionDestino, liquidezOrigen, liquidezDestino,
            liquidezNombre;
    TextView encabezadoInversion;
    int selectorCambioInversion = 0;
    final int cambioInversionOrigen = 0, cambioInversionDestino = 1,
            cambioInversionOrigenLiquidez = 2, cambioInversionDestinoLiquidez = 3;
    boolean botonPorcentajesAplanado, botonAgregarInversionAplanado;
    Vibrator vibrator;
    DrawerLayout drawer;
    RecyclerView recyclerViewInversiones;
    int modo = 0;
    final int modoCazar = 0, modoCorta = 1, modoLarga = 2;
    Realm realm;
    RealmResults<DB> resultadosRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botonCazar = findViewById(R.id.botonCazar);
        botonCorta = findViewById(R.id.botonCorta);
        botonLarga = findViewById(R.id.botonLarga);
        botonEmpezar = findViewById(R.id.botonEmpezar);
        botonPorcentajes = findViewById(R.id.botonPorcentajes);
        botonAgregarInversion = findViewById(R.id.botonAgregarInversion);
        botonCambioInversion = findViewById(R.id.botonCambioInversion);
        encabezadoInversion = findViewById(R.id.encabezadoInversion);
        invertido = findViewById(R.id.inversion);
        precio = findViewById(R.id.precio);
        comisionEntrada = findViewById(R.id.comisionEntrada);
        comisionSalida = findViewById(R.id.comisionSalida);
        liquidezDestino = findViewById(R.id.liquidezDestino);
        liquidezOrigen = findViewById(R.id.liquidezOrigen);
        liquidezNombre = findViewById(R.id.liquidezNombre);
        monedaOrigen = findViewById(R.id.origen);
        monedaDestino = findViewById(R.id.destino);
        precisionOrigen = findViewById(R.id.precisionOrigen);
        precisionDestino = findViewById(R.id.precisionDestino);
        botonCazar.setOnTouchListener(onTouchListener);
        botonCorta.setOnTouchListener(onTouchListener);
        botonLarga.setOnTouchListener(onTouchListener);
        botonEmpezar.setOnTouchListener(onTouchListener);
        botonPorcentajes.setOnTouchListener(onTouchListener);
        botonAgregarInversion.setOnTouchListener(onTouchListener);
        botonCambioInversion.setOnTouchListener(onTouchListener);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        botonPorcentajesAplanado = false;
        setBotonPorcentajes();


        setRecyclerViewInversiones();

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();

        resultadosRealm = realm.where(DB.class).findAll();


        if (resultadosRealm.size() > 0) {

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    DB db = resultadosRealm.get(0);

                    monedaOrigen.setText(db.getMonedaOrigen());
                    monedaDestino.setText(db.getMonedaDestino());
                    invertido.setText(db.getInvertido());
                    precio.setText(db.getPrecio());
                    comisionEntrada.setText(db.getComisionEntrada());
                    comisionSalida.setText(db.getComisionSalida());
                    liquidezOrigen.setText(db.getLiquidezOrigen());
                    liquidezDestino.setText(db.getLiquidezDestino());
                    precisionOrigen.setText(db.getPrecisionOrigen());
                    precisionDestino.setText(db.getPrecisionDestino());
                    liquidezNombre.setText(db.getLiquidezNombre());

                    adapterRecyclerInversiones.lista.addAll(db.operaciones);
                    adapterRecyclerInversiones.datos = adapterRecyclerInversiones.lista.size();
                    adapterRecyclerInversiones.notifyDataSetChanged();

                }


            });


        }


    }


    @Override
    protected void onPause() {

        resultadosRealm = realm.where(DB.class).findAll();
        final int numeroDatos = resultadosRealm.size();


        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                DB db;

                if (numeroDatos == 0) {
                    db = realm.createObject(DB.class, 1);
                } else {
                    db = resultadosRealm.get(0);
                }

                db.setMonedaOrigen(monedaOrigen.getText().toString());
                db.setMonedaDestino(monedaDestino.getText().toString());
                db.setInvertido(invertido.getText().toString());
                db.setPrecio(precio.getText().toString());
                db.setComisionEntrada(comisionEntrada.getText().toString());
                db.setComisionSalida(comisionSalida.getText().toString());
                db.setLiquidezOrigen(liquidezOrigen.getText().toString());
                db.setLiquidezDestino(liquidezDestino.getText().toString());
                db.setPrecisionOrigen(precisionOrigen.getText().toString());
                db.setPrecisionDestino(precisionDestino.getText().toString());
                db.setLiquidezNombre(liquidezNombre.getText().toString());
                db.operaciones.clear();
                db.operaciones.addAll(adapterRecyclerInversiones.lista);
            }
        });


        super.onPause();
    }

    AdapterRecyclerInversiones adapterRecyclerInversiones;

    private void setRecyclerViewInversiones() {
        recyclerViewInversiones = findViewById(R.id.recyclerInversiones);
        adapterRecyclerInversiones = new AdapterRecyclerInversiones(this);
        recyclerViewInversiones.setAdapter(adapterRecyclerInversiones);
        recyclerViewInversiones.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(ItemTouchHelper.UP, ItemTouchHelper.START | ItemTouchHelper.END);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {


                adapterRecyclerInversiones.quitarDatos(viewHolder.getLayoutPosition());


                Snackbar.make(findViewById(R.id.botonEmpezar), "Inversion borrada!", Snackbar.LENGTH_LONG)
                        .setAction("Regresar", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                adapterRecyclerInversiones.recuperarDatos();
                            }
                        }).show();


            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerViewInversiones);
    }

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {


                vibrator.vibrate(50);
                switch (view.getId()) {
                    case R.id.botonCazar: {


                        botonCazar.setBackgroundResource(R.drawable.fondo_botones_superior_presionado);
                        botonCorta.setBackgroundResource(R.drawable.fondo_botones_superior);
                        botonLarga.setBackgroundResource(R.drawable.fondo_botones_superior);

                        modo = modoCazar;


                        break;
                    }
                    case R.id.botonCorta: {


                        botonCazar.setBackgroundResource(R.drawable.fondo_botones_superior);
                        botonCorta.setBackgroundResource(R.drawable.fondo_botones_superior_presionado);
                        botonLarga.setBackgroundResource(R.drawable.fondo_botones_superior);

                        modo = modoCorta;


                        break;
                    }


                    case R.id.botonLarga: {


                        botonCazar.setBackgroundResource(R.drawable.fondo_botones_superior);
                        botonCorta.setBackgroundResource(R.drawable.fondo_botones_superior);
                        botonLarga.setBackgroundResource(R.drawable.fondo_botones_superior_presionado);

                        modo = modoLarga;


                        break;
                    }

                    case R.id.botonPorcentajes: {

                        setBotonPorcentajes();

                        break;
                    }
                    case R.id.botonEmpezar: {
                        empezarCalculos();
                        break;
                    }
                    case R.id.botonAgregarInversion: {

                        botonAgregarInversionAplanado = true;
                        if (checarSiFaltanDatos())
                            break;

                        agregarInversion();
                        break;
                    }

                    case R.id.botonCambioInversion: {

                        selectorCambioInversion += 1;

                        if (selectorCambioInversion > 4)
                            selectorCambioInversion = 0;


                        switch (selectorCambioInversion) {


                            case cambioInversionOrigen: {
                                encabezadoInversion.setText("Inversion origen");
                                break;

                            }
                            case cambioInversionDestino: {
                                encabezadoInversion.setText("Inversion destino");
                                break;

                            }
                            case cambioInversionOrigenLiquidez: {
                                encabezadoInversion.setText("Inversion origen con liquidez");
                                break;

                            }

                            case cambioInversionDestinoLiquidez: {
                                encabezadoInversion.setText("Inversion destino con liquidez");
                                break;

                            }
                        }


                    }
                }


            }


            return true;
        }
    };

    private void agregarInversion() {

        double precioNum = 0, invertidoNum = 0, cantidadNum = 0;

        precioNum = Double.parseDouble(precio.getText().toString());


        switch (selectorCambioInversion) {


            case cambioInversionOrigen: {
                invertidoNum = Double.parseDouble(invertido.getText().toString());
                cantidadNum = invertidoNum / precioNum;
                break;

            }
            case cambioInversionDestino: {
                cantidadNum = Double.parseDouble(invertido.getText().toString());
                invertidoNum = cantidadNum * precioNum;
                break;

            }
            case cambioInversionOrigenLiquidez: {
                invertidoNum = Double.parseDouble(invertido.getText().toString());
                invertidoNum /= Double.parseDouble(liquidezOrigen.getText().toString());
                cantidadNum = invertidoNum / precioNum;
                break;
            }

            case cambioInversionDestinoLiquidez: {
                cantidadNum = Double.parseDouble(invertido.getText().toString());
                cantidadNum /= Double.parseDouble(liquidezDestino.getText().toString());
                invertidoNum = cantidadNum * precioNum;
                break;

            }
        }


        String precioImportar, invertidoImportar, cantidadImportar,
                formatoImportarOrigen, formatoImportarDestino;

        if (precisionOrigen.getText().toString().isEmpty()) {

            formatoImportarOrigen = "%.8f";

        } else
            formatoImportarOrigen = "%." + precisionOrigen.getText().toString() + "f";


        if (precisionDestino.getText().toString().isEmpty()) {

            formatoImportarDestino = "%.8f";

        } else
            formatoImportarDestino = "%." + precisionDestino.getText().toString() + "f";

        precioImportar = String.format(formatoImportarOrigen, precioNum);
        invertidoImportar = String.format(formatoImportarOrigen, invertidoNum);
        cantidadImportar = String.format(formatoImportarDestino, cantidadNum);

        precio.setText("");
        invertido.setText("");

        adapterRecyclerInversiones.agregarDatos(precioImportar,
                invertidoImportar, cantidadImportar);
    }

    private void setBotonPorcentajes() {
        if (!botonPorcentajesAplanado) {

            botonPorcentajes.setBackgroundResource(R.drawable.fondo_botones_superior_presionado);
            botonPorcentajesAplanado = true;
        } else {
            botonPorcentajes.setBackgroundResource(R.drawable.fondo_botones_superior);
            botonPorcentajesAplanado = false;
        }
    }


    void empezarCalculos() {

        if (checarSiFaltanDatos()) {
            drawer.closeDrawer(Gravity.START);
            return;
        }

        if (!invertido.getText().toString().isEmpty())
            agregarInversion();


        Intent mIntent = new Intent(this, Calculos.class);


        String precisionOrigenFormato;

        if (precisionOrigen.getText().toString().isEmpty()) {

            precisionOrigenFormato = "%.8f";

            mIntent.putExtra("precisionOrigen", precisionOrigenFormato);


        } else {

            precisionOrigenFormato = "%." + precisionOrigen.getText().toString() + "f";

            mIntent.putExtra("precisionOrigen", precisionOrigenFormato);

        }


        if (precisionDestino.getText().toString().isEmpty()) {

            mIntent.putExtra("precisionDestino", "%.8f");

        } else {

            String formato = "%." + precisionDestino.getText().toString() + "f";

            mIntent.putExtra("precisionDestino", formato);

        }


        if (liquidezNombre.getText().toString().isEmpty())
            mIntent.putExtra("liquidezNombre", "USD");
        else
            mIntent.putExtra("liquidezNombre", liquidezNombre.getText().toString());


        if (liquidezOrigen.getText().toString().isEmpty())
            mIntent.putExtra("liquidezOrigen", 1.0);
        else
            mIntent.putExtra("liquidezOrigen", Double.parseDouble(liquidezOrigen.getText().toString()));

        if (liquidezDestino.getText().toString().isEmpty())
            mIntent.putExtra("liquidezDestino", 1.0);
        else
            mIntent.putExtra("liquidezDestino", Double.parseDouble(liquidezDestino.getText().toString()));


        mIntent.putExtra("precisionOrigenNumero", precisionOrigen.getText().toString());
        mIntent.putExtra("precisionDestinoNumero", precisionDestino.getText().toString());
        mIntent.putExtra("monedaOrigenNombre", monedaOrigen.getText().toString().toUpperCase());
        mIntent.putExtra("monedaDestinoNombre", monedaDestino.getText().toString().toUpperCase());
        mIntent.putExtra("comisionEntrada", Double.parseDouble(comisionEntrada.getText().toString()));
        mIntent.putExtra("comisionSalida", Double.parseDouble(comisionSalida.getText().toString()));
        mIntent.putExtra("modo", modo);
        mIntent.putExtra("botonPorcentajesAplanado", botonPorcentajesAplanado);


        int numeroDeDatos = adapterRecyclerInversiones.lista.size();

        double invertidoFinalVarios = 0, invertidoVariosImportar = 0,
                inversion, precio, precioVariosImportar;


        for (int i = 0; i < numeroDeDatos; i++) {

            precio = Double.parseDouble(adapterRecyclerInversiones.lista.get(i).getPrecio());
            inversion = Double.parseDouble(adapterRecyclerInversiones.lista.get(i).getInversion());

            invertidoFinalVarios += (inversion / precio);
            invertidoVariosImportar += inversion;

            String etiquetaPrecio, etiquetaInvertido;

            etiquetaPrecio = "precio" + i;
            etiquetaInvertido = "inversion" + i;

            mIntent.putExtra(etiquetaPrecio, adapterRecyclerInversiones.lista.get(i).getPrecio());
            mIntent.putExtra(etiquetaInvertido, adapterRecyclerInversiones.lista.get(i).getInversion());

        }

        precioVariosImportar = invertidoVariosImportar / invertidoFinalVarios;
        mIntent.putExtra("precio", precioVariosImportar);
        mIntent.putExtra("invertido", invertidoVariosImportar);
        mIntent.putExtra("multiplesDatos", true);


        startActivityForResult(mIntent, 1);
    }

    private boolean checarSiFaltanDatos() {
        ArrayList<EditText> listaDeRevisionArray;

        if (adapterRecyclerInversiones.lista.size() > 0) {
            EditText[] listaDeRevision = {monedaOrigen, monedaDestino,
                    comisionEntrada, comisionSalida};
            listaDeRevisionArray = new ArrayList<>(Arrays.asList(listaDeRevision));
        } else {
            EditText[] listaDeRevision = {monedaOrigen, monedaDestino,
                    comisionEntrada, comisionSalida, precio, invertido};
            listaDeRevisionArray = new ArrayList<>(Arrays.asList(listaDeRevision));
        }


        if (botonAgregarInversionAplanado) {
            EditText[] listaDeRevision = {monedaOrigen, monedaDestino,
                    comisionEntrada, comisionSalida, precio, invertido};
            listaDeRevisionArray = new ArrayList<>(Arrays.asList(listaDeRevision));
            botonAgregarInversionAplanado = false;
        }

        for (EditText texto : listaDeRevisionArray) {

            if (texto.getText().toString().isEmpty()) {
                Snackbar.make(findViewById(R.id.botonEmpezar), "Faltan datos por llenar!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        drawer.closeDrawer(Gravity.START);
        if (requestCode == 1)
            if (data != null) {
                if (data.getExtras().getBoolean("salir", false))
                    finish();
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

}


/*

InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
 */