package ModeloDAO;

import Modelo.Equipo;
import Modelo.Jugador;

import java.util.ArrayList;

public class EquipoDAO {

    private static ArrayList<Equipo> listaEquipos = new ArrayList<>();

    public EquipoDAO() {}

    public void crearEquipo(Equipo e) {
        listaEquipos.add(e);
    }

    public ArrayList<Equipo> obtenerTodosLosEquipos() {
        return listaEquipos;
        //Devuelve un ArrayList nuevo para la seguridad de datos
    }

    public Equipo obtenerEquipoPorCodigo(int codEquipo) {
        return listaEquipos.stream()
                .filter(e -> e.getCodEquipo() == codEquipo)
                .findFirst().orElse(null);
    }
    public void actualizarEquipo(Equipo nuevoEquipo) {
        listaEquipos.replaceAll(e -> e.getCodEquipo() == nuevoEquipo.getCodEquipo() ? nuevoEquipo: e);
            //si e.getCodEquipo == nuevoEquipo.getCodEquipo, sustituimos, si no, dejamos a 'e' igual
    }
    public boolean eliminarEquipo(int codEquipo) {
        return listaEquipos.removeIf(e -> e.getCodEquipo() == codEquipo);
        //quita el equipo en caso de encontrarlo por su codigo
        //devuelve boolean para confirmar en Controller
    }
    public void anadirJugador(Equipo eq, Jugador j) {
        eq.agregarJugador(j);
    }
}