package main.MSTandShortestPath.SSSP;

import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Queue;
import kotlin.Pair;


public class MooreSSSP extends NCDetectedSSSP {

    public MooreSSSP(EdgeWeightedDigraph g, int s) {
        super(g, s);
    }
    @Override
    protected void compute() {
        if (negativeCycle != null) reportCycle(negativeCycle);
        if (dist != null) return;
        dist = new double[g.V()];
        edgeTo = new DirectedEdge[g.V()];
        for (int i = 0; i < g.V(); i ++) {
            dist[i] = Double.POSITIVE_INFINITY;
        }
        dist[s] = 0;
        boolean[] inqueue = new boolean[g.V()];
        Queue<Integer> q = new Queue<>();
        q.enqueue(s);
        inqueue[s] = true;
        int phase = 0;
        Outer:
        while (!q.isEmpty()) {
            phase ++;
            int qSize = q.size();
            for (int i = 0; i < qSize; i ++) {
                int u = q.dequeue();
                inqueue[u] = false;
                for (DirectedEdge edge:g.adj(u)) {
                    int v = edge.to();
                    double newDist = dist[u] + edge.weight();
                    if (newDist < dist[v]) {
                        dist[v] = newDist;
                        edgeTo[v] = edge;
                        if (phase == g.V()) {
                            // find tense edge in V phase, there is a negative cycle
                            negativeCycle = findCycle(edgeTo, v);
                            break Outer;
                        }
                        if (!inqueue[v]) {
                            q.enqueue(v);
                            inqueue[v] = true;
                        }
                    }
                }
            }
        }
        if (negativeCycle != null) reportCycle(negativeCycle);
    }

    public static void main(String[] args) {
        Pair<EdgeWeightedDigraph, Integer> params = parseGraph(args);
        unitTest(new MooreSSSP(params.getFirst(), params.getSecond()), params.getFirst(), params.getSecond());
    }

}
