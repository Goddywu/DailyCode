package com.example.demospring2.a学习;

import java.util.Arrays;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-06-14
 */
public class t11_排序 {

    /**
     * 冒泡排序  O(n * logN)
     * 遍历数组，每个元素和后面的数字比较
     */
    private static void bubbleSort(int[] nums) {
        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] > nums[j]) {
                    swap(nums, i, j);
                }
            }
        }
    }

    /**
     * 快速排序
     * 选择一个基准数，通过一趟排序将要排序的数据分割成独立的两部分；其中一部分的所有数据都比另外一部分的所有数据都要小。
     * 然后，再按此方法对这两部分数据分别进行快速排序，整个排序过程可以递归进行，以此达到整个数据变成有序序列。
     */
    public static void quickSort(int[] nums) {
        quickSort(nums, 0, nums.length - 1);
    }

    private static void quickSort(int[] nums, int left, int right) {
        if (left >= right) {
            return;
        }
        int base = nums[left];
        int l = left;
        int r = right;
        while (l < r) {
            while (l < r && nums[r] > base) {
                r--;
            }
            nums[l] = nums[r];
            while (l < r && nums[l] < base) {
                l++;
            }
            nums[r] = nums[l];
        }
        nums[l] = base;
        quickSort(nums, left, l - 1);
        quickSort(nums, l + 1, right);
    }

    /**
     * 选择排序 O(n * logN)
     * 首先在未排序的数列中找到最小(or最大)元素，然后将其存放到数列的起始位置；
     * 接着，再从剩余未排序的元素中继续寻找最小(or最大)元素，然后放到已排序序列的末尾。
     * 以此类推，直到所有元素均排序完毕
     */
    private static void selectionSort(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            int minIndex = i;
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[j] < nums[minIndex]) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                swap(nums, i, minIndex);
            }
        }
    }

    /**
     * 插入排序 O(n * logN)
     * 把n个待排序的元素看成为一个有序表和一个无序表。
     * 开始时有序表中只包含1个元素，无序表中包含有n-1个元素，
     * 排序过程中每次从无序表中取出第一个元素，将它插入到有序表中的适当位置，使之成为新的有序表，
     * 重复n-1次可完成排序过程
     */
    private static void insertionSort(int[] nums) {
        for (int i = 1; i < nums.length; i++) {
            int j = i - 1;
            int numI = nums[i];
            while (j >= 0) {
                if (numI < nums[j]) {
                    swap(nums, j + 1, j);
                    j--;
                } else {
                    nums[j + 1] = numI;
                    break;
                }
            }
        }
    }

    /**
     * 希尔排序
     * 对于n个待排序的数列，取一个小于n的整数gap(gap被称为步长)将待排序元素分成若干个组子序列，所有距离为gap的倍数的记录放在同一个组中；
     * 然后，对各组内的元素进行直接插入排序。这一趟排序完成之后，每一个组的元素都是有序的。
     * 然后减小gap的值，并重复执行上述的分组和排序。重复这样的操作，当gap=1时，整个数列就是有序的
     * 1. 把所有数据分成n组，一般是总数/2组
     * 2. 组内走插入排序
     * 3. 把所有数据重新分为n/2组，排序，直至所有数据分为1组
     */
    private static void shellSort(int[] nums) {

    }

    /**
     * 堆排序
     * 堆排序是指利用堆这种数据结构所设计的一种排序算法。
     * 堆是一个近似完全二叉树的结构，并同时满足堆积的性质：即子结点的键值或索引总是小于（或者大于）它的父节点
     */
    private static void heapSort(int[] nums) {

    }

    /**
     * 归并排序
     * 将两个的有序数列合并成一个有序数列
     *
     */
    private static void mergeSort(int[] nums) {

    }

    /**
     * 桶排序
     * 将数组分到有限数量的桶子里。每个桶子再个别排序
     */
    private static void buketSort(int[] nums) {

    }

    /**
     * 基数排序
     * 将整数按位数切割成不同的数字，然后按每个位数分别比较。
     * 具体做法是: 将所有待比较数值统一为同样的数位长度，数位较短的数前面补零。
     * 然后，从最低位开始，依次进行一次排序。这样从最低位排序一直到最高位排序完成以后, 数列就变成一个有序序列
     */
    private static void radixSort(int[] nums) {

    }


    private static int[] copyArray(int[] nums) {
        return Arrays.copyOf(nums, nums.length);
    }

    private static void swap(int[] nums, int x, int y) {
        int tmp = nums[x];
        nums[x] = nums[y];
        nums[y] = tmp;
    }


    public static void main(String[] args) {
        int[][] numsArray = new int[][]{
                {2, 3, 5, 4, 1, 6, 9, 8, 7},
                {7, 3, 5, 4, 1, 6, 9, 8, 7, 2, 3, 1}
        };

        for (int[] nums : numsArray) {
            System.out.println("-----原数组：" + Arrays.toString(nums));

            int[] copy = copyArray(nums);
            bubbleSort(copy);
            System.out.println("冒泡排序：" + Arrays.toString(copy));

            copy = copyArray(nums);
            quickSort(copy);
            System.out.println("快速排序：" + Arrays.toString(copy));

            copy = copyArray(nums);
            selectionSort(copy);
            System.out.println("选择排序：" + Arrays.toString(copy));

            copy = copyArray(nums);
            insertionSort(copy);
            System.out.println("插入排序：" + Arrays.toString(copy));

            copy = copyArray(nums);
            shellSort(copy);
            System.out.println("希尔排序：" + Arrays.toString(copy));

            copy = copyArray(nums);
            heapSort(copy);
            System.out.println("堆排序：" + Arrays.toString(copy));

            copy = copyArray(nums);
            mergeSort(copy);
            System.out.println("归并排序：" + Arrays.toString(copy));

            copy = copyArray(nums);
            buketSort(copy);
            System.out.println("桶排序：" + Arrays.toString(copy));

            copy = copyArray(nums);
            radixSort(copy);
            System.out.println("基数排序：" + Arrays.toString(copy));

        }
    }
}
