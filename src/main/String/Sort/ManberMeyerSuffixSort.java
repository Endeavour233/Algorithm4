package main.String.Sort;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class ManberMeyerSuffixSort {

    private static final int R = 1 << 8;
    private ManberMeyerSuffixSort() {

    }


    private static class SuffixComparator implements Comparator<Integer> {
        private int[] pos;
        private int offset;

        public SuffixComparator(int[] pos, int offset) {
            this.pos = pos;
            this.offset = offset;
        }

        private int getPos(Integer o, int offset) {
            int finalOffset = offset + o;
            if (finalOffset >= pos.length) {
                return -1;
            } else {
                return pos[finalOffset];
            }
        }


        @Override
        public int compare(Integer o1, Integer o2) {
            int pos1 = getPos(o1, 0);
            int pos2 = getPos(o2, 0);
            if (pos1 == pos2) {
                int pos11 = getPos(o1, offset);
                int pos21 = getPos(o2, offset);
                return  pos11 - pos21;
            } else {
                return pos1 - pos2;
            }
        }
    }



    /**
     * get an ascendingly sorted suffix array of {@code str}
     * @return an ascendingly sorted suffix array of {@code str}. Each entry value represents for the suffix of {@code str} starting from that value
     */
    public static int[] getSortedSuffixArray(String str) {
        int[] suffix = new int[str.length()];
        for (int i = 0; i < str.length(); i ++) {
            suffix[i] = i;
        }
        int[] pos = new int[str.length()];
        // sort by the first character
        int[] count = new int[R + 1];
        for (int offset:suffix) {
            count[str.charAt(offset) + 1] ++;
        }
        for (int r = 0; r < R - 1; r ++) {
            count[r + 1] += count[r];
        }
        // count[i]: the position where i should go
        Integer[] aux = new Integer[suffix.length];
        for (int offset:suffix) {
            pos[offset] = count[str.charAt(offset)];
        }
        for (int offset:suffix) {
            aux[count[str.charAt(offset)] ++] = offset;
        }
        // currently, count[i] is the position where i + 1 starts to show in aux
        int sorted = 1;
        int[] newPos = new int[pos.length];
        //given that the suffix array has been sorted by str.substring(0, 2^i), sort the suffix array by str.substring(0, 2^{i + 1})
        while (sorted < str.length()) {
            SuffixComparator comparator = new SuffixComparator(pos, sorted);
            Arrays.sort(aux, comparator);
            int posId = 0;
            newPos[aux[0]] = posId;
            for (int i = 1; i < aux.length; i ++) {
                int result = comparator.compare(aux[i - 1], aux[i]);
                if (result != 0) {
                    posId ++;
                }
                newPos[aux[i]] = posId;
            }
            int[] tmp = pos;
            pos = newPos;
            newPos = tmp;
            sorted = (sorted << 1);
        }
        for (int i = 0; i < aux.length; i ++) {
            suffix[i] = aux[i];
        }
        return suffix;
    }

    public static void main(String[] args) {
        while (StdIn.hasNextLine()) {
            String num = StdIn.readLine();
            if (num.isEmpty()) break;
            long n = Long.valueOf(num);
            Random r = new Random();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < n; i ++) {
                sb.append((char) r.nextInt(256));
            }
            String str = sb.toString();
            Integer[] suffixStr1 = new Integer[str.length()];
            for (int i = 0; i < str.length(); i ++) {
                suffixStr1[i] = i;
            }
            long time = System.nanoTime();
            Arrays.sort(suffixStr1, new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return str.substring(o1).compareTo(str.substring(o2));
                }
            });
            System.out.println("array sort consume: " + (System.nanoTime() - time));
            time = System.nanoTime();
            int[] suffix = ManberMeyerSuffixSort.getSortedSuffixArray(str);
            System.out.println("manberMeyser consume: " + (System.nanoTime() - time));
            boolean correct = true;
            for (int i = 0; i < suffix.length; i ++) {
                if (suffixStr1[i] == suffix[i]) {
                    StdOut.println(str.substring(suffix[i]));
                } else {
                    StdOut.println("system: " + suffixStr1[i] + " my: " + suffix[i]);
                }
            }
            if (!correct) {
                StdOut.println("attention! wrong!!!!");
            }
        }
    }
}
