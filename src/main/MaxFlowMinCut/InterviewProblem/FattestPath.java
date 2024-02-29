package main.MaxFlowMinCut.InterviewProblem;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;

import java.util.PriorityQueue;

public class FattestPath {

    private double[] width;
    private DirectedEdge[] edgeTo;
    public FattestPath(EdgeWeightedDigraph g, int s) {
        width = new double[g.V()];
        edgeTo = new DirectedEdge[g.V()];
        boolean[] marked = new boolean[g.V()];
        for (int i = 0; i < g.V(); i ++) {
            width[i] = Double.NEGATIVE_INFINITY;
        }
        width[s] = Double.POSITIVE_INFINITY;
        marked[s] = true;
        PriorityQueue<DirectedEdge> pq = new PriorityQueue<>((o1, o2) -> Double.compare(Math.min(width[o2.from()], o2.weight()), Math.min(width[o1.from()], o1.weight())));
        for (DirectedEdge edge:g.adj(s)) {
            pq.add(edge);
        }
        while (!pq.isEmpty()) {
            DirectedEdge e = pq.poll();
            int u = e.to();
            if (!marked[u]) {
                marked[u] = true;
                for (DirectedEdge edge:g.adj(u)) {
                    int v = edge.to();
                    double newWidth = Math.min(width[u], edge.weight());
                    if (newWidth > width[v]) {
                        width[v] = newWidth;
                        edgeTo[v] = edge;
                        pq.add(edge);
                    }
                }
            }
        }
    }

    public boolean hasPathTo(int v) {
        return width[v] != Double.NEGATIVE_INFINITY;
    }

    public Iterable<DirectedEdge> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Bag<DirectedEdge> st = new Bag<>();
        DirectedEdge e;
        while ((e = edgeTo[v]) != null) {
            st.add(e);
            v = e.from();
        }
        return st;
    }

    public static void main(String[] args) {

    }
}
