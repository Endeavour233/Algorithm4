package main.String.Search;


import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class KMP extends SubstringSearch {
    public static final int R = 1 << 8;
    private int[][] dfa;
    private final int n;
    public KMP(String pattern) {
        super(pattern);
        n = pattern.length();
        dfa = new int [R][n];
        // build dfa
        int x = 0;
        dfa[pattern.charAt(0)][0] = 1;
        for (int j = 1; j < n; j ++) {
            for (int c = 0; c < R; c ++) {
                dfa[c][j] = dfa[c][x];
            }
            dfa[pattern.charAt(j)][j] = j + 1;
            x = dfa[pattern.charAt(j)][x];
        }
    }
    @Override
    public Iterable<Integer> searchIn(String str) {
        List<Integer> result = new ArrayList<>();
        int matched = 0;
        for (int i = 0; i < str.length(); i ++) {
            matched = dfa[str.charAt(i)][matched];
            if (matched == n) {
                matched = 0;
                result.add(i - n + 1);
            }
        }
        if (result.isEmpty()) {
            return null;
        } else {
            return result;
        }
    }

    public static void main(String[] args) {
        KMP kmp = new KMP(args[0]);
        StdOut.println("test offline search---------------------");
        while (StdIn.hasNextLine()) {
            String str = StdIn.readLine();
            if (str.isEmpty()) break;
            Iterable<Integer> inds = kmp.searchIn(str);
            if (inds == null) {
                StdOut.println(String.format("not found pattern %s in %s", args[0], str));
            } else {
                StdOut.println(String.format("found substrings in %s which match the pattern %s", str, args[0]));
                inds.forEach(integer -> StdOut.print(integer + " "));
                StdOut.println();
            }
        }
    }

}
