public class SessionCache {
    private static class Node {
        Session session;
        Node next;

        Node(Session session) {
            this.session = session;
            this.next = null;
        }
    }

    private Node[] table;
    private int size;

    public SessionCache(int size) {
        this.size = size;
        this.table = new Node[size];
    }

    private int hash(String token) {
        return Math.abs(token.hashCode()) % size;
    }

    public void login(String token, String username, String role, long ttlMs) {
        if (token == null) return;
        int index = hash(token);
        long expiresAt = System.currentTimeMillis() + ttlMs;
        Session nueva = new Session(token, username, role, expiresAt);

        Node aux = table[index];
        while (aux != null) {
            if (aux.session.getToken().equals(token)) {
                aux.session = nueva;
                return;
            }
            aux = aux.next;
        }

        Node nodo = new Node(nueva);
        nodo.next = table[index];
        table[index] = nodo;
    }

    public Session validate(String token) {
        if (token == null) return null;
        int index = hash(token);
        Node aux = table[index];
        long now = System.currentTimeMillis();
        while (aux != null) {
            if (aux.session.getToken().equals(token)) {
                if (aux.session.isExpired(now)) {
                    return null;
                }
                return aux.session;
            }
            aux = aux.next;
        }
        return null;
    }

    public void logout(String token) {
        if (token == null) return;
        int index = hash(token);
        Node aux = table[index];
        Node prev = null;
        while (aux != null) {
            if (aux.session.getToken().equals(token)) {
                if (prev == null) {
                    table[index] = aux.next;
                } else {
                    prev.next = aux.next;
                }
                return;
            }
            prev = aux;
            aux = aux.next;
        }
    }

    public void cleanExpired() {
        long now = System.currentTimeMillis();
        int removidas = 0;
        for (int i = 0; i < size; i++) {
            Node aux = table[i];
            Node prev = null;
            while (aux != null) {
                if (aux.session.isExpired(now)) {
                    Node siguiente = aux.next;
                    if (prev == null) {
                        table[i] = siguiente;
                    } else {
                        prev.next = siguiente;
                    }
                    removidas++;
                    aux = siguiente;
                } else {
                    prev = aux;
                    aux = aux.next;
                }
            }
        }
        System.out.println("cleanExpired(): se eliminaron " + removidas + " sesion(es) expirada(s).");
    }

    public int countActive() {
        int total = 0;
        for (int i = 0; i < size; i++) {
            Node aux = table[i];
            while (aux != null) {
                total++;
                aux = aux.next;
            }
        }
        return total;
    }

    public void printTable() {
        for (int i = 0; i < size; i++) {
            System.out.print("  Indice " + i + ": ");
            Node aux = table[i];
            if (aux == null) {
                System.out.println("vacio");
            } else {
                StringBuilder sb = new StringBuilder();
                while (aux != null) {
                    sb.append(aux.session.toString());
                    if (aux.next != null) sb.append(" -> ");
                    aux = aux.next;
                }
                System.out.println(sb.toString());
            }
        }
    }
}
