public interface BinarySearchTree<E extends Comparable<E>> {
    void insert(E data) throws ItemDuplicated;
    E search(E data) throws ItemNoFound;
    public void delete(E data) throws ExceptionIsEmpty, ItemNoFound;
    boolean isEmpty();
}
