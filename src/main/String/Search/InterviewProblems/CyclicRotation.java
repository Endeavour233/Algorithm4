package main.String.Search.InterviewProblems;


import edu.princeton.cs.algs4.StdIn;

public class CyclicRotation {
    public static final int R = 1 << 8;
    public static boolean check(String s, String t) {
        if (s.length() != t.length()) return false;
        if (s.isEmpty()) return true;
        int[][] dfa = new int[R][s.length()];
        dfa[s.charAt(0)][0] = 1;
        int x = 0;
        for (int j = 1; j < s.length(); j ++) {
            for (int c = 0; c < R; c ++) {
                dfa[c][j] = dfa[c][x];
            }
            dfa[s.charAt(j)][j] = j + 1;
            x = dfa[s.charAt(j)][x];
        }
        x = 0;
        for (int i = 0; i < (2 * t.length()); i ++) {
            x = dfa[t.charAt(i % (t.length()))][x];
            if (x == s.length()) return true;
        }
        return false;
    }

    public static void main(String[] args) {
        while (StdIn.hasNextLine()) {
            String str = StdIn.readLine();
            if (str.isEmpty()) break;
            String[] st = str.split(" +");
            if (st.length < 2) break;
            System.out.printf("check %s and %s: ", st[0], st[1]);
            System.out.println(CyclicRotation.check(st[0], st[1]));
        }
    }
}
