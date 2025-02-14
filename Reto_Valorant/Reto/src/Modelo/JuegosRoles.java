package Modelo;

import java.util.ArrayList;

public class JuegosRoles {

    private ArrayList<Juego> listaJuegos;
    private ArrayList<Rol> listaRoles;;

    public JuegosRoles() {
    }

    public JuegosRoles(ArrayList<Juego> listaJuegos, ArrayList<Rol> listaRoles) {
        this.listaJuegos = listaJuegos;
        this.listaRoles = listaRoles;
    }

    public ArrayList<Juego> getListaJuegos() {
        return listaJuegos;
    }

    public void setListaJuegos(ArrayList<Juego> listaJuegos) {
        this.listaJuegos = listaJuegos;
    }

    public ArrayList<Rol> getListaRoles() {
        return listaRoles;
    }

    public void setListaRoles(ArrayList<Rol> listaRoles) {
        this.listaRoles = listaRoles;
    }
}
