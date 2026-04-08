public class Maximo {

    public static int max(int x, int y){
        int result;

        if(x == y){
            result = x;
        }else{
            if(x > y){
                result = x;
            }else{
                result = y;
            }
        }

        return result;
    }
}