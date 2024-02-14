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

