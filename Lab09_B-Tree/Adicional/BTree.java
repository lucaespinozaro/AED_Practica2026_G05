import java.util.ArrayList;
import java.util.List;

public class BTree<E extends Comparable<E>> {

    private BNode<E> root;
    private int orden;
    private boolean up;
    private BNode<E> nDes;

    public BTree(int orden) { this.orden = orden; }

    public boolean isEmpty() { return root == null; }
    public BNode<E> getRoot() { return root; }
    public int getOrden()     { return orden; }

    // ── Insert ──────────────────────────────────────────────
    public boolean insert(E cl) {
        up = false;
        E med = push(root, cl);
        if (up) {
            BNode<E> pnew = new BNode<>(orden);
            pnew.count = 1;
            pnew.keys.set(0, med);
            pnew.childs.set(0, root);
            pnew.childs.set(1, nDes);
            root = pnew;
            return true;
        }
        return med != null;
    }

    private E push(BNode<E> cur, E cl) {
        int[] pos = new int[1];
        if (cur == null) { up = true; nDes = null; return cl; }
        if (cur.searchNode(cl, pos)) { up = false; return null; }
        E med = push(cur.childs.get(pos[0]), cl);
        if (up) {
            if (cur.nodeFull(orden - 1)) med = dividedNode(cur, med, pos[0]);
            else { up = false; putNode(cur, med, nDes, pos[0]); }
        }
        return med;
    }

    private void putNode(BNode<E> cur, E cl, BNode<E> rd, int k) {
        for (int i = cur.count - 1; i >= k; i--) {
            cur.keys.set(i + 1, cur.keys.get(i));
            cur.childs.set(i + 2, cur.childs.get(i + 1));
        }
        cur.keys.set(k, cl); cur.childs.set(k + 1, rd); cur.count++;
    }

    private E dividedNode(BNode<E> cur, E cl, int k) {
        BNode<E> rd = nDes;
        int pos = (k <= orden / 2) ? orden / 2 : orden / 2 + 1;
        nDes = new BNode<>(orden);
        for (int i = pos; i < orden - 1; i++) {
            nDes.keys.set(i - pos, cur.keys.get(i));
            nDes.childs.set(i - pos + 1, cur.childs.get(i + 1));
        }
        nDes.count = (orden - 1) - pos;
        cur.count  = pos;
        if (k <= orden / 2) putNode(cur, cl, rd, k);
        else                putNode(nDes, cl, rd, k - pos);
        E med = cur.keys.get(cur.count - 1);
        nDes.childs.set(0, cur.childs.get(cur.count));
        cur.count--;
        return med;
    }

    // ── Delete ──────────────────────────────────────────────
    public boolean delete(E key) {
        if (root == null) return false;
        boolean ok = delete(root, key);
        if (root.count == 0) root = root.childs.get(0);
        return ok;
    }

    private boolean delete(BNode<E> node, E key) {
        int[] pos = new int[1];
        boolean found = node.searchNode(key, pos);
        if (found) {
            if (node.childs.get(pos[0]) == null) { removeKey(node, pos[0]); return true; }
            E pred = getPred(node, pos[0]);
            node.keys.set(pos[0], pred);
            boolean r = delete(node.childs.get(pos[0]), pred);
            if (node.childs.get(pos[0]).count < (orden - 1) / 2) fix(node, pos[0]);
            return r;
        } else {
            if (node.childs.get(pos[0]) == null) return false;
            boolean r = delete(node.childs.get(pos[0]), key);
            if (node.childs.get(pos[0]).count < (orden - 1) / 2) fix(node, pos[0]);
            return r;
        }
    }

    private void removeKey(BNode<E> n, int idx) {
        for (int i = idx; i < n.count - 1; i++) n.keys.set(i, n.keys.get(i + 1));
        n.keys.set(n.count - 1, null); n.count--;
    }

    private E getPred(BNode<E> n, int idx) {
        BNode<E> cur = n.childs.get(idx);
        while (cur.childs.get(cur.count) != null) cur = cur.childs.get(cur.count);
        return cur.keys.get(cur.count - 1);
    }

