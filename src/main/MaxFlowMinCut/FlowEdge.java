package main.MaxFlowMinCut;

public class FlowEdge {
    public final int from;
    public final int to;
    public final int capacity;
    private int flow;
    public FlowEdge(int from, int to, int capacity) {
        this.from = from;
        this.to = to;
        this.capacity = capacity;
    }

    /**
     * residual capacity of other(t) -> t: c_f(other(t), t)
     */
    public int residualCapacityTo(int t) {
        if (t == to) {
            return capacity - flow;
        } else {
            return flow;
        }
    }

    /**
     * increase edge's flow by {@code delta} given that this edge is corresponding to an edge other(t) -> t in residual network
     * @param delta
     * @param t
     */
    public void increaseFlowTo(int delta, int t) {
        if (t == to) {
            flow += delta;
        } else {
            flow -= delta;
        }
    }

    public int other(int u) {
        if (u == from) {
            return to;
        } else {
            return from;
        }
    }

    public int flow() {
        return flow;
    }

    public String toString() {
        return String.format("%d -> %d %d %d", from, to, capacity, flow);
    }
}
