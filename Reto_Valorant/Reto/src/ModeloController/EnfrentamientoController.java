package ModeloController;

import Modelo.Enfrentamiento;
import Modelo.Equipo;
import Modelo.Jornada;
import ModeloDAO.EnfrentamientoDAO;
import ModeloDAO.EquipoDAO;
import ModeloDAO.JornadaDAO;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnfrentamientoController {
    private EquipoDAO equipoDAO;
    private JornadaDAO jornadaDAO;
    private EnfrentamientoDAO enfrentamientoDAO;
    ArrayList<Enfrentamiento> enfrentamientos;
    ArrayList<Enfrentamiento> enfrentamientosMitad1;
    ArrayList<Jornada> jornadas;
    ArrayList<Equipo> equipos;

    public void crearEnfrentamientos() {
        boolean yes = true;
        declararVariables();
        do{
            try {
                primeraMitad();
                segundaMitad();
                decirEnfrentamientos();
                yes = false;
            } catch (IllegalArgumentException e) {
                System.out.println("No se han encontrado equipos. " + e.getMessage());
                yes = true;
            }
        }while(yes);
    }
    private void declararVariables(){
        enfrentamientoDAO = new EnfrentamientoDAO();
        jornadaDAO = new JornadaDAO();
        equipoDAO = new EquipoDAO();
        enfrentamientos = enfrentamientoDAO.getEnfrentamientos();
        enfrentamientosMitad1 = new ArrayList<>();
        equipos = new ArrayList<>();
        jornadas = jornadaDAO.getJornadas();
    }
    private void primeraMitad(){
        for (int p = 0; p < jornadas.size()/2; p++) {
            equipos = equipoDAO.obtenerTodosLosEquipos();
            hacerEnfrentamiento(p);
        }
    }
    private void segundaMitad(){
        Random rand = new Random();
        for (int p = jornadas.size()/2; p < jornadas.size(); p++) {
                Enfrentamiento enfrentamiento = new Enfrentamiento();
                enfrentamiento.setHora(elegirHora());
                enfrentamiento.setJornada(jornadas.get(p));
                int enfre = rand.nextInt(enfrentamientosMitad1.size());
                enfrentamiento.setEquipo1(enfrentamientosMitad1.get(enfre).getEquipo2());
                enfrentamiento.setEquipo2(enfrentamientosMitad1.get(enfre).getEquipo1());
                enfrentamientosMitad1.remove(enfre);
                enfrentamientoDAO.anadirEnfrentamientos(enfrentamiento);
        }
    }
    private void decirEnfrentamientos(){
        for (Enfrentamiento enfrentamiento : enfrentamientos) {
            System.out.println(enfrentamiento.getHora() + " " + enfrentamiento.getEquipo1().getNombre() + " " +
                    enfrentamiento.getEquipo1().getCodEquipo() + " " + enfrentamiento.getEquipo1().getFechaFundacion()
                    + " " + enfrentamiento.getEquipo2().getNombre() + " " +
                    enfrentamiento.getEquipo2().getCodEquipo() + " " + enfrentamiento.getEquipo2().getFechaFundacion()
                    + " " + enfrentamiento.getJornada().getFechaInicio()+ " " +
                    enfrentamiento.getJornada().getNumJornada());
        }
    }
    private void hacerEnfrentamiento(int p){
        for (int i = 0; i <= equipos.size()/2; i++) {
            Enfrentamiento enfrentamiento = new Enfrentamiento();
            enfrentamiento.setHora(elegirHora());
            enfrentamiento.setEquipo1(elegirEquipo(equipos));
            equipos.remove(enfrentamiento.getEquipo1());
            equipoDAO.crearEquipo(enfrentamiento.getEquipo1());

            noSeHanEnfrentado(enfrentamiento);

            equipos.remove(enfrentamiento.getEquipo2());
            equipoDAO.crearEquipo(enfrentamiento.getEquipo2());
            enfrentamiento.setJornada(jornadas.get(p));
            enfrentamientoDAO.anadirEnfrentamientos(enfrentamiento);
            enfrentamientosMitad1.add(enfrentamiento);
        }
    }
    private void noSeHanEnfrentado(Enfrentamiento enfrentamiento){
        boolean yes = false;
        do {
            enfrentamiento.setEquipo2(elegirEquipo(equipos));
            try{
                for (int j = 1; j <= enfrentamientos.size(); j++) {
                    for (int o = 0; o <= enfrentamientos.size(); o++)
                        if (o != j) {
                            if (enfrentamientos.get(j).getEquipo1() == enfrentamientos.get(o).getEquipo1()
                                    && enfrentamientos.get(j).getEquipo2() == enfrentamientos.get(o).getEquipo2()) {
                                yes = true;
                            } else {
                                o = enfrentamientos.size() + 1;
                                j = enfrentamientos.size() + 1;
                                yes = false;
                            }
                        }
                }
            }catch (IndexOutOfBoundsException e){
                System.out.println();
            }
        }while (yes);
    }
    private LocalTime elegirHora() {
        Random rand = new Random();
        int hora = rand.nextInt(15);
        return LocalTime.of(7, 0, 0).plusHours(hora);
    }
    private Equipo elegirEquipo(List<Equipo> equipos) {
        Random rand = new Random();
        int eq1 = rand.nextInt(equipos.size());
        return equipos.get(eq1);
    }
}