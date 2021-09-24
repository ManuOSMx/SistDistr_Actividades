import java.lang.Thread;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;

public class Matrices{
    static Object lock = new Object();
    static long checksum = 0;
    static int N = 10;
    static long[][] A = new long[N][N];
    static long[][] B = new long[N][N];
    static long[][] C = new long[N][N];

    static class Worker extends Thread {
        Socket connection;
        int nodo;
        Worker(Socket connection){
            this.connection = connection;
        }
        public void run(){
            //Algoritmo 1
            try {
                DataInputStream entrada = new DataInputStream(connection.getInputStream());
                DataOutputStream salida = new DataOutputStream(connection.getOutputStream());
                long ai[][] = new long[N/2][N];
                long bi[][] = new long[N/2][N];
                
                int nodo = entrada.readInt();

                if(nodo == 1){
                    for (int i = 0; i < N/2; i++) {
                        for (int j = 0; j < N; j++) {
                            ai[i][j] = A[i][j];
                            bi[i][j] = B[i][j];
                        }
                    }
                }else if(nodo == 2){
                    for (int i = 0; i < N/2; i++) {
                        for (int j = 0; j < N; j++) {
                            ai[i][j] = A[i][j];
                            bi[i][j] = B[i + N / 2][j];
                        }
                    }
                } else if(nodo == 3){
                    for (int i = 0; i < N/2; i++) {
                        for (int j = 0; j < N; j++) {
                            ai[i][j] = A[i + N / 2][j];
                            bi[i][j] = B[i][j];
                        }
                    }
                } else if(nodo == 4){
                    for (int i = 0; i < N/2; i++) {
                        for (int j = 0; j < N; j++) {
                            ai[i][j] = A[i + N / 2][j];
                            bi[i][j] = B[i + N / 2][j];
                        }
                    }
                }

                sMat(ai, N/2, N, salida);
                sMat(bi, N/2, N, salida);

                long ci[][] = rMat(N/2, N/2, entrada);

                if(nodo == 1){
                    for (int i = 0; i < N/2; i++) {
                        for (int j = 0; j < N/2; j++) {
                            C[i][j] = ci[i][j];
                        }
                    }
                }else if(nodo == 2){
                    for (int i = 0; i < N/2; i++) {
                        for (int j = 0; j < N/2; j++) {
                            C[i][j +  N / 2] = ci[i][j];
                        }
                    }
                } else if(nodo == 3){
                    for (int i = 0; i < N/2; i++) {
                        for (int j = 0; j < N/2; j++) {
                            C[i + N / 2][j] = ci[i][j];
                        }
                    }
                } else if(nodo == 4){
                    for (int i = 0; i < N/2; i++) {
                        for (int j = 0; j < N/2; j++) {
                            C[i + N / 2][j +  N / 2] = ci[i][j];
                        }
                    }
                }
                entrada.close();
                salida.close();
                connection.close();

            } catch (IOException e){
                e.printStackTrace();
            }
        }

    }
    static void read(DataInputStream f,byte[] b,int posicion,int longitud) throws Exception {
        while (longitud > 0) {
            int n = f.read(b,posicion,longitud);
            posicion += n;
            longitud -= n;
        }
    }

    static void pMat(long matrix[][], int rows, int cols){
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.printf("%4d", matrix[i][j]);
            }
            System.out.println("");
        }
    }

    static void sMat(long matrix[][], int rows, int cols, DataOutputStream salida){
        for (int i = 0; i < rows; i++) {
            ByteBuffer bf = ByteBuffer.allocate(cols*4);
            for (int j = 0; j < cols; j++) {
                bf.putLong(matrix[i][j]);
            }
            byte[] bytes = bf.array();
            try {
                salida.write(bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static long[][] rMat(int rows, int cols, DataInputStream entrada)   {
        long matrix[][] = new long[rows][cols];
        for (int i = 0; i < rows; i++) {
            byte[] bytes = new byte[cols * 4];
            try{
                read(entrada, bytes, 0, cols * 4);
                ByteBuffer bf = ByteBuffer.wrap(bytes);
                for (int j = 0; j < cols; j++)
                    matrix[i][j] = bf.getInt();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return matrix;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: java Pi <nodo>");
            System.exit(0);
        }
        int nodo = Integer.valueOf(args[0]);
        if (nodo == 0) {
            ServerSocket server = new ServerSocket(50000);
            Worker w[] = new Worker[4];

            
            for (int i = 0; i < N; i++){
                for (int j = 0; j < N; j++)
                {
                    A[i][j] = i + 3 * j;
                    B[i][j] = i - 3 * j;
                    C[i][j] = 0;
                }
            }

            System.out.println("Matriz A:");
            pMat(A, N, N);
            System.out.println("Matriz B:");
            pMat(B, N, N);
            
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < i; j++) {
                    long x = B[i][j];
                    B[i][j] = B[j][i];
                    B[j][i] = x;
                }
            }

            System.out.println("Matriz B^T:");
            pMat(B, N, N);
            
            for(int i=0; i < 4; i++){
                Socket client = server.accept();
                w[i] = new Worker(client);
                w[i].start();
            }
            for(int i=0; i < 4; i++){
                w[i].join();
            }

            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    checksum += C[i][j];
                }
            }
            System.out.println("check sum = " + checksum);

            server.close();

        } else {
            
            Socket connection = null;

            for(;;)
            try {
                //
                connection = new Socket("localhost",50000);
                break;
            } catch (Exception e) {
                Thread.sleep(100);
            }
            
            DataInputStream entrada = new DataInputStream(connection.getInputStream());
            DataOutputStream salida = new DataOutputStream(connection.getOutputStream());

            salida.writeInt(nodo);

            long ai[][] = rMat(N/2, N, entrada);
            long bi[][] = rMat(N/2, N, entrada);
            long ci[][] = new long[N/2][N/2];

            for (int i = 0; i < N/2; i++)
                for (int j = 0; j < N/2; j++)
                    for (int k = 0; k < N; k++)
                        ci[i][j] += ai[i][k] * bi[j][k];

            sMat(ci, N/2, N/2, salida);

            entrada.close();
            salida.close();
            connection.close();
        }
    }
}
