import Modelo.Juego;
import ModeloController.*;
import ModeloDAO.CompeticionDAO;
import ModeloDAO.JuegoDAO;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static JuegoController juegoController;
    public static JugadorController jugadorController;
    public static EquipoController equipoController;
    public static JornadaController jornadaController;
    public static CompeticionController competicionController;
    public static EnfrentamientoController enfrentamientoController;
    public static String opcionesSinJuego;
    public static String opcionesSinJornadas;
    public static String opcionesConJornadas;

    public static void main(String[] args) {
        declararVariables();
        opcionesSinJuego();
        eligeElJuego();
        opcionesSinJornadas();
        opcionesConJornadas();
    }
    public static void declararVariables() {
        juegoController = new JuegoController();
        jugadorController = new JugadorController();
        equipoController = new EquipoController();
        jornadaController = new JornadaController();
        competicionController = new CompeticionController();
        enfrentamientoController = new EnfrentamientoController();
        opcionesSinJuego = """
                            1. Añadir un juego nuevo.
                            2. Eliminar un juego.
                            3. Modificar un juego.
                            4. Ver los juegos que hay.
                            5. Continuar a la competicion.
                            """;
        opcionesSinJornadas = """
                                1. Crear un Jugador.
                                2. Crear un Equipo.
                                3. Modificar un Jugador.
                                4. Modificar un Equipo.
                                5. Eliminar un Jugador.
                                6. Eliminar un Equipo.
                                7. Ver todos los jugadores.
                                8. Ver todos los equipos.
                                9. Ver informacion de un jugador en concreto.
                                10. Ver informacion de un equipo en concreto.
                                11. Ver los jugadores de un equipo.
                                12. Crear las jornadas.
                                """;
        opcionesConJornadas = """
                                1. Modificar un Jugador.
                                2. Modificar un Equipo.
                                3. Ver todos los jugadores.
                                4. Ver todos los equipos.
                                5. Ver informacion de un jugador en concreto.
                                6. Ver informacion de un equipo en concreto.
                                7. Ver los jugadores de un equipo.
                                8. Ver los enfrentamientos de una jornada.
                                9. Ver los enfrentamientos de un equipo.
                                10. Añadir un resultado a un enfrentamiento.
                                11. Ver la puntuacion de un equipo.
                                """;
    }

    public static void opcionesSinJuego() {
        Scanner sc = new Scanner(System.in);
        boolean yes = true;
        do {
            try {
                System.out.println(opcionesSinJuego);
                int opcion = sc.nextInt();
                switch (opcion) {
                    case 1 -> juegoController.anadirJuego();
                    case 2 -> juegoController.eliminarJuego();
                    case 3 -> juegoController.modificarJuego();
                    case 4 -> juegoController.verTodosJuegos();
                    default -> yes = validarCrearCompeticion(); //Cuando tengamos competicion pasarlo ahi
                }
            }catch (InputMismatchException e){
                sc.nextLine();
                System.out.println("Vuelve a teclear la opcion por favor " + e.getMessage()+"\n");
                // para notificar que ha pasado, ocurre cuando se 'lia' el Scanner
            }catch (NullPointerException e){
                System.out.println("La opcion es nula, aconsejamos crear antes para despues modificar\n");
            }catch (NumberFormatException e){
                System.out.println("No se acepta ese numero " +e.getMessage() +"\n");
            }
        }while(yes);
    }

    public static boolean validarCrearCompeticion(){
        boolean yes = true;
        JuegoDAO juegoDAO = new JuegoDAO();
        if (juegoDAO.obtenerTodosJuegos().isEmpty())
            System.out.println("No puedes continuar porque no hay ningun juego");
        else
            yes = false;
        return yes;
    }

    public static void eligeElJuego(){
        Juego juego = juegoController.elegirElJuego();
        equipoController.definirFechaFundacion(juego);
        competicionController.crearCompeticion(juego);
    }

    public static void opcionesSinJornadas() {
        Scanner sc = new Scanner(System.in);
        boolean yes=true;
        do {
            try {
                System.out.println(opcionesSinJornadas);
                int opcion = sc.nextInt();
                switch (opcion) {
                    case 1 -> jugadorController.dataValidation();
                    case 2 -> equipoController.validarDatosEquipo();
                    case 3 -> jugadorController.modificarJugador();
                    case 4 -> equipoController.modificarEquipo();
                    case 5 -> jugadorController.eliminarJugador();
                    case 6 -> equipoController.eliminarEquipo();
                    case 7 -> jugadorController.verTodosJugadores();
                    case 8 -> equipoController.verTodosEquipos();
                    case 9 -> jugadorController.verPorNombre();
                    case 10 -> equipoController.verPorNombre();
                    case 11 -> equipoController.verJugadores();
                    default -> {
                        yes = jornadaController.validarCreacionJornada();
                        if (!yes)
                            enfrentamientoController.crearEnfrentamientos();
                    }
                }
            }catch (InputMismatchException e){
                sc.nextLine();
                System.out.println("Vuelve a teclear la opcion por favor " + e.getMessage()+"\n");
                // para notificar que ha pasado, ocurre cuando se 'lia' el Scanner
            }catch (NullPointerException e){
                System.out.println("La opcion es nula, aconsejamos crear antes para despues modificar\n");
                yes = true;
            }catch (NumberFormatException e){
                System.out.println("No se acepta ese numero " +e.getMessage() +"\n");
            }
        }while(yes);
    }

    public static void opcionesConJornadas() {
        Scanner sc = new Scanner(System.in);
        boolean yes = true;
        do {
            System.out.println(opcionesConJornadas);
            int opcion = sc.nextInt();
            switch (opcion) {
                case 1 -> jornadaController.validarCreacionJornada();
                default -> yes = false;
            }
        }while(yes);
    }
}