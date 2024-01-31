package main.Graph.InterviewProblems;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;

import java.util.ArrayList;
import java.util.List;

public class ShortestDirectedCycle {

    private final Digraph g;

    private List<Integer> cycle;
    private boolean computed;
    ShortestDirectedCycle(Digraph g) {
        this.g = g;
    }

    private void shortestCycleFrom(int v) {
        System.out.print("detecting cycle for " + v);
        Queue<Integer> q  = new Queue<>();
        boolean[] visited = new boolean[g.V()];
        int[] edgeTo = new int[g.V()];
        q.enqueue(v);
        visited[v] = true;
        int len = 0;
        boolean found = false;
        int u = 0;
        outer: while (!q.isEmpty()) {
            int qSize = q.size();
            for (int i = 0; i < qSize; i ++) {
                u = q.dequeue();
                for (int w:g.adj(u)) {
                    if (w == v) {
                        // the shortest cycle found
                        // cycle len = len + 1
                        found = true;
                        break outer;
                    } else {
                        if (!visited[w]) {
                            visited[w] = true;
                            edgeTo[w] = u;
                            q.enqueue(w);
                        }
                    }
                }
            }
            len ++;
        }
        if (found) {
            System.out.println(": find a cycle with len " + (len + 1));
            if (cycle == null || len + 1 < cycle.size() - 1) {
                Stack<Integer> st = new Stack<>();
                while (u != v) {
                    st.push(u);
                    u = edgeTo[u];
                }
                ArrayList<Integer> result = new ArrayList<>();
                result.add(v);
                while (!st.isEmpty()) {
                    result.add(st.pop());
                }
                result.add(v);
                cycle = result;
            }
        } else {
            System.out.println(": no cycle");
        }
    }

    private void computeCycle() {
        if (computed) return;
        for (int i = 0; i < g.V(); i ++) {
            shortestCycleFrom(i);
        }
        computed = true;
    }

    /**
     * return the shortest cycle in the digraph
     * @return null if it's DAG
     */
    public List<Integer> getCycle() {
        computeCycle();
        assert computed;
        return cycle;
    }

    private static void unitTest(Digraph g, String description) {
        System.out.println(description);
        System.out.println("----------------------------------------------");
        System.out.print(g);
        List<Integer> cycle = (new ShortestDirectedCycle(g)).getCycle();
        System.out.print("shortest directed cycle: ");
        if (cycle == null) {
            System.out.print("no cycle");
        } else {
            for (int v:cycle) {
                System.out.print(v + " ");
            }
        }
        System.out.println("\n");
    }
    public static void main(String[] args) {
        Digraph g = new Digraph(new In(args[0]));
        String description = args[1];
        unitTest(g, description);
    }
}
