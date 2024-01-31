package algorithm.src.main.UndirectedGraph.InterviewProblems;

import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;



public class NonRecursiveDFS {
    private Graph g;
    private int src;
    private int[] edgeTo;
    private boolean dfsed;
    public NonRecursiveDFS(Graph g, int src) {
        this.g = g;
        this.src = src;
        edgeTo = new int[g.V()];
        for (int i = 0; i < g.V(); i ++) {
            edgeTo[i] = -1;
        }
    }

    private static class Edge {
        public final int from;
        public final int to;
        Edge(int from, int to) {
            this.from = from;
            this.to = to;
        }
    }

    /**
     * implement dfs using whatever-first search with a stack
     */
    private void dfs() {
        Stack<Edge> st = new Stack<Edge>();
        st.push(new Edge(src, src));
        while (!st.isEmpty()) {
            Edge e = st.pop();
            if (edgeTo[e.to] == -1) {
                edgeTo[e.to] = e.from;
                for (int v:g.adj(e.to)) {
                    if (edgeTo[v] == -1) {
                        st.push(new Edge(e.to, v));
                    }
                }
            }
        }
        dfsed = true;
    }

    public String getPath(int v) {
        if (!dfsed) dfs();
        assert(dfsed);
        if (edgeTo[v] == -1) return src + " can not reach " + v;
        Stack<Integer> path = new Stack<>();
        while (edgeTo[v] != v) {
            path.push(v);
            v = edgeTo[v];
        }
        StringBuilder sb = new StringBuilder(src + "");
        for (int u:path) {
            sb.append(" -> ").append(u);
        }
        return sb.toString();
    }


    public static void main(String[] args) {
         Graph g = new Graph(new In(args[0]));
         int src = Integer.parseInt(args[1]);
         NonRecursiveDFS dfs = new NonRecursiveDFS(g, src);
         for (int v = 0; v < g.V(); v ++) {
            System.out.println(dfs.getPath(v));
         }
    }
}
