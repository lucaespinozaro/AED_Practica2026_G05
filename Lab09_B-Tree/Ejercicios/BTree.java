public class BTree<E extends Comparable<E>> {
    private BNode<E> root;
    private int orden;
    private boolean up;
    private BNode<E> nDes;

    public BTree(int orden) {
        this.orden = orden;
        this.root = null;
    }

    public boolean isEmpty() {
        return this.root == null;
    }
    
    public BNode<E> getRoot() {
        return this.root;
    }

    public void insert(E cl) {
        up = false;
        E mediana;
        BNode<E> pnew;
        mediana = push(this.root, cl);
        if (up) {
            pnew = new BNode<E>(this.orden);
            pnew.count = 1;
            pnew.keys.set(0, mediana);
            pnew.childs.set(0, this.root);
            pnew.childs.set(1, nDes);
            this.root = pnew;
        }
    }

    private E push(BNode<E> current, E cl) {
        int pos[] = new int[1];
        E mediana;
        if (current == null) {
            up = true;
            nDes = null;
            return cl;
        } else {
            boolean fl;
            fl = current.searchNode(cl, pos);
            if (fl) {
                System.out.println("Item duplicado\n");
                up = false;
                return null;
            }
            mediana = push(current.childs.get(pos[0]), cl);
            if (up) {
                if (current.nodeFull(this.orden - 1)) 
                    mediana = dividedNode(current, mediana, pos[0]);
                else {
                    up = false;
                    putNode(current, mediana, nDes, pos[0]);
                }
            }
            return mediana;
        }
    }

    private void putNode(BNode<E> current, E cl, BNode<E> rd, int k) {
        int i;
        for (i = current.count - 1; i >= k; i--) {
            current.keys.set(i + 1, current.keys.get(i));
            current.childs.set(i + 2, current.childs.get(i + 1));
        }
        current.keys.set(k, cl);
        current.childs.set(k + 1, rd);
        current.count++;
    }

    private E dividedNode(BNode<E> current, E cl, int k) {
        BNode<E> rd = nDes;
        int i, posMdna;
        posMdna = (k <= this.orden / 2) ? this.orden / 2 : this.orden / 2 + 1;
        nDes = new BNode<E>(this.orden);
        for (i = posMdna; i < this.orden - 1; i++) {
            nDes.keys.set(i - posMdna, current.keys.get(i));
            nDes.childs.set(i - posMdna + 1, current.childs.get(i + 1));
        }
        nDes.count = (this.orden - 1) - posMdna;
        current.count = posMdna;
        if (k <= this.orden / 2) 
            putNode(current, cl, rd, k);
        else 
            putNode(nDes, cl, rd, k - posMdna);
        E median = current.keys.get(current.count - 1);
        nDes.childs.set(0, current.childs.get(current.count));
        current.count--;
        for (int j = current.count; j < this.orden; j++) {
            current.keys.set(j, null);
        }
        for (int j = current.count + 1; j < this.orden; j++) {
            current.childs.set(j, null);
        }
        return median;
    }
    
    public void delete(E key) {
        if (root == null) {
            System.out.println("El árbol está vacío.");
            return;
        }
        delete(root, key);
        if (root.count == 0) {
            if (root.childs.get(0) == null) {
                root = null;
            } else {
                root = root.childs.get(0);
            }
        }
    }
    
    private boolean delete(BNode<E> node, E key) {
        int[] pos = new int[1];
        boolean found = node.searchNode(key, pos);
        if (found) {
            if (node.childs.get(pos[0]) == null) {
                removeKey(node, pos[0]);
                return true;
            } else {
                E pred = getPredecessor(node, pos[0]);
                node.keys.set(pos[0], pred);
                boolean isDeleted = delete(node.childs.get(pos[0]), pred);
                if (node.childs.get(pos[0]).count < (orden - 1) / 2) {
                    fix(node, pos[0]);
                }
                return isDeleted;
            }
        } else {
            if (node.childs.get(pos[0]) == null) {
                return false;
            } else {
                boolean isDeleted = delete(node.childs.get(pos[0]), key);
                if (node.childs.get(pos[0]).count < (orden - 1) / 2) {
                    fix(node, pos[0]);
                }
                return isDeleted;
            }
        }
    }
    
    private void removeKey(BNode<E> node, int index) {
        for (int i = index; i < node.count - 1; i++) {
            node.keys.set(i, node.keys.get(i + 1));
        }
        node.keys.set(node.count - 1, null);
        node.count--;
    }

    private E getPredecessor(BNode<E> node, int index) {
        BNode<E> current = node.childs.get(index);
        while (current.childs.get(current.count) != null) {
            current = current.childs.get(current.count);
        }
        return current.keys.get(current.count - 1);
    }
    
    private void fix(BNode<E> parent, int index) {
        if (index > 0 && parent.childs.get(index - 1).count > (orden - 1) / 2) {
            borrowFromLeft(parent, index);
        } else if (index < parent.count && parent.childs.get(index + 1).count > (orden - 1) / 2) {
            borrowFromRight(parent, index);
        } else {
            if (index > 0) {
                merge(parent, index - 1);
            } else {
                merge(parent, index);
            }
        }
    }
    
    private void borrowFromLeft(BNode<E> parent, int index) {
        BNode<E> left = parent.childs.get(index - 1);
        BNode<E> current = parent.childs.get(index);
        for (int i = current.count - 1; i >= 0; i--) {
            current.keys.set(i + 1, current.keys.get(i));
        }
        current.keys.set(0, parent.keys.get(index - 1));
        parent.keys.set(index - 1, left.keys.get(left.count - 1));
        left.keys.set(left.count - 1, null);
        if (left.childs.get(left.count) != null) {
            for (int i = current.count; i >= 0; i--) {
                current.childs.set(i + 1, current.childs.get(i));
            }
            current.childs.set(0, left.childs.get(left.count));
            left.childs.set(left.count, null);
        }
        current.count++;
        left.count--;
    }

    private void borrowFromRight(BNode<E> parent, int index) {
        BNode<E> right = parent.childs.get(index + 1);
        BNode<E> current = parent.childs.get(index);
        current.keys.set(current.count, parent.keys.get(index));
        parent.keys.set(index, right.keys.get(0));
        for (int i = 0; i < right.count - 1; i++) {
            right.keys.set(i, right.keys.get(i + 1));
        }
        right.keys.set(right.count - 1, null);
        if (right.childs.get(0) != null) {
            current.childs.set(current.count + 1, right.childs.get(0));
            for (int i = 0; i < right.count; i++) {
                right.childs.set(i, right.childs.get(i + 1));
            }
            right.childs.set(right.count, null);
        }
        current.count++;
        right.count--;
    }
    
    private void merge(BNode<E> parent, int index) {
        BNode<E> left = parent.childs.get(index);
        BNode<E> right = parent.childs.get(index + 1);
        left.keys.set(left.count, parent.keys.get(index));
        left.count++;
        for (int i = 0; i < right.count; i++) {
            left.keys.set(left.count + i, right.keys.get(i));
        }
        for (int i = 0; i <= right.count; i++) {
            left.childs.set(left.count + i, right.childs.get(i));
        }
        left.count += right.count;
        for (int i = index; i < parent.count - 1; i++) {
            parent.keys.set(i, parent.keys.get(i + 1));
            parent.childs.set(i + 1, parent.childs.get(i + 2));
        }
        parent.keys.set(parent.count - 1, null);
        parent.childs.set(parent.count, null);
        parent.count--;
    }
    
    @Override
    public String toString() {
        String s = "";
        if (isEmpty()) {
            s += "BTree is empty...";
        } else {
            s = String.format("%-10s\t%-20s\t%-10s\t%-10s\n", "Id.Nodo", "Claves Nodo", "Id.Padre", "Id.Hijos");
            s += writeTree(this.root, null);
        }
        return s;
    }

    private String writeTree(BNode<E> current, BNode<E> parent) {
        if (current == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        String idNodo = String.valueOf(current.getIdNode());
        
        StringBuilder keysSb = new StringBuilder();
        keysSb.append("(");
        for (int i = 0; i < current.count; i++) {
            keysSb.append(current.keys.get(i));
            if (i < current.count - 1) {
                keysSb.append(", ");
            }
        }
        keysSb.append(")");
        String clavesNodo = keysSb.toString();

        String idPadre = (parent == null) ? "--" : "[" + parent.getIdNode() + "]";

        String idHijos = "--";
        if (current.childs.get(0) != null) {
            StringBuilder childsSb = new StringBuilder();
            childsSb.append("[");
            for (int i = 0; i <= current.count; i++) {
                if (current.childs.get(i) != null) {
                    childsSb.append(current.childs.get(i).getIdNode());
                    if (i < current.count && current.childs.get(i + 1) != null) {
                        childsSb.append(", ");
                    }
                }
            }
            childsSb.append("]");
            idHijos = childsSb.toString();
        }

        sb.append(String.format("%-10s\t%-20s\t%-10s\t%-10s\n", idNodo, clavesNodo, idPadre, idHijos));

        for (int i = 0; i <= current.count; i++) {
            sb.append(writeTree(current.childs.get(i), current));
        }

        return sb.toString();
    }
    
    public boolean search(E cl) {
        if (root == null) {
            return false;
        }
        return search(root, cl);
    }

    private boolean search(BNode<E> current, E cl) {
        int[] pos = new int[1];
        boolean found = current.searchNode(cl, pos);
        if (found) {
            System.out.println(cl + " se encuentra en el nodo " + current.getIdNode() + " en la posición " + pos[0]);
            return true;
        }
        if (current.childs.get(pos[0]) == null) {
            return false;
        }
        return search(current.childs.get(pos[0]), cl);
    }
    
    public void searchRange(E min, E max) {
        if (min == null || max == null || min.compareTo(max) > 0) {
            System.out.println("Rango inválido.");
            return;
        }
        if (root == null) {
            System.out.println("El árbol está vacío.");
            return;
        }
        boolean[] foundAny = new boolean[1];
        searchRange(root, min, max, foundAny);
        if (!foundAny[0]) {
            System.out.println("No se encontraron claves en el rango especificado.");
        }
        System.out.println();
    }
    
    private void searchRange(BNode<E> current, E min, E max, boolean[] foundAny) {
        if (current == null) {
            return;
        }
        int i = 0;
        while (i < current.count) {
            E key = current.keys.get(i);
            if (key.compareTo(min) >= 0) {
                searchRange(current.childs.get(i), min, max, foundAny);
                
                if (key.compareTo(max) <= 0) {
                    System.out.print(key + " ");
                    foundAny[0] = true;
                } else {
                    return; 
                }
            }
            i++;
        }
        searchRange(current.childs.get(i), min, max, foundAny);
    }
}
