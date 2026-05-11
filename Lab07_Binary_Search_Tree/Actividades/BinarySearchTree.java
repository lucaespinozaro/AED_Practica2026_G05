public interface BinarySearchTree<E extends Comparable<E>> {
    void insert(E data) throws ItemDuplicated;

    boolean search(E data) throws ItemNoFound;

    void delete(E data) throws ExceptionIsEmpty;

    boolean isEmpty();
}
