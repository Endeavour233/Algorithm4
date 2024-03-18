package main.String.Boggle;


import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class BoggleSolver {
    private static final int[] score = {0, 0, 0, 1, 1, 2, 3, 5, 11};
    private static final int MINIMUM_VALID_WORD_LENGTH = 3;
    private static final int[][] offsets = {
            {-1,-1},{-1,0},{-1,1},
            {0,-1},{0,1},
            {1,-1},{1,0},{1,1}
    };


    // build a trie with dictionary
    private RwayTrie dict;
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        buildTrie(dictionary);
    }

    private void buildTrie(String[] dictionary) {
        dict = new RwayTrie();
        for (String word:dictionary) {
            if (word.length() >= MINIMUM_VALID_WORD_LENGTH) {
                dict.insert(word);
            }
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Node root = dict.getRoot();
        if (root == null) return new ArrayList<>();
        int m = board.rows();
        int n = board.cols();
        RwayTrie boardST = new RwayTrie();
        boolean[][] marked = new boolean[m][n];
        for (int i = 0; i < m; i ++) {
            for (int j = 0; j < n; j ++) {
                char c = board.getLetter(i, j);
                Node node = dict.getNextNode(root, c);
                if (node != null) {
                    String prefix = "" + c;
                    if (c == BoggleConstant.SPECIAL_CASE) {
                        prefix = prefix + BoggleConstant.SPECIAL_CASE_APPEND;
                    }
                    collect(prefix, node, marked, board, m, n, i, j, boardST);
                }
            }
        }
        return boardST.keys();
    }

    // the word from root to node corresponds to prefix
    // board[i][j] == prefix.charAt(prefix.length - 1)
    // marked[i][j] = false
    // node is not null
    private void collect(String prefix, Node node, boolean[][] marked, BoggleBoard board, int m, int n, int i, int j, RwayTrie boardst) {
        marked[i][j] = true;
        if (node.isEnd) {
            boardst.insert(prefix);
        }
        for (int[] offset:offsets) {
            int newI = i + offset[0];
            int newJ = j + offset[1];
            if (newI >= 0 && newJ >= 0 && newI < m && newJ < n && !marked[newI][newJ]) {
                char c = board.getLetter(newI, newJ);
                Node next = dict.getNextNode(node, c);
                if (next != null) {
                    String newPrefix = String.format("%s%c",prefix,c);
                    if (c == BoggleConstant.SPECIAL_CASE) {
                        newPrefix = String.format("%s%c", newPrefix, BoggleConstant.SPECIAL_CASE_APPEND);
                    }
                    collect(newPrefix, next, marked, board, m, n, newI, newJ, boardst);
                }
            }
        }
        marked[i][j] = false;
    }


    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (dict.search(word)) {
            if (word.length() >= score.length) {
                return score[score.length - 1];
            } else {
                return score[word.length()];
            }
        } else {
            return 0;
        }
    }


    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word:solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }


}
