public class AVLTree implements BinarySearchTree {
    private static class Node {
        private int data;
        private Node left;
        private Node right;
        private int height;

        public Node(int data) {
            this.data = data;
            this.height = 1;
        }
    }

    private Node root;
    private int size;
    private int[] traversalArray;
    private int arrayRealSize = 0;

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
    public void insert(int data) {
        root = insert(root, data);
    }

    private Node insert(Node node, int data) {
        if (node == null) {
            size++;
            return new Node(data);
        }

        if (data < node.data) {
            node.left = insert(node.left, data);
        } else if (data > node.data) {
            node.right = insert(node.right, data);
        } else {
            return node;
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));

        int balance = getBalance(node);

        if (balance > 1 && data < node.left.data) {
            return rightRotate(node);
        }

        if (balance < -1 && data > node.right.data) {
            return leftRotate(node);
        }

        if (balance > 1 && data > node.left.data) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && data < node.right.data) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    public void delete(int data) {
        root = delete(root, data);
    }

    private Node delete(Node node, int data) {
        if (node == null) {
            return node;
        }

        if (data < node.data) {
            node.left = delete(node.left, data);
        } else if (data > node.data) {
            node.right = delete(node.right, data);
        } else {
            if (node.left == null || node.right == null) {
                Node temp = null;

                if (temp == node.left) {
                    temp = node.right;
                } else {
                    temp = node.left;
                }

                if (temp == null) {
                    temp = node;
                    node = null;
                } else {
                    node = temp;
                }
                size--;
            } else {
                Node temp = min(node.right);
                node.data = temp.data;
                node.right = delete(node.right, temp.data);
            }
        }

        if (node == null) {
            return node;
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));

        int balance = getBalance(node);

        if (balance > 1 && getBalance(node.left) >= 0) {
            return rightRotate(node);
        }

        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && getBalance(node.right) <= 0) {
            return leftRotate(node);
        }

        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    private Node min(Node node) {
        Node current = node;

        while (current.left != null) {
            current = current.left;
        }

        return current;
    }

    public int getSize() {
        return size;
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

    private int height(Node node) {
        if (node == null) {
            return 0;
        }

        return node.height;
    }

    private int getBalance(Node node) {
        if (node == null) {
            return 0;
        }

        return height(node.left) - height(node.right);
    }

    private Node rightRotate(Node node) {
        Node left = node.left;
        Node right = left.right;

        left.right = node;
        node.left = right;

        node.height = 1 + Math.max(height(node.left), height(node.right));
        left.height = 1 + Math.max(height(left.left), height(left.right));

        return left;
    }

    private Node leftRotate(Node node) {
        Node right = node.right;
        Node left = right.left;

        right.left = node;
        node.right = left;

        node.height = 1 + Math.max(height(node.left), height(node.right));
        right.height = 1 + Math.max(height(right.left), height(right.right));

        return right;
    }
}