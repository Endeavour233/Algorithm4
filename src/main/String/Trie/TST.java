package main.String.Trie;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import kotlin.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


// key can't be an empty string
public class TST<T> implements Trie<T> {
    private static class Node<T> {
        char k;
        T value;
        Node<T> left;
        Node<T> middle;
        Node<T> right;
        public Node(char key) {
            k = key;
        }
    }

    private static final char WILDCARD = '.';

    private Node<T> root;
    // from < word.length
    private Node<T> insert(Node<T> node, String word, T value, int from) {
        char c = word.charAt(from);
        if (node == null) {
            node = new Node<T>(c);
        }
        if (node.k == c) {
            if (from == word.length() - 1) {
                node.value = value;
            } else {
                node.middle = insert(node.middle, word, value, from + 1);
            }
        } else {
            if (node.k < c) {
                node.right = insert(node.right, word, value, from);
            } else {
                node.left = insert(node.left, word, value, from);
            }
        }
        return node;
    }
    public void insert(String word, T value) {
        root = insert(root, word, value, 0);
    }
    // to < word.length
    // the string from root to the parent of node is supposed to be equal to word.substring(0,to)
    private Node<T> search(Node<T> node, String word, int to) {
        if (node == null) return null;
        char c = word.charAt(to);
        if (node.k == c) {
            if (to == word.length() - 1) {
                return node;
            } else {
                return search(node.middle, word, to + 1);
            }
        } else {
            if (node.k < c) {
                return search(node.right, word, to);
            } else {
                return search(node.left, word, to);
            }
        }
    }


    public T search(String word) {
        Node<T> n = search(root, word, 0);
        if (n == null) {
            return null;
        } else {
            return n.value;
        }
    }

    public Iterable<String> keys() {
        List<String> result = new ArrayList<>();
        collectAll(root, "", result);
        return result;
    }
    // the string from root to node's parent is equal to prefix
    private void collectAll(Node node, String prefix, List<String> container) {
        if (node == null) return;
        collectAll(node.left, prefix, container);
        String newPrefix = String.format("%s%s", prefix, node.k);
        if (node.value != null) {
            // add the key corresponding to the value stored in node
            container.add(newPrefix);
        }
        // collect keys started with newPrefix
        collectAll(node.middle, newPrefix, container);
        // collect keys larger than node.k
        collectAll(node.right, prefix, container);
    }
    public Iterable<String> keysThatMatch(String s) {
        List<String> keys = new ArrayList<>();
        collectMatch(root, s, "", keys);
        return keys;
    }
    // prefix.length < s.length
    // the string from root to node's parent is supposed to be equal to prefix
    private void collectMatch(Node node, String s, String prefix, List<String> container) {
        if (node == null) return;
        char targetChr = s.charAt(prefix.length());
        if (targetChr == WILDCARD) {
            collectMatch(node.left, s, prefix, container);
            String newPrefix = String.format("%s%s", prefix, node.k);
            if (newPrefix.length() == s.length()) {
                if (node.value != null) {
                    container.add(newPrefix);
                }
            } else {
                collectMatch(node.middle, s, newPrefix, container);
            }
            collectMatch(node.right, s, prefix, container);
        } else {
            if (node.k == targetChr) {
                String newPrefix = String.format("%s%s", prefix, node.k);
                if (newPrefix.length() == s.length()) {
                    if (node.value != null) {
                        container.add(newPrefix);
                    }
                } else {
                    collectMatch(node.middle, s, newPrefix, container);
                }
            } else {
                if (node.k < targetChr) {
                    collectMatch(node.right, s, prefix, container);
                } else {
                    collectMatch(node.left, s, prefix, container);
                }
            }
        }
    }

    public Iterable<String> keysWithPrefix(String prefix) {
        Node n = search(root, prefix, 0);
        List<String> result = new ArrayList<>();
        if (n.value != null) {
            result.add(prefix);
        }
        collectAll(n.middle, prefix, result);
        return result;
    }

