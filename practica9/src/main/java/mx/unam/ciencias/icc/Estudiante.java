package mx.unam.ciencias.icc;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Clase para representar estudiantes. Un estudiante tiene nombre, número de
 * cuenta, promedio y edad. La clase implementa {@link Registro}, por lo que
 * puede representarse con una línea de texto y definir sus propiedades con una
 * línea de texto; además de determinar si sus campos cazan valores arbitrarios.
 */
public class Estudiante implements Registro<Estudiante, CampoEstudiante> {

    /* Nombre del estudiante. */
    private StringProperty nombre;
    /* Número de cuenta. */
    private IntegerProperty cuenta;
    /* Pormedio del estudiante. */
    private DoubleProperty promedio;
    /* Edad del estudiante.*/
    private IntegerProperty edad;

    /**
     * Define el estado inicial de un estudiante.
     * @param nombre el nombre del estudiante.
     * @param cuenta el número de cuenta del estudiante.
     * @param promedio el promedio del estudiante.
     * @param edad la edad del estudiante.
     */
    public Estudiante(String nombre,
                      int    cuenta,
                      double promedio,
                      int    edad) {
        this.nombre = new SimpleStringProperty(nombre);
	this.cuenta   = new SimpleIntegerProperty(cuenta);
        this.promedio = new SimpleDoubleProperty(promedio);
        this.edad     = new SimpleIntegerProperty(edad);
    }

    /**
     * Regresa el nombre del estudiante.
     * @return el nombre del estudiante.
     */
    public String getNombre() {
        return nombre.get();
    }

    /**
     * Define el nombre del estudiante.
     * @param nombre el nuevo nombre del estudiante.
     */
    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    /**
     * Regresa la propiedad del nombre.
     * @return la propiedad del nombre.
     */
    public StringProperty nombreProperty() {
        return this.nombre;
    }

    /**
     * Regresa el número de cuenta del estudiante.
     * @return el número de cuenta del estudiante.
     */
    public int getCuenta() {
        return cuenta.get();
    }

    /**
     * Define el número cuenta del estudiante.
     * @param cuenta el nuevo número de cuenta del estudiante.
     */
    public void setCuenta(int cuenta) {
        this.cuenta.set(cuenta);
    }

    /**
     * Regresa la propiedad del número de cuenta.
     * @return la propiedad del número de cuenta.
     */
    public IntegerProperty cuentaProperty() {
        return this.cuenta;
    }

    /**
     * Regresa el promedio del estudiante.
     * @return el promedio del estudiante.
     */
    public double getPromedio() {
        return promedio.get();
    }

    /**
     * Define el promedio del estudiante.
     * @param promedio el nuevo promedio del estudiante.
     */
    public void setPromedio(double promedio) {
        this.promedio.set(promedio);
    }

    /**
     * Regresa la propiedad del promedio.
     * @return la propiedad del promedio.
     */
    public DoubleProperty promedioProperty() {
        return this.promedio;
    }

    /**
     * Regresa la edad del estudiante.
     * @return la edad del estudiante.
     */
    public int getEdad() {
        return edad.get();
    }

    /**
     * Define la edad del estudiante.
     * @param edad la nueva edad del estudiante.
     */
    public void setEdad(int edad) {
        this.edad.set(edad);
    }

    /**
     * Regresa la propiedad de la edad.
     * @return la propiedad de la edad.
     */
    public IntegerProperty edadProperty() {
        return this.edad;
    }

    /**
     * Regresa una representación en cadena del estudiante.
     * @return una representación en cadena del estudiante.
     */
    @Override public String toString() {
        return String.format(
            "Nombre   : %s\n" +
            "Cuenta   : %09d\n" +
            "Promedio : %2.2f\n" +
            "Edad     : %d",
            nombre.get(), cuenta.get(), promedio.get(), edad.get());
    }

    /**
     * Nos dice si el objeto recibido es un estudiante igual al que manda llamar
     * el método.
     * @param objeto el objeto con el que el estudiante se comparará.
     * @return <code>true</code> si el objeto o es un estudiante con las mismas
     *         propiedades que el objeto que manda llamar al método,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (!(objeto instanceof Estudiante))
            return false;
        Estudiante estudiante = (Estudiante)objeto;
	if(this.getNombre().equals(estudiante.getNombre()) &&
	   this.getCuenta() == estudiante.getCuenta() &&
	   this.getPromedio() == estudiante.getPromedio() &&
	   this.getEdad() == estudiante.getEdad())
	       return true;
	return false;
    }

    /**
     * Regresa el estudiante serializado en una línea de texto. La línea de
     * texto que este método regresa debe ser aceptada por el método {@link
     * Estudiante#deserializa}.
     * @return la serialización del estudiante en una línea de texto.
     */
    @Override public String serializa() {
        return String.format("%s\t%d\t%2.2f\t%d\n",
			     nombre.get(), cuenta.get(), promedio.get(), edad.get());
    }

