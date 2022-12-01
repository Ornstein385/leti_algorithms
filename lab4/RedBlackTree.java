import java.awt.*;
import java.util.LinkedList;

class Node {
    int value;
    Node left;
    Node right;
    Node parent;
    Color color;

    public Node(int value) {
        this.value = value;
    }
}


class RedBlackTree {

    class NilNode extends Node {
        private NilNode() {
            super(0);
            this.color = Color.BLACK;
        }
    }

    public Node root;
    private int size = 0;

    private int[] traversalArray;
    private int arrayRealSize = 0;

    public int getSize() {
        return size;
    }

    public int getMaxHeight() {
        if (root == null) {
            return 1;
        }
        return maxHeight(root, 1);
    }

    private int maxHeight(Node node, int h) {
        if (node.left != null && node.right != null) {
            return Math.max(maxHeight(node.left, h + 1), maxHeight(node.right, h + 1));
        } else if (node.left != null) {
            return maxHeight(node.left, h + 1);
        } else if (node.right != null) {
            return maxHeight(node.right, h + 1);
        } else {
            return h;
        }
    }

    public int[] getWidthTraversalArray() {
        traversalArray = new int[size];
        arrayRealSize = 0;
        if (root == null) {
            return traversalArray;
        }
        LinkedList<Node> queue = new LinkedList<>();
        queue.addLast(root);
        while (!queue.isEmpty()) {
            Node node = queue.pollFirst();
            traversalArray[arrayRealSize++] = node.value;
            if (node.left != null) {
                queue.addLast(node.left);
            }
            if (node.right != null) {
                queue.addLast(node.right);
            }
        }
        return traversalArray;
    }

    public int[] getPreorderTraversalArray() {
        traversalArray = new int[size];
        arrayRealSize = 0;
        if (root == null) {
            return traversalArray;
        }
        preorderTraversal(root);
        return traversalArray;
    }

    private void preorderTraversal(Node node) {
        traversalArray[arrayRealSize++] = node.value;
        if (node.left != null) {
            preorderTraversal(node.left);
        }
        if (node.right != null) {
            preorderTraversal(node.right);
        }
    }

    public int[] getInorderTraversalArray() {
        traversalArray = new int[size];
        arrayRealSize = 0;
        if (root == null) {
            return traversalArray;
        }
        inorderTraversal(root);
        return traversalArray;
    }

    private void inorderTraversal(Node node) {
        if (node.left != null) {
            inorderTraversal(node.left);
        }
        traversalArray[arrayRealSize++] = node.value;
        if (node.right != null) {
            inorderTraversal(node.right);
        }
    }

    public int[] getPostorderTraversalArray() {
        traversalArray = new int[size];
        arrayRealSize = 0;
        if (root == null) {
            return traversalArray;
        }
        postorderTraversal(root);
        return traversalArray;
    }

    private void postorderTraversal(Node node) {
        if (node.left != null) {
            postorderTraversal(node.left);
        }
        if (node.right != null) {
            postorderTraversal(node.right);
        }
        traversalArray[arrayRealSize++] = node.value;
    }

    private void replaceParentsChild(Node parent, Node oldChild, Node newChild) {
        if (parent == null) {
            root = newChild;
        } else if (parent.left == oldChild) {
            parent.left = newChild;
        } else if (parent.right == oldChild) {
            parent.right = newChild;
        } else {
            //throw new IllegalStateException("у родителя отсутствуют дети");
            return;
        }

        if (newChild != null) {
            newChild.parent = parent;
        }
    }

    public int minValue() {
        if (root == null) {
            throw new IllegalStateException("в дереве нет элементов");
        }
        Node node = root;
        while (root.left != null) {
            node = node.left;
        }
        return node.value;
    }

    public int maxValue() {
        if (root == null) {
            throw new IllegalStateException("в дереве нет элементов");
        }
        Node node = root;
        while (root.right != null) {
            node = node.right;
        }
        return node.value;
    }

