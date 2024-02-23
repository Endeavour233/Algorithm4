package main.MSTandShortestPath.SSSP;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Stack;
import kotlin.Pair;

public class TopoDfsSSSP extends SSSP {

    private Bag<DirectedEdge> cycle;

    public TopoDfsSSSP(EdgeWeightedDigraph g, int s) {
        super(g, s);
    }
    /**
     * compute topoordering. if find a cycle, collect the cycle
     * @return the start vertex v of the cycle if there is the cycle and has not been collect;
     *         {@code -1} if there is a cycle and the cycle has been collected;
     *         {@code -2} if there is no cycle.
     */
    private int dfs(EdgeWeightedDigraph g, int v, Stack<Integer> st, boolean[] marked, boolean[] isActive) {
        marked[v] = true;
        isActive[v] = true;
        for (DirectedEdge e:g.adj(v)) {
            int to = e.to();
            if (isActive[to]) {
                cycle = new Bag<>();
                cycle.add(e);
                if (v == to) {
                    return -1;
                } else {
                    return to;
                }
            }
            if (!marked[to]) {
                int cycleStatus = dfs(g, to, st, marked, isActive);
                if (cycleStatus != -2) {
                    if (cycleStatus == -1) {
                        return -1;
                    } else {
                        cycle.add(e);
                        if (v == cycleStatus) {
                            return -1;
                        } else {
                            return cycleStatus;
                        }
                    }
                }
            }
        }
        st.push(v);
        isActive[v] = false;
        return -2;
    }

    private Iterable<Integer> topoOrdering(EdgeWeightedDigraph g) {
        Stack<Integer> st = new Stack<>();
        boolean[] marked = new boolean[g.V()];
        boolean[] isActive = new boolean[g.V()];
        for (int i = 0; i < g.V(); i ++) {
            if (!marked[i]) {
                if (dfs(g, i, st, marked, isActive) != -2) {
                    return null;
                }
            }
        }
        return st;
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
        Iterable<Integer> topo = topoOrdering(g);
        if (topo == null) reportCycle(cycle);
        int v = g.V();
        dist = new double[v];
        edgeTo = new DirectedEdge[v];
        for (int i = 0; i < v; i ++) {
            dist[i] = Double.POSITIVE_INFINITY;
        }
        dist[s] = 0;
        for (int u:topo) {
            for (DirectedEdge edge:g.adj(u)) {
                relax(edge, dist, edgeTo);
            }
        }
    }

    private void relax(DirectedEdge edge, double[] dist, DirectedEdge[] edgeTo) {
        double distu = dist[edge.from()];
        double distv = dist[edge.to()];
        if (distu == Double.POSITIVE_INFINITY) return;
        double newDist = distu + edge.weight();
        if (newDist < distv) {
            dist[edge.to()] = newDist;
            edgeTo[edge.to()] = edge;
        }
    }

    public static void main(String[] args) {
        Pair<EdgeWeightedDigraph, Integer> params = parseGraph(args);
        unitTest(new TopoDfsSSSP(params.getFirst(), params.getSecond()), params.getFirst(), params.getSecond());
    }

}
