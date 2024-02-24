package main.MSTandShortestPath.SSSP;

import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import kotlin.Pair;

import java.util.PriorityQueue;

/**
 * only work for graph without negative edges. (modified from {@link LazyDijkstraSSSP})
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
        relax(s, pq);
        while (!pq.isEmpty()) {
            DirectedEdge edge = pq.poll();
            int u = edge.to();
            if (!marked[u]) {
                marked[u] = true;
                relax(u, pq);
            }
        }
    }

    private void relax(int u, PriorityQueue<DirectedEdge> pq) {
        for (DirectedEdge e:g.adj(u)) {
            int to = e.to();
            double newDist = dist[u] + e.weight();
            if (newDist < dist[to]) {
                // `to` must have not been marked - because there is no negative edge.
                // for those edges e the `to` of which not marked but that can't construct a shorter path to `to`
                // there is no need to insert it. Because there must have been an edge e' in pq s.t. the to of which is the same
                // as e and dist[e`.from] + weight <= dist[e.from] + weight. e` will be polled out before e and `to` will be marked.
                // then e will be ignored.
                dist[to] = newDist;
                edgeTo[to] = e;
                // because there is no negative edges, dist[u = e.from()] won't change after e is inserted into pq.
                // pq's comparator is ensured to perform normally.
                pq.add(e);
            }
        }
    }

    public static void main(String[] args) {
        Pair<EdgeWeightedDigraph, Integer> params = parseGraph(args);
        unitTest(new LazyDijkstraSSSP2(params.getFirst(), params.getSecond()), params.getFirst(), params.getSecond());
    }
}

