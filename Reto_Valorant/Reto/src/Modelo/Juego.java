package Modelo;

import java.time.LocalDate;

public class Juego {

    private int codjuego;
    private String nombre;
    private LocalDate fechaSalida;

    public Juego() {
    }

    public Juego(int codjuego, String nombre, LocalDate fechaSalida) {
        this.codjuego = codjuego;
        this.nombre = nombre;
        this.fechaSalida = fechaSalida;
    }

    public int getCodjuego() {
        return codjuego;
    }

    public void setCodjuego(int codjuego) {
        this.codjuego = codjuego;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nombre: " + getNombre()+"\n" +
                "Codigo de equipo: " + getCodjuego() + "\n" +
                "Fecha de fundacion: " + getFechaSalida());
        /*
        if (!listaJugadores.isEmpty()) {
            for (Jugador jugador : getListaJugadores()) {
                sb.append("\nNickname: " + jugador.getNickname());
            }
        }else
            sb.append("\nNo hay jugadores");

            AQUI LOS ROLES
         */
        return sb.toString();
    }
}
