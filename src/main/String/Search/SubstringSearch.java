package main.String.Search;

abstract public class SubstringSearch {
    public SubstringSearch(String pattern) {
        if  (pattern.isEmpty()) throw new IllegalArgumentException("pattern can't be an empty string!");
    }
    /**
     * search pattern in {@code str}, return a list of index s.t. str.substring(index, index + patten length) == pattern
     * @param str
     * @return a list of index s.t. str.substring(index, index + patten length) == pattern if there is such a match(substrings don't overlapped pairwise);
     *         otherwise, return {@code null}
     */
    abstract public Iterable<Integer> searchIn(String str);
}
