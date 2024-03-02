package main.MaxFlowMinCut;

import edu.princeton.cs.algs4.Queue;
public class FlowNetwork {
    private Queue<FlowEdge>[] edges;
    public final int V;

    public FlowNetwork(int V) {
        this.V = V;
        edges =(Queue<FlowEdge>[]) new Queue[V];
        for (int i = 0; i < V; i ++) {
            edges[i] = new Queue();
        }
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= V) throw new IllegalArgumentException("vertex is not valid");
    }

    public void addEdge(FlowEdge e) {
        validateVertex(e.from);
        validateVertex(e.to);
        edges[e.from].enqueue(e);
        edges[e.to].enqueue(e);
    }

    public Iterable<FlowEdge> adj(int v) {
        validateVertex(v);
        return edges[v];
    }

}
