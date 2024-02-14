package main.MSTandShortestPath.InterviewProblems;

import edu.princeton.cs.algs4.*;

public class MSTEdgeChecker {
    private final EdgeWeightedGraph g;
    public MSTEdgeChecker(EdgeWeightedGraph g) {
        this.g = g;
    }

    /**
     * whether there is a path between u and v all the edges of which have a weight smaller than weight
     * @return true if there is such a path; otherwise, false
     */
    private boolean reachable(int u, int v, double weight) {
        Queue<Integer> q = new Queue<>();
        q.enqueue(u);
        boolean[] visited = new boolean[g.V()];
        visited[u] = true;
        while (!q.isEmpty()) {
            Integer cur = q.dequeue();
            for (Edge e:g.adj(cur)) {
                if (e.weight() < weight) {
                    int other = e.other(cur);
                    if (other == v) return true;
                    if (!visited[other]) {
                        visited[other] = true;
                        q.enqueue(other);
                    }
                }
            }
        }
        return false;
    }

    public boolean isInMST(int u, int v) {
        // selfloop is never in MST
        if (u == v) return false;
        Edge e = null;
        for (Edge edge:g.adj(u)) {
            if (edge.other(u) == v) {
                e = edge;
                break;
            }
        }
        // no such an edge u - v in graph
        if (e == null) return false;
        return !reachable(u, v, e.weight());
    }

    private static void unitTest(EdgeWeightedGraph g, int u, int v) {
        MSTEdgeChecker checker = new MSTEdgeChecker(g);
        System.out.println("edge " + u + " - " + v + " in MST: " + checker.isInMST(u, v));
        System.out.println("validating ----------------------- ");
        System.out.println("MST: ");
        PrimMST mst = new PrimMST(g);
        boolean found = false;
        for (Edge e: mst.edges()) {
            System.out.print(e);
            int s = e.either();
            int t = e.other(s);
            if (s == u && t == v || s == v && t == u) {
                System.out.print(" *");
                found = true;
            }
            System.out.println();
        }
        System.out.println("edge " + u + " - " + v + " in MST: " + found);
    }
    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraph g = new EdgeWeightedGraph(in);
        int u = Integer.parseInt(args[1]);
        int v = Integer.parseInt(args[2]);
        unitTest(g, u, v);
    }
}
