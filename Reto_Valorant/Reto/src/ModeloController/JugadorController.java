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

    //Mejorada la validacion de si se puede crear el jugador, añadido nickname, fechaNac y sueldo.
    //Añadida la validacion de maximo 6 jugadores por equipo.


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
            j.setEquipo(this.validarEquipos());
            //Si este metodo devuelve null hay que dar una opcion de modificar jugador para dar de alta en un equipo
            jugadorDAO.agregar(j);
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
                    isValid = false;
                    JOptionPane.showMessageDialog(null,"El sueldo no puede ser menor que " + SUELDO);
                }else {
                    isValid = true;
                }
            }catch (NullPointerException e){
                System.out.println("No se puede ingresar el sueldo vacio.");
            }
        }while (!isValid);
        return sueldo;
    }
    private Optional<Equipo> validarEquipos() {
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
                return Optional.empty();
            } else {
                if (validarAnadirEquipo(nombre)) {
                    return equipos.stream()
                        .filter(e -> e.getNombre().equals(nombre))
                        .findFirst();
                }else {
                    return Optional.empty();
                }
            }
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
}