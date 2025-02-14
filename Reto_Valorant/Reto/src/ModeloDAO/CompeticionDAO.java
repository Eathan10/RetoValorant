package ModeloDAO;

import Modelo.Competicion;

import java.util.ArrayList;

public class CompeticionDAO {
    private static ArrayList<Competicion> competiciones = new ArrayList<>();
    public void anadirCompeticion(Competicion c) {
        if (c == null) {
            competiciones = new ArrayList<>();
        }
        competiciones.add(c);
    }
    public ArrayList<Competicion> obtenerTodasCompeticiones() {
        return competiciones;
    }
}
