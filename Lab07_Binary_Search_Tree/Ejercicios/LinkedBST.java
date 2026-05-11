import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class LinkedBST<E> implements BinarySearchTree<E> {

  class Node {
    public E data;
    public Node left;
    public Node right;

    public Node(E data) {
      this(data, null, null);
    }

    public Node(E data, Node left, Node right) {
      this.data  = data;
      this.left  = left;
      this.right = right;
    }
  }

  private Node root;

  public LinkedBST() {
    this.root = null;
  }



  @Override
  public boolean isEmpty() {
    return root == null;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void insert(E data) throws ItemDuplicated {
    root = insertRec(root, data);
  }

  @SuppressWarnings("unchecked")
  private Node insertRec(Node node, E data) throws ItemDuplicated {
    if (node == null) return new Node(data);
    int cmp = ((Comparable<E>) data).compareTo(node.data);
    if      (cmp < 0) node.left  = insertRec(node.left,  data);
    else if (cmp > 0) node.right = insertRec(node.right, data);
    else throw new ItemDuplicated("Dato duplicado: " + data);
    return node;
  }

  @Override
  @SuppressWarnings("unchecked")
  public E search(E data) throws ItemNotFound {
    Node current = root;
    while (current != null) {
      int cmp = ((Comparable<E>) data).compareTo(current.data);
      if      (cmp == 0) return current.data;
      else if (cmp  < 0) current = current.left;
      else               current = current.right;
    }
    throw new ItemNotFound("No encontrado: " + data);
  }

  @Override
  @SuppressWarnings("unchecked")
  public void delete(E data) throws ExceptionIsEmpty {
    if (root == null) throw new ExceptionIsEmpty("El BST esta vacio.");
    root = deleteRec(root, data);
  }

  @SuppressWarnings("unchecked")
  private Node deleteRec(Node node, E data) {
    if (node == null) return null;
    int cmp = ((Comparable<E>) data).compareTo(node.data);
    if (cmp < 0) {
      node.left  = deleteRec(node.left,  data);
    } else if (cmp > 0) {
      node.right = deleteRec(node.right, data);
    } else {
      if (node.left  == null) return node.right;
      if (node.right == null) return node.left;
      Node min = findMin(node.right);
      node.data  = min.data;
      node.right = deleteRec(node.right, min.data);
    }
    return node;
  }

  private Node findMin(Node node) {
    while (node.left != null) node = node.left;
    return node;
  }


  public void inOrder() {
    inOrderRec(root);
    System.out.println();
  }

  private void inOrderRec(Node node) {
    if (node == null) return;
    inOrderRec(node.left);
    System.out.print(node.data + " ");
    inOrderRec(node.right);
  }

  public void preOrder() {
    preOrderRec(root);
    System.out.println();
  }

  private void preOrderRec(Node node) {
    if (node == null) return;
    System.out.print(node.data + " ");
    preOrderRec(node.left);
    preOrderRec(node.right);
  }

  public void postOrder() {
    postOrderRec(root);
    System.out.println();
  }

  private void postOrderRec(Node node) {
    if (node == null) return;
    postOrderRec(node.left);
    postOrderRec(node.right);
    System.out.print(node.data + " ");
  }



  public void destroyNodes() throws ExceptionIsEmpty {
    if (root == null)
      throw new ExceptionIsEmpty("El BST esta vacio.");
    root = null;
  }



  public int countAllNodes() {
    if (root == null) return 0;
    int count = 0;
    Queue<Node> queue = new LinkedList<>();
    queue.add(root);
    while (!queue.isEmpty()) {
      Node current = queue.poll();
      count++;
      if (current.left  != null) queue.add(current.left);
      if (current.right != null) queue.add(current.right);
    }
    return count;
  }



  public int countNodes() {
    if (root == null) return 0;
    int count = 0;
    Queue<Node> queue = new LinkedList<>();
    queue.add(root);
    while (!queue.isEmpty()) {
      Node current = queue.poll();
      if (current.left != null || current.right != null)
        count++;
      if (current.left  != null) queue.add(current.left);
      if (current.right != null) queue.add(current.right);
    }
    return count;
  }



  @SuppressWarnings("unchecked")
  public int height(E x) {
    Node target = null;
    Node current = root;
    while (current != null) {
      int cmp = ((Comparable<E>) x).compareTo(current.data);
      if      (cmp == 0) { target = current; break; }
      else if (cmp  < 0) current = current.left;
      else               current = current.right;
    }
    if (target == null) return -1;

    Queue<Node> queue = new LinkedList<>();
    queue.add(target);
    int h = -1;
    while (!queue.isEmpty()) {
      int size = queue.size();
      h++;
      for (int i = 0; i < size; i++) {
        Node node = queue.poll();
        if (node.left  != null) queue.add(node.left);
        if (node.right != null) queue.add(node.right);
      }
    }
    return h;
  }



  public int amplitude(int nivel) {
    if (root == null) return 0;
    int treeHeight = height(root.data);
    if (nivel < 0 || nivel > treeHeight) return 0;

    Queue<Node> queue = new LinkedList<>();
    queue.add(root);
    int currentLevel = 0;
    while (!queue.isEmpty()) {
      int size = queue.size();
      if (currentLevel == nivel) return size;
      currentLevel++;
      for (int i = 0; i < size; i++) {
        Node node = queue.poll();
        if (node.left  != null) queue.add(node.left);
        if (node.right != null) queue.add(node.right);
      }
    }
    return 0;
  }



  public int areaBST() {
    if (root == null) return 0;
    int leaves = 0;
    Queue<Node> queue = new LinkedList<>();
    queue.add(root);
    while (!queue.isEmpty()) {
      Node node = queue.poll();
      if (node.left == null && node.right == null)
        leaves++;
      if (node.left  != null) queue.add(node.left);
      if (node.right != null) queue.add(node.right);
    }
    return leaves * height(root.data);
  }



  public void drawBST() {
    System.out.println(drawHelper(root, 0));
  }

  private String drawHelper(Node node, int level) {
    if (node == null) return "";
    StringBuilder sb = new StringBuilder();
    sb.append(drawHelper(node.right, level + 1));
    sb.append("  ".repeat(level))
      .append("[").append(node.data).append("]\n");
    sb.append(drawHelper(node.left, level + 1));
    return sb.toString();
  }



  public void parenthesize() {
    parenthesizeNode(root, 0);
  }

  private void parenthesizeNode(Node node, int depth) {
    if (node == null) return;
    String indent = "  ".repeat(depth);
    boolean hasChildren = (node.left != null || node.right != null);
    if (hasChildren) {
      System.out.println(indent + node.data + " (");
      if (node.left  != null) parenthesizeNode(node.left,  depth + 1);
      if (node.right != null) parenthesizeNode(node.right, depth + 1);
      System.out.println(indent + ")");
    } else {
      System.out.println(indent + node.data);
    }
  }



  public boolean isValidBST() {
    return validateBST(root, null, null);
  }

  @SuppressWarnings("unchecked")
  private boolean validateBST(Node node, E min, E max) {
    if (node == null) return true;
    if (min != null && ((Comparable<E>) node.data).compareTo(min) <= 0)
      return false;
    if (max != null && ((Comparable<E>) node.data).compareTo(max) >= 0)
      return false;
    return validateBST(node.left,  min,       node.data)
      && validateBST(node.right, node.data, max);
  }



  public List<E> searchRange(E min, E max) {
    List<E> result = new ArrayList<>();
    searchRangeHelper(root, min, max, result);
    return result;
  }

  @SuppressWarnings("unchecked")
  private void searchRangeHelper(Node node, E min, E max, List<E> result) {
    if (node == null) return;
    int cmpMin = ((Comparable<E>) min).compareTo(node.data);
    int cmpMax = ((Comparable<E>) max).compareTo(node.data);
    if (cmpMin < 0)
      searchRangeHelper(node.left,  min, max, result);
    if (cmpMin <= 0 && cmpMax >= 0)
      result.add(node.data);
    if (cmpMax > 0)
      searchRangeHelper(node.right, min, max, result);
  }



  public int countLeaves() {
    if (root == null) return 0;
    int count = 0;
    Queue<Node> queue = new LinkedList<>();
    queue.add(root);
    while (!queue.isEmpty()) {
      Node node = queue.poll();
      if (node.left == null && node.right == null)
        count++;
      if (node.left  != null) queue.add(node.left);
      if (node.right != null) queue.add(node.right);
    }
    return count;
  }



  public void printDescending() {
    printDescHelper(root);
    System.out.println();
  }

  private void printDescHelper(Node node) {
    if (node == null) return;
    printDescHelper(node.right);
    System.out.print(node.data + " ");
    printDescHelper(node.left);
  }



  @Override
  public String toString() {
    return drawHelper(root, 0);
  }
}
