package main.String.Sort;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.*;

public class LSD {
    private static final int R = 1 << 8;
    private static final int INT_BIT_CNT = 32;
    private static final int BIT_CNT_PER_PART = 4;

    private static final int RN = 1 << BIT_CNT_PER_PART;
    private static final int MASK = RN - 1;
    private LSD() {

    }

    /**
     * sort {@code strs} by strs.charAt(i)
     * @param p
     */
    private static void sortBy(int p, String[] strs) {
        int[] count = new int[R + 1];
        for (String str:strs) {
            count[str.charAt(p) + 1] ++;
        }
        for (int i = 0; i < R - 1; i ++) {
            count[i + 1] += count[i];
        }
        String[] aux = new String[strs.length];
        for (String str:strs) {
            aux[count[str.charAt(p)] ++] = str;
        }
        System.arraycopy(aux, 0, strs, 0, strs.length);
    }

    public static void sort(String[] strs, int w) {
        for (int i = w - 1; i >= 0; i --) {
            sortBy(i, strs);
        }
    }

    private static int getKey(int num, int p) {
        return (num >> (p * BIT_CNT_PER_PART)) & MASK;
    }

    /**
     * sort nums by bits in part p(0-base indexed from the least significant to the most significant)
     * @param p
     * @param nums
     */
    private static void sortBy(int p, int[] nums, int[] aux, int w) {
        int[] count = new int[RN + 1];
        for (int num:nums) {
            int key = getKey(num, p);
            count[key + 1] ++;
        }
        if (p == w - 1) {
            // the most significant part, 1 << (BIT_PER_PART - 1) is the smallest key, and the smallest key - 1  is the largest one
            int smallestKey = 1 << (BIT_CNT_PER_PART - 1);
            count[smallestKey] = 0;
            int i = smallestKey;
            while (i < smallestKey - 1 || i >= smallestKey) {
                int next = (i + 1) % (RN + 1);
                count[next] += count[i];
                i = next;
            }
        } else {
            for (int i = 0; i < RN - 1; i ++) {
                count[i + 1] += count[i];
            }
        }
        for (int num:nums) {
            int key = getKey(num, p);
            aux[count[key] ++] = num;
        }
    }

    public static void sort(int[] nums) {
        int w = INT_BIT_CNT / BIT_CNT_PER_PART;
        int[] aux = new int[nums.length];
        for (int i = 0; i < w; i ++) {
            sortBy(i, nums, aux, w);
            // aux was filled with numbers in nums sorted by the last (i + 1) part
            int[] tmp = nums;
            nums = aux;
            aux = tmp;
        }
        // because w is even, now, aux points to the original nums array. that is, the original array has been sorted
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
        boolean isNum = true;
        try {
            Integer.parseInt(strs[0]);
        } catch (NumberFormatException e) {
            isNum = false;
        }
        if (isNum) {
            int[] nums1 = new int[strs.length];
            int[] nums2 = new int[strs.length];
            for (int i = 0; i < strs.length; i ++) {
                nums1[i] = Integer.parseInt(strs[i]);
                nums2[i] = nums1[i];
            }
            Arrays.sort(nums1);
            LSD.sort(nums2);
            for (int i = 0; i < nums1.length; i ++) {
                if (nums1[i] != nums2[i]) {
                    StdOut.print("wrong!");
                    return;
                }
            }
            StdOut.println("correct");
            for (int num:nums2) {
                StdOut.print(num +" ");
            }
        } else {
            String[] strs2 = new String[strs.length];
            for (int i = 0; i < strs2.length; i ++) {
                strs2[i] = strs[i];
            }
            Arrays.sort(strs);
            LSD.sort(strs2, strs[0].length());
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


//        int n = Integer.parseInt(args[0]);
//        int[] nums1 = new int[n];
//        int[] nums2 = new int[n];
//        Random random = new Random(System.currentTimeMillis());
//        for (int i = 0; i < nums1.length; i ++) {
//            nums1[i] = random.nextInt();
//            nums2[i] = nums1[i];
//        }
//        Arrays.sort(nums1);
//        sort(nums2);
//        for (int i = 0; i < nums1.length; i ++) {
//            if (nums1[i] != nums2[i]) {
//                System.out.print("wrong!");
//                return;
//            } else {
//                System.out.print(nums2[i] + " ");
//            }
//        }
//        System.out.println();
//        System.out.print("correct!");
    }
}
