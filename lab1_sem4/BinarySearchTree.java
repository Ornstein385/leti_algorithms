public interface BinarySearchTree {
    void insert(int key);

    void delete(int key);

    int getSize();

    int[] toArray();

    int getTreeHeight();
}
