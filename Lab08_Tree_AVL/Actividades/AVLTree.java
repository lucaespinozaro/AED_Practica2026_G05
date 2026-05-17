package avltree;

public class AVLTree<E extends Comparable<E>> extends BSTree<E> {
  private boolean heightChanged;

  public void insert(E data) {
    root = insertRec((NodeAVL<E>) root, data);
  }

  private NodeAVL<E> insertRec(NodeAVL<E> node, E data) {
    if (node == null) {
      heightChanged = true;
      return new NodeAVL<>(data);
    }

    int cmp = data.compareTo(node.data);
    if (cmp < 0) {
      node.left = insertRec((NodeAVL<E>) node.left, data);
      if (heightChanged) {
        switch (node.bf) {
        case 1: node.bf = 0; heightChanged = false; break;
        case 0: node.bf = -1; break;
        case -1: node = balanceToRight(node); heightChanged = false; break;
        }
      }
    } else if (cmp > 0) {
      node.right = insertRec((NodeAVL<E>) node.right, data);
      if (heightChanged) {
        switch (node.bf) {
        case -1: node.bf = 0; heightChanged = false; break;
        case 0: node.bf = 1; break;
        case 1: node = balanceToLeft(node); heightChanged = false; break;
        }
      }
    } else {
      heightChanged = false;
    }
    return node;
  }
  //-------------------------------------------------------
  //EJERCICIO 3
  //-------------------------------------------------------  
  public void remove(E data) {
    root = removeRec((NodeAVL<E>) root, data);
  }

  private NodeAVL<E> removeRec(NodeAVL<E> node, E data) {
    if (node == null) {
      heightChanged = false;
      return null;
    }

    int cmp = data.compareTo(node.data);
    if (cmp < 0) {
      node.left = removeRec((NodeAVL<E>) node.left, data);
      if (heightChanged) {
        node = balanceOnRemoveLeft(node);
      }
    } else if (cmp > 0) {
      node.right = removeRec((NodeAVL<E>) node.right, data);
      if (heightChanged) {
        node = balanceOnRemoveRight(node);
      }
    } else {
      if (node.left == null || node.right == null) {
        heightChanged = true;
        return (NodeAVL<E>) (node.left != null ? node.left : node.right);
      } else {
        NodeAVL<E> successor = getMin((NodeAVL<E>) node.right);
        node.data = successor.data;
        node.right = removeRec((NodeAVL<E>) node.right, successor.data);
        if (heightChanged) {
          node = balanceOnRemoveRight(node);
        }
      }
    }
    return node;
  }

  private NodeAVL<E> getMin(NodeAVL<E> node) {
    while (node.left != null) node = (NodeAVL<E>) node.left;
    return node;
  }

  private NodeAVL<E> balanceOnRemoveLeft(NodeAVL<E> node) {
    switch (node.bf) {
    case -1: node.bf = 0; break;
    case 0: node.bf = 1; heightChanged = false; break;
    case 1:
      NodeAVL<E> rightChild = (NodeAVL<E>) node.right;
      int bfr = rightChild.bf;
      node = balanceToLeft(node);
      if (bfr == 0) heightChanged = false;
      break;
    }
    return node;
  }

  private NodeAVL<E> balanceOnRemoveRight(NodeAVL<E> node) {
    switch (node.bf) {
    case 1: node.bf = 0; break;
    case 0: node.bf = -1; heightChanged = false; break;
    case -1:
      NodeAVL<E> leftChild = (NodeAVL<E>) node.left;
      int bfl = leftChild.bf;
      node = balanceToRight(node);
      if (bfl == 0) heightChanged = false;
      break;
    }
    return node;
  }

