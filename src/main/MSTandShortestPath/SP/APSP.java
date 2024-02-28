package main.MSTandShortestPath.SP;

import edu.princeton.cs.algs4.*;
import kotlin.Triple;

public abstract class APSP {
    protected final EdgeWeightedDigraph g;
    protected double[][] dist;
    protected DirectedEdge[][] edgeTo;
    public APSP(EdgeWeightedDigraph g) {
        this.g = g;
    }


    abstract protected void compute();

    public boolean hasPath(int s, int t) {
        compute();
        assert dist != null;
        return dist[s][t] != Double.POSITIVE_INFINITY;
    }


    public double dist(int s, int t) {
        compute();
        assert dist != null;
        return dist[s][t];
    }


    public Iterable<DirectedEdge> path(int s, int t) {
        compute();
        assert dist != null;
        if (!hasPath(s, t)) return null;
        Stack<DirectedEdge> st = new Stack<>();
        DirectedEdge edge;
        while ((edge = edgeTo[s][t]) != null) {
            st.push(edge);
            t = edge.from();
        }
        return st;
    }

    protected static void unitTest(APSP sp, EdgeWeightedDigraph g, int s, int t) {
            if (sp.hasPath(s, t)) {
                StdOut.printf("%d to %d (%.2f) ", s, t, sp.dist(s, t));
                for (DirectedEdge edge:sp.path(s, t)) {
                    StdOut.print(edge + " ");
                }
                StdOut.println();
                StdOut.println("---------------Validating with BellmanSSSP(g, s)---------------");
                SSSP sssp = new MooreSSSP(g, s);
                boolean hasPath = sssp.hasPathTo(t);
                if (!hasPath) {
                    StdOut.println("attention! Bellman shows has no path!");
                } else {
                    if (Double.compare(sssp.distTo(t), sp.dist(s, t)) != 0) {
                        StdOut.println("attention! Bellman shows a different distance: " + sssp.distTo(t) + "apsp: " + sp.dist(s, t));
                    } else {
                        // it's possible that bellman find a different shortest path
                        StdOut.printf("%d to %d (%.2f) ", s, t, sssp.distTo(t));
                        for (DirectedEdge edge:sssp.pathTo(t)) {
                            StdOut.print(edge + " ");
                        }
                    }
                }
            } else {
                StdOut.printf("%d to %d          no path\n", s, t);
                StdOut.println("---------------Validating with BellmanSSSP(g, s)---------------");
                SSSP sssp = new BellmanSSSP(g, s);
                if (sssp.hasPathTo(t)) {
                    StdOut.println("attention! bellman shows that there is a path");
                }
            }
    }

    protected static Triple<EdgeWeightedDigraph, Integer, Integer> parseGraph(String[] args) {
        EdgeWeightedDigraph g = new EdgeWeightedDigraph(new In(args[0]));
        int s = Integer.parseInt(args[1]);
        int t = Integer.parseInt(args[2]);
        return new Triple<>(g, s, t);
    }

}

