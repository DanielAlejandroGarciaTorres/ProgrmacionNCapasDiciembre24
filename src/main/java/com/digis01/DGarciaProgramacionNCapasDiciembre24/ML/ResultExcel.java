/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.digis01.DGarciaProgramacionNCapasDiciembre24.ML;

/**
 *
 * @author digis
 */
public class ResultExcel {
    
    public int fila;
    public String texto;
    public String descripcionProblema;
    
    public ResultExcel(int fila, String texto, String descripcionProblema){
        this.fila = fila;
        this.texto = texto;
        this.descripcionProblema = descripcionProblema;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getDescripcionProblema() {
        return descripcionProblema;
    }

    public void setDescripcionProblema(String descripcionProblema) {
        this.descripcionProblema = descripcionProblema;
    }
    
    
    
}