    private void fix(BNode<E> p, int idx) {
        int min = (orden - 1) / 2;
        if (idx > 0 && p.childs.get(idx - 1).count > min)           borrowLeft(p, idx);
        else if (idx < p.count && p.childs.get(idx + 1).count > min) borrowRight(p, idx);
        else if (idx > 0) merge(p, idx - 1);
        else              merge(p, idx);
    }

    private void borrowLeft(BNode<E> p, int idx) {
        BNode<E> left = p.childs.get(idx - 1), cur = p.childs.get(idx);
        for (int i = cur.count - 1; i >= 0; i--) cur.keys.set(i + 1, cur.keys.get(i));
        cur.keys.set(0, p.keys.get(idx - 1));
        p.keys.set(idx - 1, left.keys.get(left.count - 1));
        left.keys.set(left.count - 1, null);
        if (left.childs.get(left.count) != null) {
            for (int i = cur.count; i >= 0; i--) cur.childs.set(i + 1, cur.childs.get(i));
            cur.childs.set(0, left.childs.get(left.count));
            left.childs.set(left.count, null);
        }
        cur.count++; left.count--;
    }

    private void borrowRight(BNode<E> p, int idx) {
        BNode<E> right = p.childs.get(idx + 1), cur = p.childs.get(idx);
        cur.keys.set(cur.count, p.keys.get(idx));
        p.keys.set(idx, right.keys.get(0));
        for (int i = 0; i < right.count - 1; i++) right.keys.set(i, right.keys.get(i + 1));
        right.keys.set(right.count - 1, null);
        if (right.childs.get(0) != null) {
            cur.childs.set(cur.count + 1, right.childs.get(0));
            for (int i = 0; i < right.count; i++) right.childs.set(i, right.childs.get(i + 1));
            right.childs.set(right.count, null);
        }
        cur.count++; right.count--;
    }

    private void merge(BNode<E> p, int idx) {
        BNode<E> left = p.childs.get(idx), right = p.childs.get(idx + 1);
        left.keys.set(left.count, p.keys.get(idx)); left.count++;
        for (int i = 0; i < right.count; i++) left.keys.set(left.count + i, right.keys.get(i));
        for (int i = 0; i <= right.count; i++) left.childs.set(left.count + i, right.childs.get(i));
        left.count += right.count;
        for (int i = idx; i < p.count - 1; i++) {
            p.keys.set(i, p.keys.get(i + 1));
            p.childs.set(i + 1, p.childs.get(i + 2));
        }
        p.keys.set(p.count - 1, null); p.childs.set(p.count, null); p.count--;
    }

    // ── Search ──────────────────────────────────────────────
    public boolean search(E cl) { return root != null && search(root, cl); }

    private boolean search(BNode<E> cur, E cl) {
        int[] pos = new int[1];
        if (cur.searchNode(cl, pos)) return true;
        if (cur.childs.get(pos[0]) == null) return false;
        return search(cur.childs.get(pos[0]), cl);
    }

    public List<E> searchRange(E min, E max) {
        List<E> res = new ArrayList<>();
        if (root != null && min != null && max != null && min.compareTo(max) <= 0)
            searchRange(root, min, max, res);
        return res;
    }

    private void searchRange(BNode<E> cur, E min, E max, List<E> res) {
        if (cur == null) return;
        int i = 0;
        while (i < cur.count) {
            E key = cur.keys.get(i);
            if (key.compareTo(min) >= 0) {
                searchRange(cur.childs.get(i), min, max, res);
                if (key.compareTo(max) <= 0) res.add(key);
                else return;
            } else {
                if (i == cur.count - 1 || cur.keys.get(i + 1).compareTo(min) >= 0)
                    searchRange(cur.childs.get(i), min, max, res);
            }
            i++;
        }
        searchRange(cur.childs.get(i), min, max, res);
    }

    public List<E> inorder() {
        List<E> res = new ArrayList<>();
        inorder(root, res);
        return res;
    }

    private void inorder(BNode<E> n, List<E> res) {
        if (n == null) return;
        for (int i = 0; i < n.count; i++) { inorder(n.childs.get(i), res); res.add(n.keys.get(i)); }
        inorder(n.childs.get(n.count), res);
    }
}
