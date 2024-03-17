package main.String.Search.InterviewProblems;

import edu.princeton.cs.algs4.StdIn;

public class TandemRepeat {
    public static final int R = 1 << 8;
    public static String find(String b, String s) {
        if (b.isEmpty()) return "";
        int m = b.length();
        int n = s.length();
        int k = n / m;
        int[][] dfa = new int[R][k * m];
        dfa[b.charAt(0)][0] = 1;
        int x = 0;
        for (int j = 1; j < (k * m); j ++) {
            for (int c = 0; c < R; c ++) {
                dfa[c][j] = dfa[c][x];
            }
            char chr = b.charAt(j % m);
            dfa[chr][j] = j + 1;
            x = dfa[chr][x];
        }
        x = 0;
        int maxState = 0;
        int targetInd = -1;
        for (int i = 0; i < n; i ++) {
            x = dfa[s.charAt(i)][x];
            if (x > maxState) {
                targetInd = i - x + 1;
                maxState = x;
            }
            if (x == k * m) break;
        }
        if (targetInd == -1) {
            return null;
        } else {
            return s.substring(targetInd, maxState / m * m + targetInd);
        }
    }

    public static void main(String[] args) {
        while (StdIn.hasNextLine()) {
            String str = StdIn.readLine();
            if (str.isEmpty()) break;
            String[] st = str.split(" +");
            if (st.length < 2) break;
            System.out.printf("check b: %s and s: %s: ", st[0], st[1]);
            String tandem = TandemRepeat.find(st[0], st[1]);
            if (tandem == null) {
                System.out.printf("%s is not a substring of %s\n", st[0], st[1]);
            } else {
                System.out.println(tandem);
            }
        }
    }
}
