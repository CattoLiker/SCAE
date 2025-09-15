package com.scaeproyecto.sistemacontrolalmuerzoescolarctn;

public class Alumno {

    private int idEstudiante;
    private String nombre;
    private String apellido;
    private String curso;
    private String seccion;
    private String especialidad;
    private String estado;  // Test

    // Constructor, getters y setters
    public Alumno(int idEstudiante, String nombre, String apellido, String curso, String seccion, String especialidad, String estado) {
        this.idEstudiante = idEstudiante;
        this.nombre = nombre;
        this.apellido = apellido;
        this.curso = curso;
        this.seccion = seccion;
        this.especialidad = especialidad;
        this.estado = estado;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getCurso() {
        return curso;
    }

    public String getSeccion() {
        return seccion;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
}
