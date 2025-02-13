package ModeloController;

import Modelo.Competicion;
import Modelo.Juego;
import ModeloDAO.CompeticionDAO;
import ModeloDAO.JornadaDAO;

import java.util.Set;
import java.util.stream.Collectors;

public class CompeticionController {
    private Competicion competicion;
    private static CompeticionDAO competicionDAO;
    private static JornadaDAO jornadaDAO;

    public CompeticionController() {
        competicionDAO = new CompeticionDAO();
        jornadaDAO = new JornadaDAO();
    }

    public void crearCompeticion(Juego juego) {
        competicion = new Competicion();
            competicion.setCodCompeticion(generarCodCompeticion());
            competicion.setEstado(descubrirEstado());
            competicion.setJuego(juego);
        competicionDAO.anadirCompeticion(competicion);
    }
    private int generarCodCompeticion() {
        Set<Integer> codigosCompeticion = competicionDAO.obtenerTodasCompeticiones()
                .stream().map(Competicion::getCodCompeticion)
                .collect(Collectors.toSet());
        int codCompeticion = 0;
        while (codigosCompeticion.contains(codCompeticion)) {
            codCompeticion++;
            //hasta que no encuentra un nuevo codigo no sale del loop
        }
        return codCompeticion;
    }
    private char descubrirEstado(){
        char estado;
        try {
            jornadaDAO.getJornadas();
                estado = 'A';
        }catch (NullPointerException e){
            estado = 'C';
        }
        return estado;
    }
    public void actualizarCompeticion(Competicion competicion) {
        competicion.setEstado(descubrirEstado());
    }
}
