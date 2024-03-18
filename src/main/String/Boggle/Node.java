package main.String.Boggle;

class Node {
    static final int R = BoggleConstant.R;
    static final char BASE = BoggleConstant.BASE;
    boolean isEnd;
    Node[] nexts = new Node[R];
}
