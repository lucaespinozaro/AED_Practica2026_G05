public class SumaVector {

    public static int suma(int v[], int tamaño){

        if(tamaño <= 0){
            return 0;
        }

        int result = 0;

        for(int i = 0; i < tamaño; i++){
            if(v[i] >= 0){
                result = result + v[i];
            }else{
                result = result + v[i];
            }
        }

        return result;
    }
}