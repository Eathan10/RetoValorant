package ModeloDAO;

import Modelo.Equipo;
import Modelo.Juego;

import java.util.ArrayList;

public class JuegoDAO {
    private static ArrayList<Juego> listaJuegos = new ArrayList<>();

    public JuegoDAO() {}

    public void crearJuego(Juego j) {
        listaJuegos.add(j);
    }
    public ArrayList<Juego> obtenerTodosJuegos() {
        return listaJuegos;
        //Devuelve un ArrayList nuevo para la seguridad de datos
    }
    public boolean eliminarJuego(int codJuego) {
        return listaJuegos.removeIf(j -> j.getCodjuego() == codJuego);
        //quita el equipo en caso de encontrarlo por su codigo
        //devuelve boolean para confirmar en Controller
    }

}
