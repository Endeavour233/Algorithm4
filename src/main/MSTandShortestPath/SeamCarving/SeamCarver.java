package main.MSTandShortestPath.SeamCarving;

import edu.princeton.cs.algs4.Picture;

import java.awt.Color;


public class SeamCarver {

    private Picture pic;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        validNotNull(picture);
        // ensure that the original picture won't be mutated by SeamCarver
        pic = new Picture(picture);
    }


    // current picture
    public Picture picture() {
        return new Picture(pic);
    }

    // width of current picture
    public int width() {
        return pic.width();
    }

    // height of current picture
    public int height() {
        return pic.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        validIndexBoundary(x, pic.width());
        validIndexBoundary(y, pic.height());
        if (x == 0 || y == 0 || x == pic.width() - 1 || y == pic.height() - 1) return 1000;
        Color leftColor = pic.get(x - 1, y);
        Color rightColor = pic.get(x + 1, y);
        Color topColor = pic.get(x, y - 1);
        Color bottomColor = pic.get(x, y + 1);
        int rdx = leftColor.getRed() - rightColor.getRed();
        int gdx = leftColor.getGreen() - rightColor.getGreen();
        int bdx = leftColor.getBlue() - rightColor.getBlue();
        int dx = rdx * rdx + gdx * gdx + bdx * bdx;
        int rdy = topColor.getRed() - bottomColor.getRed();
        int gdy = topColor.getGreen() - bottomColor.getGreen();
        int bdy = topColor.getBlue() - bottomColor.getBlue();
        int dy = rdy * rdy + gdy * gdy + bdy * bdy;
        return Math.sqrt(dx + dy);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int height = pic.height();
        int width = pic.width();
        if (height == 0 || width == 0) return new int[0];
        double[] minSeamEnergy = new double[height];
        double[] tmp = new double[height];
        // result[i] stores the seam value
        int[][] prevTo = new int[height][width];
        for (int i = 0; i < height; i ++) {
            minSeamEnergy[i] = energy(0, i);
        }
        int[] offsets = {-1, 0, 1};
        double minEnergy;
        int prev;
        for (int i = 1; i < width; i ++) {
            for (int j = 0; j < height; j ++) {
                minEnergy = Double.POSITIVE_INFINITY;
                prev = 0;
                for (int offset:offsets) {
                    int y = j + offset;
                    if (y >= 0 && y < height && minSeamEnergy[y] < minEnergy) {
                        minEnergy = minSeamEnergy[y];
                        prev = y;
                    }
                }
                tmp[j] = minEnergy + energy(i, j);
                prevTo[j][i] = prev;
            }
            double[] cur = tmp;
            tmp = minSeamEnergy;
            minSeamEnergy = cur;
        }
        minEnergy = minSeamEnergy[0];
        prev = 0;
        for (int i = 1; i < height; i ++) {
            if (minSeamEnergy[i] < minEnergy) {
                minEnergy = minSeamEnergy[i];
                prev = i;
            }
        }
        int[] result = new int[width];
        result[width - 1] = prev;
        for (int i = width - 1; i > 0; i --) {
            result[i - 1] = prevTo[prev][i];
            prev = result[i - 1];
        }
        return result;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int height = pic.height();
        int width = pic.width();
        if (height == 0 || width == 0) return new int[0];
        double[] minSeamEnergy = new double[width];
        double[] tmp = new double[width];
        int[][] prevTo = new int[height][width];
        for (int i = 0; i < width; i ++) {
            minSeamEnergy[i] = energy(i, 0);
        }
        int[] offsets = {-1, 0, 1};
        double minEnergy;
        int prev;
        for (int i = 1; i < height; i ++) {
            for (int j = 0; j < width; j ++) {
                minEnergy = Double.POSITIVE_INFINITY;
                prev = 0;
                for (int offset:offsets) {
                    int x = j + offset;
                    if (x >= 0 && x < width && minSeamEnergy[x] < minEnergy) {
                        minEnergy = minSeamEnergy[x];
                        prev = x;
                    }
                }
                tmp[j] = minEnergy + energy(j, i);
                prevTo[i][j] = prev;
            }
            double[] cur = tmp;
            tmp = minSeamEnergy;
            minSeamEnergy = cur;
        }
        minEnergy = minSeamEnergy[0];
        prev = 0;
        for (int i = 1; i < width; i ++) {
            if (minSeamEnergy[i] < minEnergy) {
                minEnergy = minSeamEnergy[i];
                prev = i;
            }
        }
        int[] result = new int[height];
        result[height - 1] = prev;
        for (int i = height - 1; i > 0; i --) {
            result[i - 1] = prevTo[i][prev];
            prev = result[i - 1];
        }
        return result;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        validNotNull(seam);
        int height = pic.height();
        int width = pic.width();
        if (seam.length != width) throw new IllegalArgumentException("seam length is not equal to picture's width");
        if (height <= 1) throw new IllegalArgumentException("the height of the current picture is <= 1, no horizontal seam can be removed");
        Picture p = new Picture(width, height - 1);
        for (int col = 0; col < width; col++) {
            if (seam[col] >= height || seam[col] < 0) throw new IllegalArgumentException("out of boundary");
            if (col >= 1 && Math.abs(seam[col] - seam[col - 1]) > 1) throw new IllegalArgumentException("2 adjacent entries of seam differ by more than 1");
            for (int row = 0; row < height - 1; row++) {
                if (row < seam[col]) {
                    p.set(col, row, pic.get(col, row));
                } else {
                    p.set(col, row, pic.get(col, row + 1));
                }
            }
        }
        pic = p;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        validNotNull(seam);
        int height = pic.height();
        int width = pic.width();
        if (seam.length != height) throw new IllegalArgumentException("seam length is not equal to picture's height");
        if (width <= 1) throw new IllegalArgumentException("the width of the current picture is <= 1, no vertical seam can be removed");
        Picture p = new Picture(width - 1, height);
        for (int row = 0; row < height; row++) {
            if (seam[row] >= width || seam[row] < 0) throw new IllegalArgumentException("out of boundary");
            if (row >= 1 && Math.abs(seam[row] - seam[row - 1]) > 1) throw new IllegalArgumentException("2 adjacent entries of seam differ by more than 1");
            for (int col = 0; col < width - 1; col++) {
                if (col < seam[row]) {
                    p.set(col, row, pic.get(col, row));
                } else {
                    p.set(col, row, pic.get(col + 1, row));
                }
            }
        }
        pic = p;
    }

    /**
     * throw IllegalArgumentException if indices are out of bound
     * @param i
     */
    private void validIndexBoundary(int i, int range) {
        if (i < 0 || i >= range) throw new IllegalArgumentException("out of boundary");
    }

    private void validNotNull(Object obj) {
        if (obj == null) throw new IllegalArgumentException("null pointer!");
    }

    //  unit testing (optional)
    public static void main(String[] args) {

    }

}
