package main.MSTandShortestPath.SSSP;

import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Stack;

abstract public class NCDetectedSSSP extends SSSP {

    protected Iterable<DirectedEdge> negativeCycle;

    public NCDetectedSSSP(EdgeWeightedDigraph g, int s) {
        super(g, s);
    }

    public Iterable<DirectedEdge> getNegativeCycle() {
        try {
            compute();
        } catch (IllegalArgumentException e) {

        }
        assert dist != null;
        return negativeCycle;
    }

    protected void checkCycle() {
        if (negativeCycle == null) return;
        StringBuilder sb = new StringBuilder("there is negative cycle: \n");
        for (DirectedEdge e:negativeCycle) {
            sb.append(e);
            sb.append("\n");
        }
        throw new IllegalArgumentException(sb.toString());
    }

    protected void findCycle(DirectedEdge[] edgeTo, int v) {
        Stack<DirectedEdge> cycle = new Stack<>();
        int slow = edgeTo[v].from();
        int fast = edgeTo[slow].from();
        while (slow != fast) {
            slow = edgeTo[slow].from();
            fast = edgeTo[edgeTo[fast].from()].from();
        }
        slow = v;
        while (slow != fast) {
            slow = edgeTo[slow].from();
            fast = edgeTo[fast].from();
        }
        do {
            DirectedEdge e = edgeTo[slow];
            cycle.push(e);
            slow = e.from();
        } while (slow != fast);
        negativeCycle = cycle;
    }
}
