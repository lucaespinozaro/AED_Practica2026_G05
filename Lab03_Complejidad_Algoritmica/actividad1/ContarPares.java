public class ContarPares {

    public static int contarPares(int A[], int n){
        int contador = 0;

        for(int i = 0; i < n; i++){
            if(A[i] % 2 == 0){
                contador++;
            }
        }

        return contador;
    }
}