package main.Graph.WordNet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class DAGReachable {

    private final Digraph g;
    private boolean computed;

    private int vstar;

    public DAGReachable(Digraph g) {
        this.g = g;
    }

    private void compute() {
        if (computed) return;
        boolean found = false;
        for (int i = 0; i < g.V(); i ++) {
            if (g.outdegree(i) == 0) {
                if (found) {
                    // more than one vertex have 0 outdegree, there is no such a vertex that's reachable from every other vertex
                    vstar = -1;
                    computed = true;
                    return;
                } else {
                    vstar = i;
                    computed = true;
                    found = true;
                }
            }
        }
        if (!found) {
            vstar = -1;
            computed = true;
        }
    }

    /**
     * get the vertex that is reachable from any vertices of {@code g}
     * @return the vertex that's reachable from every other vertex if {@code g} has one,
     *         and -1 otherwise
     */
    public int allReachable() {
        compute();
        assert computed;
        return vstar;
    }

    private static void unitTest(Digraph g, String description) {
        System.out.println(description);
        System.out.println("---------------------------------");
        System.out.print(g);
        System.out.print("Reachable vertex: ");
        int v = (new DAGReachable(g)).allReachable();
        if (v == -1) {
            System.out.print("no such a vertex");
        } else {
            System.out.print(v);
        }
    }


    public static void main(String[] args) {
        unitTest(new Digraph(new In(args[0])), args[1]);
    }
}
