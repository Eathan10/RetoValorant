package Modelo;

import java.time.LocalDate;

public class Jornada {
    private int numJornada;
    private LocalDate fechaInicio;
    private Competicion competicion;

    public Jornada(int numJornada, LocalDate fechaInicio, Competicion competicion) {
        this.numJornada = numJornada;
        this.fechaInicio = fechaInicio;
        this.competicion = competicion;
    }

    public Jornada() {
    }

    public int getNumJornada() {
        return numJornada;
    }

    public void setNumJornada(int numJornada) {
        this.numJornada = numJornada;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Competicion getCompeticion() {
        return competicion;
    }

    public void setCompeticion(Competicion competicion) {
        this.competicion = competicion;
    }
}
