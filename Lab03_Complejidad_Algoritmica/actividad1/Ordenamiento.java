public class Ordenamiento {

    public static void ordenar(int v[], int tamaño){

        if(tamaño <= 1){
            return;
        }

        int i, j, aux;

        for(i = 0; i < tamaño - 1; i++){
            for(j = 0; j < tamaño - 1 - i; j++){
                if(v[j] > v[j + 1]){
                    aux = v[j];
                    v[j] = v[j + 1];
                    v[j + 1] = aux;
                }
            }
        }
    }
}