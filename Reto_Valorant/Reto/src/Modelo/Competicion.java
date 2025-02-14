package Modelo;

public class Competicion {

    private int codCompeticion;
    private char estado;
    private Juego juego;

    public Competicion() {
    }

    public Competicion(int codCompeticion, char estado, Juego juego) {
        this.codCompeticion = codCompeticion;
        this.estado = estado;
        this.juego = juego;
    }

    public int getCodCompeticion() {
        return codCompeticion;
    }

    public void setCodCompeticion(int codCompeticion) {
        this.codCompeticion = codCompeticion;
    }

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }

    public Juego getJuego() {
        return juego;
    }

    public void setJuego(Juego juego) {
        this.juego = juego;
    }
}
