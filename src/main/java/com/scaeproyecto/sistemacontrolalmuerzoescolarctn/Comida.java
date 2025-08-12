/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.scaeproyecto.sistemacontrolalmuerzoescolarctn;

/**
 *
 * @author USER
 */
public class Comida {

    private int idComidas;
    private String nombre;
    private String descripcion;

    public Comida(int idComidas, String nombre, String descripcion) {
        this.idComidas = idComidas;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getIdComidas() {
        return idComidas;
    }

    public void setIdComidas(int idComidas) {
        this.idComidas = idComidas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
