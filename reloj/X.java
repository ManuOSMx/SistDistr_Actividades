import java.util.LinkedList;

class X {
    static LinkedList<Integer> cola = new LinkedList<Integer>();
    public static void main(String[] args) {
        cola.push(1);
        cola.push(2);
        cola.push(3);
        cola.push(4);

        while(cola.size() > 0) {
            System.out.println(cola.removeFirst());            
        }
    }
} 