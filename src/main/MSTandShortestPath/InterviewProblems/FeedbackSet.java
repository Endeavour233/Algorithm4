package main.MSTandShortestPath.InterviewProblems;

import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import main.MSTandShortestPath.MST.MaximumST;

import java.util.HashSet;
import java.util.Set;

public class FeedbackSet {

    private final EdgeWeightedGraph g;
    private Queue<Edge> minimumSet;

    public FeedbackSet(EdgeWeightedGraph g) {
        this.g = g;
    }

    private void compute() {
        if (minimumSet != null) return;
        minimumSet = new Queue<>();
        MaximumST maximumST = new MaximumST(g);
        Set<Edge> set = new HashSet<>();
        for (Edge e:maximumST.edges()) {
            set.add(e);
        }
        for (Edge e:g.edges()) {
            if (!set.contains(e)) {
                minimumSet.enqueue(e);
            }
        }
    }

    public Iterable<Edge> minimumSet() {
        compute();
        assert minimumSet != null;
        return minimumSet;
    }
    public static void main(String[] args) {
        EdgeWeightedGraph g = new EdgeWeightedGraph(new In(args[0]));
        FeedbackSet feedbackSet = new FeedbackSet(g);
        for (Edge e: feedbackSet.minimumSet()) {
            System.out.println(e);
        }
    }
}
