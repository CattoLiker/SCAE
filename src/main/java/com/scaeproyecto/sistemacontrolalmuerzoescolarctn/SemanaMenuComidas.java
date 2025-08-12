package com.scaeproyecto.sistemacontrolalmuerzoescolarctn;

public class SemanaMenuComidas {

    private int diaSemana;             // 1 a 5 (lunes a viernes)
    private String diaDelaSemana;
    private int idComidas;             // FK a Comidas
    private int idSemanaMenu;          // FK a SemanaMenu (1 a 4)
    private String nombreComida;       // Para mostrar el nombre en la tabla

    public SemanaMenuComidas(int diaSemana, int idComidas, int idSemanaMenu, String nombreComida) {
        this.diaSemana = diaSemana;
        this.idComidas = idComidas;
        this.idSemanaMenu = idSemanaMenu;
        this.nombreComida = nombreComida;
    }


    public int getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(int d) {
        this.diaSemana = d;
    }

    public int getIdComidas() {
        return idComidas;
    }

    public void setIdComidas(int id) {
        this.idComidas = id;
    }

    public int getIdSemanaMenu() {
        return idSemanaMenu;
    }

    public void setIdSemanaMenu(int id) {
        this.idSemanaMenu = id;
    }

    public String getNombreComida() {
        return nombreComida;
    }

    public void setNombreComida(String nombreComida) {
        this.nombreComida = nombreComida;
    }
}
