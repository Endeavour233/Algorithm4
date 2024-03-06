package main.String.Sort;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class ThreeWayQuick {
    private ThreeWayQuick() {

    }
    private static int charAt(String str, int ind) {
        if (ind >= str.length()) {
            return -1;
        } else {
            return str.charAt(ind);
        }
    }

    private static void swap(String[] strs, int i, int j) {
        String tmp = strs[i];
        strs[i] = strs[j];
        strs[j] = tmp;
    }
    // sort strs by strs.substring(from) between lo and hi(inclusive)
    // lo < hi
    private static void sort(String[] strs, int lo, int hi, int from) {
        // partition by charAt(str, from)
        int pivot = charAt(strs[lo], from);
        int l = lo;
        int h = hi;
        int i = lo + 1;
        while (i <= h) {
            int curChr = charAt(strs[i], from);
            if (curChr < pivot) {
                swap(strs, l ++, i ++);
            } else {
                if (curChr == pivot) {
                    i ++;
                } else {
                    swap(strs, h --, i);
                }
            }
        }
        if (lo < l - 1) {
            sort(strs, lo, l - 1, from);
        }
        if (l < h) {
            sort(strs, l, h, from + 1);
        }
        if (h + 1 < hi) {
            sort(strs, h + 1, hi, from);
        }
    }

    public static void sort(String[] strs) {
        sort(strs, 0, strs.length - 1, 0);
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
        ThreeWayQuick.sort(strs2);
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
