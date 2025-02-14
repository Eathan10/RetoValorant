package ModeloDAO;

import Modelo.Jugador;

import java.util.ArrayList;
import java.util.List;

public class JugadorDAO {

        private static ArrayList<Jugador> jugadores = new ArrayList<>();


        public JugadorDAO() {}


        public void agregar(Jugador jugador) {
            jugadores.add(jugador);
        }


        public ArrayList<Jugador> obtenerTodos(){
            return new ArrayList<>(jugadores);
        }

        public void modificar(int codJugador, Jugador nuevoJugador) {
            jugadores.replaceAll(j -> j.getCodJugador() == nuevoJugador.getCodJugador() ? nuevoJugador : j);
        }


        public boolean eliminar(int codJugador) {
            return jugadores.removeIf(j -> j.getCodJugador() == codJugador);
        }


        public Jugador obtenerPorCodigo(int codJugador) {
            return jugadores.stream().filter(j -> j.getCodJugador() == codJugador).findFirst().get();
        }


        public ArrayList<Jugador> obtenerPorEquipo(int codEquipo) {
            List<Jugador> jugadors = jugadores.stream().filter(j -> j.getEquipo().getCodEquipo() == codEquipo).toList();
            return new ArrayList<>(jugadors);
            //obtiene los jugadores por Equipo con codigo coincidente y los añade a 'jugadores'

        }


}
