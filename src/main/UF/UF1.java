package main.UF;

import java.util.Arrays;

public class UF1 extends UF {

    private int[] size;
    private int[] father;
    public UF1(int n) {
        super(n);
        size = new int[n];
        father = new int[n];
        Arrays.fill(size, 1);
        for (int i = 0; i < n; i ++) {
            father[i] = i;
        }
    }

    private int find(int i) {
        while (father[i] != i) {
            father[i] = father[father[i]];
            i = father[i];
        }
        return i;
    }

    @Override
    public boolean union(int i, int j) {
        checkValidity(i);
        checkValidity(j);
        int ancestori = find(i);
        int ancestorj = find(j);
        if (ancestori == ancestorj) {
            return false;
        }
        if (size[ancestori] < size[ancestorj]) {
            father[ancestori] = ancestorj;
            size[ancestorj] += size[ancestori];
        } else {
            father[ancestorj] = ancestori;
            size[ancestori] += size[ancestorj];
        }
        return true;
    }

    public static void main(String[] args) {

    }
}
