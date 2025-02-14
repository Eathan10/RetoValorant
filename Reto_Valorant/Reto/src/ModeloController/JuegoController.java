package ModeloController;

import Modelo.Juego;
import ModeloDAO.JuegoDAO;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JuegoController {
    private static JuegoDAO juegoDAO;
    private static final LocalDate SALIDAPRIMERJUEGO = LocalDate.of(1972,11,29);

    public JuegoController() {juegoDAO = new JuegoDAO();}

    public void anadirJuego() {
        Juego juego = new Juego();
            juego.setCodjuego(generarCodJuego());
            juego.setNombre(validarNombre());
            juego.setFechaSalida(validarFechaSalida());
        juegoDAO.crearJuego(juego);
    }
    private int generarCodJuego() {
        int codJuego = 0;
        try {
            Set<Integer> codigosJuegos = juegoDAO.obtenerTodosJuegos()
                    .stream().map(Juego::getCodjuego)
                    .collect(Collectors.toSet());
            while (codigosJuegos.contains(codJuego)) {
                codJuego++;
                //hasta que no encuentra un nuevo codigo no sale del loop
            }
        }catch (NullPointerException e){
            codJuego = 1;
        }
        return codJuego;
    }
    private String validarNombre() {
        String var = "";
        boolean isFinished = false;
        Pattern p = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9 _-]{3,15}$"); //15 como mucho como en MER/MR
        do {
            try {
                var = JOptionPane.showInputDialog(null, "Ingrese el nombre del juego");
                Matcher matcher = p.matcher(var);
                if (var.trim().isBlank()){
                    JOptionPane.showMessageDialog(null, "El nombre del juego no puede ser nulo");
                } else if (matcher.matches()) {
                    isFinished = true;
                }else {
                    JOptionPane.showMessageDialog(null, var + " tiene un patron inválido");
                }
            } catch (NumberFormatException e) {
                System.out.println("El nombre del juego NO debe ser menor que 3 o mayor que 15");
            } catch (NullPointerException e) {
                System.out.println("No se admite valor nulo");
            }
        } while(!isFinished);

        return var;
    }
    private LocalDate validarFechaSalida(){
        boolean validFecha = false;
        LocalDate fechaPars = null;
        String fechaNOpars = "";
        do {
            try {
                fechaNOpars = JOptionPane.showInputDialog(null, "Ingrese la fecha de salida para el juego");
                if (fechaNOpars.isBlank()) {
                    JOptionPane.showMessageDialog(null, "la fecha no puede estar vacia");
                } else {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    fechaPars = LocalDate.parse(fechaNOpars,formatter);
                    if (fechaPars.isBefore(SALIDAPRIMERJUEGO) || fechaPars.isAfter(LocalDate.now())) {
                        JOptionPane.showMessageDialog(null, "La fecha de fundacion no puede ser anterior al año de creacion del primer juego ni posterior a hoy");
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
    public void modificarJuego(){
        Juego j = new Juego();
        ArrayList<Juego> juegos = juegoDAO.obtenerTodosJuegos();
        boolean continuar;
        do {
            try {
                String opc= (String) JOptionPane.showInputDialog(null,
                        "Que Juego?",
                        "Opciones",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        juegos.stream().map(Juego::getNombre).toArray(String[]::new),
                        juegos.getFirst().getNombre()
                );
                if (opc==null || opc.isEmpty()) {
                    continuar = false;
                }else {
                    j = juegoDAO.obtenerTodosJuegos().stream().filter(juego -> juego.getNombre().equals(opc)).findFirst().orElse(null);
                    //con solo el nombre obtiene todos los datos de Equipo eq
                    continuar = true;
                }
            }catch (NullPointerException e){
                continuar = false;
            }
        }while (JOptionPane.showConfirmDialog(null,"Quiere continuar modificando juegos?") == 0);
        //sale de repetitiva
        if (continuar)
            opcionesModificar(j);
    }
    private void opcionesModificar(Juego j){
        String[] opc = {"Nombre","Fecha de salida"};
        try {
            String opcion = (String) JOptionPane.showInputDialog(null,
                    "Que quieres modificar",
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
                    case "Nombre" -> j.setNombre(this.validarNombre());
                    case "Fecha de fundacion" -> j.setFechaSalida(this.validarFechaSalida());
                    default -> JOptionPane.showMessageDialog(null,"No se puede modificar eso en el juego");
                }
            }
        }catch (NullPointerException e){
            System.out.println("No se aceptan valores nulos");
        }
    }
    public void eliminarJuego(){
        Juego j = new Juego();
        ArrayList<Juego> juegos = juegoDAO.obtenerTodosJuegos();
        boolean continuar = false;
        do {
            try {
                String opc= (String) JOptionPane.showInputDialog(null,
                        "Que juego quiere eliminar?",
                        "Opciones",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        juegos.stream().map(Juego::getNombre).toArray(String[]::new),
                        juegos.getFirst().getNombre()
                );
                if (opc==null || opc.isEmpty()) {
                    continuar = false;
                }else if (juegoDAO.obtenerTodosJuegos().isEmpty()){
                        JOptionPane.showMessageDialog(null,"No hay juegos para eliminar");
                }else {
                    j = juegoDAO.obtenerTodosJuegos().stream().filter(juego -> juego.getNombre().equals(opc)).findFirst().orElse(null);
                    continuar = true;
                    //con solo el nombre obtiene todos los datos de Equipo eq
                }
            }catch (NullPointerException e){
                continuar = false;
            }
        }while (JOptionPane.showConfirmDialog(null,"Quiere continuar eliminando juegos?") == 0 && juegoDAO.obtenerTodosJuegos().isEmpty());
        //sale de repetitiva con respuesta de usuario o si no hay mas juegos para eliminar
        if (continuar)
            juegoDAO.eliminarJuego(Objects.requireNonNull(j).getCodjuego());
    }
    public void verTodosJuegos(){
        try {
            ArrayList<Juego> juegos = juegoDAO.obtenerTodosJuegos();
            for (Juego j : juegos){
                Object[] options = { "OK", "CANCEL"};
                JOptionPane.showOptionDialog(null, j.toString(), "Continuar",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, options, options[0]);
                //Arreglado
            }
        }catch (NullPointerException e){
            System.out.println("No hay juegos para enseñar");
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("ERROR: " + e.getMessage());
        }
    }
    public Juego elegirElJuego(){
        Juego j = new Juego();
        ArrayList<Juego> juegos = juegoDAO.obtenerTodosJuegos();
        try {
            String opc= (String) JOptionPane.showInputDialog(null,
                    "Que Juego?",
                    "Opciones",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    juegos.stream().map(Juego::getNombre).toArray(String[]::new),
                    juegos.getFirst().getNombre()
            );
            if (opc==null || opc.isEmpty()) {
                JOptionPane.showMessageDialog(null,"El juego no puede ser nulo");
            }else {
                j = juegoDAO.obtenerTodosJuegos().stream().filter(juego -> juego.getNombre().equals(opc)).findFirst().orElse(null);
                //con solo el nombre obtiene todos los datos de Equipo eq
            }
        }catch (NullPointerException e){
            System.out.println("El juego no puede ser nulo");
        }
        return j;
    }
}
