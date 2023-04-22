public class RedBlackTree implements BinarySearchTree {

    private Node root;
    private int size = 0;
    private int[] traversalArray;
    private int arrayRealSize = 0;

    private static class Node {
        int data;
        boolean isRed;
        Node left;
        Node right;

        Node(int data, boolean isRed) {
            this.data = data;
            this.isRed = isRed;
            this.left = null;
            this.right = null;
        }
    }

    public int getSize() {
        return size;
    }

    public int getTreeHeight() {
        return root == null ? 0 : treeHeight(root);
    }

    private int treeHeight(Node node) {
        int l = 1, r = 1;
        if (node.left != null){
            l = 1 + treeHeight(node.left);
        }
        if (node.right != null){
            r = 1 + treeHeight(node.right);
        }
        return Math.max(l, r);
    }

    public int[] toArray() {
        return getInorderTraversalArray();
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
        traversalArray[arrayRealSize++] = node.data;
        if (node.right != null) {
            inorderTraversal(node.right);
        }
    }

    public void insert(int value) {
        root = insert(root, value);
        root.isRed = false;
    }

    private Node insert(Node node, int value) {
        if (node == null) {
            size++;
            return new Node(value, true);
        }

        if (value < node.data) {
            node.left = insert(node.left, value);
        } else if (value > node.data) {
            node.right = insert(node.right, value);
        } else {
            return node;
        }
        
        if (isRed(node.right) && !isRed(node.left)) {
            node = rotateLeft(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }

        return node;
    }

    public void delete(int value) {
        root = delete(root, value);
        if (root != null) {
            root.isRed = false;
        }
    }

    private Node delete(Node node, int value) {
        if (node == null) {
            return null;
        }

        if (value < node.data) {
            node.left = delete(node.left, value);
        } else if (value > node.data) {
            node.right = delete(node.right, value);
        } else {
            if (node.left == null) {
                size--;
                return node.right;
            } else if (node.right == null) {
                size--;
                return node.left;
            } else {
                Node minNode = findMin(node.right);
                node.data = minNode.data;
                node.right = delete(node.right, minNode.data);
            }
        }

        // balance the tree
        if (isRed(node.right) && !isRed(node.left)) {
            node = rotateLeft(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }

        return node;
    }

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node rotateLeft(Node node) {
        Node newRoot = node.right;
        node.right = newRoot.left;
        newRoot.left = node;
        newRoot.isRed = node.isRed;
        node.isRed = true;
        return newRoot;
    }

    private Node rotateRight(Node node) {
        Node newRoot = node.left;
        node.left = newRoot.right;
        newRoot.right = node;
        newRoot.isRed = node.isRed;
        node.isRed = true;
        return newRoot;
    }

    private void flipColors(Node node) {
        node.isRed = true;
        node.left.isRed = false;
        node.right.isRed = false;
    }

    private boolean isRed(Node node) {
        if (node == null) {
            return false;
        }
        return node.isRed;
    }
}