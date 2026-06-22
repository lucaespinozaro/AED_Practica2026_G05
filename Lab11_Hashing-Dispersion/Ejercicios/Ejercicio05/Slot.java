/**
 * Celda simple de la tabla hash cerrada (ocupada o vacia).
 */
public class Slot {
    private int key;
    private boolean occupied;

    public Slot() {
        this.key = 0;
        this.occupied = false;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    @Override
    public String toString() {
        return occupied ? String.valueOf(key) : "vacio";
    }
}
