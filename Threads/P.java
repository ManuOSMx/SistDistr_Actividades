public class P {
    static class Worker extends Thread {
        public void run(){
            for(int i=1; i <= 10000000; i++) System.out.println(i);
        }
    }
    public static void main(String[] args) throws Exception {
        Worker w1 = new Worker();
        Worker w2 = new Worker();
        
        w1.start();
        w2.start();

        w1.join();
        w2.join();
    }
}
