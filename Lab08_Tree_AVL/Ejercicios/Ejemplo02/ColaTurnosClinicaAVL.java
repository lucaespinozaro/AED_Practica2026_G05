public class ColaTurnosClinicaAVL {
    public static void main(String[] args) {
        AVLTree<Integer> colaTurnos = new AVLTree<>();

        try {
            System.out.println("--- REGISTRO DE TURNOS EN CLINICA ---");
            System.out.println("Registrando turno 10...");
            colaTurnos.insert(10);
            System.out.println("Registrando turno 20...");
            colaTurnos.insert(20);
            System.out.println("Registrando turno 30...");
            colaTurnos.insert(30);

            System.out.print("Turnos activos (Inorden): ");
            colaTurnos.inOrder();
            System.out.print("Estructura de la cola (Niveles): ");
            colaTurnos.breadthFirst();

            System.out.println("\n--- BUSQUEDA DE TURNOS ---");
            int turnoAbuscar = 20;
            System.out.print("Verificando turno " + turnoAbuscar + ": ");
            if (colaTurnos.search(turnoAbuscar)) {
                System.out.println("El paciente esta en sala de espera.");
            } else {
                System.out.println("Turno no valido o ya atendido.");
            }

            System.out.println("\n--- ATENCION DE PACIENTES (ELIMINACION) ---");
            System.out.println("Atendiendo y liberando turno 10...");
            colaTurnos.remove(10);

            System.out.print("Turnos restantes (Inorden): ");
            colaTurnos.inOrder();
            System.out.print("Estructura de la cola (Niveles): ");
            colaTurnos.breadthFirst();

        } catch (ItemDuplicated e) {
            System.out.println(e.getMessage());
        }
    }
}
