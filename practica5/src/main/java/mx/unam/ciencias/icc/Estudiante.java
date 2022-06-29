package mx.unam.ciencias.icc;

/**
 * Clase para representar estudiantes. Un estudiante tiene nombre, número de
 * cuenta, promedio y edad.
 */
public class Estudiante implements Registro {

    /* Nombre del estudiante. */
    private String nombre;
    /* Número de cuenta. */
    private int cuenta;
    /* Pormedio del estudiante. */
    private double promedio;
    /* Edad del estudiante.*/
    private int edad;

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
        this.nombre = nombre;
        this.cuenta = cuenta;
  	this.promedio = promedio;
 	this.edad = edad;
    }

    /**
     * Regresa el nombre del estudiante.
     * @return el nombre del estudiante.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Define el nombre del estudiante.
     * @param nombre el nuevo nombre del estudiante.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Regresa el número de cuenta del estudiante.
     * @return el número de cuenta del estudiante.
     */
    public int getCuenta() {
        return cuenta;
    }

    /**
     * Define el número cuenta del estudiante.
     * @param cuenta el nuevo número de cuenta del estudiante.
     */
    public void setCuenta(int cuenta) {
        this.cuenta = cuenta;
    }

    /**
     * Regresa el promedio del estudiante.
     * @return el promedio del estudiante.
     */
    public double getPromedio() {
        return promedio;
    }

    /**
     * Define el promedio del estudiante.
     * @param promedio el nuevo promedio del estudiante.
     */
    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    /**
     * Regresa la edad del estudiante.
     * @return la edad del estudiante.
     */
    public int getEdad() {
        return edad;
    }

    /**
     * Define la edad del estudiante.
     * @param edad la nueva edad del estudiante.
     */
    public void setEdad(int edad) {
        this.edad = edad;
    }

    /**
     * Regresa una representación en cadena del estudiante.
     * @return una representación en cadena del estudiante.
     */
    @Override public String toString() {
        String est = String.format("Nombre   : %s\n" +
                                   "Cuenta   : %09d\n" +
                                   "Promedio : %2.2f\n" +
                                   "Edad     : %d",
                                    nombre, cuenta, promedio, edad);
	return est;
    }

    /**
     * Nos dice si el objeto recibido es un estudiante igual al que manda llamar
     * el método.
     * @param objeto el objeto con el que el estudiante se comparará.
     * @return <code>true</code> si el objeto recibido es un estudiante con las
     *         mismas propiedades que el objeto que manda llamar al método,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (!(objeto instanceof Estudiante))
            return false;
        Estudiante estudiante =	(Estudiante)objeto;

	if(objeto == null)
	    return false;
	else if(this.toString().equals(objeto.toString()))
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
        String linea = String.format("%s\t%d\t%2.2f\t%d\n",
                                     nombre, cuenta, promedio, edad);
	return linea;
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
	nombre = propiedades[0];

	try {
            cuenta  = Integer.valueOf(propiedades[1]);
            promedio = Double.valueOf(propiedades[2]);
            edad = Integer.valueOf(propiedades[3]);
        } catch (NumberFormatException nfe) {
            throw new ExcepcionLineaInvalida();
        }
    }


    /**
     * Actualiza los valores del estudiante con los del registro recibido.
     * @param registro el registro con el cual actualizar los valores.
     * @throws IllegalArgumentException si el registro no es instancia de {@link
     *         Estudiante}.
     */
    @Override public void actualiza(Registro registro) {
	if(!(registro instanceof Estudiante)|| registro == null)
	    throw new IllegalArgumentException();

	Estudiante est = (Estudiante)registro;
	nombre = est.nombre;
	cuenta = est.cuenta;
	promedio = est.promedio;
	edad = est.edad;
    }

    private boolean cazaNombre(Object objeto){
	if(!(objeto instanceof String))
	    return false;

	String n = (String)objeto;

	if(n == "")
	    return false;

	return nombre.indexOf(n) != -1;
    }

    private boolean cazaCuenta(Object objeto){
	if(!(objeto instanceof Integer))
	    return false;

	Integer c = (Integer)objeto;

	return cuenta >= c.intValue();
    }

    private boolean cazaEdad(Object objeto){
	if(!(objeto instanceof Integer))
	    return false;

	Integer e = (Integer)objeto;

	return edad >= e.intValue();
    }

    private boolean cazaPromedio(Object objeto){
	if(!(objeto instanceof Double))
	    return false;

	Double p = (Double)objeto;

	return promedio >= p.doubleValue();
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
     * @throws IllegalArgumentException si el campo no es instancia de {@link
     *         CampoEstudiante}.
     */
    @Override public boolean caza(Enum campo, Object valor) {
        if(!(campo instanceof CampoEstudiante))
	    throw new IllegalArgumentException();

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
