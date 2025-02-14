package ModeloDAO;

import Modelo.Enfrentamiento;
import Modelo.Equipo;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class EnfrentamientoDAO {
    private static ArrayList<Enfrentamiento> enfrentamientos;

    public void anadirEnfrentamientos(Enfrentamiento en) {
        try {
            enfrentamientos.add(en);
        }catch(NullPointerException e) {
            enfrentamientos = new ArrayList<>();
            enfrentamientos.add(en);
        }
    }

    public ArrayList<Enfrentamiento> getEnfrentamientos() {
        if (enfrentamientos == null){
            enfrentamientos = new ArrayList<>();
        }
        return enfrentamientos;
    }

    public Enfrentamiento getEnfrentamientoPorId(int id) {
             return enfrentamientos.stream().filter(e -> e.getIdEnfrentamiento() == id).findFirst().orElse(null);
             //el enrentamiento tiene que tener un ID para saber a cual referirse, no puede ser x fecha u hora
    }
    public Enfrentamiento getEnfrentamientoPorEquipos(Equipo eq1,Equipo eq2){
        return enfrentamientos.stream().filter(e -> e.getEquipo1().equals(eq1) && e.getEquipo2().equals(eq2)).findFirst().orElse(null);
    }

    /*
        No hace falta nada mas por que segun ejercicio:
            Una vez se cierra periodo de inscripcion de equipos no se puede alterar los enfrentamientos.
    */


}
