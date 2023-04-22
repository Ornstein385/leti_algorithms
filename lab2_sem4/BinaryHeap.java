public class BinaryHeap {
    private int[] heap;
    private int size;

    public BinaryHeap() {
        heap = new int[10];
        size = 0;
    }

    public void push(int value) {
        if (size >= heap.length - 1) {
            int[] temp = new int[heap.length * 2];
            System.arraycopy(heap, 0, temp, 0, heap.length);
            heap = temp;
        }
        size++;
        int index = size;
        heap[index] = value;
        while (index > 1 && heap[index] < heap[index / 2]) {
            int temp = heap[index];
            heap[index] = heap[index / 2];
            heap[index / 2] = temp;
            index = index / 2;
        }
    }

    public int pop() {
        if (size == 0) {
            throw new IndexOutOfBoundsException();
        }
        int result = heap[1];
        heap[1] = heap[size];
        heap[size] = 0;
        size--;
        int index = 1;
        while (true) {
            int child = index * 2;
            if (child > size) {
                break;
            }
            if (child < size && heap[child] > heap[child + 1]) {
                child = child + 1;
            }
            if (heap[index] > heap[child]) {
                int temp = heap[index];
                heap[index] = heap[child];
                heap[child] = temp;
                index = child;
            } else {
                break;
            }
        }
        return result;
    }

    public int peek() {
        if (size == 0) {
            throw new IndexOutOfBoundsException();
        }
        return heap[1];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int getSize() {
        return size;
    }
}