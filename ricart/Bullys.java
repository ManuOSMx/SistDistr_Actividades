
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Bullys {
    static ArrayList<String> hosts;
    static ArrayList<Integer> puertos;
    static int num_nodos;
    static int nodo;
    static int coordinador_actual;
   
    static void eleccion(int nodo){
        
    }
    static class Worker extends Thread{
        static Socket conexion;
        private Worker(Socket conexion) {
            this.conexion = conexion;
        }
        public void run() {
            try {
                System.out.println("Inicio el thread Worker");
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());
            
                String mensaje = entrada.readUTF();
                
                if (mensaje == "ELECCION") {
                    salida.writeUTF("OK");
                    eleccion(nodo);
                }
                if (mensaje == "COORDINADOR") {
                    coordinador_actual = entrada.readInt();
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }   
        }
    }
    
    static class Servidor extends Thread{
        public Servidor(){}
        public void run(){
            try {
                ServerSocket servidor = new ServerSocket(puertos.get(nodo));
                Socket conexion = null;
                for(;;){
                    conexion = servidor.accept();
                    Worker w = new Worker(conexion);
                    w.start();   
                }
            } catch (Exception e) {
                System.out.println("Ocurrio un error: "+ e.getMessage());
            }
        }
    }
    static String envia_mensaje_coordinador(String host, int puerto){
        try {
            Socket conexion = new Socket(host, puerto);
        } catch (Exception e) {
            return "";
        }
        return host;
    }
    public static void main(String[] args) {
        if(args.length < 2){
            System.err.println("Faltan argumentos:");
            System.err.println("java EM <nodo> [ARREGLO]<ip:puerto>");
            System.exit(-1);
        }
        nodo = Integer.valueOf(args[0]);
        num_nodos = args.length - 1;
        hosts = new ArrayList<>();
        puertos = new ArrayList<>();

        
        String[] dir;
        for (int i = 1; i < args.length; i++) {
            dir = args[i].split(":");
            hosts.add(dir[0]);
            puertos.add(Integer.valueOf(dir[1]));
        }
        
        Servidor s = new Servidor();
        s.start();
    }
   
    static String envia_mensaje_eleccion(String host, int puerto){
        try {
            Socket conexion = new Socket(host, puerto);
        } catch (Exception e) {
            return "";
        }
        return host;
    }
}