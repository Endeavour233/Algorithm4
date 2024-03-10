package main.String.Trie;

public interface Trie<T> {
    public void insert(String key, T value);
    public T search(String key);
    public Iterable<String> keys();

    public Iterable<String> keysThatMatch(String s);


    public Iterable<String> keysWithPrefix(String prefix);
    // longest string in trie that is a prefix of query
    public String longestPrefixOf(String query);
}
