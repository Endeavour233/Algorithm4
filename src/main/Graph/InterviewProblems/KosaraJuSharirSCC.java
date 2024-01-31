package main.Graph.InterviewProblems;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

import java.util.ArrayList;
import java.util.List;

public class KosaraJuSharirSCC {

    private final Digraph g;
    private int[] ids;
    private List<List<Integer>> sccs;
    public KosaraJuSharirSCC(Digraph g) {
        this.g = g;
    }
    private void dfs(Digraph g, int v, Stack<Integer> st, boolean[] marked) {
        marked[v] = true;
        for (int w:g.adj(v)) {
            if (!marked[w]) {
                dfs(g, w, st, marked);
            }
        }
        st.push(v);
    }
    private Iterable<Integer> findRevPostOrder(Digraph g) {
        Stack<Integer> st = new Stack<>();
        boolean[] marked = new boolean[g.V()];
        for (int i = 0; i < g.V(); i ++) {
            if (!marked[i]) {
                dfs(g, i, st, marked);
            }
        }
        return st;
    }

    private void markAndCollect(int v, int id, boolean[] marked, List<Integer> container) {
        marked[v] = true;
        ids[v] = id;
        container.add(v);
        for (int w:g.adj(v)) {
            if (!marked[w]) {
                markAndCollect(w, id, marked, container);
            }
        }
    }

    private void compute() {
        if (ids != null) return;
        Digraph revG = g.reverse();
        Iterable<Integer> revPostOrder = findRevPostOrder(revG);
        ids = new int[g.V()];
        sccs = new ArrayList<>();
        int id = 0;
        boolean[] marked = new boolean[g.V()];
        for (int v:revPostOrder) {
            if (!marked[v]) {
                List<Integer> component = new ArrayList<>();
                markAndCollect(v, id, marked, component);
                sccs.add(component);
                id ++;
            }
        }
    }

    /**
     * 0-based index of the strong component that {@code v} lies in
     */
    public int id(int v) {
        compute();
        return ids[v];
    }

    /**
     * the number of the strong components in  {@code g}
     */
    public int count() {
        compute();
        return sccs.size();
    }

    /**
     * vertices that lie in the strong component specified by {@code id}
     * @param id 0-based index of the strong component
     */
    public List<Integer> query(int id) {
        compute();
        return sccs.get(id);
    }

    private static void unitTest(Digraph g, String description) {
        System.out.println(description);
        System.out.println("------------------------------------");
        System.out.print(g);
        KosaraJuSharirSCC sc = new KosaraJuSharirSCC(g);
        int count = sc.count();
        System.out.println("there are " + count + " strong components in the graph");
        for (int i = 0; i < count; i ++) {
            System.out.print(i + ": ");
            for (int v:sc.query(i)) {
                System.out.print(v +" ");
                assert(sc.id(v) == i);
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        unitTest(new Digraph(new In(args[0])), args[1]);
    }
}
