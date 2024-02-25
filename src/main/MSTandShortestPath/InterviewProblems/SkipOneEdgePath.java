package main.MSTandShortestPath.InterviewProblems;

import edu.princeton.cs.algs4.*;
import main.MSTandShortestPath.SSSP.DijkstraSSSP;

import java.util.HashMap;

public class SkipOneEdgePath {
    private Queue<DirectedEdge> path;
    private double distance = Double.POSITIVE_INFINITY;
    public SkipOneEdgePath(EdgeWeightedDigraph g, int s, int t) {
        if (s == t) {
            path = new Queue<>();
            distance = 0;
            return;
        }
        // find the shortest path from s to every other vertex
        DijkstraSSSP spFromS = new DijkstraSSSP(g, s);
        if (!spFromS.hasPathTo(t)) {
            return;
        }
        HashMap<DirectedEdge, DirectedEdge> edgeMap = new HashMap<>();
        EdgeWeightedDigraph revG = new EdgeWeightedDigraph(g.V());
        for (DirectedEdge edge:g.edges()) {
            DirectedEdge revE = new DirectedEdge(edge.to(), edge.from(), edge.weight());
            revG.addEdge(revE);
            edgeMap.put(revE, edge);
        }
        // find the shortest path from every other vertex to t
        DijkstraSSSP spToT = new DijkstraSSSP(revG, t);
        DirectedEdge edge = null;
        double dist = Double.POSITIVE_INFINITY;
        // the implementation of g.edges should ensure that it returns the original edge objects of g instead of copies.
        for (DirectedEdge e:g.edges()) {

                int from = e.from();
                int to = e.to();
                if (!spFromS.hasPathTo(from) || !spToT.hasPathTo(to)) continue;
                double newDist = spFromS.distTo(from) + spToT.distTo(to);
                if (newDist < dist) {
                    dist = newDist;
                    edge = e;
                }

        }
        if (edge != null) {
            path = new Queue<>();
            distance = dist;
            for (DirectedEdge e:spFromS.pathTo(edge.from())) {
                path.enqueue(e);
            }
            path.enqueue(edge);
            Bag<DirectedEdge> bag = new Bag<>();
            for (DirectedEdge e: spToT.pathTo(edge.to())) {
                bag.add(edgeMap.get(e));
            }
            for (DirectedEdge e:bag) {
                path.enqueue(e);
            }
        }
    }

    /**
     * the shortest path from s to t after changing the weight of one edge to 0
     * @return {@code null} if t is not reachable from s
     */
    public Iterable<DirectedEdge> getPath() {
        if (path == null) return null;
        Queue<DirectedEdge> result = new Queue<>();
        for (DirectedEdge e:path) {
            result.enqueue(e);
        }
        return result;
    }

    /**
     * the length of the shortest path from s to t after changing the weight of one edge to 0
     * @return {@code infinity} if t is not reachable from s
     */
    public double getDist() {
        return distance;
    }

    public static void main(String[] args) {
        EdgeWeightedDigraph g = new EdgeWeightedDigraph(new In(args[0]));
        int s = Integer.parseInt(args[1]);
        int t = Integer.parseInt(args[2]);
        System.out.println("skip one edge path:");
        SkipOneEdgePath ssp = new SkipOneEdgePath(g, s, t);
        Iterable<DirectedEdge> path = ssp.getPath();
        if (path == null) {
            System.out.print(t + "is not reachable from " + s);
        } else {
            for (DirectedEdge e:ssp.getPath()) {
                System.out.print(e + " ");
            }
        }
    }
}
