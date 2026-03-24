public class Busqueda<T> {
    private String color;
    private int posicion;
    private T objeto;

    public Busqueda(String color, int posicion, T objeto) {
        this.color = color;
        this.posicion = posicion;
        this.objeto = objeto;
    }

    public String getColor() {
        return color;
    }

    public int getPosicion() {
        return posicion;
    }

    public T getObjeto() {
        return objeto;
    }
}
