import java.util.ArrayList;

public class BNode<E> {
    private static int idCounter = 1;
    private int idNode;
    protected ArrayList<E> keys;
    protected ArrayList<BNode<E>> childs;
    protected int count;

    public BNode(int n) {
        this.idNode = idCounter++;
        this.keys   = new ArrayList<>(n);
        this.childs = new ArrayList<>(n + 1);
        this.count  = 0;
        for (int i = 0; i < n;     i++) keys.add(null);
        for (int i = 0; i <= n;    i++) childs.add(null);
    }

    public static void resetCounter() { idCounter = 1; }
    public int getIdNode() { return idNode; }
    public boolean nodeFull(int maxKeys) { return count == maxKeys; }

    public boolean searchNode(E key, int[] pos) {
        pos[0] = 0;
        while (pos[0] < count && ((Comparable<E>) key).compareTo(keys.get(pos[0])) > 0)
            pos[0]++;
        return pos[0] < count && ((Comparable<E>) key).compareTo(keys.get(pos[0])) == 0;
    }
}
