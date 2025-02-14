package ModeloController;

import Modelo.Equipo;
import Modelo.Jugador;
import Modelo.Rol;
import ModeloDAO.EquipoDAO;
import ModeloDAO.JugadorDAO;
import Nacionalidades.Country;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.*;

public class JugadorController {

    private static JugadorDAO jugadorDAO;
    private static EquipoDAO equipoDAO;
    ArrayList<Equipo> equipos;
    private static final int SUELDO = 1184;
    private static RolController rolController;
    public JugadorController() {
    }

    public void dataValidation(){
        this.declararVariables();
        if (this.validarCreacion()) {
            Jugador j = new Jugador();
            j.setCodJugador(this.generarCodJugador());
            j.setNombre(this.validarNomApeNik("Nombre", "Ingresa el nombre del jugador.", "^[A-ZÁÉÍÓÚÑÄËÏÖÜ][a-záéíóúñäëïöü\\s]*$"));
            j.setApellido(this.validarNomApeNik("Apellido", "Ingresa el apellido del jugador.", "^[A-ZÁÉÍÓÚÑÄËÏÖÜ][a-záéíóúñäëïöü\\s]*$"));
            j.setNacionalidad(this.validarNacionalidad());
            j.setFechaNacimiento(this.validarFechaNacimiento());

            ArrayList<Rol> roles = rolController.conseguirRoles();
            String seleccionRol = (String) JOptionPane.showInputDialog(
                    null,
                    "Selecciona una opción:",
                    "Lista Desplegable",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcionesRol,
                    opcionesRol[0]);

            j.setRol(seleccionRol);

            j.setNickname(this.validarNomApeNik("Nickname", "Ingresa el nickname del jugador.", "\\S{3,16}"));
            j.setSueldo(this.validarSueldo());
            Equipo equipo = this.validarEquipos();
            j.setEquipo(equipo);
            //Si este metodo devuelve null hay que dar una opcion de modificar jugador para dar de alta en un equipo
            jugadorDAO.agregar(j);
            if (equipo != null) {
                equipoDAO.anadirJugador(equipo, j);
            }
        }else {
            JOptionPane.showMessageDialog(null, "No se puede crear ningún jugador hasta que exista al menos un equipo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void declararVariables() {
        //rolController = new RolControlles();
        equipoDAO = new EquipoDAO();
        jugadorDAO = new JugadorDAO();
        equipos = equipoDAO.obtenerTodosLosEquipos();
    }
    private boolean validarCreacion(){
        return !equipos.isEmpty();
    }
    private int generarCodJugador(){
        Set<Integer> codExistentes = jugadorDAO.obtenerTodos().stream().map(Jugador::getCodJugador).collect(toSet());
        //recoge todos los codigos de los jugadores
        int codJugador = 0;
        while (codExistentes.contains(codJugador)) {
            codJugador++;
            //hasta que no sea mas grande el nuevo codigo, no sale del bucle
        }
        return codJugador;
    }
    private String validarNomApeNik(String dato,String msj, String patron){
        boolean isValid = false;
        Pattern pattern = Pattern.compile(patron);
        String var="";
        do {
            try {
                var = JOptionPane.showInputDialog(null,msj);
                Matcher matcher = pattern.matcher(var);

                if (matcher.matches()) {
                    isValid = true;
                }else {
                    System.out.println(dato + " no utiliza un formato valido");
                }

            }catch (NullPointerException e){
                System.out.println("No se puede ingresar el " + dato + " vacio");
            }
        }while (!isValid);
        return var;
    }
    private String validarNacionalidad(){
        boolean isValid = false;
        Pattern pattern = Pattern.compile("^[A-ZÁÉÍÓÚÜÑ][a-záéíóúüñ]+(?:-[A-ZÁÉÍÓÚÜÑ][a-záéíóúüñ]+)*(?: [A-ZÁÉÍÓÚÜÑ][a-záéíóúüñ]+(?:-[A-ZÁÉÍÓÚÜÑ][a-záéíóúüñ]+)*)*$");
        //generado por ChatGPT
        String var="";
        do {
            try {
                var= JOptionPane.showInputDialog(null,"En que pais nacio el jugador?");
                Matcher matcher = pattern.matcher(var);

                if (matcher.matches()) {
                    var = getCodigoOSI(var);
                    if (var == null) {
                        System.out.println("Nacionalidad no encontrada");
                    }else {
                        isValid = true;
                    }
                }else {
                    System.out.println("Nacionalidad no utiliza un formato valido");
                }

            }catch (NullPointerException e){
                System.out.println("No se puede ingresar la nacionalidad vacio");
            }
        }while (!isValid);
        return var;
    }
    private String getCodigoOSI(String nombrePais) {
        for (Country pais : Country.values()) {
            if (pais.getName().equalsIgnoreCase(nombrePais)) {
                return pais.getThreeDigitsCode();
                // asi todos los jugadores tienen nacionalidad OSI (esp,fra,cro...)
            }
        }
        return null; //en caso de no encontrar devuelve null
    }
    private LocalDate validarFechaNacimiento(){
        boolean isValid = false;
        LocalDate fechaNacimiento = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        do {
            try {
                String fecha = JOptionPane.showInputDialog(null,"Ingresa la fecha de nacimiento");
                fechaNacimiento = LocalDate.parse(fecha, formatter);
                isValid = true;
            }catch (DateTimeParseException e){
                System.out.println("Ingresa una fecha en el formato adecuado.");
            }catch (NullPointerException e){
                System.out.println("No se puede ingresar la fecha vacía.");
            }
        }while (!isValid);
        return fechaNacimiento;
    }
    private int validarSueldo(){
        boolean isValid = false;
        int sueldo = 0;
        do {
            try {
                sueldo = Integer.parseInt(JOptionPane.showInputDialog(null,"Ingresa el sueldo del jugador"));
                if (sueldo < SUELDO) {
                    JOptionPane.showMessageDialog(null,"El sueldo no puede ser menor que " + SUELDO);
                }else {
                    isValid = true;
                }
            }catch (NullPointerException e){
                System.out.println("No se puede ingresar el sueldo vacio.");
            }catch (NumberFormatException e){
                JOptionPane.showMessageDialog(null,"Numero no aceptado " + e.getMessage());
            }
        }while (!isValid);
        return sueldo;
    }
    private Equipo validarEquipos() {
        String nombre = (String) JOptionPane.showInputDialog(
                null,
                "Selecciona el equipo al que pertenece el Jugador",
                "Opciones",
                JOptionPane.PLAIN_MESSAGE,
                null,
                equipos.stream().map(Equipo::getNombre).toArray(String[]::new),
                equipos.getFirst().getNombre()
        );
            if (nombre == null) {
                JOptionPane.showMessageDialog(null, "No se seleccionó ningún equipo");
            } else {

                if (validarAnadirEquipo(nombre)) {
                    return equipos.stream().filter(e -> e.getNombre().equals(nombre)).findFirst().orElse(null);
                }
            }
        return null;
    }
    private boolean validarAnadirEquipo(String nombre) {
        Equipo equipoEncontrado = null;
        for (Equipo equipo : equipos) {
            if (equipo.getNombre().equals(nombre)) {
                equipoEncontrado = equipo;
                break;
            }
        }
        if (equipoEncontrado != null) {
            if (equipoEncontrado.getListaJugadores().size() <= 6)
                return true;
            else{
                JOptionPane.showMessageDialog(null,"El equipo ya tiene 6 jugadores, no puede tener mas jugadores.");
                return false;
            }
        }else {
            JOptionPane.showMessageDialog(null,"El equipo no existe.");
            return false;
        }
    }
    public void modificarJugador(){
        Jugador j = new Jugador();
        ArrayList<Jugador> jugadores = jugadorDAO.obtenerTodos();
        do {
            try {
                String opc= (String) JOptionPane.showInputDialog(null,
                        "Que jugador?",
                        "Opciones",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        jugadores.stream().map(Jugador::getNombre).toArray(String[]::new),
                        jugadores.getFirst().getNombre()
                );
                if (opc==null || opc.isEmpty()) {
                    JOptionPane.showMessageDialog(null,"El jugador no puede ser nulo");
                }else {
                    j = jugadorDAO.obtenerTodos().stream().filter(jugador -> jugador.getNombre().equals(opc)).findFirst().orElse(null);
                }
            }catch (NullPointerException e){
                System.out.println("el jugador no puede ser nulo");
            }
        }while (JOptionPane.showConfirmDialog(null,"Quiere continuar modificando jugadores?")==1);
            opcionesModificar(j);
    }
    private void opcionesModificar(Jugador j){
        String[] opc = {"Nombre","Apellido","Nacionalidad","Fecha de nacimiento","Nickname","Sueldo","Equipo"};
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
                JOptionPane.showMessageDialog(null,"No se puede una opcion");
            }else {
                switch (opcion){
                    case "Nombre" -> j.setNombre(this.validarNomApeNik("Nombre", "Ingresa el nombre del jugador.", "^[A-ZÁÉÍÓÚÑÄËÏÖÜ][a-záéíóúñäëïöü\\s]*$"));
                    case "Apellido" -> j.setApellido(this.validarNomApeNik("Apellido", "Ingresa el apellido del jugador.", "^[A-ZÁÉÍÓÚÑÄËÏÖÜ][a-záéíóúñäëïöü\\s]*$"));
                    case "Nacionalidad" ->  j.setNacionalidad(this.validarNacionalidad());
                    case "Fecha de nacimiento" -> j.setFechaNacimiento(this.validarFechaNacimiento());
                    case "Nickname" -> j.setNickname(this.validarNomApeNik("Nickname", "Ingresa el nickname del jugador.", "\\S{3,16}"));
                    case "Sueldo" -> j.setSueldo(this.validarSueldo());
                    case "Equipo" -> j.setEquipo(this.validarEquipos());
                    default -> JOptionPane.showMessageDialog(null,"No se puede modificar eso en el jugador");
                }
            }

        }catch (NullPointerException e){
            System.out.println("No se aceptan valores nulos");
        }
    }
    public void eliminarJugador(){
        Jugador j = new Jugador();
        ArrayList<Jugador> jugadores = jugadorDAO.obtenerTodos();
        do {
            try {
                String opc= (String) JOptionPane.showInputDialog(null,
                        "Que jugador quiere eliminar?",
                        "Opciones",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        jugadores.stream().map(Jugador::getNombre).toArray(String[]::new),
                        jugadores.getFirst().getNombre()
                );
                if (opc==null || opc.isEmpty()) {
                    JOptionPane.showMessageDialog(null,"El jugador no puede ser nulo");
                }else {
                    j = jugadorDAO.obtenerTodos().stream().filter(jugador -> jugador.getNombre().equals(opc)).findFirst().orElse(null);
                }
            }catch (NullPointerException e){
                System.out.println("el jugador no puede ser nulo");
            }
        }while (JOptionPane.showConfirmDialog(null,"Quieere continuar eliminando juadores?")==0);
        jugadorDAO.eliminar(Objects.requireNonNull(j).getCodJugador());
    }
    public void verTodosJugadores(){
        try {
            ArrayList<Jugador> jugadores
             = jugadorDAO.obtenerTodos();
            for (Jugador j : jugadores){
                Object[] options = { "OK", "CANCEL"};
                JOptionPane.showOptionDialog(null, j.toString(), "Continuar",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, options, options[0]);
            }
        }catch (NullPointerException e){
            System.out.println("No hay jugadores para enseñar");
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("ERROR: " + e.getMessage());
        }
    }
    public void verPorNombre() {
        String[] nombres = jugadorDAO.obtenerTodos().stream().map(Jugador::getNombre).toArray(String[]::new);
        do {
            String equipoElegido = (String) JOptionPane.showInputDialog(null,
                    "Elija el nombre del jugador que quiere ver",
                    "Opciones",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    nombres,
                    nombres[0]
            );
            if (equipoElegido == null || equipoElegido.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null,"El nombre no puede ser nulo");
            }else {

                Jugador j = jugadorDAO.obtenerTodos().stream().filter(jugador -> jugador.getNombre().equals(equipoElegido)).findFirst().orElse(null);

                JOptionPane.showMessageDialog(null, Objects.requireNonNull(j).toString());
            }
        }while (JOptionPane.showConfirmDialog(null,"Quiere continuar viendo jugadores?") == 0);
    }
    private void mostrarJugadoresRepetidos(List<Jugador> nombresIguales) {

        String[] opciones = nombresIguales.stream()
                .map(j -> j.getNombre() + " " + j.getApellido())
                .toArray(String[]::new);

        String eleccion = seleccionarJugadorPorApellido(opciones);

        if (eleccion != null && !eleccion.trim().isEmpty()) {
            seleccionarJugador(nombresIguales, eleccion);
        } else {
            JOptionPane.showMessageDialog(null, "El jugador no puede ser nulo.");
        }
    }
    private String seleccionarJugadorPorApellido(String[] opciones) {
        return (String) JOptionPane.showInputDialog(
                null,
                "Parece que hay más de un jugador con ese nombre.",
                "Elige a uno",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opciones,
                opciones[0]
        );
    }
    private void seleccionarJugador(List<Jugador> nombresIguales, String eleccion) {

        Jugador jugador = nombresIguales.stream()
                .filter(j -> (j.getNombre() + " " + j.getApellido()).equals(eleccion))
                .findFirst().orElse(null);

        JOptionPane.showMessageDialog(null, Objects.requireNonNull(jugador).toString());
    }
}
