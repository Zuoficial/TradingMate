package com.smoowy.tradingmate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button botonCazar, botonComprar, botonEmpezar, botonPorcentajes;
    EditText invertido, precio, comision, monedaOrigen, monedaDestino,
            precisionOrigen, precisionDestino, precisionPrecio,
            precisionInversion;
    boolean modoComprar;
    boolean botonPorcentajesAplanado;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        botonCazar = findViewById(R.id.botonCazar);
        botonComprar = findViewById(R.id.botonComprar);
        botonEmpezar = findViewById(R.id.botonEmpezar);
        botonPorcentajes = findViewById(R.id.botonPorcentajesM);
        invertido = findViewById(R.id.invertido);
        precio = findViewById(R.id.precio);
        comision = findViewById(R.id.comision);
        monedaOrigen = findViewById(R.id.origen);
        monedaDestino = findViewById(R.id.destino);
        precisionOrigen = findViewById(R.id.precisionOrigen);
        precisionDestino = findViewById(R.id.precisionDestino);
        precisionPrecio = findViewById(R.id.precisionPrecio);
        precisionInversion = findViewById(R.id.precisionInversion);
        botonCazar.setOnTouchListener(onTouchListener);
        botonComprar.setOnTouchListener(onTouchListener);
        botonEmpezar.setOnTouchListener(onTouchListener);
        botonPorcentajes.setOnTouchListener(onTouchListener);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences("TradingMatePreferences", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("monedaOrigenNombre")) {


            monedaOrigen.setText(sharedPreferences.getString("monedaOrigenNombre", ""));
            monedaDestino.setText(sharedPreferences.getString("monedaDestinoNombre", ""));
            invertido.setText(sharedPreferences.getString("invertido", ""));
            precio.setText(sharedPreferences.getString("precio", ""));
            comision.setText(sharedPreferences.getString("comision", ""));
            precisionOrigen.setText(sharedPreferences.getString("precisionOrigenNumero", ""));
            precisionDestino.setText(sharedPreferences.getString("precisionDestinoNumero", ""));
            precisionPrecio.setText(sharedPreferences.getString("precisionPrecioNumero", ""));
            precisionInversion.setText(sharedPreferences.getString("precisionInversionNumero", ""));
        }

        botonPorcentajesAplanado = false;
        setBotonPorcentajes();


    }

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {


                vibrator.vibrate(50);
                switch (view.getId()) {
                    case R.id.botonCazar: {


                        botonCazar.setBackgroundResource(R.drawable.fondo_botones_presionado);
                        botonComprar.setBackgroundResource(R.drawable.fondo_botones);

                        modoComprar = false;


                        break;
                    }
                    case R.id.botonComprar: {


                        botonCazar.setBackgroundResource(R.drawable.fondo_botones);
                        botonComprar.setBackgroundResource(R.drawable.fondo_botones_presionado);

                        modoComprar = true;


                        break;
                    }

                    case R.id.botonPorcentajesM: {

                        setBotonPorcentajes();

                        break;
                    }
                    case R.id.botonEmpezar: {
                        empezarCalculos();
                        break;
                    }
                }


            }


            return true;
        }
    };

    private void setBotonPorcentajes() {
        if (!botonPorcentajesAplanado) {

            botonPorcentajes.setBackgroundResource(R.drawable.fondo_botones_presionado);
            botonPorcentajesAplanado = true;
        } else {
            botonPorcentajes.setBackgroundResource(R.drawable.fondo_botones);
            botonPorcentajesAplanado = false;
        }
    }


    void empezarCalculos() {

        EditText[] listaDeRevision = {monedaOrigen, monedaDestino, invertido, precio, invertido, comision};

        boolean faltanDatos = false;

        for (EditText texto : listaDeRevision) {

            if (texto.getText().toString().isEmpty()) {

                faltanDatos = true;
            }
        }

        if (faltanDatos) {

            Snackbar.make(findViewById(R.id.botonEmpezar), "Faltan datos por llenar!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        Intent mIntent = new Intent(this, Calculos.class);


        String precisionOrigenFormato;

        if (precisionOrigen.getText().toString().equals("")) {

            precisionOrigenFormato = "%.8f";

            mIntent.putExtra("precisionOrigen", precisionOrigenFormato);


        } else {

            precisionOrigenFormato = "%." + precisionOrigen.getText().toString() + "f";

            mIntent.putExtra("precisionOrigen", precisionOrigenFormato);

        }


        if (precisionDestino.getText().toString().equals("")) {

            mIntent.putExtra("precisionDestino", "%.8f");

        } else {

            String formato = "%." + precisionDestino.getText().toString() + "f";

            mIntent.putExtra("precisionDestino", formato);

        }

        if (precisionPrecio.getText().toString().equals("")) {
            mIntent.putExtra("precisionPrecio", precisionOrigenFormato);

        } else {

            String formato = "%." + precisionPrecio.getText().toString() + "f";

            mIntent.putExtra("precisionPrecio", formato);

        }

        if (precisionInversion.getText().toString().equals("")) {
            mIntent.putExtra("precisionInversion", "%.8f");

        } else {

            String formato = "%." + precisionInversion.getText().toString() + "f";

            mIntent.putExtra("precisionInversion", formato);

        }

        mIntent.putExtra("precisionOrigenNumero", precisionOrigen.getText().toString());
        mIntent.putExtra("precisionDestinoNumero", precisionDestino.getText().toString());
        mIntent.putExtra("precisionPrecioNumero", precisionPrecio.getText().toString());
        mIntent.putExtra("precisionInversionNumero", precisionInversion.getText().toString());
        mIntent.putExtra("monedaOrigenNombre", monedaOrigen.getText().toString().toUpperCase());
        mIntent.putExtra("monedaDestinoNombre", monedaDestino.getText().toString().toUpperCase());
        mIntent.putExtra("precio", Double.parseDouble(precio.getText().toString()));
        mIntent.putExtra("invertido", Double.parseDouble(invertido.getText().toString()));
        mIntent.putExtra("comision", Double.parseDouble(comision.getText().toString()));
        mIntent.putExtra("modoComprar", modoComprar);
        mIntent.putExtra("botonPorcentajesAplanado", botonPorcentajesAplanado);


        startActivityForResult(mIntent, 1);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == 1)
            if (data != null) {
                if (data.getExtras().getBoolean("salir", false))
                    finish();
            }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
