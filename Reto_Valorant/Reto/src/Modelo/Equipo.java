package Modelo;

import java.time.LocalDate;
import java.util.ArrayList;


public class Equipo {

    private int codEquipo; // Código único del equipo (ej: EQ001)
    private int puntuacion; // Puntuación actual del equipo
    private int contJugadores;
    private String nombre; // Nombre del equipo
    private LocalDate fechaFundacion; // Fecha de fundación del equipo
    private ArrayList<Jugador> listaJugadores; // Lista de jugadores del equipo
    /*
      Constructor sin argumentos. Inicializa la lista de jugadores.
     */
    public Equipo() {
        this.listaJugadores = new ArrayList<>();
    }


    public Equipo(int codEquipo, String nombre, LocalDate fechaFundacion, int puntuacion) {
        this.codEquipo = codEquipo;
        this.nombre = nombre;
        this.fechaFundacion = fechaFundacion;
        this.puntuacion = puntuacion;
        this.contJugadores = 0;
    }

    public Equipo(int codEquipo, String nombre, LocalDate fechaFundacion, int puntuacion, ArrayList<Jugador> listaJugadores) {
        this.codEquipo = codEquipo;
        this.nombre = nombre;
        this.fechaFundacion = fechaFundacion;
        this.puntuacion = puntuacion;
        this.listaJugadores = listaJugadores;
    }

    // Getters y setters

    public int getCodEquipo() {
        return codEquipo;
    }

    public void setCodEquipo(int codEquipo) {
        this.codEquipo = codEquipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaFundacion() {
        return fechaFundacion;
    }

    public void setFechaFundacion(LocalDate fechaFundacion) {
        this.fechaFundacion = fechaFundacion;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public ArrayList<Jugador> getListaJugadores() {
        return listaJugadores;
    }

    public void setListaJugadores(ArrayList<Jugador> listaJugadores) {
        this.listaJugadores = listaJugadores;
    }

    // Metodos que podrian ser necesarios

    /*
      Agrega un jugador a la lista del equipo.
     */
    public void agregarJugador(Jugador jugador) {
        this.listaJugadores.add(jugador);
    }

    /*
      Elimina un jugador de la lista del equipo.
     */
    public void eliminarJugador(Jugador jugador) {
        this.listaJugadores.remove(jugador);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nombre: " + getNombre()+"\n" +
                "Codigo de equipo: " + getCodEquipo() + "\n" +
                "Fecha de fundacion: " + getFechaFundacion());
        if (!listaJugadores.isEmpty()) {
            for (Jugador jugador : getListaJugadores()) {
                sb.append("\nNickname: " + jugador.getNickname());
            }
        }else
            sb.append("\nNo hay jugadores");
        return sb.toString();
    }
}