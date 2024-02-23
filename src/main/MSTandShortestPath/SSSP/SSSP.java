package main.MSTandShortestPath.SSSP;


import edu.princeton.cs.algs4.*;
import kotlin.Pair;

public abstract class SSSP {
    protected final EdgeWeightedDigraph g;
    protected final int s;
    protected double[] dist;
    protected DirectedEdge[] edgeTo;
    public SSSP(EdgeWeightedDigraph g, int s) {
        this.g = g;
        this.s = s;
    }


    abstract protected void compute();

    public boolean hasPathTo(int v) {
        compute();
        assert dist != null;
        return dist[v] != Double.POSITIVE_INFINITY;
    }


    public double distTo(int v) {
        compute();
        assert dist != null;
        return dist[v];
    }


    public Iterable<DirectedEdge> pathTo(int v) {
        compute();
        assert dist != null;
        if (!hasPathTo(v)) return null;
        Stack<DirectedEdge> st = new Stack<>();
        DirectedEdge edge;
        while ((edge = edgeTo[v]) != null) {
            st.push(edge);
            v = edge.from();
        }
        return st;
    }

    protected static void unitTest(SSSP sp, EdgeWeightedDigraph g, int s) {
        for (int i = 0; i < g.V(); i ++) {
            if (sp.hasPathTo(i)) {
                StdOut.printf("%d to %d (%.2f) ", s, i, sp.distTo(i));
                for (DirectedEdge edge:sp.pathTo(i)) {
                    StdOut.print(edge + " ");
                }
                StdOut.println();
            } else {
                StdOut.printf("%d to %d          no path\n", s, i);
            }
        }
    }

    protected static Pair<EdgeWeightedDigraph, Integer> parseGraph(String[] args) {
        EdgeWeightedDigraph g = new EdgeWeightedDigraph(new In(args[0]));
        int s = Integer.parseInt(args[1]);
        return new Pair<>(g, s);
    }

}
