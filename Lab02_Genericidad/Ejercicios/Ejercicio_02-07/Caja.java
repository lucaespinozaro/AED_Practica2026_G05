public class Caja<T> {
    private String color;
    private T objeto;

    public Caja(String color, T objeto) {
        this.color = color;
        this.objeto = objeto;
    }

    public T getObjeto() {
        return objeto;
    }

    public String getColor() {
        return color;
    }

    public T delete() {
        T temp = objeto;
        objeto = null;
        return temp;
    }
}
