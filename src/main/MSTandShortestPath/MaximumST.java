package main.MSTandShortestPath;

import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

import java.util.PriorityQueue;

/**
 * use lazyprim to compute maximum-weight spanning forest
 */
public class MaximumST {

    private final EdgeWeightedGraph g;
    private Edge[] edgeTo;


    public MaximumST(EdgeWeightedGraph g) {
        this.g = g;
    }

    private void prim(int v, boolean[] inT) {
        inT[v] = true;
        PriorityQueue<Edge> pq = new PriorityQueue<>((o1, o2) -> {
            double w1 = -o1.weight();
            double w2 = -o2.weight();
            if (w1 < w2) {
                return -1;
            } else {
                if (w1 > w2) {
                    return 1;
                } else {
                    int v11 = o1.either();
                    int v12 = o1.other(v11);
                    int v1 = Math.max(v11, v12);
                    int u1 = Math.min(v11, v12);
                    int v21 = o2.either();
                    int v22 = o2.other(v21);
                    int v2 = Math.max(v21, v22);
                    int u2 = Math.min(v21, v22);
                    if (v1 == v2) {
                        return u1 - u2;
                    } else {
                        return v1 - v2;
                    }
                }
            }
        });
        for (Edge e:g.adj(v)) {
            // to be compatible with parallel edge
            if (!inT[e.other(v)]) {
                pq.add(e);
            }
        }
        while (!pq.isEmpty()) {
            Edge e = pq.poll();
            int s = e.either();
            int t = e.other(s);
            if (!inT[s] || !inT[t]) {
                int curV = 0;
                if (!inT[s]) {
                    curV = s;
                } else {
                    curV = t;
                }
                inT[curV] = true;
                edgeTo[curV] = e;
                for (Edge edge:g.adj(curV)) {
                    if (!inT[edge.other(curV)]) {
                        pq.add(edge);
                    }
                }
            }
        }
    }

    private void compute() {
        if (edgeTo != null) return;
        boolean[] inT = new boolean[g.V()];
        edgeTo = new Edge[g.V()];
        for (int i = 0; i < g.V(); i ++) {
            if (!inT[i]) {
                prim(i, inT);
            }
        }
    }

    public Iterable<Edge> edges() {
        compute();
        assert edgeTo != null;
        Queue<Edge> maximumst = new Queue<>();
        for (int i = 0; i < edgeTo.length; i ++) {
            Edge e = edgeTo[i];
            if (e != null) {
                maximumst.enqueue(e);
            }
        }
        return maximumst;
    }


    public static void main(String[] args) {
        EdgeWeightedGraph g = new EdgeWeightedGraph(new In(args[0]));
        MaximumST maximumST = new MaximumST(g);
        double weight = 0.0;
        for (Edge e: maximumST.edges()) {
            System.out.println(e);
            weight += e.weight();
        }
        System.out.println("weight: " + weight);
    }
}
