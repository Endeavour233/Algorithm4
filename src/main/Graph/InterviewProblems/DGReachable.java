package main.Graph.InterviewProblems;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.List;

public class DGReachable {
    private final Digraph g;
    private boolean computed = false;
    private List<Integer> sink;
    public DGReachable(Digraph g) {
        this.g = g;
    }

    private void compute() {
        if (computed) return;

        // mark vertices with their corresponding strong component ids
        KosaraJuSharirSCC sc = new KosaraJuSharirSCC(g);

        // construct scc(g)/kernal DAG of g
        Digraph sccg = new Digraph(sc.count());
        for (int u = 0; u < g.V(); u ++) {
            for (int v:g.adj(u)) {
                int idu = sc.id(u);
                int idv = sc.id(v);
                if (idu != idv) {
                    sccg.addEdge(idu, idv);
                }
            }
        }

        // check whether there is exactly one sink component in scc(g). if so, query all vertices in this component by its id.
        // they are vertices that are reachable from every other vertex in g.
        int targetId = (new DAGReachable(sccg)).allReachable();
        if (targetId != -1) {
            // there is exactly one sink component with id: targetId in scc(G)
            sink = sc.query(targetId);
        }

        computed = true;
    }

    /**
     * a list of vertices that are reachable from every other vertex in {@code g}
     * @return a non-empty list of vertices that are reachable from every other vertex in {@code g},
     *         return {@code null} if there is no such a vertex.
     */
    public List<Integer> reachable() {
        compute();
        assert computed;
        return sink;
    }

    private static int countReach(int v, Digraph g, int prev, boolean[] marked) {
        marked[v] = true;
        prev ++;
        for (int u:g.adj(v)) {
            if (!marked[u]) {
                prev = countReach(u, g, prev, marked);
            }
        }
        return prev;
    }

    private static void unitTest(Digraph g, String description) {
        System.out.println(description);
        System.out.println("-----------------------------------------");
        System.out.print(g);
        List<Integer> vs = new DGReachable(g).reachable();
        if (vs == null) {
            System.out.println("there is no such a vertex that's reachable from every other vertex");
        } else {
            System.out.print("solution: ");
            for (int v:vs) {
                System.out.print(v + " ");
            }
            System.out.println();
        }
        System.out.println("validating..............");
        Digraph revG = g.reverse();
        System.out.println(g.V() + " vertices in graph");
        for (int i = 0; i < revG.V(); i ++) {
            boolean[] marked = new boolean[revG.V()];
            int reachCnt = countReach(i, revG, 0, marked);
            System.out.println(i + ": there are " + reachCnt + " vertices that can reach it. isSolution: " + (reachCnt == g.V()));
        }
    }
    public static void main(String[] args) {
        unitTest(new Digraph(new In(args[0])), args[1]);
    }
}
