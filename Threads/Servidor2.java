import java.net.ServerSocket;
import java.net.Socket;

public class Servidor2 {
    static class Worker extends Thread {
        Socket conexion;
        Worker(Socket conexion){
            this.conexion = conexion;
        }
        public void run(){
        }
    }
    public static void main(String[] args) throws Exception{
        ServerSocket servidor = new ServerSocket(50000);
        for(;;){
            Socket conexion = servidor.accept();
            Worker w = new Worker(conexion);
            w.start();
        }
    }
}