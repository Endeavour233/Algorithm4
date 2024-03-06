package main.String.Sort;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class MSD {
    private static final int R = 1 << 8;
    private MSD() {

    }

    private static int charAt(String str, int ind) {
        if (ind >= str.length()) {
            return -1;
        } else {
            return str.charAt(ind);
        }
    }
    // lo < hi
    // sort strs by strs.substring(from) between lo and hi(inclusive)
    private static void sort(String[] strs, String[] aux, int lo, int hi, int from) {
        int[] count = new int[R + 2];
        for (int i = lo; i <= hi; i ++) {
            count[charAt(strs[i], from) + 2] ++;
        }
        // after the for loop as below, count[i]::= number of key values that are smaller than or equal to i - 2. that is the start pos(offset to lo) of i - 1
        // (count[R + 1] is the number of key value: R - 1)
        for (int i = 0; i < R; i ++) {
            count[i + 1] += count[i];
        }
        for (int i = lo; i <= hi; i ++) {
            aux[lo + (count[charAt(strs[i], from) + 1] ++)] = strs[i];
        }
        for (int i = lo; i <= hi; i ++) {
            strs[i] = aux[i];
        }
        // currently, count[i] ::= start pos of key i in current strs array (count[R + 1] is the number of key value: R - 1)
        for (int r = 0; r < R; r ++) {
            int l = count[r];
            int h = count[r + 1] - 1;
            if (l < h) {
                sort(strs, aux, l, h, from + 1);
            }
        }
    }

    public static void sort(String[] strs) {
        sort(strs, new String[strs.length], 0, strs.length - 1, 0);
    }

    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        while (StdIn.hasNextLine()) {
            String s = StdIn.readLine();
            if (s.isEmpty()) break;
            list.add(s);
        }
        if (list.isEmpty()) return;
        String[] strs = new String[list.size()];
        list.toArray(strs);
        String[] strs2 = new String[strs.length];
        for (int i = 0; i < strs2.length; i ++) {
            strs2[i] = strs[i];
        }
        Arrays.sort(strs);
        MSD.sort(strs2);
        for (int i = 0; i < strs2.length; i ++) {
            if (!strs[i].equals(strs2[i])) {
                StdOut.print("wrong!");
                return;
            }
        }
        StdOut.println("correct");
        for (String str:strs2) {
            StdOut.print(str +" ");
        }
    }
}
