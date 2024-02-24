# Minimum bottleneck spanning tree
## Algorithm
Every MST is a minimum bottleneck spanning tree
## Correctness
let uv be the maximum edge of MST, remove it from MST and there are 2 components S, T. for any edges st that s in S and t in T(denote this set with ST), weight(st) >= weight(uv). (otherwise, we can replace uv with st in MST and get a smaller weight sum, which contradicts to the definition of MST). 
For any spanning tree, there must be an edge in ST. That is, there must be an edge st in ST, weight(st) >= weight(uv) = bottleneck(MST). Thus, for any spanning tree T, bottleneck(T) >= weight(st) >= weight(uv) = bottleneck(MST). MST is minimum bottleneck tree.

# Is an edge in a MST
## Algorithm
use dfs/bfs to check whether there is path between u and v consisting of edges whose weight is strictly less than that of uv. uv is not in MST iff there is such a path.
## Correctness
### lemma
if e is in some MST, then there is a cut s.t. the weight of e is smaller or equal to that of all crossing edges

proof: 
e is in some MST -> remove e from this MST and we get 2 components. Think of these 2 components as a cut, among all crossing edges st (edges whose endpoints are in different component), weight(e) <= weight(st). otherwise, after replacing e with the smaller one, we can get a new spanning tree with smaller sum of weights, which contradicts to the 
fact that the original tree is a MST. proved.

### prop
there is a path between u and v containing only edges whose weight is strictly smaller than uv iff uv is not in any MST
proof: 
1. there is a path -> for any cut, there must be an edge u'v' in this path that is a crossing edge of this cut. Since weight(u'v') < weight(uv), by the contrapositive of lemma, e is not in any MST

2. e is not in any MST -> there is a path. we'll prove its contrapositive, i.e. for any paths, there must be an edge whose weight is larger than or equal to that of e -> e is in some MST. suppose that e is not in any MST, then take one MST T, there must be a path between u and v. And the weight of all the edges in this path must be smaller than that of e, otherwise, replace the edge whose weight is larger than equal to e with e, we'll get a new spanning tree the weight sum of which is either smaller or equal to T, contradictory to the fact that T is a MST or the fact that e is not in any MST. we've found a path between u and v the weight of whose edges is all smaller than that of e. It contradicts to our condition. So, e is in some MST. 

## Performance
time complexity: O(V + E)

## Implementation
[MSTEdgeChecker](MSTEdgeChecker.java)

# Minimum-weight feedback edge set
A feedback edge set of a graph is a subset of edges that contains at least one edge from every cycle in the graph. If the edges of a feedback edge set are removed, the resulting graph is acyclic. Given an edge-weighted graph, design an efficient algorithm to find a feedback edge set of minimum weight. Assume the edge weights are positive.
## algorithm
find MST; the complement of MST
## correctness
after a minimum-weight feedback edge set is removed, we get a spanning tree. if the spanning tree has the maximum weight, then its complement is the minimum-weight feedback set
## Performance
time complexity: O(V + E) to get the complement of maximum-weight spanning tree + time spent on finding maximum-weight spanning tree(generally, O(ElogV))
## Implementation
[FeedbackSet](FeedbackSet.java)

# Monotonic shortest path
Given an edge-weighted digraph, find a monotonic shortest path from s to every other vertex. A path is monotonic if the weight of every edge on the path is either strictly increasing or strictly decreasing.
## algorithm
find the shortest ascending path and descending path. choose the shorter one.
how to find the shortest ascending path?
let `dist[i,v]` be the length of shortest ascending path consisting of at most i edges, then
 

# Second Shortest Path
Given an edge-weighted digraph and let P be a shortest path from vertex s to vertex t. Design an ElogV algorithm to find a path (not necessarily simple) other than P from s to t that is as short as possible. Assume all of the edge weights are strictly positive.
## Algorithm
1. compute the shortest path from s to any other vertex
2. compute the shortest path from every other vertex to t
3. based on the shortest paths computed, for each edge u -> v not in P, compute the shortest path from s to t containing u -> v, that is shortest(s -> u) + u -> v + shortest(v -> t). Among all these paths, the shortest one is what we want.
## Correctness
prop: P is a simple path
proof: Suppose that P contains at least one cycle, then it must be positive. remove this cycle, we get a new path the length of which is strictly less than P. contradict to the fact that P is the shortest path.

prop:if P is a simple path, a path T from s to t is not P iff it contains at least one edge not in P
proof:
<-: obvious
->: suppose that T consists of edges in P(e0, e1, e2, ..., ep)
1) it uses some edges in P repeatedly: T: ...,ei, ...ej, ei, ... then there is a ej in P the endpoint of which is the same as the startpoint of some other edge ei in P. it follows that P has a cycle, contradict to the fact that P is simple.
2) it's a permutation of edges in P: T:(e0',e1', ..., ep'). let et' be the first edge in T different from P. et' = ej(j > t). then the startpoint of ej is the same as the startpoint of et. thus, the endpoint of e_{j-1} is the same as the startpoint of et. it follows that there is a cycle in P, contradict.\
3) it removes some edges in P: P is simple. after removing some edges, s is not connected to t. contradict.
Therefore, T contains at least one edge not in P

To find the shortest one among edges from s to t other than P is actually to find the shortest one among edges from s to t containing at least one edge not in P, i.e. shortest_{e not in P}(shortest_{T contains e}(T))

## Performance
for positive edge graph, use dijkstra, step 1 takes O((V + E)logV); in step 2, we reverse the graph and run dijkstra, it takes O(V + E + (V + E)logV); step 3 takes O(E). the total time complexity is O(ElogV).

## Implementation
[SecondShortestPath](SecondShortestPath.java)


