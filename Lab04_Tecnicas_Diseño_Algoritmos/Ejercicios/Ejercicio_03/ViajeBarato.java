import java.util.*;

public class ViajeBarato {
    static void resolver(int[][] T, int n, int[][] C, int[][] P) {
        if (T == null || C == null || P == null)
            throw new IllegalArgumentException("Matrices no pueden ser null");

        if (T.length != n || C.length != n || P.length != n)
            throw new IllegalArgumentException("Dimensiones incorrectas");

        for (int i = 0; i < n; i++) {
            if (T[i].length != n || C[i].length != n || P[i].length != n)
                throw new IllegalArgumentException("Matrices deben ser cuadradas");
        }

        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                C[i][j] = (i == j) ? 0 : T[i][j];
                P[i][j] = -1;
            }
        }

        for (int d = 2; d < n; d++) {
            for (int i = 0; i < n - d; i++) {
                int j = i + d;
                for (int k = i + 1; k < j; k++) {
                    int cost = C[i][k] + C[k][j];
                    if (cost < C[i][j]) {
                        C[i][j] = cost;
                        P[i][j] = k;
                    }
                }
            }
        }
    }

    static void reconstruir(int i, int j, int[][] P, List<Integer> r) {
        if (P == null || r == null)
            throw new IllegalArgumentException("Parametros no pueden ser null");

        if (i < 0 || j >= P.length || i > j)
            throw new IllegalArgumentException("Indices invalidos");

        if (i == j) return;

        int k = P[i][j];
        if (k == -1) return;

        reconstruir(i, k, P, r);
        r.add(k);
        reconstruir(k, j, P, r);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n;
        while (true) {
            System.out.print("Numero de embarcaderos: ");
            if (sc.hasNextInt()) {
                n = sc.nextInt();
                if (n > 1) break;
            } else sc.next();
            System.out.println("Ingrese un entero mayor a 1.");
        }

        int[][] T = new int[n][n];

        System.out.println("Ingrese tarifas (solo j > i):");
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                while (true) {
                    System.out.print("De " + i + " a " + j + ": ");
                    if (sc.hasNextInt()) {
                        int val = sc.nextInt();
                        if (val >= 0) {
                            T[i][j] = val;
                            break;
                        }
                    } else sc.next();
                    System.out.println("Ingrese un entero >= 0.");
                }
            }
        }

        int[][] C = new int[n][n];
        int[][] P = new int[n][n];

        resolver(T, n, C, P);

        System.out.println("\nMatriz C:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                System.out.print((j < i ? "-\t" : C[i][j] + "\t"));
            System.out.println();
        }

        int o, d;
        while (true) {
            System.out.print("\nOrigen (0 a " + (n - 2) + "): ");
            if (sc.hasNextInt()) {
                o = sc.nextInt();
                System.out.print("Destino (" + (o + 1) + " a " + (n - 1) + "): ");
                if (sc.hasNextInt()) {
                    d = sc.nextInt();
                    if (o >= 0 && d < n && o < d) break;
                } else sc.next();
            } else sc.next();
            System.out.println("Valores inválidos.");
        }

        List<Integer> ruta = new ArrayList<>();
        ruta.add(o);
        reconstruir(o, d, P, ruta);
        ruta.add(d);

        System.out.println("\nCosto minimo: " + C[o][d]);

        System.out.print("Ruta: ");
        for (int i = 0; i < ruta.size(); i++) {
            System.out.print(ruta.get(i));
            if (i < ruta.size() - 1) System.out.print(" -> ");
        }
        System.out.println();
    }
}
