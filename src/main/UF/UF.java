package main.UF;

public abstract class UF {
    public final int n;
    protected UF(int n) {
        this.n = n;
    }

    protected void checkValidity(int i) {
        if (i < 0 || i >= n) throw new IllegalArgumentException("i must be in [0, " + n + ")");
    }

    /**
     * union {@code i} and {@code j}
     * @return {@code true} if {@code i} and {@code j} is in different component previously;
     *         {@code false} otherwise
     */
    public abstract boolean union(int i, int j);
}
