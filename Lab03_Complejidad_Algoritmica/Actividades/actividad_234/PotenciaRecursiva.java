package Actividad_234;
public class PotenciaRecursiva {

    public static double potencia(double x, int y){

        if(y == 0){
            return 1.0;
        }

        if(y % 2 == 1){
            return x * potencia(x, y - 1);
        }else{
            double t = potencia(x, y / 2);
            return t * t;
        }
    }
}