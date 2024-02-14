package main.PriorityQueue;

public class IndexMinPQ<Key extends Comparable<Key>> {

    private int[] heap;
    private int[] heapPos;
    private Key[] keys;

    private int capacity;

    private int n;

    /**
     *
     * @param capacity can insert i in [0, capacity)
     */
    public IndexMinPQ(int capacity) {
        heap = new int[capacity + 1];
        heapPos = new int[capacity];
        keys = (Key[]) new Comparable[capacity];
        this.capacity = capacity;
    }

    /**
     * find the heapPos that stores the index associated with a smaller key
     * @param l a heapPos, must be smaller than or equal to {@code n}
     * @param r a heapPos, can be larger than {@code n}
     * @return {@code l} if {@code r} is not a valid heapPos or  heap[l] < heap[r];
     *         {@code r} otherwise
     */
    private int min(int l, int r) {
        if (r > n) return l;
        if (less(heap[l], heap[r])) return l;
        return r;
    }

    /**
     * check whether the key associated with {@code i} is strictly smaller than that of {@code j}
     * @param i an index
     * @param j the other index
     * @return {@code true} if the key associated with {@code i} is strictly smaller
     *         {@code false} otherwise
     */
    private boolean less(int i, int j) {
       return keys[i].compareTo(keys[j]) < 0;
    }

    private void swap(int i, int j) {
        int tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
        heapPos[heap[i]] = i;
        heapPos[heap[j]] = j;
    }

    private void swim(int k) {
        while (k > 1) {
            int p = k / 2;
            if (less(heap[k], heap[p])) {
                swap(k, p);
                k = p;
            } else {
                break;
            }
        }
    }

    private void sink(int i) {
        int l;
        while ((l = i * 2) <= n) {
            int r = l + 1;
            int minPos = min(l, r);
            if (less(heap[minPos], heap[i])) {
                swap(minPos, i);
                i = minPos;
            } else {
                break;
            }
        }
    }

    private void checkValidity(int i) {
        if (i < 0 || i >= capacity) throw new IllegalArgumentException("i should be in [0, " + capacity + ")");
    }

    /**
     * insert i
     * @param i an index
     * @param key key associated with i
     * @throws IllegalArgumentException if i has been inserted before
     */

    public void insert(int i, Key key) {
        checkValidity(i);
        if (contains(i)) throw new IllegalArgumentException("i has been inserted yet");
        keys[i] = key;
        n ++;
        heap[n] = i;
        heapPos[i] = n;
        swim(n);
    }

    /**
     * whether {@code i} has been inserted
     *
     * @param  i an index
     * @return {@code true} if {@code i} has been inserted
     *         {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= i < capacity}
     */
    public boolean contains(int i) {
        checkValidity(i);
        return heapPos[i] != 0;
    }


    /**
     * delete the index associated with the smallest key and return it
     * @return the index associated with the smallest key if pq is not empty;
     *         {@code -1} otherwise
     */
    public int deleteMin() {
        if (n < 1) return -1;
        int result = heap[1];
        swap(1, n);
        heapPos[result] = 0;
        n --;
        sink(1);
        return result;
    }

    public void decreaseKey(int i, Key key) {
        if (!contains(i)) throw new IllegalArgumentException("i is not in pq");
        int pos = heapPos[i];
        keys[i] = key;
        swim(pos);
    }
    public static void main(String[] args) {

    }
}
