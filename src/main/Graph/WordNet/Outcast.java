package main.Graph.WordNet;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private final WordNet net;
    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        net = wordnet;
    }
    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns)  {
        int[][] dist = new int[nouns.length][nouns.length];
        int maxD = -1;
        int result = -1;
        for (int i = 0; i < nouns.length; i ++) {
            int d = 0;
            for (int j = 0; j < i; j ++) {
                d += dist[j][i];
            }
            for (int j = (i + 1); j < nouns.length; j ++) {
                dist[i][j] = net.distance(nouns[i], nouns[j]);
                d += dist[i][j];
            }
            if (d > maxD) {
                maxD = d;
                result = i;
            }
        }
        return nouns[result];
    }
    // see test client below
    public static void main(String[] args)  {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
