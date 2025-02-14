

package ModeloController;

import Modelo.Equipo;
import Modelo.Juego;
import Modelo.Jugador;
import ModeloDAO.EquipoDAO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

public class EquipoController{

    private static EquipoDAO eDAO;
    private static LocalDate FECHAFUNDACION;
    //fecha fundacion hay que meterla en Juego

    public EquipoController() {
        eDAO = new EquipoDAO();
    }

    public void definirFechaFundacion(Juego j) {
        FECHAFUNDACION = j.getFechaSalida();
    }
    public void validarDatosEquipo() {
        Equipo equ = new Equipo();
            equ.setNombre(this.validarDato());
            equ.setCodEquipo(this.generarCodEquipo());
            equ.setFechaFundacion(this.validarFechaFundacion());
        eDAO.crearEquipo(equ);
    }
    private String validarDato() {
        String var = "";
        boolean isFinished = false;
        Pattern p = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9 _-]{3,15}$"); //15 como mucho como en MER/MR

        do {
            try {
                var = JOptionPane.showInputDialog(null, "Ingrese el nombre del equipo");
                Matcher matcher = p.matcher(var);

                if (var.trim().isBlank()){
                    JOptionPane.showMessageDialog(null, "El nombre del equipo no puede ser nulo");
                } else if (matcher.matches()) {
                    isFinished = true;
                }else {
                    JOptionPane.showMessageDialog(null, var + " tiene un patron inválido");
                }
            } catch (NumberFormatException e) {
                System.out.println("El nombre del equipo NO debe ser menor que 3 o mayor que 15: " +e.getMessage());
            } catch (NullPointerException e) {
                System.out.println("No se admite valor nulo");
            }
        } while(!isFinished);

