import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class Token {
  static DataInputStream entrada;
  static DataOutputStream salida;
  static boolean inicio = true;
  static String ip;
  static int nodo;
  static long token;

  static class Worker extends Thread {
    public void run() {
      try {
        SSLServerSocketFactory socket_factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        ServerSocket socket_servidor = socket_factory.createServerSocket(20000 + nodo);
        Socket conexion = socket_servidor.accept();
        entrada = new DataInputStream(conexion.getInputStream());
      } catch (Exception e) {
        System.err.println("Ocurrió un error");
      }
    }
  }

  public static void main(String[] args) throws Exception {

    if (args.length != 2) {
      System.err.println("Se debe pasar como parametros el numero del nodo y la IP del siguiente nodo en el anillo");
      System.exit(1);
    }

    nodo = Integer.valueOf(args[0]);
    ip = args[1];

    // Algoritmo 2
    Worker w = new Worker();
    w.start();
    SSLSocketFactory cliente = (SSLSocketFactory) SSLSocketFactory.getDefault();
    Socket conexion = null;
    for (;;) {
      try {
        conexion = cliente.createSocket(ip, 20000 + (nodo + 1) % 4);
        break;
      } catch (Exception e) {
        System.out.println("Conectando...");
        Thread.sleep(500);
      }
    }
    salida = new DataOutputStream(conexion.getOutputStream());
    w.join();
    for (;;) {
      if (nodo == 0) {
        if (inicio == true) {
          inicio = false;
          token = 1;
        } else {
          token = entrada.readLong();
          token++;
          System.out.println("Nodo: " + nodo + "\tToken: " + token);
        }
      } else {
        token = entrada.readLong();
        token++;
        System.out.println("Nodo: " + nodo + "\tToken: " + token);
      }
      if (nodo == 0 && token >= 1000) {
        break;
      }
      salida.writeLong(token);
    }
  }
}