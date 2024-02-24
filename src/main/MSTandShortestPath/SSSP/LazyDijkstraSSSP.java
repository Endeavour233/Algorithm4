package main.MSTandShortestPath.SSSP;

import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import kotlin.Pair;

import java.util.PriorityQueue;

/**
 * only work for graph without negative edges. (whatever-first search version)
 */
public class LazyDijkstraSSSP extends SSSP {

    public LazyDijkstraSSSP(EdgeWeightedDigraph g, int s) {
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
        PriorityQueue<DirectedEdge> pq = new PriorityQueue<>((o1, o2) -> {
            double diff = dist[o1.from()] + o1.weight() - (dist[o2.from()] + o2.weight());
            if (diff < 0) {
                return -1;
            } else {
                if (diff > 0) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        boolean[] marked = new boolean[g.V()];
        marked[s] = true;
        for (DirectedEdge edge:g.adj(s)) {
            if (!marked[edge.to()]) {
                pq.add(edge);
            }
        }
        while (!pq.isEmpty()) {
            DirectedEdge edge = pq.poll();
            int u = edge.to();
            if (!marked[u]) {
                marked[u] = true;
                dist[u] = dist[edge.from()] + edge.weight();
                edgeTo[u] = edge;
                for (DirectedEdge e:g.adj(u)) {
                    if (!marked[e.to()]) {
                        pq.add(e);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Pair<EdgeWeightedDigraph, Integer> params = parseGraph(args);
        unitTest(new LazyDijkstraSSSP(params.getFirst(), params.getSecond()), params.getFirst(), params.getSecond());
    }
}
