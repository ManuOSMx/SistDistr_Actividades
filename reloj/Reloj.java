import java.net.ServerSocket;
import java.net.Socket;

class Reloj {

    static String[] hosts;
    static int[] puertos;
    static int num_nodos;
    static int nodo;

    static class Worker extends Thread {

        Socket conexion;

        public Worker(Socket conexion) {
            this.conexion = conexion;
        }

        public void run() {

            System.out.println("Inició el thread Worker");

        }
    }

    static class Servidor extends Thread {

        public void run() {

            try {
                ServerSocket servidor = new ServerSocket(puertos[nodo]);

                for (;;) {
                    Socket cliente = servidor.accept();
                    Worker w = new Worker(cliente);
                    w.start();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        nodo = Integer.parseInt(args[0]);
        num_nodos = args.length - 1;

        hosts = new String[num_nodos];
        puertos = new int[num_nodos];

        for (int i = 0; i < num_nodos; i++) {
            String[] piezas = args[i + 1].split(":");
            hosts[i] = piezas[0];
            puertos[i] = Integer.parseInt(piezas[1]);
        }

        Servidor servidor = new Servidor();
        servidor.start();
    }

}
