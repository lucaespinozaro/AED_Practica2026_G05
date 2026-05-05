public class Main {
    public static void main(String[] args) {
        PriorityQueueHybrid<String> pq = new PriorityQueueHybrid<>(3);

        pq.enqueue("A", 2, 5);
        pq.enqueue("B", 2, 1);
        pq.enqueue("C", 1, 3);
        pq.enqueue("D", 2, 3);

        while (!pq.isEmpty()) {
            System.out.println(pq.dequeue());
        }
    }
}
