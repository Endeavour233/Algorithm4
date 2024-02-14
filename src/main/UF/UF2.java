package main.UF;

import edu.princeton.cs.algs4.Bag;

/**
 * for kruskalmst
 */
public class UF2 extends UF {
    private int[] comp;
    private int[] size;
    private Bag<Integer>[] adjs;

    public UF2(int n) {
        super(n);
        comp = new int[n];
        size = new int[n];
        adjs = (Bag<Integer>[]) new Bag[n];
        for (int i = 0; i < n; i ++) {
            comp[i] = i;
            size[i] = 1;
            adjs[i] = new Bag<>();
        }
    }

    /**
     * label all the vertices reachable from {@code i} with {@code targetComp}
     * @return the number of vertices labelled
     */
    private int dfsAndLabel(int i, int targetComp) {
        comp[i] = targetComp;
        int cnt = 1;
        for (int j:adjs[i]) {
            if (comp[j] != targetComp) {
                cnt += dfsAndLabel(j, targetComp);
            }
        }
        return cnt;
    }

    @Override
    public boolean union(int i, int j) {
        checkValidity(i);
        checkValidity(j);
        int compi = comp[i];
        int compj = comp[j];
        if (compi == compj) return false;
        if (size[compi] < size[compj]) {
            int cnt = dfsAndLabel(i, compj);
            size[compj] += cnt;
        } else {
            int cnt = dfsAndLabel(j, compi);
            size[compi] += cnt;
        }
        adjs[i].add(j);
        adjs[j].add(i);
        return true;
    }
}
