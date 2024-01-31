# NonrecursiveDFS
## Algorithm
whatever-first search using a stack, see details in [algorithms by jeff:graphs](http://jeffe.cs.illinois.edu/teaching/algorithms/book/05-graphs.pdf)
## Implementation
[NonRecursiveDFS](NonRecursiveDFS.java)
# Diameter of A Tree
## Algorithm
```
Diameter(tree):
pick an arbitrary vertex v from the tree
bfs from v to find the farthest vertex w
bfs from w to find the longest path w ->...-> u - diameter
```
### Correctness
firstly, because it's a tree, for any vertices u and v, there is exactly one path from u to v. We denote the path with path(u,v) and the length of the path with dist(u,v)
for any path(x, y), v is connected to path(x,y) as well as the longest path(w,u). That is, there is a vertex m1 in path(x,y) and a vertex m2 in path(u,v) s.t. path(m1, m) does not share any edges with path(x,y) and path(m2, m) does not share any edges with path(w,u)

### Performance
time complexity: O(V + E)
space complexity: O(V)

## Implementation
[Diameter](Diameter.java)

# Center of A Tree
## Algorithm
midpoint of the longest path (diameter)
### correctness
for any v, longestPath(v) >= ceil(diameter / 2). otherwise, dist(v, w) <= longestPath(v) < ceil(diameter / 2),
dist(v, u) <= longestPath(v) < ceil(diameter / 2), dist(u, w) <= dist(u, v) + dist(v, w) <= 2 * ceil(diameter/2) - 2 < diameter. contradict to the fact that dist(u, v) = diameter
and for the midpoint of the longest path in the tree - m, longestPath(m) = ceil(diameter / 2). Because suppose there is a vertex v, dist(v, m) > ceil(diameter / 2), then without loss of generality, dist(v, u) = dist(u, m) + dist(v, m) > ceil(diameter / 2) + floor(diameter / 2)  = diameter. contradict to the fact that diameter is the length of the longest path in the tree.
so for any vertex n, dist(m, n) <= ceil(diameter / 2) -> longestPath(m) <= ceil(diameter / 2). so longestPath(m) = ceil(diameter / 2)
Midpoint is the vertex which reaches the minimum maximum distance. That is, m is the center of the tree.
### performance
the same as Diameter
## Implementation
[Center](Center.java)

# Euler Cycle
## Algorithm
see [Eulerian Path](https://cp-algorithms.com/graph/euler_path.html)

## Implementation
// all vertices have even degrees
// try to find euler cycle from the vertex with non-zero degree
// check the graph is sufficiently connected
[EulerCycle](EulerCycle.java)

# Shortest Directed Cycle
## Algorithm
for each vertex v, use BFS to find the shortest cycle v -> ... -> v (if there is one). the shortest one among all these found cycles is the solution.
## Implementation
[ShortestDirectedCycle](ShortestDirectedCycle.java)

# Hamilton Path in DAG
## Algorithm
find topological sorting and check whether there is an edge between each consecutive pair of vertices
### correctness
we need to prove that if there is a Hamilton Path in DAG, then the topological sorting must be the same as the path.

Suppose there is a topoordering T: v0' -> ...vt' -> ... vn' different from the path P: v0 -> ...vt ->... vn and vt, vt' is the first different vertex between these 2 vertex sequences.
vt' should be in the right of vt in P. Let vs be the predecessor of vt' in P. vs should appear after vt' in T. Then, for T, edge vs -> vt' points from right to left, contradict to the definition of topoordering.

Therefore, if there is a Hamilton Path in DAG, the path is unique. And the topoordering of this DAG is unique, which is the order in which vertices appear in the path.
### performance
O(V + E)
## Implementation
[DAGHamiltonian](DAGHamiltonian.java)

# Reachable from any other vertices in DAG
## Algorithm
find vertices that have 0 outdegree.
if we find exactly one such vertex, it's the solution. Otherwise, there does not exist a vertex that's reachable from any other vertices in DAG.

### correctness
prop: for DAG, there is a vertex reachable from any other vertices iff there is exactly 1 vertex that has 0 outdegree and this vertex is the vertex that's reachable from others.

proof: If there is such a vertex in DAG, its outdegree must be 0(otherwise, there will be a cycle).
What's more, for any other vertex v(v != v*) in G, because it can reach v*, there is a path v -> ... -> v*. So outdegree(v) > 0(v != v*).

on the other hand, for DAG, if there is exactly one vertex v that has 0 degree, for any vertex u0(u0 != v), because outdegree(u0) > 0, there is an edge u0 -> u1, if u1 == v, then u0 can reach v. otherwise, because outdegree(u1) > 0 and there is no cycle, there is an edge u1 -> u2(u2 != u0 and u2 != u1). Similarly, we can prolong the path u0 -> u1 -> u2 ->... until we get a um = v. Since thhere is only finite vertices in the graph, we can always find such a vm and conclude that v is reachable from u0. So, v is the vertex that's reachable from any other vertices.

proved.

### performance
O(V)

## Implementation
[DAGReachable](DAGReachable.java)

# Reachable from any other vertices in Digraph
## Algorithm
1. compute scc(G)(or kernel DAG of G).
   1) use kosaraju-sharir to mark each vertex with its corresponding strong component
   2) new a graph scc(G) with V = number of strong components in G. Iterate through edges in G, if for the edge w -> v, w and v are marked with different strong component ids c0 and c1, add an edge c0 -> c1 for scc(G)
2. Check whether there is exactly 1 sink component in scc(G). return the sink component if so. Otherwise, there is no such a vertex.
    1) Apply [DAGReachable](DAGReachable.java) to scc(G) we've constructed to get the id of our desired component
   2) By id, query all the vertices that lies in the corresponding strong component and return them.
### correctness
if there is such a vertex v, then any vertex u in the same strong component as v is also the solution.
proof: for any w, w can reach v. since u is in the same strong component as v, v can reach u. So w can reach u. Proved.

there is such a vertex iff scc(G) has exactly one sink component.
proof:
1. if there is such a vertex v, then the strong component C it lies in is a sink component(for any  edge s -> t s.t s lies in C, for any vertex w in C, w can reach s and s can reach t, w can reach t. On the other hand, t can reach v and v can reach w. Thus, t can reach w. Therefore, t lies in C as well, i.e. C is a sink component). 
And for any other strong component C', let u be an arbitrary vertex from C'. u can reach v. So there is a path u ->...-> v. let w be the first vertex in this path that's not from C' and w' be the predecessor of w. w' -> w is an edge from C'. C' is not a sink component.

2. if scc(G) has exactly one sink component C. let v be an arbitrary vertex from C, for any vertex u, 

    if u lies in C, u can reach v. 

    if u lies in another component C0, because C0 is not a sink component, there is an edge from C0 to C1. if C1 = C, then u can reach v. if not, 
because C1 is not a sink component, there is an edge from C1 to C2 (C2 != C0). Similarly, we can continuously prolong the sequence C0 -> C1 -> .... (Cn != Ct(t < n)) until we find a Cm = C. Because there is only finite components in graph, we can find such a Cm and conclude that u can reach v.

    Therefore, v is the vertex we want. proved.
### performance
1 1) O(V + E) 2) O(V + E) 2 1) O(V) 2) O(1) total: O(V + E)
## Implementation
[DGReachable](DGReachable.java)



