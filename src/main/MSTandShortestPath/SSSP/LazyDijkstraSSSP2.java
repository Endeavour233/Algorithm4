package main.MSTandShortestPath.SSSP;

import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import kotlin.Pair;
import main.PriorityQueue.IndexMinPQ;

/**
 * only work for graph without negative edges. modified from {@link LazyDijkstraSSSP}
 */
public class LazyDijkstraSSSP2 extends SSSP {

    public LazyDijkstraSSSP2(EdgeWeightedDigraph g, int s) {
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
        boolean[] marked = new boolean[g.V()];
        int u;
        while ((u = pq.deleteMin()) != -1) {
            if (!marked[u]) {
                marked[u] = true;
                for (DirectedEdge e:g.adj(u)) {
                    double newDist = dist[u] + e.weight();
                    int to = e.to();
                    if (newDist < dist[to]) {
                        dist[to] = newDist;
                        edgeTo[to] = e;
                        if (pq.contains(to)) {
                            pq.decreaseKey(to, dist[to]);
                        } else {
                            pq.insert(to, dist[to]);
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Pair<EdgeWeightedDigraph, Integer> param = parseGraph(args);
        unitTest(new LazyDijkstraSSSP2(param.getFirst(), param.getSecond()), param.getFirst(), param.getSecond());
    }
}
