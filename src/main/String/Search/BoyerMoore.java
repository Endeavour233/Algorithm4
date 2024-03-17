package main.String.Search;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class BoyerMoore extends SubstringSearch {
    public static final int R = 1 << 8;
    private final int[] rightest;
    private final String pat;
    public BoyerMoore(String pattern) {
        super(pattern);
        rightest = new int[R];
        for (int i = 0; i < R; i ++) {
            rightest[i] = -1;
        }
        for (int i = 0; i < pattern.length(); i ++) {
            rightest[pattern.charAt(i)] = i;
        }
        pat = pattern;
    }
    @Override
    public Iterable<Integer> searchIn(String str) {
        List<Integer> result = new ArrayList<>();
        int m = pat.length();
        int r = m - 1;
        while (r < str.length()) {
            int i = r;
            char c;
            boolean mismatch = false;
            for (int j = m - 1; j >= 0; j --) {
                if (pat.charAt(j) != (c = str.charAt(i))) {
                    if (rightest[c] == -1) {
                        // not present in the pattern, move to i + M
                        r = i + m;
                    } else {
                        // present in pattern, align it with its rightest occurrence in the pattern
                        // if this alignment will cause backup, we should simply move r one step right
                        r = Math.max(r + 1, i + m - rightest[c] - 1);
                    }
                    mismatch = true;
                    break;
                }
                i --;
            }
            if (!mismatch) {
                // matched
                result.add(i + 1);
                // search for the next match
                r = i + 2 * m;
            }
        }
        if (result.isEmpty()) {
            return null;
        } else {
            return result;
        }
    }

    public static void main(String[] args) {
        BoyerMoore bm = new BoyerMoore(args[0]);
        StdOut.println("test offline search---------------------");
        while (StdIn.hasNextLine()) {
            String str = StdIn.readLine();
            if (str.isEmpty()) break;
            Iterable<Integer> inds = bm.searchIn(str);
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
