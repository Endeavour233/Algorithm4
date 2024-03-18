package main.String.Boggle;

import java.util.ArrayList;
import java.util.List;



class RwayTrie {
    private Node root;

    void insert(String key) {
        root = insert(root, key, 0);
    }

    private Node insert(Node node, String key, int from) {
        if (node == null) {
            node = new Node();
        }
        if (from == key.length()) {
            node.isEnd = true;
        } else {
            node.nexts[key.charAt(from) - Node.BASE] = insert(node.nexts[key.charAt(from) - Node.BASE], key, from + 1);
        }
        return node;
    }

    boolean search(String key) {
        Node node = search(root, key, 0);
        if (node == null) {
            return false;
        } else {
            return node.isEnd;
        }
    }

    // find the node corresponds to key
    // the string from root to node is equal to key.substring(0, to)
    // to <= key.length
    private Node search(Node node, String key, int to) {
        if (node == null) return null;
        if (to == key.length()) {
            return node;
        } else {
            return search(node.nexts[key.charAt(to) - Node.BASE], key, to + 1);
        }
    }

    Iterable<String> keys() {
        List<String> keys = new ArrayList<>();
        if (root == null) return keys;
        collectAll(root, "", keys);
        return keys;
    }

    // the string from root to node is supposed to be equal with curPrefix
    // node != null
    private void collectAll(Node node, String curPrefix, List<String> container) {
        if (node.isEnd) {
            container.add(curPrefix);
        }
        for (int i = 0; i < Node.R; i ++) {
            if (node.nexts[i] != null) {
                String newPrefix = String.format("%s%s", curPrefix, (char) (i + Node.BASE));
                collectAll(node.nexts[i], newPrefix, container);
            }
        }
    }

    Node getRoot() {
        return root;
    }

    Node getNextNode(Node node, char c) {
        if (node == null) return null;
        Node next = node.nexts[c - Node.BASE];
        if (next != null) {
            if (c == BoggleConstant.SPECIAL_CASE) {
                next = next.nexts[BoggleConstant.SPECIAL_CASE_APPEND - Node.BASE];
            }
        }
        return next;
    }

}
