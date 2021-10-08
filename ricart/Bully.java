import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

enum Estado {
    Normal,
    EsperandoRecurso,
    AdquirirRecurso;
}

class Bully {
    static String[] hosts;
    static int[] puertos;
    static int num_nodos;
    static int nodo;

    static long reloj_logico;
    static Object lock = new Object();

    static LinkedList<Integer> cola = new LinkedList<Integer>();


    static int num_ok_recibidos;
    static long tiempo_logico_enviado;
    static Estado estado = Estado.Normal;

    static class Servidor extends Thread {
        public void run() {
            try {
                ServerSocket servidor = new ServerSocket(puertos[nodo]);
            } catch (Exception e) {
                //TODO: handle exception
            }
        }
    }
    static void envia_helo(String host, int puerto) throws Exception {
        Socket conexion;
        for(;;){
            try {
                conexion = new Socket(host,puerto);
                break;
            } catch (Exception e) {
                Thread.sleep(100);
            }
        }
        try {
            DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
            salida.writeUTF("HELO");
        } finally {
            conexion.close();
        }
    }
    static void envia_peticion(int id_recurso, int nodo, long tiempo_logico, String host, int puerto) throws Exception {
        Socket conexion = new Socket(host, puerto);
        try {
            DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
            salida.writeUTF("RQS");
            salida.writeInt(id_recurso);
            salida.writeInt(nodo);
            salida.writeLong(tiempo_logico);
        } finally {
            conexion.close();
        }
    }
    static void envia_ok(long tiempo_logico, String host, int puerto)throws Exception {
        Socket conexion = new Socket(host, puerto);

        try{
            DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
            salida.writeUTF("OK");
            salida.writeLong(tiempo_logico);
        } finally {
            conexion.close();
        }
    }
    static void bloquea() throws Exception {
        System.out.println("Bloquea");
        estado = Estado.EsperandoRecurso;

        num_ok_recibidos = 0;
        synchronized(lock){
            tiempo_logico_enviado = reloj_logico;
        }
        for(int i = 0; i < num_nodos; i++){

        }
    }
}