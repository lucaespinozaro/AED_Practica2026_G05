/**
 * Celda de la tabla hash cerrada con tres estados posibles.
 */
public class Entry {
    public enum Status { EMPTY, OCCUPIED, DELETED }

    private int key;
    private Status status;

    public Entry() {
        this.key = 0;
        this.status = Status.EMPTY;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        if (status == Status.OCCUPIED) {
            return "OCCUPIED(" + key + ")";
        } else if (status == Status.DELETED) {
            return "DELETED (antes tenia " + key + ")";
        } else {
            return "EMPTY";
        }
    }
}
