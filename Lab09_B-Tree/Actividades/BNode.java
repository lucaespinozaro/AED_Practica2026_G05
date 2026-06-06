import java.util.ArrayList;

public class BNode<E> {
    private static int idCounter = 1;
    private int idNode;
    protected ArrayList<E> keys;
    protected ArrayList<BNode<E>> childs;
    protected int count;

    public BNode(int n) {
        this.idNode = idCounter++;
        this.keys = new ArrayList<E>(n);
        this.childs = new ArrayList<BNode<E>>(n);
        this.count = 0;
        for (int i = 0; i < n; i++) {
            this.keys.add(null);
            this.childs.add(null);
        }
    }

    public int getIdNode() {
        return this.idNode;
    }

    public boolean nodeFull(int maxKeys) {
        return this.count == maxKeys;
    }

    public boolean nodeEmpty() {
        return this.count == 0;
    }

    public boolean searchNode(E key, int[] pos) {
        pos[0] = 0;
        while (pos[0] < count && ((Comparable<E>) key).compareTo(keys.get(pos[0])) > 0) {
            pos[0]++;
        }
        if (pos[0] < count && ((Comparable<E>) key).compareTo(keys.get(pos[0])) == 0) {
            return true;
        } else {
            return false;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < count; i++) {
            sb.append(keys.get(i));
            if (i < count - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
