package main.String.Trie;

public interface Trie<T> {
    void insert(String key, T value);
    T search(String key);
    Iterable<String> keys();

    Iterable<String> keysThatMatch(String s);


    Iterable<String> keysWithPrefix(String prefix);
    // longest string in trie that is a prefix of query
    String longestPrefixOf(String query);

    boolean delete(String key);
}
