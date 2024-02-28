package main.MSTandShortestPath.SP;

import edu.princeton.cs.algs4.*;
import kotlin.Pair;

public class DfsSSSP extends SSSP {

    private Queue<DirectedEdge> cycle;

    public DfsSSSP(EdgeWeightedDigraph g, int s) {
        super(g, s);
    }

    private double min(double distu, double distv, double uv) {
        if (distu == Double.POSITIVE_INFINITY && distv == Double.POSITIVE_INFINITY) return Double.POSITIVE_INFINITY;
        if (distu == Double.POSITIVE_INFINITY) return distv + uv;
        if (distv == Double.POSITIVE_INFINITY) return distu;
        return Math.min(distu, distv + uv);
    }

    /**
     * compute shortest and path. if find a cycle, collect the cycle
     * @param revG
     * @param v
     * @param dist
     * @param edgeTo
     * @param isActive
     * @return the start vertex v of the cycle if there is the cycle and has not been collect;
     *         {@code -1} if there is a cycle and the cycle has been collected;
     *         {@code -2} if there is no cycle.
     */
    private int dfs(EdgeWeightedDigraph revG, int v, double[] dist, DirectedEdge[] edgeTo, boolean[] isActive) {
        if (v == s) {
            dist[v] = 0;
        } else {
            dist[v] = Double.POSITIVE_INFINITY;
        }
        isActive[v] = true;
        for (DirectedEdge edge:revG.adj(v)) {
            int to = edge.to();
            if (isActive[to]) {
                cycle = new Queue<>();
                cycle.enqueue(new DirectedEdge(edge.to(), edge.from(), edge.weight()));
                if (v == to) {
                    return -1;
                } else {
                    return to;
                }
            }
            if (dist[to] == Double.NEGATIVE_INFINITY) {
                int cycleStatus = dfs(revG, to, dist, edgeTo, isActive);
                if (cycleStatus != -2) {
                    if (cycleStatus == -1) {
                        return -1;
                    } else {
                        cycle.enqueue(new DirectedEdge(edge.to(), edge.from(), edge.weight()));
                        if (v == cycleStatus) {
                            return -1;
                        } else {
                            return cycleStatus;
                        }
                    }
                }
            }
            double newDist = min(dist[v], dist[to], edge.weight());
            if (newDist != Double.POSITIVE_INFINITY && newDist != dist[v]) {
                dist[v] = newDist;
                edgeTo[v] = new DirectedEdge(edge.to(), edge.from(), edge.weight());
            }
        }
        isActive[v] = false;
        return -2;
    }

    private EdgeWeightedDigraph reverseG(EdgeWeightedDigraph g) {
        EdgeWeightedDigraph revG = new EdgeWeightedDigraph(g.V());
        for (DirectedEdge edge:g.edges()) {
            revG.addEdge(new DirectedEdge(edge.to(), edge.from(), edge.weight()));
        }
        return revG;
    }

    private void reportCycle(Iterable<DirectedEdge> cycle) {
        StringBuilder sb = new StringBuilder("there is a cycle: \n");
        for (DirectedEdge e:cycle) {
            sb.append(e);
            sb.append("\n");
        }
        throw new IllegalArgumentException(sb.toString());
    }

    @Override
    protected void compute() {
        if (cycle != null) reportCycle(cycle);
        if (dist != null) return;
        EdgeWeightedDigraph revG = reverseG(g);
        int v = revG.V();
        dist = new double[v];
        edgeTo = new DirectedEdge[v];
        boolean[] isActive = new boolean[v];
        for (int i = 0; i < v; i ++) {
            dist[i] = Double.NEGATIVE_INFINITY;
        }
        for (int i = 0; i < g.V(); i ++) {
            if (dist[i] == Double.NEGATIVE_INFINITY) {
                if (dfs(revG, i, dist, edgeTo, isActive) != -2) {
                    reportCycle(cycle);
                }
            }
        }
    }

    public static void main(String[] args) {
        Pair<EdgeWeightedDigraph, Integer> params = parseGraph(args);
        unitTest(new DfsSSSP(params.getFirst(), params.getSecond()), params.getFirst(), params.getSecond());
    }
}
