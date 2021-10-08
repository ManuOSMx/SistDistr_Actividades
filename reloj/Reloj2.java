import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

class Reloj2 {

    static String[] hosts;
    static int[] puertos;
    static int num_nodos;
    static int nodo;
    static long reloj_logico;

    static Object obj = new Object();

    static class Worker extends Thread {

        Socket conexion;

        public Worker(Socket conexion) {
            this.conexion = conexion;
        }

        public void run() {

            long tiempo_recibido;

            try {
                DataInputStream dis = new DataInputStream(conexion.getInputStream());

                tiempo_recibido = dis.readLong();

                synchronized (obj) {
                    if (tiempo_recibido > reloj_logico) {

                        reloj_logico = tiempo_recibido + 1;
                    }
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

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
                    w.join();

                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

        }
    }

    static class Reloj extends Thread {

        public void run() {
            for (;;) {

                synchronized (obj) {
                    System.out.println(reloj_logico);
                    switch (nodo) {
                        case 0:
                            reloj_logico += 4;
                            break;
                        case 1:
                            reloj_logico += 5;
                            break;
                        case 2:
                            reloj_logico += 6;
                            break;
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }

    static void envia_mensaje(long tiempo_logico, String host, int puerto) throws Exception {
        Socket cliente = null;

        for (;;) {
            try {
                cliente = new Socket(host, puerto);

                break;

            } catch (Exception e) {
                Thread.sleep(100);
            }
        }

        DataOutputStream dos = new DataOutputStream(cliente.getOutputStream());
        dos.writeLong(tiempo_logico);

        dos.close();
        cliente.close();
    }

    public static void main(String[] args) throws Exception {

        nodo = Integer.parseInt(args[0]);
        System.out.println("NODO: " + nodo);
        num_nodos = args.length - 1;

        hosts = new String[num_nodos];
        puertos = new int[num_nodos];

        reloj_logico = 0;

        for (int i = 0; i < num_nodos; i++) {
            String[] piezas = args[i + 1].split(":");
            hosts[i] = piezas[0];
            puertos[i] = Integer.parseInt(piezas[1]);

            System.out.println("Host: " + hosts[i] + " Puerto: " + puertos[i]);
        }

        Servidor servidor = new Servidor();
        servidor.start();

        for (int i = 0; i < num_nodos; i++) {
            if (i != nodo)
                envia_mensaje(-1, hosts[i], puertos[i]);
        }

        Reloj reloj = new Reloj();
        reloj.start();
        servidor.join();

    }

}