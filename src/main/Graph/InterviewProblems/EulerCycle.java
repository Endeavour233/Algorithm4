package main.Graph.InterviewProblems;

import edu.princeton.cs.algs4.*;

import java.util.ArrayList;
import java.util.List;

public class EulerCycle {

    private static class Edge {
        public final int u;
        public final int v;
        public boolean used;

        public int other(int w) {
            if (w == u) {
                return v;
            } else {
                return u;
            }
        }
        Edge(int u, int v) {
            this.u = u;
            this.v = v;
        }
    }

    private final Graph g;
    private List<Integer> cycle;
    private String errMsg;

    // self loop, parallel edges are allowed
    public EulerCycle(Graph g) {
        this.g = g;
    }




    /**
     * fill adjs and return a vertex with non-zero degree. also check whether all the vertices are even-degree.
     * @param adjs
     * @return
     */
    private int getMutableAdjs(Queue<Edge>[] adjs) {
        int nonZero = -1;
        for (int v = 0; v < g.V(); v ++) {
            if ((g.degree(v) & 1) != 0) {
                throw new IllegalArgumentException("vertex " + v + " has odd degree, there is no euler cycle");
            }

            int selfLoopCnt = 0;
            for (int u:g.adj(v)) {
                if (u == v) {
                    selfLoopCnt ++;
                    //2 v in adj(v) only stands for 1 self-loop, so add edge (v,v) only when self-loop count is even
                    if ((selfLoopCnt & 1) == 0) {
                        adjs[v].enqueue(new Edge(u, v));
                    }
                } else {
                    if (u > v) {
                        // add edge for both u and v
                        Edge e = new Edge(v, u);
                        adjs[v].enqueue(e);
                        adjs[u].enqueue(e);
                    }
                }
            }
            if (!adjs[v].isEmpty() && nonZero == -1) {
                nonZero = v;
            }
        }
        if (nonZero == -1) {
            throw new IllegalArgumentException("all the vertices are isolated, there is no euler cycle");
        }
        return nonZero;
    }

    private boolean computed() {
        return cycle != null || errMsg != null;
    }

    private void computeCycle() {
        if (computed()) return;
        Queue<Edge>[] adjs = new Queue[g.V()];
        for (int i = 0; i < g.V(); i ++) {
            adjs[i] = new Queue<>();
        }
        int v = getMutableAdjs(adjs);
        cycle = new ArrayList<>();
        findPath(v, cycle, adjs);
        if (cycle.size() - 1 != g.E()) {
            cycle = null;
            throw new IllegalArgumentException("the graph is not sufficiently connected, there is no euler cycle");
        }
    }

    private void findPath(int v, List<Integer> cycle, Queue<Edge>[] adjs) {
        // recursion stack
        Stack<Integer> st = new Stack<>();
        st.push(v);
        while (!st.isEmpty()) {
            v = st.peek();
            // try to find an unused edge of v
            boolean found = false;
            while (!adjs[v].isEmpty()) {
                Edge e = adjs[v].dequeue();
                if (!e.used) {
                    e.used = true;
                    found = true;
                    // recursive calls to the other end of the edge
                    st.push(e.other(v));
                    break;
                }
            }
            if (!found) {
                // all edges of the current v has been used. And v can be added to cycle as well as pop out of the recursion stack
                cycle.add(v);
                st.pop();
            }
        }
    }

    /**
     *
     * @return if there is not an euler cycle, return null. You can check the reason by {@link #getErrMsg()}. Otherwise, a list of vertices representing the cycle.
     */
    public List<Integer> getCycle() {
        try {
            computeCycle();
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assert(computed());
        return cycle;
    }

    public String getErrMsg() {
        assert(errMsg != null);
        return errMsg;
    }

    private static void unitTest(Graph g, String description) {
        // print g
        System.out.println(description);
        System.out.println("----------------------------------------------");
        System.out.print(g);
        System.out.print("euler cycle: ");
        EulerCycle cycle = new EulerCycle(g);
        if (cycle.getCycle() != null) {
            for (int v:cycle.getCycle()) {
                System.out.print(v + " ");
            }
        } else {
            System.out.print(cycle.getErrMsg());
        }
        System.out.println("\n");
    }


    public static void main(String[] args) {
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);

        Graph g = GraphGenerator.eulerianCycle(V, E);
        unitTest(g, "euler cycle");

        // union of two disjoint cycles
        Graph H1 = GraphGenerator.eulerianCycle(V/2, E/2);
        Graph H2 = GraphGenerator.eulerianCycle(V - V/2, E - E/2);
        int[] perm = new int[V];
        for (int i = 0; i < V; i++)
            perm[i] = i;
        StdRandom.shuffle(perm);
        Graph G5 = new Graph(V);
        for (int v = 0; v < H1.V(); v++)
            for (int w : H1.adj(v))
                G5.addEdge(perm[v], perm[w]);
        for (int v = 0; v < H2.V(); v++)
            for (int w : H2.adj(v))
                G5.addEdge(perm[V/2 + v], perm[V/2 + w]);
        unitTest(G5, "Union of two disjoint cycles");
    }

}