        return var;
    }
    private int generarCodEquipo() {
        Set<Integer> codigosEquipo = eDAO.obtenerTodosLosEquipos()
                    .stream().map(Equipo::getCodEquipo)
                    .collect(Collectors.toSet());
        int codEquipo = 0;
        while (codigosEquipo.contains(codEquipo)) {
            codEquipo++;
            //hasta que no encuentra un nuevo codigo no sale del loop
        }
        return codEquipo;
    }
    private LocalDate validarFechaFundacion() {
        boolean validFecha = false;
        LocalDate fechaPars = null;
        String fechaNOpars = "";
        do {
            try {
                fechaNOpars = JOptionPane.showInputDialog(null, "Ingrese la fecha de fundacion del equipo");
                if (fechaNOpars.isBlank()) {
                    JOptionPane.showMessageDialog(null, "la fecha no puede estar vacia");
                } else {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    fechaPars = LocalDate.parse(fechaNOpars,formatter);
                    if (fechaPars.isBefore(FECHAFUNDACION) || fechaPars.isAfter(LocalDate.now())) {
                        JOptionPane.showMessageDialog(null, "La fecha de fundacion no puede ser anterior al año de creacion del juego ni posterior a hoy");
                    }else
                        validFecha = true;
                }
            } catch (NumberFormatException | DateTimeParseException e) {
                System.out.println(fechaNOpars + " error al parsear la fecha : " +e.getMessage());
            } catch (NullPointerException e) {
                System.out.println(fechaNOpars + " no puede ser nulo");
            }
        } while(!validFecha);

        return fechaPars;
    }
    public void modificarEquipo(){
        Equipo eq = new Equipo();
        ArrayList<Equipo> equipos = eDAO.obtenerTodosLosEquipos();
        boolean continuar ;
        do {
            try {
                String opc= (String) JOptionPane.showInputDialog(null,
                        "Que equipo?",
                        "Opciones",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        equipos.stream().map(Equipo::getNombre).toArray(String[]::new),
                        equipos.getFirst().getNombre()
                );
                if (opc==null || opc.isEmpty()) {
                    continuar = false;
                }else {
                    eq = eDAO.obtenerTodosLosEquipos().stream().filter(equipo -> equipo.getNombre().equals(opc)).findFirst().orElse(null);
                    //con solo el nombre obtiene todos los datos de Equipo eq
                    continuar = true;
                }
            }catch (NullPointerException e){
                continuar = false;
            }
        }while (JOptionPane.showConfirmDialog(null,"Quiere continuar modificando equipos?") == 1);
        //sale de repetitiva
        if (continuar)
            opcionesModificar(eq);
    }
    private void opcionesModificar(Equipo eq){
        String[] opc = {"Nombre","Fecha de fundacion","Jugadores"};
        try {
            String opcion = (String) JOptionPane.showInputDialog(null,
                    "Que quieres modificar?",
                    "Opciones",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    opc,
                    opc[0]
            );
            if (opcion.isBlank()){
                JOptionPane.showMessageDialog(null,"No se permiten opciones nulas");
            }else {
                switch (opcion){
                    case "Nombre" -> eq.setNombre(this.validarDato());
                    case "Fecha de fundacion" -> eq.setFechaFundacion(this.validarFechaFundacion());
                    //case "Jugadores" -> e.setListaJugadores();
                    //No seria mejor poner esta opcion solo en el jugador?
                    default -> JOptionPane.showMessageDialog(null,"No se puede modificar eso en el equipo");
                }
            }
        }catch (NullPointerException e){
            System.out.println("No se aceptan valores nulos");
        }
    }
    public void eliminarEquipo(){
        Equipo eq = new Equipo();
        ArrayList<Equipo> equipos = eDAO.obtenerTodosLosEquipos();
        boolean continuar ;
        do {
            try {
                String opc= (String) JOptionPane.showInputDialog(null,
                        "Que equipo quiere eliminar?",
                        "Opciones",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        equipos.stream().map(Equipo::getNombre).toArray(String[]::new),
                        equipos.getFirst().getNombre()
                );
                if (opc==null || opc.isEmpty()) {
                    continuar = false;
                }else {
                    eq = eDAO.obtenerTodosLosEquipos().stream().filter(equipo -> equipo.getNombre().equals(opc)).findFirst().orElse(null);
                    //con solo el nombre obtiene todos los datos de Equipo eq
                    continuar = true;
                }
            }catch (NullPointerException e){
                continuar = false;
            }
        }while (JOptionPane.showConfirmDialog(null,"Quiere continuar eliminando equipos?") == 0);
        //sale de repetitiva
        if (continuar)
            eDAO.eliminarEquipo(Objects.requireNonNull(eq).getCodEquipo());
    }
    public void verTodosEquipos(){
        try {
            ArrayList<Equipo> equipos
                    = eDAO.obtenerTodosLosEquipos();
            for (Equipo j : equipos){
                Object[] options = { "OK", "CANCEL"};
                JOptionPane.showOptionDialog(null, j.toString(), "Continuar",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, options, options[0]);
                //Arreglado
            }
        }catch (NullPointerException e){
            System.out.println("No hay jugadores para enseñar");
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("ERROR: " + e.getMessage());
        }
    }
    public void verPorNombre(){
        String[] nombres = eDAO.obtenerTodosLosEquipos().stream().map(Equipo::getNombre).toArray(String[]::new);
        do {
            String equipoElegido = (String) JOptionPane.showInputDialog(null,
                    "Elija el nombre del equipo que quiere ver",
                    "Opciones",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    nombres,
                    nombres[0]
            );
            if (equipoElegido == null || equipoElegido.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null,"El nombre no puede ser nulo");
            }else {

                Equipo e = eDAO.obtenerTodosLosEquipos().stream().filter(equipo -> equipo.getNombre().equals(equipoElegido)).findFirst().orElse(null);

                JOptionPane.showMessageDialog(null, Objects.requireNonNull(e).toString());
            }
        }while (JOptionPane.showConfirmDialog(null,"quiere continuar viendo equipos?") == 0);
    }
    public void verJugadores() {
        String[] nombres = eDAO.obtenerTodosLosEquipos().stream().map(Equipo::getNombre).toArray(String[]::new);
        do {
            String equipoElegido = (String) JOptionPane.showInputDialog(null,
                    "Elija el nombre del equipo que quiere ver sus jugadores",
                    "Opciones",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    nombres,
                    nombres[0]
            );
            if (equipoElegido == null || equipoElegido.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null,"El nombre no puede ser nulo");
            }else {

                Equipo e = eDAO.obtenerTodosLosEquipos().stream().filter(equipo -> equipo.getNombre().equals(equipoElegido)).findFirst().orElse(null);

                ArrayList<Jugador> jugadores = Objects.requireNonNull(e).getListaJugadores();
                for (Jugador j : jugadores) {
                    Object[] options = { "OK", "CANCEL"};
                    JOptionPane.showOptionDialog(null, j.toString(), "Continuar",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                            null, options, options[0]);
                }
            }
        }while (JOptionPane.showConfirmDialog(null,"quiere continuar viendo equipos?") == 0);
    }
}
