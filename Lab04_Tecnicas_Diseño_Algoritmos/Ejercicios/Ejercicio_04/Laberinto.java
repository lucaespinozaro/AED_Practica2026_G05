public class Laberinto {
    // Las cuatro direcciones posibles: abajo, derecha, arriba, izquierda
    static int[] filaDir  = { 1,  0, -1,  0};
    static int[] colDir   = { 0,  1,  0, -1};
 
    public static boolean resolverLaberinto(int[][] maze) {
        if (maze == null)
            throw new IllegalArgumentException("El laberinto no puede ser null");
        
        int n = maze.length;
        int[][] solucion = new int[n][n];
 
        if (resolver(maze, 0, 0, solucion, n)) {
            System.out.println("Camino encontrado:");
            imprimirSolucion(solucion, n);
            return true;
        } else {
            System.out.println("No hay salida.");
            return false;
        }
    }
 
    private static boolean resolver(int[][] maze, int fila, int col, int[][] solucion, int n) {
        // Caso base: llegamos a la esquina inferior derecha
        if (fila == n - 1 && col == n - 1) {
            solucion[fila][col] = 1;
            return true;
        }
 
        // Verificamos que la celda actual sea válida
        if (!esValida(maze, fila, col, solucion, n))
            return false;
 
        // Marcamos esta celda como parte del camino
        solucion[fila][col] = 1;
 
        // Probamos las cuatro direcciones
        for (int d = 0; d < 4; d++) {
            int nuevaFila = fila + filaDir[d];
            int nuevaCol  = col  + colDir[d];
 
            if (resolver(maze, nuevaFila, nuevaCol, solucion, n))
                return true; // encontramos el camino
        }
 
        // Backtracking: ninguna dirección funcionó, desmarcamos la celda
        solucion[fila][col] = 0;
        return false;
    }
 
    private static boolean esValida(int[][] maze, int fila, int col, int[][] solucion, int n) {
        // La celda debe estar dentro del laberinto,
        // ser camino libre (0) y no haber sido visitada antes
        return fila >= 0 && fila < n
            && col  >= 0 && col  < n
            && maze[fila][col] == 0
            && solucion[fila][col] == 0;
    }
 
    private static void imprimirSolucion(int[][] solucion, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(solucion[i][j] + " ");
            }
            System.out.println();
        }
    }
 
    public static void main(String[] args) {
        // Ejemplo 1 — debería dar true
        int[][] maze1 = {
            {0, 0, 1},
            {1, 0, 1},
            {1, 0, 0}
        };
        System.out.println("Ejemplo 1: " + resolverLaberinto(maze1));
 
        System.out.println();
 
        // Ejemplo 2 — debería dar false
        int[][] maze2 = {
            {0, 1},
            {1, 0}
        };
        System.out.println("Ejemplo 2: " + resolverLaberinto(maze2));
    }
}
