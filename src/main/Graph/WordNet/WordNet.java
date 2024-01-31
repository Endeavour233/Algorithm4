package main.Graph.WordNet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;



import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class WordNet {

    //key: noun, value: the ids of the synsets the noun appears
    private Map<String, List<Integer>> nounIds;
    private ArrayList<String> synset;
    private final Digraph setNet;
    private SAP sap;

    private void checkNull(Object obj) {
        if (obj == null) throw new IllegalArgumentException("null argument");
    }

    private int mapVertices(String fileName) {
        checkNull(fileName);
        In synsetsInfo = new In(fileName);
        Map<String, List<Integer>> map = new HashMap<>();
        ArrayList<String> vertices = new ArrayList<>();
        int vertexCnt = 0;
        try {
            while (synsetsInfo.hasNextLine()) {
                String nouns = synsetsInfo.readLine().split(",")[1];
                vertices.add(nouns);
                for (String noun:nouns.split(" +")) {
                    map.computeIfAbsent(noun, k -> new ArrayList<>()).add(vertexCnt);
                }
                vertexCnt ++;
            }
        } finally {
            synsetsInfo.close();
        }
        nounIds = map;
        synset = vertices;
        return vertexCnt;
    }

    private void addEdges(Digraph net, String hypernyms) {
        checkNull(hypernyms);
        In edgeInfo = new In(hypernyms);
        try {
            while (edgeInfo.hasNextLine()) {
                String[] edges = edgeInfo.readLine().split(",");
                if (edges.length > 1) {
                    int from = Integer.parseInt(edges[0]);
                    for (int i = 1; i < edges.length; i ++) {
                        net.addEdge(from, Integer.parseInt(edges[i]));
                    }
                }
            }
        } finally {
            edgeInfo.close();
        }
    }

    private boolean dfs(int v, Digraph g, boolean[] marked, boolean[] active) {
        marked[v] = true;
        active[v] = true;
        for (int u:g.adj(v)) {
            if (active[u]) {
                // cycle detected
                return true;
            } else {
                if (!marked[u]) {
                    if (dfs(u, g, marked, active)) {
                        return true;
                    }
                }
            }
        }
        active[v] = false;
        return false;
    }

    private boolean isAcyclic(Digraph g) {
        boolean[] marked = new boolean[g.V()];
        boolean[] active = new boolean[g.V()];
        for (int i = 0; i < g.V(); i ++) {
            if (!marked[i]) {
                if (dfs(i, g, marked, active)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkRooted(Digraph g) {
        DAGReachable reachable = new DAGReachable(g);
        int root = reachable.allReachable();
        return root != -1;
    }
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        int V = mapVertices(synsets);
        Digraph net = new Digraph(V);
        addEdges(net, hypernyms);
        if (!isAcyclic(net)) throw new IllegalArgumentException("wordnet is not a DAG");
        if (!checkRooted(net)) throw new IllegalArgumentException("wordnet is not rooted");
        setNet = net;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounIds.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        checkNull(word);
        return nounIds.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("either nounA or nounB is not in the wordNet");
        }
        if (sap == null) {
            sap = new SAP(setNet);
        }
        Iterable<Integer> A = nounIds.get(nounA);
        Iterable<Integer> B = nounIds.get(nounB);
        int dist = sap.length(A, B);
        if (dist == -1) {
            // never reach! because wordNet is rooted
            throw new IllegalArgumentException("no common ancestor!");
        } else {
            return dist;
        }
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("either nounA or nounB is not in the wordNet");
        }
        if (sap == null) {
            sap = new SAP(setNet);
        }
        Iterable<Integer> A = nounIds.get(nounA);
        Iterable<Integer> B = nounIds.get(nounB);
        int ancestor = sap.ancestor(A, B);
        if (ancestor == -1) {
            // never reach! because wordNet is rooted
            throw new IllegalArgumentException("no common ancestor!");
        } else {
            return synset.get(ancestor);
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        while (!StdIn.isEmpty()) {
            String noun1 = StdIn.readString();
            String noun2 = StdIn.readString();
            int dist = wordnet.distance(noun1, noun2);
            String sap = wordnet.sap(noun1, noun2);
            StdOut.printf("distance = %d, sap = %s\n", dist, sap);
        }
    }
}
