package main.MSTandShortestPath.SP;

import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import kotlin.Triple;

import java.util.HashMap;

public class JohnsonAPSP extends APSP {

    private Iterable<DirectedEdge> negativeCycle;
    public JohnsonAPSP(EdgeWeightedDigraph g) {
        super(g);
    }

    @Override
    protected void compute() {
        if (negativeCycle != null) throw new IllegalArgumentException("there is a negative cycle!");
        if (dist != null) return;
        dist = new double[g.V()][g.V()];
        edgeTo = new DirectedEdge[g.V()][g.V()];
        // compute pi
        EdgeWeightedDigraph dummyAddedGraph = addDummyVertex(g);
        MooreSSSP sssp = new MooreSSSP(dummyAddedGraph, g.V());
        double[] pi = new double[g.V()];
        for (int i = 0; i < g.V(); i ++) {
            try {
                pi[i] = sssp.distTo(i);
            } catch (IllegalArgumentException e) {
                negativeCycle = sssp.getNegativeCycle();
                break;
            }
        }
        if (negativeCycle != null) throw new IllegalArgumentException("there is a negative cycle!");
        HashMap<DirectedEdge, DirectedEdge> edgeMap = new HashMap<>();
        EdgeWeightedDigraph reWeightedGraph = reWeighting(g, pi, edgeMap);
        for (int s = 0; s < g.V(); s ++) {
            DijkstraSSSP dijkstraSSSP = new DijkstraSSSP(reWeightedGraph, s);
            for (int t = 0; t < g.V(); t++) {
                dist[s][t] = dijkstraSSSP.distTo(t) + pi[t] - pi[s];
                edgeTo[s][t] = edgeMap.get(dijkstraSSSP.edgeTo[t]);
            }
        }
    }

    private EdgeWeightedDigraph addDummyVertex(EdgeWeightedDigraph g) {
        EdgeWeightedDigraph dummyAddedGraph = new EdgeWeightedDigraph(g.V() + 1);
        for (DirectedEdge edge:g.edges()) {
            dummyAddedGraph.addEdge(edge);
        }
        for (int i = 0; i < g.V(); i ++) {
            dummyAddedGraph.addEdge(new DirectedEdge(g.V(), i, 0));
        }
        return dummyAddedGraph;
    }

    private EdgeWeightedDigraph reWeighting(EdgeWeightedDigraph g, double[] pi, HashMap<DirectedEdge, DirectedEdge> edgeMap) {
        EdgeWeightedDigraph graph = new EdgeWeightedDigraph(g.V());
        for (DirectedEdge edge:g.edges()) {
            int u = edge.from();
            int v = edge.to();
            DirectedEdge reweighed = new DirectedEdge(u, v, pi[u] + edge.weight() - pi[v]);
            edgeMap.put(reweighed, edge);
            graph.addEdge(reweighed);
        }
        return graph;
    }

    public static void main(String[] args) {
        Triple<EdgeWeightedDigraph, Integer, Integer> param = parseGraph(args);
        unitTest(new JohnsonAPSP(param.getFirst()), param.getFirst(), param.getSecond(), param.getThird());
    }

}
