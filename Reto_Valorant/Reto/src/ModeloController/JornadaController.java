package ModeloController;

import Modelo.Equipo;
import Modelo.Jornada;
import ModeloDAO.CompeticionDAO;
import ModeloDAO.EquipoDAO;
import ModeloDAO.JornadaDAO;

import javax.swing.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;

public class JornadaController {
    private static JornadaDAO jornadaDAO;
    private static CompeticionDAO competicionDAO;
    private static CompeticionController competicionController;
    private static ArrayList<Jornada> jornadas;
    private static ArrayList<Equipo> equipos;

    private static final int[] meses31 = {1,3,5,7,8,10,12};

    public JornadaController() {
        jornadaDAO = new JornadaDAO();
        competicionDAO = new CompeticionDAO();
        competicionController = new CompeticionController();
        EquipoDAO equipoDAO = new EquipoDAO();
        competicionDAO.obtenerTodasCompeticiones();
        jornadas = jornadaDAO.getJornadas(); // Asegúrate de inicializar aquí
        equipos = equipoDAO.obtenerTodosLosEquipos();
    }

    public boolean validarCreacionJornada(){
        boolean resultado = true;
        try {
            if (equipos.size() % 2 == 0 && equiposMas2Jugadores()) {
                crearJornada();
                competicionController.actualizarCompeticion(jornadas.getLast().getCompeticion());
                resultado = false;
            } else {
                JOptionPane.showMessageDialog(null,"La cantidad de equipos no es par");
            }
        }catch (NullPointerException e){
            System.out.println("No existe ningun equipo.");
        }
        return resultado;
    }

    private boolean equiposMas2Jugadores(){
        boolean resultado = true;
        for (Equipo equipo : equipos) {
            if (equipo.getListaJugadores().size() < 2){
                JOptionPane.showMessageDialog(null, "El equipo " + equipo.getNombre() + " tiene que tener al menos 2 jugadores para continuar");
                resultado = false;
                break;
            }
        }
        return resultado;
    }
    private void crearJornada(){
        for (int i = 0; i < equipos.size(); i++){
            Jornada jornada = new Jornada();
                jornada.setNumJornada(elegirNumJornada());
                jornada.setFechaInicio(elegirFecha());
                jornada.setCompeticion(competicionDAO.obtenerTodasCompeticiones().getLast());
            jornadaDAO.anadirJornada(jornada);
        }
    }
    private int elegirNumJornada(){
        int numJornada;
        try {
            numJornada = jornadas.getLast().getNumJornada()+1;
        }catch (NullPointerException | NoSuchElementException e){
            numJornada = 1;
        }
        return numJornada;
    }
    private LocalDate elegirFecha(){
        int mes;
        int dia;
        int year;
        try {
            LocalDate fecha = elegirDia();
            mes = fecha.getMonthValue();
            dia = fecha.getDayOfMonth();
            year = fecha.getYear();
        }catch (NullPointerException | NoSuchElementException e){
            mes = elegirMes();
            dia = elegirDiaInicial(mes);
            year = 2025;
        }
        return LocalDate.of(year,mes,dia);
    }
    private int elegirMes(){
        Random random = new Random();
        return random.nextInt(11)+1;
    }
    private int elegirDiaInicial(int mes){
        Random random = new Random();
        int randomDia = 0;
        if (mes == 2)
            randomDia = random.nextInt(27)+1;
        else{
            for (int i : meses31) {
                if (mes == i)
                    randomDia = random.nextInt(30)+1;
                else
                    randomDia = random.nextInt(29)+1;
            }
        }
        return randomDia;
    }
    private LocalDate elegirDia(){
        Random random = new Random();
        int randomDia;
        randomDia = random.nextInt(7);
        DayOfWeek diaJornada = jornadas.getLast().getFechaInicio().getDayOfWeek();
        int diasHastaDomingo = DayOfWeek.SUNDAY.getValue() - diaJornada.getValue();
        // Si el día actual no es domingo, suma los días necesarios
        if (diasHastaDomingo < 0) {
            diasHastaDomingo += 7;
        }
        return jornadas.getLast().getFechaInicio().plusDays(diasHastaDomingo).plusDays(randomDia);
    }
}
