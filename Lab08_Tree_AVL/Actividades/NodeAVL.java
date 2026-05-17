package avltree;

public class NodeAVL<E> extends Node<E>
{
  protected int bf; // Factor de equilibrio (Balance Factor)

  public NodeAVL(E data) {
    super(data);
    this.bf = 0;
  }
}