    /**
     * Deserializa una línea de texto en las propiedades del estudiante. La
     * serialización producida por el método {@link Estudiante#serializa} debe
     * ser aceptada por este método.
     * @param linea la línea a deserializar.
     * @throws ExcepcionLineaInvalida si la línea recibida es nula, vacía o no
     *         es una serialización válida de un estudiante.
     */
    @Override public void deserializa(String linea) {
        if (linea == null || linea.equals(""))
	    throw new ExcepcionLineaInvalida();

	String[] propiedades = linea.trim().split("\t");

	if (propiedades.length != 4)
	    throw new ExcepcionLineaInvalida();

	int cta;
	int edd;
        double prom;
	nombre.set(propiedades[0]);

	try {
            cuenta.set(Integer.valueOf(propiedades[1]));
            promedio.set(Double.valueOf(propiedades[2]));
            edad.set(Integer.valueOf(propiedades[3]));
        } catch (NumberFormatException nfe) {
            throw new ExcepcionLineaInvalida();
        }
    }

    /**
     * Actualiza los valores del estudiante con los del estudiante recibido.
     * @param estudiante el estudiante con el cual actualizar los valores.
     * @throws IllegalArgumentException si el estudiante es <code>null</code>.
     */
    public void actualiza(Estudiante estudiante) {
        if(estudiante == null)
	    throw new IllegalArgumentException();

	Estudiante est = estudiante;
	nombre.set(est.nombre.get());
	cuenta.set(est.cuenta.get());
	promedio.set(est.promedio.get());
	edad.set(est.edad.get());
    }

    private boolean cazaNombre(Object objeto){
	if(!(objeto instanceof String))
	    return false;

	String n = (String)objeto;

	if(n == "")
	    return false;

	return nombre.get().indexOf(n) != -1;
    }

    private boolean cazaCuenta(Object objeto){
	if(!(objeto instanceof Integer)) return false;
	Integer c = (Integer)objeto;
	return cuenta.get() >= c.intValue();
    }

    private boolean cazaEdad(Object objeto){
	if(!(objeto instanceof Integer)) return false;
	Integer e = (Integer)objeto;
	return edad.get() >= e.intValue();
    }

    private boolean cazaPromedio(Object objeto){
	if(!(objeto instanceof Double)) return false;
	Double p = (Double)objeto;
	return promedio.get() >= p.doubleValue();
    }

    /**
     * Nos dice si el estudiante caza el valor dado en el campo especificado.
     * @param campo el campo que hay que cazar.
     * @param valor el valor con el que debe cazar el campo del registro.
     * @return <code>true</code> si:
     *         <ul>
     *           <li><code>campo</code> es {@link CampoEstudiante#NOMBRE} y
     *              <code>valor</code> es instancia de {@link String} y es una
     *              subcadena del nombre del estudiante.</li>
     *           <li><code>campo</code> es {@link CampoEstudiante#CUENTA} y
     *              <code>valor</code> es instancia de {@link Integer} y su
     *              valor entero es menor o igual a la cuenta del
     *              estudiante.</li>
     *           <li><code>campo</code> es {@link CampoEstudiante#PROMEDIO} y
     *              <code>valor</code> es instancia de {@link Double} y su
     *              valor doble es menor o igual al promedio del
     *              estudiante.</li>
     *           <li><code>campo</code> es {@link CampoEstudiante#EDAD} y
     *              <code>valor</code> es instancia de {@link Integer} y su
     *              valor entero es menor o igual a la edad del
     *              estudiante.</li>
     *         </ul>
     *         <code>false</code> en otro caso.
     * @throws IllegalArgumentException si el campo es <code>null</code>.
     */
    @Override public boolean caza(CampoEstudiante campo, Object valor) {
        if(!(campo instanceof CampoEstudiante)) throw new IllegalArgumentException();
	CampoEstudiante camp = (CampoEstudiante)campo;
        switch(camp){
          case NOMBRE:
            return cazaNombre(valor);
          case CUENTA:
            return cazaCuenta(valor);
          case EDAD:
            return cazaEdad(valor);
          case PROMEDIO:
            return cazaPromedio(valor);
          default:
            return false;
	}
    }
}
