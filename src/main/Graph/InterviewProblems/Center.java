package main.Graph.InterviewProblems;
import algorithm.src.main.UndirectedGraph.InterviewProblems.Diameter;

import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.In;

import java.util.List;


public class Center {
    private final Graph tree;
    private int center = -1;

    /**
     *
     * @param tree an undirected tree
     */
    public Center(Graph tree) {
        this.tree = tree;
    }

    private void computeCenter() {
        if (center != -1) return;
        Diameter d = new Diameter(tree);
        List<Integer> longestPath = d.getLongestPath();
        center = longestPath.get(longestPath.size() / 2);
    }

    public int getCenter() {
        computeCenter();
        assert(center != -1);
        return center;
    }


    public static void main(String[] args) {
        Center c = new Center(new Graph(new In(args[0])));
        System.out.print("center is " + c.getCenter());
    }
}
