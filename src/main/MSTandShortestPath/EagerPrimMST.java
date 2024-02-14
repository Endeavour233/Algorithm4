package main.MSTandShortestPath;

import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import main.PriorityQueue.IndexMinPQ;

import java.util.Arrays;

/**
 * need distinct weight?
 */
public class EagerPrimMST {

    private final EdgeWeightedGraph g;
    private Edge[] edgeTo;

    private double weight;

    public EagerPrimMST(EdgeWeightedGraph g) {
        this.g = g;
    }

    private void computeMST() {
        if (edgeTo != null) return;
        edgeTo = new Edge[g.V()];
        double[] dist = new double[g.V()];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        IndexMinPQ<Double> pq = new IndexMinPQ<>(g.V());
        for (int i = 0; i < g.V(); i ++) {
            if (dist[i] == Double.POSITIVE_INFINITY) {
                weight += prim(i, pq, dist);
            }
        }
    }

    private double prim(int v, IndexMinPQ<Double> pq, double[] dist) {
        dist[v] = Double.NEGATIVE_INFINITY;
        for (Edge e:g.adj(v)) {
            int other = e.other(v);
            // to be compatible with parallel edge
            if (e.weight() < dist[other]) {
                dist[other] = e.weight();
                edgeTo[other] = e;
                pq.insert(other, dist[other]);
            }
        }
        int u;
        double weight = 0.0;
        while ((u = pq.deleteMin()) != -1) {
            dist[u] = Double.NEGATIVE_INFINITY;
            weight += edgeTo[u].weight();
            for (Edge e:g.adj(u)) {
                int other = e.other(u);
                if (e.weight() < dist[other]) {
                    if (dist[other] == Double.POSITIVE_INFINITY) {
                        dist[other] = e.weight();
                        pq.insert(other, dist[other]);
                    } else {
                        dist[other] = e.weight();
                        pq.decreaseKey(other, dist[other]);
                    }
                    edgeTo[other] = e;
                }
            }
        }
        return weight;
    }

    public Iterable<Edge> edges() {
        computeMST();
        assert edgeTo != null;
        Queue<Edge> result = new Queue<>();
        for (Edge e:edgeTo) {
            if (e != null) {
                result.enqueue(e);
            }
        }
        return result;
    }

    public double weight() {
        computeMST();
        assert edgeTo != null;
        return weight;
    }

    public static void main(String[] args) {
        EdgeWeightedGraph g = new EdgeWeightedGraph(new In(args[0]));
        EagerPrimMST mst = new EagerPrimMST(g);
        for (Edge e: mst.edges()) {
            System.out.println(e);
        }
        System.out.println("weight: " + mst.weight());
    }
}
