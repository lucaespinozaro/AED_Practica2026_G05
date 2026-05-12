public interface BinarySearchTree<E extends Comparable<E>> {
    void insert(E data) throws ItemDuplicated;
    E search(E data) throws ItemNoFound;
    void delete(E data) throws ExceptionIsEmpty, ItemNoFounds;
    boolean isEmpty();
}
