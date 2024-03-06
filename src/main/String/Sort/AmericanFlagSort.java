package main.String.Sort;

import java.util.Random;

/**
 * in-place key-indexed sorting
 */
public class AmericanFlagSort {

    public static void sort(int[] nums, int R) {
        int[] count = new int[R + 1];
        for (int num:nums) {
            count[num + 1] ++;
        }
        for (int i = 0; i < R - 1; i ++) {
            count[i + 1] += count[i];
        }
        // count[i]::= the position to put i
        int[] start = new int[count.length];
        for (int i = 0; i < R; i ++) {
            start[i] = count[i];
        }
        int cur = 0;
        while (cur < nums.length) {
            int targetPos = count[nums[cur]];
            int firstPos = start[nums[cur]];
            if (cur >= firstPos && cur < targetPos) {
                cur = targetPos;
            } else {
                if (targetPos == cur) {
                    count[nums[cur]] ++;
                    cur ++;
                } else {
                    int tmp = nums[cur];
                    nums[cur] = nums[targetPos];
                    nums[targetPos] = tmp;
                    count[tmp] ++;
                }
            }
        }
    }

    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int R = Integer.parseInt(args[1]);
        int[] nums = new int[N];
        Random r = new Random(System.currentTimeMillis());
        for (int i = 0; i < N; i ++) {
            nums[i] = r.nextInt(R);
        }
        sort(nums, R);
        for (int i = 1; i < nums.length; i ++) {
            if (nums[i] < nums[i - 1]) {
                System.out.println("wrong!");
                return;
            }
        }
        System.out.println("success!");
    }
}
