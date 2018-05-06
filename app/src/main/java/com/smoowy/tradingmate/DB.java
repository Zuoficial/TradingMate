package com.smoowy.tradingmate;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DB extends RealmObject {

    @PrimaryKey
    int id;
    String monedaOrigen;
    String monedaDestino;
    String invertido;
    String precio;
    String comisionEntrada;
    String comisionSalida;
    String precisionOrigen;
    String precisionDestino;
    String liquidezOrigen;
    String liquidezDestino;
    String liquidezNombre;
    RealmList<DBOpInversiones> operaciones;

    public String getMonedaOrigen() {
        return monedaOrigen;
    }

    public void setMonedaOrigen(String monedaOrigen) {
        this.monedaOrigen = monedaOrigen;
    }

    public String getMonedaDestino() {
        return monedaDestino;
    }

    public void setMonedaDestino(String monedaDestino) {
        this.monedaDestino = monedaDestino;
    }

    public String getInvertido() {
        return invertido;
    }

    public void setInvertido(String invertido) {
        this.invertido = invertido;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getComisionEntrada() {
        return comisionEntrada;
    }

    public void setComisionEntrada(String comisionEntrada) {
        this.comisionEntrada = comisionEntrada;
    }

    public String getComisionSalida() {
        return comisionSalida;
    }

    public void setComisionSalida(String comisionSalida) {
        this.comisionSalida = comisionSalida;
    }

    public String getPrecisionOrigen() {
        return precisionOrigen;
    }

    public void setPrecisionOrigen(String precisionOrigen) {
        this.precisionOrigen = precisionOrigen;
    }

    public String getPrecisionDestino() {
        return precisionDestino;
    }

    public void setPrecisionDestino(String precisionDestino) {
        this.precisionDestino = precisionDestino;
    }

    public String getLiquidezOrigen() {
        return liquidezOrigen;
    }

    public void setLiquidezOrigen(String liquidezOrigen) {
        this.liquidezOrigen = liquidezOrigen;
    }

    public String getLiquidezDestino() {
        return liquidezDestino;
    }

    public void setLiquidezDestino(String liquidezDestino) {
        this.liquidezDestino = liquidezDestino;
    }

    public String getLiquidezNombre() {
        return liquidezNombre;
    }

    public void setLiquidezNombre(String liquidezNombre) {
        this.liquidezNombre = liquidezNombre;
    }





}
