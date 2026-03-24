import java.util.Arrays;

public class Main
{
  public static void main (String[] args)
  {
    String[] palabras = {"A", "B", "C", "D"};
    System.out.println("Antes : " + Arrays.toString(palabras));
    intercambio(palabras, 1, 3);
    System.out.println("Después (i=1, j=3): " + Arrays.toString(palabras));

    Integer[] numeros = {10, 20, 30, 40};
    System.out.println("Antes : " + Arrays.toString(numeros));
    intercambio(numeros, 0, 2);
    System.out.println("Después (i=0, j=2): " + Arrays.toString(numeros));
  }

  public static <T> void intercambio(T[] arr, int i, int j)
  {
    if (arr == null) {
      throw new IllegalArgumentException("El arreglo no existe");
    }
    if (i < 0 || i >= arr.length || j < 0 || j >= arr.length) {
      throw new IllegalArgumentException(
                                         "Índices fuera de rango: i=" + i + ", j=" + j +
                                         ", longitud=" + arr.length
                                         );
    }
    
    T temp = arr[i];
    arr[i]  = arr[j];
    arr[j]  = temp;
  }
}
