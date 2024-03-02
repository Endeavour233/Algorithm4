package main.MaxFlowMinCut;

import java.util.LinkedList;
import java.util.Queue;

public class FulkersonMaxFlow {

    private boolean[] marked;
    // augmenting path
    private FlowEdge[] edgeTo;

    private int maxFlow;

    public FulkersonMaxFlow(FlowNetwork network, int s, int t) {
        while (hasAugmentingPath(network, s, t)) {
            int F = Integer.MAX_VALUE;
            int v = t;
            while (v != s) {
                F = Math.min(F, edgeTo[v].residualCapacityTo(v));
                v = edgeTo[v].other(v);
            }
            v = t;
            while (v != s) {
                edgeTo[v].increaseFlowTo(F, v);
                v = edgeTo[v].other(v);
            }
        }
        int flow = 0;
        for (FlowEdge edge:network.adj(s)) {
            if (edge.from == s) {
                flow += edge.flow();
            } else {
                flow -= edge.flow();
            }
        }
        maxFlow = flow;
    }

    /**
     * use whatever first search to find augmenting path: s = v0 - v1 - ... - vn = t. s.t. c_f(v_i -> v_{i + 1}) > 0
     * in current implementation, we try to find the augmenting path with the shortest length using BFS
     */
    private boolean hasAugmentingPath(FlowNetwork network, int s, int t) {
        marked = new boolean[network.V];
        edgeTo = new FlowEdge[network.V];
        marked[s] = true;
        Queue<Integer> q = new LinkedList<>();
        q.add(s);
        while (!q.isEmpty()) {
            int u = q.poll();
            for (FlowEdge edge:network.adj(u)) {
                int v = edge.other(u);
                if (edge.residualCapacityTo(v) > 0) {
                    if (!marked[v]) {
                        marked[v] = true;
                        edgeTo[v] = edge;
                        if (v == t) return true;
                        q.add(v);
                    }
                }
            }
        }
        return false;
    }



    public boolean inCut(int v) {
        return marked[v];
    }

    public int maxFlow() {
        return maxFlow;
    }



}