    // longest string in trie that is a prefix of query
    public String longestPrefixOf(String query) {
        int length = longest(root, query, 0, 0);
        if (length == 0) {
            return null;
        } else {
            return query.substring(0, length);
        }
    }
    // to < query.length
    private int longest(Node<T> node, String query, int to, int longest) {
        if (node == null) return longest;
        char targetChr = query.charAt(to);
        if (node.k == targetChr) {
            if (node.value != null) {
                longest = to + 1;
            }
            if (to + 1 < query.length()) {
                longest = longest(node.middle, query, to + 1, longest);
            }
        } else {
            if (node.k < targetChr) {
                longest = longest(node.right, query, to, longest);
            } else {
                longest = longest(node.left, query, to, longest);
            }
        }
        return longest;
    }

    @Override
    public boolean delete(String key) {
        Pair<Node<T>, Boolean> result = delete(root, 0, key);
        root = result.getFirst();
        return result.getSecond();
    }
    // the string from root to node's parent is supposed to be equal to key.substring(0, to)
    // to < key.length
    private Pair<Node<T>, Boolean> delete(Node<T> node, int to, String key) {
        if (node == null) return new Pair<>(null, false);
        char targetChr = key.charAt(to);
        boolean deleted = false;
        if (node.k == targetChr) {
            if (to == key.length() - 1) {
                if (node.value != null) {
                    node.value = null;
                    deleted = true;
                }
            } else {
                Pair<Node<T>, Boolean> result = delete(node.middle, to + 1, key);
                node.middle = result.getFirst();
                deleted = result.getSecond();
            }
        } else {
            if (node.k < targetChr) {
                Pair<Node<T>, Boolean> result = delete(node.right, to, key);
                node.right = result.getFirst();
                deleted = result.getSecond();
            }  else {
                Pair<Node<T>, Boolean> result = delete(node.left, to, key);
                node.left = result.getFirst();
                deleted = result.getSecond();
            }
        }
        if (node.value != null) return new Pair<>(node, deleted);
        if (node.left != null || node.middle != null || node.right != null) {
            return new Pair<>(node, deleted);
        } else {
            return new Pair<>(null, deleted);
        }
    }


    public static void main(String[] args) {
        TST<Integer> trie = new TST<Integer>();
        int i = 0;
        ArrayList<String> str = new ArrayList<>();
        while (StdIn.hasNextLine()) {
            String word = StdIn.readLine();
            if (word.isEmpty()) break;
            str.add(word);
            trie.insert(word, i ++);
        }
        StdOut.println("test keys --------------------------------");
        Collections.sort(str);
        i = 0;
        for (String key:trie.keys()) {
            if (!key.equals(str.get(i ++))) {
                StdOut.println("keys not match");

            }
        }
        if (i != str.size()) {
            StdOut.println("missing keys in trie");
        }
        StdOut.println("test search -----------------------------");
        while (StdIn.hasNextLine()) {
            String word = StdIn.readLine();
            if (word.isEmpty()) break;
            Integer index = trie.search(word);
            if (index == null) {
                StdOut.println("not found");
            } else {
                StdOut.println("index: " + index);
            }
        }
        StdOut.println("test keysWithPrefix ---------------------");
        String prefix = StdIn.readLine();
        for (String key:trie.keysWithPrefix(prefix)) {
            StdOut.println(key);
        }
        StdOut.println("test keysThatMatch -----------------------");
        String regx = StdIn.readLine();
        for (String key:trie.keysThatMatch(regx)) {
            StdOut.println(key);
        }
        StdOut.println("test longestPrefixOf");
        StdOut.println(trie.longestPrefixOf(StdIn.readLine()));
        StdOut.println("test delete -------------------------");
        while (StdIn.hasNextLine()) {
            String s = StdIn.readLine();
            if (s.isEmpty()) break;
            StdOut.println("delete " + s + " result: " + trie.delete(s));
        }
        StdOut.println("current keys in trie:");
        for (String key:trie.keys()) {
            StdOut.println(key);
        }
    }
}
