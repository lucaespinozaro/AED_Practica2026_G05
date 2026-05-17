package avltree;

public class BSTree<E extends Comparable<E>> {
  protected Node<E> root;

  public BSTree() {
    this.root = null;
  }

  public boolean isEmpty() {
    return root == null;
  }
}