  private NodeAVL<E> balanceToLeft(NodeAVL<E> node) {
    NodeAVL<E> hijo = (NodeAVL<E>) node.right;
    if (hijo.bf == 1) { // RSL
      node.bf = 0;
      hijo.bf = 0;
      return rotateSL(node);
    } else if (hijo.bf == -1) { // RDL
      NodeAVL<E> nieto = (NodeAVL<E>) hijo.left;
      if (nieto.bf == 1) { node.bf = -1; hijo.bf = 0; }
      else if (nieto.bf == -1) { node.bf = 0; hijo.bf = 1; }
      else { node.bf = 0; hijo.bf = 0; }
      nieto.bf = 0;
      node.right = rotateSR(hijo);
      return rotateSL(node);
    } else { // hijo.bf == 0 (Caso de eliminación)
      node.bf = 1;
      hijo.bf = -1;
      return rotateSL(node);
    }
  }

  private NodeAVL<E> balanceToRight(NodeAVL<E> node) {
    NodeAVL<E> hijo = (NodeAVL<E>) node.left;
    if (hijo.bf == -1) { // RSR
      node.bf = 0;
      hijo.bf = 0;
      return rotateSR(node);
    } else if (hijo.bf == 1) { // RDR
      NodeAVL<E> nieto = (NodeAVL<E>) hijo.right;
      if (nieto.bf == -1) { node.bf = 1; hijo.bf = 0; }
      else if (nieto.bf == 1) { node.bf = 0; hijo.bf = -1; }
      else { node.bf = 0; hijo.bf = 0; }
      nieto.bf = 0;
      node.left = rotateSL(hijo);
      return rotateSR(node);
    } else { // hijo.bf == 0 (Caso de eliminación)
      node.bf = -1;
      hijo.bf = 1;
      return rotateSR(node);
    }
  }

  private NodeAVL<E> rotateSL(NodeAVL<E> node) {
    NodeAVL<E> hijo = (NodeAVL<E>) node.right;
    node.right = hijo.left;
    hijo.left = node;
    return hijo;
  }

  private NodeAVL<E> rotateSR(NodeAVL<E> node) {
    NodeAVL<E> hijo = (NodeAVL<E>) node.left;
    node.left = hijo.right;
    hijo.right = node;
    return hijo;
  }

  public void inOrder() {
    inOrderRec((NodeAVL<E>) root);
    System.out.println();
  }

  private void inOrderRec(NodeAVL<E> node) {
    if (node != null) {
      inOrderRec((NodeAVL<E>) node.left);
      System.out.print(node.data + "(" + node.bf + ") ");
      inOrderRec((NodeAVL<E>) node.right);
    }
  }

  // ==========================================
  // EJERCICIOS 4 Y 5: RECORRIDO POR AMPLITUD RECURSIVO
  // ==========================================
  public void breadthFirst() {
    int h = height((NodeAVL<E>) root);
    for (int i = 1; i <= h; i++) {
      printGivenLevel((NodeAVL<E>) root, i);
    }
    System.out.println();
  }

  private int height(NodeAVL<E> node) {
    if (node == null) return 0;
    return 1 + Math.max(height((NodeAVL<E>) node.left), height((NodeAVL<E>) node.right));
  }

  private void printGivenLevel(NodeAVL<E> node, int level) {
    if (node == null) return;
    if (level == 1) {
      System.out.print(node.data + "(" + node.bf + ") ");
    } else if (level > 1) {
      printGivenLevel((NodeAVL<E>) node.left, level - 1);
      printGivenLevel((NodeAVL<E>) node.right, level - 1);
    }
  }

  // ==========================================
  // EJERCICIO 6: RECORRIDO EN PREORDEN
  // ==========================================
  public void preOrder() {
    preOrderRec((NodeAVL<E>) root);
    System.out.println();
  }

  private void preOrderRec(NodeAVL<E> node) {
    if (node != null) {
      System.out.print(node.data + "(" + node.bf + ") ");
      preOrderRec((NodeAVL<E>) node.left);
      preOrderRec((NodeAVL<E>) node.right);
    }
  }
}
