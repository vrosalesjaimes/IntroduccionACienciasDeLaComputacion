package mx.unam.ciencias.icc.red;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import mx.unam.ciencias.icc.BaseDeDatos;
import mx.unam.ciencias.icc.Lista;
import mx.unam.ciencias.icc.Registro;

/**
 * Clase abstracta para servidores de bases de datos genéricas.
 */
public abstract class ServidorBaseDeDatos<R extends Registro<R, ?>> {

    /* La base de datos. */
    private BaseDeDatos<R, ?> bdd;
    /* La ruta donde cargar/guardar la base de datos. */
    private String ruta;
    /* El servidor de enchufes. */
    private ServerSocket servidor;
    /* El puerto. */
    private int puerto;
    /* Lista con las conexiones. */
    private Lista<Conexion<R>> conexiones;
    /* Bandera de continuación. */
    private boolean continuaEjecucion;
    /* Escuchas del servidor. */
    private Lista<EscuchaServidor> escuchas;

    /**
     * Crea un nuevo servidor usando la ruta recibida para poblar la base de
     * datos.
     * @param puerto el puerto dónde escuchar por conexiones.
     * @param ruta la ruta en el disco del cual cargar/guardar la base de
     *             datos. Puede ser <code>null</code>, en cuyo caso se usará el
     *             nombre por omisión <code>base-de-datos.bd</code>.
     * @throws IOException si ocurre un error de entrada o salida.
     */
    public ServidorBaseDeDatos(int puerto, String ruta)
        throws IOException {
        this.bdd = creaBaseDeDatos();
        this.servidor = new ServerSocket(puerto);
        this.puerto = puerto;
	
	if(ruta == null)
	    this.ruta = "base-de-datos.bd";
	else
	    this.ruta = ruta;

	this.conexiones = new Lista<Conexion<R>>();
        this.continuaEjecucion = true;
        this.escuchas = new Lista<EscuchaServidor>();

        leeBaseDeDatos(this.ruta);
    }

    /**
     * Comienza a escuchar por conexiones de clientes.
     */
    public void sirve() {
        while (continuaEjecucion) {
            try {
                Socket socket = servidor.accept();
                Conexion<R> conexion = new Conexion<R>(bdd, socket);

                notificaEscuchas("Conexión recibida de: %s.",socket.getInetAddress().getCanonicalHostName());
		
                notificaEscuchas("Serie de conexión: %d.",conexion.getSerie());

                conexion.agregaEscucha((con, men) -> procesaNuevoMensaje(con, men));

                synchronized (conexiones) {
                    conexiones.agregaFinal(conexion);
                }

                new Thread(() -> conexion.recibeMensajes()).start();
            } catch (IOException ioe) {
                notificaEscuchas("Error al aceptar conexión.");
            }
        }
    }

    /**
     * Agrega un escucha de servidor.
     * @param escucha el escucha a agregar.
     */
    public void agregaEscucha(EscuchaServidor escucha) {
        escuchas.agregaFinal(escucha);
    }

    /**
     * Limpia todos los escuchas del servidor.
     */
    public void limpiaEscuchas() {
        escuchas.limpia();
    }

