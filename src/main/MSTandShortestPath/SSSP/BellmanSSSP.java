package main.MSTandShortestPath.SSSP;


import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import kotlin.Pair;

public class BellmanSSSP extends NCDetectedSSSP {

    private Iterable<DirectedEdge> negativeCycle;

    public BellmanSSSP(EdgeWeightedDigraph g, int s) {
        super(g, s);
    }

    @Override
    protected void compute() {
        if (negativeCycle != null) {
            reportCycle(negativeCycle);
        }
        if (dist != null) return;
        dist = new double[g.V()];
        edgeTo = new DirectedEdge[g.V()];
        for (int i = 0; i < g.V(); i ++) {
            dist[i] = Double.POSITIVE_INFINITY;
        }
        dist[s] = 0;
        int iter = 0;
        boolean relaxed;
        Iterable<DirectedEdge> edges = g.edges();
        Outer:
        while (true) {
            relaxed = false;
            iter ++;
            for (DirectedEdge edge:edges) {
                int u =edge.from();
                int v = edge.to();
                double newDist;
                if (dist[u] != Double.POSITIVE_INFINITY && (newDist = dist[u] + edge.weight()) < dist[v]) {
                    dist[v] = newDist;
                    edgeTo[v] = edge;
                    if (iter == g.V()) {
                        negativeCycle = findCycle(edgeTo, v);
                        break Outer;
                    } else {
                        relaxed = true;
                    }
                }
            }
            if (!relaxed) {
                break;
            }
        }
        if (negativeCycle != null) reportCycle(negativeCycle);
    }

    public static void main(String[] args) {
        Pair<EdgeWeightedDigraph, Integer> params = parseGraph(args);
        unitTest(new BellmanSSSP(params.getFirst(), params.getSecond()), params.getFirst(), params.getSecond());
    }
}