    private void rotateRight(Node node) {
        Node parent = node.parent;
        Node leftChild = node.left;
        node.left = leftChild.right;
        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }
        leftChild.right = node;
        node.parent = leftChild;
        replaceParentsChild(parent, node, leftChild);
    }

    private void rotateLeft(Node node) {
        Node parent = node.parent;
        Node rightChild = node.right;
        node.right = rightChild.left;
        if (rightChild.left != null) {
            rightChild.left.parent = node;
        }
        rightChild.left = node;
        node.parent = rightChild;
        replaceParentsChild(parent, node, rightChild);
    }

    public void insertNode(int key) {
        Node node = root;
        Node parent = null;
        while (node != null) {
            parent = node;
            if (key < node.value) {
                node = node.left;
            } else if (key > node.value) {
                node = node.right;
            } else {
                return;
            }
        }
        Node newNode = new Node(key);
        newNode.color = Color.RED;
        size++;
        if (parent == null) {
            root = newNode;
        } else if (key < parent.value) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        newNode.parent = parent;
        fixRedBlackPropertiesAfterInsert(newNode);
    }

    private void fixRedBlackPropertiesAfterInsert(Node node) {
        Node parent = node.parent;
        if (parent == null) {
            return;
        }
        if (parent.color == Color.BLACK) {
            return;
        }
        Node grandparent = parent.parent;
        if (grandparent == null) {
            parent.color = Color.BLACK;
            return;
        }
        Node uncle = getUncle(parent);
        if (uncle != null && uncle.color == Color.RED) {
            parent.color = Color.BLACK;
            grandparent.color = Color.RED;
            uncle.color = Color.BLACK;
            fixRedBlackPropertiesAfterInsert(grandparent);
        }
        else if (parent == grandparent.left) {
            if (node == parent.right) {
                rotateLeft(parent);
                parent = node;
            }
            rotateRight(grandparent);
            parent.color = Color.BLACK;
            grandparent.color = Color.RED;
        }
        else {
            if (node == parent.left) {
                rotateRight(parent);
                parent = node;
            }
            rotateLeft(grandparent);
            parent.color = Color.BLACK;
            grandparent.color = Color.RED;
        }
    }

    private Node getUncle(Node parent) {
        Node grandparent = parent.parent;
        if (grandparent.left == parent) {
            return grandparent.right;
        } else if (grandparent.right == parent) {
            return grandparent.left;
        } else {
            throw new IllegalStateException("родитель не является ребенком дедушки");
        }
    }

    public void deleteNode(int key) {
        Node node = root;
        while (node != null && node.value != key) {
            if (key < node.value) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        if (node == null) {
            return;
        } else {
            size--;
        }
        Node movedUpNode;
        Color deletedNodeColor;
        if (node.left == null || node.right == null) {
            movedUpNode = deleteNodeWithZeroOrOneChild(node);
            deletedNodeColor = node.color;
        }
        else {
            Node inOrderSuccessor = findMinimum(node.right);
            node.value = inOrderSuccessor.value;
            movedUpNode = deleteNodeWithZeroOrOneChild(inOrderSuccessor);
            deletedNodeColor = inOrderSuccessor.color;
        }
        if (deletedNodeColor == Color.BLACK) {
            fixRedBlackPropertiesAfterDelete(movedUpNode);
            if (movedUpNode.getClass() == NilNode.class) {
                replaceParentsChild(movedUpNode.parent, movedUpNode, null);
            }
        }
    }

    private Node deleteNodeWithZeroOrOneChild(Node node) {
        if (node.left != null) {
            replaceParentsChild(node.parent, node, node.left);
            return node.left; // moved-up node
        }
        else if (node.right != null) {
            replaceParentsChild(node.parent, node, node.right);
            return node.right; // moved-up node
        }
        else {
            Node newChild = node.color == Color.BLACK ? new NilNode() : null;
            replaceParentsChild(node.parent, node, newChild);
            return newChild;
        }
    }

    private Node findMinimum(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private void fixRedBlackPropertiesAfterDelete(Node node) {
        if (node == root) {
            return;
        }
        Node sibling = getSibling(node);
        if (sibling.color == Color.RED) {
            handleRedSibling(node, sibling);
            sibling = getSibling(node);
        }
        if (isBlack(sibling.left) && isBlack(sibling.right)) {
            sibling.color = Color.RED;
            if (node.parent.color == Color.RED) {
                node.parent.color = Color.BLACK;
            }
            else {
                fixRedBlackPropertiesAfterDelete(node.parent);
            }
        }
        else {
            handleBlackSiblingWithAtLeastOneRedChild(node, sibling);
        }
    }

    private boolean isBlack(Node node) {
        return node == null || node.color == Color.BLACK;
    }

    private Node getSibling(Node node) {
        Node parent = node.parent;
        if (node == parent.left) {
            return parent.right;
        } else if (node == parent.right) {
            return parent.left;
        } else {
            throw new IllegalStateException("родитель не является ребенком дедушки");
        }
    }

    private void handleRedSibling(Node node, Node sibling) {
        sibling.color = Color.BLACK;
        node.parent.color = Color.BLACK;
        if (node == node.parent.left) {
            rotateLeft(node.parent);
        } else {
            rotateRight(node.parent);
        }
    }

    private void handleBlackSiblingWithAtLeastOneRedChild(Node node, Node sibling) {
        boolean nodeIsLeftChild = node == node.parent.left;
        if (nodeIsLeftChild && isBlack(sibling.right)) {
            sibling.left.color = Color.BLACK;
            sibling.color = Color.RED;
            rotateRight(sibling);
            sibling = node.parent.right;
        } else if (!nodeIsLeftChild && isBlack(sibling.left)) {
            sibling.right.color = Color.BLACK;
            sibling.color = Color.RED;
            rotateLeft(sibling);
            sibling = node.parent.left;
        }
        sibling.color = node.parent.color;
        node.parent.color = Color.BLACK;
        if (nodeIsLeftChild) {
            sibling.right.color = Color.BLACK;
            rotateLeft(node.parent);
        } else {
            sibling.left.color = Color.BLACK;
            rotateRight(node.parent);
        }
    }
}