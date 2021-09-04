//Ortiz Salazar Manuel Eduardo

import java.net.Socket;
import java.net.ServerSocket;
import java.io.DataInputStream;
import java.io.DataOutputStream;

class PI {
    static Object obj = new Object();
    static float pi = 0;

    static class Worker extends Thread {
        Socket conexion;

        Worker(Socket conexion) {
            this.conexion = conexion;
        }

        public void run() {
            
            try {
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());

                float suma = entrada.readFloat();
                System.out.println(suma);

                synchronized (obj) {
                    pi += suma;
                }
                
                salida.close();
                entrada.close();
                conexion.close();

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    public static void main(String[] args) throws Exception {
        int nodo = Integer.valueOf(args[0]);
        if (args.length != 1) {
            System.err.println("Uso:");
            System.err.println("Java PI <nodo>");
            System.exit(0);
        }

        if (nodo == 0) {
            ServerSocket servidor = new ServerSocket(40000);
            Worker[] v = new Worker[4];

            int i = 0;

            while (i < 4) {
                Socket conexion = new Socket();
                conexion = servidor.accept();

                Worker w = new Worker(conexion);
                v[i] = w;
                v[i].start();

                i++;
            }

            i = 0;
            while (i < 4) {
                v[i].join();
                i++;
            }

            System.out.println("-----------------\n" + "Nodo 0\nPI: " + pi);
            servidor.close();
        } else {

            Socket conexion = null;
            float suma = 0;
            int i = 0;

            for (;;) {

                try {
                    conexion = new Socket("localhost", 40000);

                    System.out.println("OK. Nodo: " + nodo);
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
            DataInputStream entrada = new DataInputStream(conexion.getInputStream());

            for (i = 0; i < 1000000; i++) {
                suma += 4.0 / (8 * i + 2 * (nodo - 2) + 3);
            }

            suma = nodo % 2 == 0 ? -suma : suma;
            salida.writeFloat(suma);

            entrada.close();
            salida.close();
            conexion.close();
        }
    }

}