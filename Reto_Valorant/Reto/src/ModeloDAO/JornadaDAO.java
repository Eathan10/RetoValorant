package ModeloDAO;


import Modelo.Jornada;

import java.util.ArrayList;

public class JornadaDAO {

    private static final ArrayList<Jornada> listaJornada = new ArrayList<>();

    public JornadaDAO() {}

    public void anadirJornada(Jornada j) {
        listaJornada.add(j);
    }

    public ArrayList<Jornada> getJornadas() {
        return listaJornada;
    }
}
