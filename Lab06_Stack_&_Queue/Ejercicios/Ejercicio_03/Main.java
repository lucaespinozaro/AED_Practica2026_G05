public class Main {
    public static void main(String[] args) {
        ColaPrioridad<String> cp = new ColaPrioridad<>(3);

        cp.enqueue("A", 0);
        cp.enqueue("B", 2);
        cp.enqueue("C", 1);
        cp.enqueue("D", 2);

        while (!cp.isEmpty()) {
            System.out.println(cp.dequeue());
        }
    }
}
