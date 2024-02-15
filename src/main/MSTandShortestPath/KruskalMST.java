package main.MSTandShortestPath;

import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import main.UF.UF;
import main.UF.UF2;

import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * compatible with unconnected graph: compute minimum spanning forest
 */
public class KruskalMST {
    private final EdgeWeightedGraph g;
    private final UF uf;
    private Queue<Edge> edges;
    private double weight;

    public KruskalMST(EdgeWeightedGraph g, UF uf) {
        if (g.V() != uf.n) throw new IllegalArgumentException("UF's n is different from graph's V");
        this.g = g;
        this.uf = uf;
    }

    private void computeMST() {
        if (edges != null) return;
        edges = new Queue<>();
        LinkedList<Edge> candidates = new LinkedList<>();
        for (Edge e:g.edges()) {
            candidates.add(e);
        }
        PriorityQueue<Edge> pq = new PriorityQueue<>(candidates);
        int added = 0;
        Edge e;
        while ((e = pq.poll()) != null) {
            int u = e.either();
            int v = e.other(u);
            if (uf.union(u, v)) {
                edges.enqueue(e);
                weight += e.weight();
                added ++;
                if (added == g.V() - 1) return;
            }
        }
    }

    public Iterable<Edge> edges() {
        computeMST();
        assert edges != null;
        return edges;
    }

    public double weight() {
        computeMST();
        assert edges != null;
        return weight;
    }

    public static void main(String[] args) {
        EdgeWeightedGraph g = new EdgeWeightedGraph(new In(args[0]));
        UF uf = new UF2(g.V());
        KruskalMST mst = new KruskalMST(g, uf);
        for (Edge e: mst.edges()) {
            System.out.println(e);
        }
        System.out.println("weight: " + mst.weight());
    }

}