    /**
     * Agrega a la base de datos los registros guardados en el archivo.
     * @param archivo el archivo de donde leer los registros.
     */
    private void leeBaseDeDatos(String archivo) {
        try {
            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(
                                        new FileInputStream(archivo)));
            bdd.carga(in);
            in.close();
        } catch (IOException ioe) {
            notificaEscuchas("Error al leer la base de datos de %s.", ruta);
        }
    }

    /**
     * Guarda la base de datos al disco duro, en el debido archivo.
     */
    private void escribeBaseDeDatos() {
        notificaEscuchas("Guardando base de datos en %s.", ruta);

        try {
            BufferedWriter out = new BufferedWriter(
                                    new OutputStreamWriter(
                                        new FileOutputStream(ruta)));
            bdd.guarda(out);
            out.close();
        } catch (IOException ioe) {
            notificaEscuchas("Error al guardar la base de datos en %s.",
                    ruta);
        }

        notificaEscuchas("Base de datos guardada.");
    }

    /**
     * Si la conexión se encuentra activa, procesa el mensaje recibido.
     * @param conexion la conexión que realizó una solicitud.
     * @param mensaje el mensaje que se recibió.
     */
    private void procesaNuevoMensaje(Conexion<R> conexion, Mensaje mensaje) {
        if (conexion.isActiva())
            switch (mensaje) {
                case BASE_DE_DATOS:
                    procesaBaseDeDatos(conexion);
                    break;
                case REGISTRO_AGREGADO:
                    procesaRegistroAgregado(conexion);
                    break;
                case REGISTRO_ELIMINADO:
                    procesaRegistroEliminado(conexion);
                    break;
                case REGISTRO_MODIFICADO:
                    procesaRegistroModificado(conexion);
                    break;
                case DESCONECTAR:
                    procesaDesconectar(conexion);
                    break;
                case DETENER_SERVICIO:
                    procesaDetenerServicio();
                    break;
                case ECO:
                    procesaEco(conexion);
                    break;
                case INVALIDO:
                    procesaInvalido(conexion);
                    break;
            }
    }

    /**
     * Procesa la solicitud de la base de datos, enviando la base de datos a
     * través de la conexión.
     * @param conexion la conexión que realizó la solicitud.
     */
    private void procesaBaseDeDatos(Conexion<R> conexion) {
        notificaEscuchas("Base de datos pedida por %d.", conexion.getSerie());

        try {
            conexion.enviaMensaje(Mensaje.BASE_DE_DATOS);
            conexion.enviaBaseDeDatos();
        } catch (IOException ioe) {
            notificaEscuchas("Error al enviar la base de datos a la conexión %d.",
                    conexion.getSerie());
        }
    }

    /**
     * Procesa la solicitud de agregar registro, agregando al registro a la
     * base de datos, y notificando a todas las conexiones del cambio.
     * Los cambios se deben reflejar en el archivo del disco duro.
     * @param conexion la conexión que agregó el registro.
     */
    private void procesaRegistroAgregado(Conexion<R> conexion) {
        try {
            R registro = conexion.recibeRegistro();
            synchronized (bdd) {
                bdd.agregaRegistro(registro);
            }

            notificaEscuchas("Registro agregado por %d.", conexion.getSerie());

            for (Conexion<R> con : conexiones) {
                if (con == conexion)
                    continue;

               con.enviaMensaje(Mensaje.REGISTRO_AGREGADO);
               con.enviaRegistro(registro);
            }
        } catch (IOException ioe) {
            notificaEscuchas("Error al agregar registro por la conexión %d.",
                    conexion.getSerie());
        }
        escribeBaseDeDatos();
    }

    /**
     * Procesa la solicitud de eliminar un registro, eliminándo el registro de
     * la base de datos y notificando a las conexiones del cambio.
     * Los cambios se deben reflejar en el archivo del disco duro.
     * @param conexion la conexión que eliminó el registro.
     */
    private void procesaRegistroEliminado(Conexion<R> conexion) {
        try {
            R registro = conexion.recibeRegistro();
            synchronized (bdd) {
                bdd.eliminaRegistro(registro);
            }

            notificaEscuchas("Registro eliminado por %d.", conexion.getSerie());

            for (Conexion<R> con : conexiones) {
                if (con == conexion)
                    continue;

               con.enviaMensaje(Mensaje.REGISTRO_ELIMINADO);
               con.enviaRegistro(registro);
            }
        } catch (IOException ioe) {
            notificaEscuchas("Error al eliminar registro por la conexión %d.",
                    conexion.getSerie());
        }
        escribeBaseDeDatos();
    }

    /**
     * Procesa la solicitud de modificar un registro, modificando el registro
     * y notificando a las conexiones del cambio.
     * Los cambios se deben reflejar en el archivo del disco duro.
     * @param conexion la conexión que modificó el registro.
     */
    private void procesaRegistroModificado(Conexion<R> conexion) {
        try {
            R registro1 = conexion.recibeRegistro();
            R registro2 = conexion.recibeRegistro();
            synchronized (bdd) {
                bdd.modificaRegistro(registro1, registro2);
            }

            for (Conexion<R> con : conexiones) {
                if (con == conexion)
                    continue;

               con.enviaMensaje(Mensaje.REGISTRO_MODIFICADO);
               con.enviaRegistro(registro1);
               con.enviaRegistro(registro2);
            }
        } catch (IOException ioe) {
            notificaEscuchas("Error al modificar registro por la conexión %d.",
                    conexion.getSerie());
        }
        escribeBaseDeDatos();
    }

    /**
     * Desconecta a un cliente.
     * @param conexion la conexión que solicitó ser desconectada.
     */
    private void procesaDesconectar(Conexion<R> conexion) {
        desconectaConexion(conexion);
    }

    /**
     * Detiene el servidor, terminando su ejecución.
     */
    private void procesaDetenerServicio() {
        continuaEjecucion = false;

        for (Conexion<R> con : conexiones)
            desconectaConexion(con);

        try {
            servidor.close();
        } catch (IOException ioe) {
            notificaEscuchas("Error al detener el servidor.");
        }
    }

    /**
     * Procesa la solicitud de eco, regresando eco.
     * @param conexion la conexión que realizó la solicitud.
     */
    private void procesaEco(Conexion<R> conexion) {
        notificaEscuchas("Solicitud de eco de %d.", conexion.getSerie());

        try {
            conexion.enviaMensaje(Mensaje.ECO);
        } catch (IOException ioe) {
            notificaEscuchas("Error al enviar ECO a la conexión %d.",
                    conexion.getSerie());
        }
    }

    /**
     * Procesa un mensaje inválido desconectando al cliente que lo mandó.
     * @param conexion la conexión que mandó el mensaje.
     */
    private void procesaInvalido(Conexion<R> conexion) {
        notificaEscuchas("Desconectando la conexión %d: Mensaje inválido.",
                conexion.getSerie());
        desconectaConexion(conexion);
    }

    /**
     * Notifica a los escuchas de alguna acción que se realizó.
     * @param formato la cadena con el formato del mensaje.
     * @param argumentos los argumentos con los que se modificará la cadena.
     */
    private void notificaEscuchas(String formato, Object ... argumentos) {
        for (EscuchaServidor escucha : escuchas)
            escucha.procesaMensaje(formato, argumentos);
    }

    /**
     * Desconecta a un cliente.
     * @param conexion la conexión a eliminar.
     */
    private void desconectaConexion(Conexion<R> conexion) {
        conexion.desconecta();
        synchronized (conexiones) {
            conexiones.elimina(conexion);
        }
        notificaEscuchas("La conexión %d ha sido desconectada.",
                conexion.getSerie());
    }
    
    
    /**
     * Crea la base de datos concreta.
     * @return la base de datos concreta.
     */
    public abstract BaseDeDatos<R, ?> creaBaseDeDatos();
}
