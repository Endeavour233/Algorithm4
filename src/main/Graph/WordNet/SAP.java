package main.Graph.WordNet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class SAP {
    private final Digraph g;
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph g) {
        Digraph copied = new Digraph(g.V());
        for (int u = 0; u < g.V(); u ++) {
            for (int v:g.adj(u)) {
                copied.addEdge(u, v);
            }
        }
        this.g = copied;
    }

    private void checkNull(Object obj) {
        if (obj == null) throw new IllegalArgumentException("null argument");
    }

    private void bfs(Iterable<Integer> srcs, boolean[] marked, int[] dist) {
        Queue<Integer> q = new Queue<>();
        for (Integer src:srcs) {
            if (src >= g.V() || src < 0) throw new IllegalArgumentException("invalid vertex: " + src);
            q.enqueue(src);
            marked[src] = true;
        }
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int u:g.adj(v)) {
                if (!marked[u]) {
                    dist[u] = dist[v] + 1;
                    marked[u] = true;
                    q.enqueue(u);
                }
            }
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        ArrayList<Integer> srcs1 = new ArrayList<>();
        srcs1.add(v);
        ArrayList<Integer> srcs2 = new ArrayList<>();
        srcs2.add(w);
        return length(srcs1, srcs2);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        ArrayList<Integer> srcs1 = new ArrayList<>();
        srcs1.add(v);
        ArrayList<Integer> srcs2 = new ArrayList<>();
        srcs2.add(w);
        return ancestor(srcs1, srcs2);
    }

    private int intersect(Iterable<Integer> v, Iterable<Integer> w) {
        checkNull(v);
        checkNull(w);
        Set<Integer> vSet = new HashSet<>();
        for (Integer i:v) {
            checkNull(i);
            vSet.add(i);
        }
        for (Integer i:w) {
            checkNull(i);
            if (vSet.contains(i)) {
                return i;
            }
        }
        return -1;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int intersect = intersect(v, w); // check null has finished
        if (intersect != -1) return 0;
        boolean[] markedFromV = new boolean[g.V()];
        int[] distFromV = new int[g.V()];
        bfs(v, markedFromV, distFromV);
        boolean[] markedFromW = new boolean[g.V()];
        int[] distFromW = new int[g.V()];
        bfs(w, markedFromW, distFromW);
        int len = -1;
        for (int i = 0; i < g.V(); i ++) {
            if (markedFromW[i] && markedFromV[i]) {
                int curLen = distFromW[i] + distFromV[i];
                if (len == -1 || curLen < len) {
                    len = curLen;
                }
            }
        }
        return len;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        int intersect = intersect(v, w); // check null has finished
        if (intersect != -1) return intersect;
        boolean[] markedFromV = new boolean[g.V()];
        int[] distFromV = new int[g.V()];
        bfs(v, markedFromV, distFromV);
        boolean[] markedFromW = new boolean[g.V()];
        int[] distFromW = new int[g.V()];
        bfs(w, markedFromW, distFromW);
        int len = -1;
        int ancestor = -1;
        for (int i = 0; i < g.V(); i ++) {
            if (markedFromW[i] && markedFromV[i]) {
                int curLen = distFromW[i] + distFromV[i];
                if (len == -1 || curLen < len) {
                    len = curLen;
                    ancestor = i;
                }
            }
        }
        return ancestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
