package algorithm.src.main.UndirectedGraph.InterviewProblems;
import edu.princeton.cs.algs4.*;

import java.util.ArrayList;
import java.util.List;

public class Diameter {
    private final Graph tree;
    
    public Diameter(Graph tree) {
        this.tree = tree;
    }

    private List<Integer> longestPath;

    public int getDiameter() {
        computeDiameter();
        assert(longestPath != null);
        return longestPath.size() - 1;
    }


    public List<Integer> getLongestPath() {
        computeDiameter();
        assert(longestPath != null);
        return new ArrayList<>(longestPath);
    }

    private void computeDiameter() {
        if (longestPath != null) return;
        int V = tree.V();
        longestPath = new ArrayList<>();
        if (V == 0) {
            return;
        }
        if (V == 1) {
            longestPath.add(0);
            return;
        }
        Queue<Integer> q = new Queue<>();
        boolean[] visited = new boolean[V];
        q.enqueue(0);
        visited[0] = true;
        Integer u = 0;
        while (!q.isEmpty()) {
            u = q.dequeue();
            for (Integer v:tree.adj(0)) {
              if (!visited[v]) {
                visited[v] = true;
                q.enqueue(v);
              }  
            }
        }
        // u is now the farthest vertex from 0
        // all entries of visited have been set to true since tree is a connected graph
        // now, we start our second bfs and say that a vertex v has been visited if visited[v] = false
        q.enqueue(u);
        visited[u] = false;
        int[] edgeTo = new int[tree.V()];
        edgeTo[u] = u;
        while (!q.isEmpty()) {
            u = q.dequeue();
            for (Integer v : tree.adj(u)) {
                if (visited[v]) {
                    visited[v] = false;
                    edgeTo[v] = u;
                    q.enqueue(v);
                }
            }
        }
        while (edgeTo[u] != u) {
            longestPath.add(u);
            u = edgeTo[u];
        }
        longestPath.add(u);
    }
    public static void main(String[] args) {
        Graph tree = new Graph(new In(args[0]));
        Diameter d = new Diameter(
            tree
        );
        System.out.print("diameter is " + d.getLongestPath());
    }
}
