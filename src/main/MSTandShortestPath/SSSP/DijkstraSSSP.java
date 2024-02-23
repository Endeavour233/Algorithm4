package main.MSTandShortestPath.SSSP;

import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import kotlin.Pair;
import main.PriorityQueue.IndexMinPQ;

public class DijkstraSSSP extends SSSP {
    /**
     * not applicable for g with negative cycle
     * @param g
     * @param s
     */
    public DijkstraSSSP(EdgeWeightedDigraph g, int s) {
        super(g, s);
    }
    @Override
    protected void compute() {
        if (dist != null) return;
        dist = new double[g.V()];
        edgeTo = new DirectedEdge[g.V()];
        for (int i = 0; i < g.V(); i ++) {
            dist[i] = Double.POSITIVE_INFINITY;
        }
        dist[s] = 0;
        IndexMinPQ<Double> pq = new IndexMinPQ<>(g.V());
        pq.insert(s, dist[s]);
        int u;
        while ((u = pq.deleteMin()) != -1) {
            for (DirectedEdge edge:g.adj(u)) {
                int v = edge.to();
                double newDist = dist[u] + edge.weight();
                if (newDist < dist[v]) {
                    dist[v] = newDist;
                    edgeTo[v] = edge;
                    if (pq.contains(v)) {
                        pq.decreaseKey(v, dist[v]);
                    } else {
                        pq.insert(v, dist[v]);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Pair<EdgeWeightedDigraph, Integer> params = parseGraph(args);
        unitTest(new DijkstraSSSP(params.getFirst(), params.getSecond()), params.getFirst(), params.getSecond());
    }
}
