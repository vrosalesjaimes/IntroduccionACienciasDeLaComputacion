package mx.unam.ciencias.icc.fx;

import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

/**
 * Clase abstracta para controladores del contenido de diálogo con formas con
 * datos de estudiantes que se aceptan o rechazan.
 */
public abstract class ControladorFormaEstudiante {

    /** El botón para aceptar. */
    @FXML protected Button botonAceptar;

    /** La ventana del diálogo. */
    protected Stage escenario;
    /** Si el usuario aceptó la forma. */
    protected boolean aceptado;

    /** El valor del nombre. */
    protected String nombre;
    /** El valor del número de cuenta. */
    protected int cuenta;
    /** El valor del promedio. */
    protected double promedio;
    /** El valor de la edad. */
    protected int edad;

    /**
     * Define el escenario del diálogo.
     * @param escenario el nuevo escenario del diálogo.
     */
    public void setEscenario(Stage escenario) {
        this.escenario = escenario;
        Scene escena = escenario.getScene();
        KeyCodeCombination combinacion;
        combinacion = new KeyCodeCombination(KeyCode.ENTER,
                                             KeyCombination.CONTROL_DOWN);
        ObservableMap<KeyCombination, Runnable> accs = escena.getAccelerators();
        accs.put(combinacion, () -> botonAceptar.fire());
    }

    /**
     * Manejador para cuando se activa el botón cancelar.
     * @param evento el evento que generó la acción.
     */
    @FXML protected void cancelar(ActionEvent evento) {
        aceptado = false;
        escenario.close();
    }

    /**
     * Nos dice si el usuario activó el botón de aceptar.
     * @return <code>true</code> si el usuario activó el botón de aceptar,
     *         <code>false</code> en otro caso.
     */
    public boolean isAceptado() {
        return aceptado;
    }

    /**
     * Define el foco incial del diálogo.
     */
    public abstract void defineFoco();

    /**
     * Verifica que el nombre sea válido.
     * @param nombre el nombre a verificar.
     * @return <code>true</code> si el nombre es válido; <code>false</code> en
     *         otro caso.
     */
    protected boolean verificaNombre(String nombre) {
        if (nombre == null || nombre.isEmpty())
            return false;

        return true;
    }

    /**
     * Verifica que el número de cuenta sea válido.
     * @param cuenta el número de cuenta a verificar.
     * @return <code>true</code> si el número de cuenta es válido;
     *         <code>false</code> en otro caso.
     */
    protected boolean verificaCuenta(String cuenta) {
        if (cuenta == null || cuenta.isEmpty())
            return false;

	int c;

	try {
            c = Integer.parseInt(cuenta);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return c >= 10000000 && c < 999999999;
    }

    /**
     * Verifica que el promedio sea válido.
     * @param promedio el promedio a verificar.
     * @return <code>true</code> si el promedio es válido; <code>false</code> en
     *         otro caso.
     */
    protected boolean verificaPromedio(String promedio) {
        if (promedio == null || promedio.isEmpty())
            return false;

	double p;
	
	try {
            p = Double.parseDouble(promedio);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return p >= 0.0 && p <= 10.0;
    }

    /**
     * Verifica que la edad sea válida.
     * @param edad la edad a verificar.
     * @return <code>true</code> si la edad es válida; <code>false</code> en
     *         otro caso.
     */
    protected boolean verificaEdad(String edad) {
	if (edad == null || edad.isEmpty())
            return false;

	int e;
	
        try {
            e = Integer.parseInt(edad);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return e >= 13 && e <= 99;
    }
}
