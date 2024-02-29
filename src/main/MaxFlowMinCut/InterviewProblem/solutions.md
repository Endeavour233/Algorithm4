# Fattest Path
Given an edge-weighted digraph and two vertices s and t, design anElogE algorithm to find a fattest path from s to t. The bottleneck capacity of a path is the minimum weight of an edge on the path. A fattest path is a path such that no other path has a higher bottleneck capacity.
## Algorithm
whatever-first search using a priority queue. use -min(width(u), weight(u,v)) as its priority
## Correctness
by induction on the order in which vertices are marked, we can prove that min(width(u), weight(u,v)) is the width of the path from s to v indicated by the edgeTo array(width(s) = infty). 
also, by induction on the order in which vertices are marked, we show that the path indicated by the edgeTo array is the fattest:

when edge (edgeTo\[v\], v) is polled out of the pq and v is marked, for any path from s to v, it must contain an edge (a, b) currently in the priority queue. bottleneck(s to v) = min(bottleneck(s to a),weight(a,b),bottleneck(b to v)) <= min(bottleneck(s to a), weight(a,b)) <= min(width(a), weight(a,b)) <= min(width(edgeTo\[v\]), weight(edgeTo\[v\], v)) = width\[v\]. Thus, edgeTo indicates a fattest path
## Performance
time complexity: O(ElogE)