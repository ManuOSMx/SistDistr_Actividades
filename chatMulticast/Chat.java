/**
 * Chat
 * Ortiz Salazar Manuel Eduardo
 * 
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;


public class Chat {
    
    static Object obj = new Object();

    static void envia_mensaje_multicast(byte[] buffer, String ip, int puerto) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        socket.send(new DatagramPacket(buffer, buffer.length, InetAddress.getByName(ip), puerto));
        socket.close();
    }

    static byte[] recibe_mensaje_multicast(MulticastSocket socket, int longitud_mensaje) throws IOException {
        byte[] buffer = new byte[longitud_mensaje];
        DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
        socket.receive(paquete);
        return paquete.getData();
    }

    static class Worker extends Thread {

        public void run() {
            try {
                
                MulticastSocket socket = new MulticastSocket(40000);
                InetSocketAddress grupo = new InetSocketAddress(InetAddress.getByName("230.0.0.0"), 40000);
                NetworkInterface netInter = NetworkInterface.getByName("em1");
                socket.joinGroup(grupo, netInter);
                
                
                for (;;) {
                    synchronized (obj) {
                        byte[] mensaje = recibe_mensaje_multicast(socket, 50);
                        System.out.print("\n" + new String(mensaje, "IBM850"));
                    }
                }
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Faltan argumentos:");
            System.err.println("java Chat <nombre>:");
            System.exit(-1);
        }

        new Worker().start();

        String nombre = args[0];        
        Scanner keyboard = new Scanner(System.in, "IBM850");
        String mensaje = "";

        for (;;) {
            System.out.print("Ingrese el mensaje a enviar:");
            mensaje = nombre + ":" + keyboard.nextLine() + "\n";
            envia_mensaje_multicast(mensaje.getBytes("IBM850"), "230.0.0.0", 40000);
        }
    }
}