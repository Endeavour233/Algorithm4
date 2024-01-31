package main.Graph.InterviewProblems;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

import java.util.ArrayList;
import java.util.List;

public class DAGHamiltonian {

    private final Digraph g;
    private List<Integer> path;
    private boolean computed;

    public DAGHamiltonian(Digraph g) {
        this.g = g;
    }

    private void toposorting(int v, Stack<Integer> st, boolean[] visited, boolean[] active) {
        visited[v] = true;
        active[v] = true;
        for (int u:g.adj(v)) {
            if (active[u]) {
                throw new IllegalArgumentException("cycle detected in the graph");
            } else {
                if (!visited[u]) {
                    toposorting(u, st, visited, active);
                }
            }
        }
        st.push(v);
        active[v] = false;
    }

    private void computePath() {
        if (computed) return;
        Stack<Integer> st = new Stack<>();
        boolean[] visited = new boolean[g.V()];
        boolean[] active = new boolean[g.V()];
        for (int i = 0; i < g.V(); i ++) {
            if (!visited[i]) {
                toposorting(i, st, visited, active);
            }
        }
        int prev = st.pop();
        ArrayList<Integer> result = new ArrayList();
        result.add(prev);
        while (!st.isEmpty()) {
            int cur = st.pop();
            boolean found = false;
            for (int u:g.adj(prev)) {
                if (cur == u) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                computed = true;
                return;
            }
            result.add(cur);
        }
        path = result;
        computed = true;
    }

    /**
     * find the Hamiltonian path of the DAG
     * @return a non-null Hamiltonian path if {@code g} has one,
     *          and {@code null} otherwise
     * @throws IllegalArgumentException if {@code g} is not a DAG
     */
    public List<Integer> getPath() {
        computePath();
        assert computed;
        return path;
    }

    private static void unitTest(Digraph g, String description) {
        System.out.println(description);
        System.out.println("---------------------------------");
        System.out.print(g);
        System.out.print("Hamilton Path: ");
        List<Integer> path = (new DAGHamiltonian(g)).getPath();
        if (path == null) {
            System.out.print("no such a path");
        } else {
            for (int v:path) {
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
