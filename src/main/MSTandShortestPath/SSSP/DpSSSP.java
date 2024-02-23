package main.MSTandShortestPath.SSSP;

import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import kotlin.Pair;

public class DpSSSP extends SSSP {

    public DpSSSP(EdgeWeightedDigraph g, int s) {
        super(g, s);
    }
    @Override
    protected void compute() {
        if (dist != null) return;
        dist = new double[g.V()];
        edgeTo = new DirectedEdge[g.V()];
        double[] mostDist = new double[g.V()];
        double[] prevMostDist = new double[g.V()];
        for (int i = 0; i < g.V(); i ++) {
            mostDist[i] = Double.POSITIVE_INFINITY;
            prevMostDist[i] = Double.POSITIVE_INFINITY;
        }
        mostDist[s] = 0;
        prevMostDist[s] = 0;
        for (int i = 1; i < g.V(); i ++) {
            for (DirectedEdge e:g.edges()) {
                int u = e.from();
                int v = e.to();
                double newDist;
                if (prevMostDist[u] != Double.POSITIVE_INFINITY && (newDist = prevMostDist[u] + e.weight()) < mostDist[v]) {
                    mostDist[v] = newDist;
                    edgeTo[v] = e;
                }
            }
            double[] tmp = prevMostDist;
            prevMostDist = mostDist;
            mostDist = tmp;
        }
        for (int i = 0; i < g.V(); i ++) {
            dist[i] = prevMostDist[i];
        }
    }

    public static void main(String[] args) {
        Pair<EdgeWeightedDigraph, Integer> params = parseGraph(args);
        unitTest(new DpSSSP(params.getFirst(), params.getSecond()), params.getFirst(), params.getSecond());
    }
}
