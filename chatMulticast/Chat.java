/**
 * Chat
 * Ortiz Salazar Manuel Eduardo
 * 
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;


public class Chat {

    static Object obj = new Object();
    static void envia_mensaje_multicast(byte[] buffer,String ip,int puerto) throws IOException
    {
      DatagramSocket socket = new DatagramSocket();
      socket.send(new DatagramPacket(buffer,buffer.length,InetAddress.getByName(ip),puerto));
      socket.close();
    }
    
    static byte[] recibe_mensaje_multicast(MulticastSocket socket,int longitud_mensaje) throws IOException
    {
      byte[] buffer = new byte[longitud_mensaje];
      DatagramPacket paquete = new DatagramPacket(buffer,buffer.length);
      socket.receive(paquete);
      return paquete.getData();
    }

   @Deprecated
    static class Worker extends Thread{
        public Worker(){}
      
        public void run(){
            try {
                MulticastSocket socket = new MulticastSocket(40000);
                InetAddress ip_group = InetAddress.getByName("230.0.0.0");
                socket.joinGroup(ip_group);
                for(;;){
                    synchronized(obj){
                        byte[] mensaje = recibe_mensaje_multicast(socket,150);
                        System.out.print("\n"+new String(mensaje,"IBM850"));
                    }
                }
            } catch (Exception e) {
                System.err.println("OcurriÃ³ un error");
            }
      }
    }
    
    public static void main(String[] args) throws Exception {
        if(args.length > 1){
            System.err.println("Faltan argumentos:");
            System.err.println("java CMC <nombre>");
            System.exit(-1);
        }
        
        //if(args.length == 0){
            new Worker().start();
        //}else{
            String nombre = args[0];
                    
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in, "IBM850"));
            String mensaje = "";

            for(;;){
                System.out.print("Ingrese el mensaje a enviar:");
                mensaje = nombre+":"+in.readLine()+"\n";
                envia_mensaje_multicast(mensaje.getBytes("IBM850"),"230.0.0.0", 40000);
            }
        //}
    }
}