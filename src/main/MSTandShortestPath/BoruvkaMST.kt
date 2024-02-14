package main.MSTandShortestPath

import edu.princeton.cs.algs4.Edge
import edu.princeton.cs.algs4.EdgeWeightedGraph
import edu.princeton.cs.algs4.In

/**
 * g must be connected!
 */
class BoruvkaMST(private val g: EdgeWeightedGraph) {

    val edges: Iterable<Edge> by lazy {
        computeMST()
    }

    val weight: Double by lazy {
        var result = 0.0
        for (e in edges) {
            result += e.weight()
        }
        result
    }

    private fun dfs(F: EdgeWeightedGraph, comp: IntArray, v: Int, label: Int) {
        comp[v] = label
        for (e in F.adj(v)) {
            val other = e.other(v)
            if (comp[other] == -1) {
                dfs(F, comp, other, label)
            }
        }
    }

    private fun dfsAndLabel(F: EdgeWeightedGraph, comp: IntArray): Int {
        var count = 0
        for (i in 0 until F.V()) {
            if (comp[i] == -1) {
                dfs(F, comp, i, count)
                count ++
            }
        }
        return count
    }

    private fun less(e1: Edge, e2: Edge): Boolean {
        return when {
            e1.weight() < e2.weight() -> {
                true
            }
            e1.weight() > e2.weight() -> {
                false
            }
            else -> {
                val u11 = e1.either()
                val u12 = e1.other(u11)
                val u21 = e2.either()
                val u22 = e2.other(u21)
                val u1 = Math.max(u11, u12)
                val u2 = Math.max(u21, u22)
                val v1 = Math.min(u11, u12)
                val v2 = Math.min(u21, u22)
                if (u1 == u2) {
                    v1 < v2
                } else {
                    u1 < u2
                }
            }
        }
    }

    private fun addSafeEdge(F: EdgeWeightedGraph, comp: IntArray, compCnt: Int) {
        val safeEdges = Array<Edge?>(compCnt){ null }
        for (e in g.edges()) {
            val u = e.either()
            val v = e.other(u)
            if (comp[u] != comp[v]) {
                // e is candidate for comp[u]'s safeEdge
                if (safeEdges[comp[u]] == null || less(e, safeEdges[comp[u]]!!)) {
                    safeEdges[comp[u]] = e
                }
                // e is candidate for comp[v]'s safeEdge
                if (safeEdges[comp[v]] == null || less(e, safeEdges[comp[v]]!!)) {
                    safeEdges[comp[v]] = e
                }
            }
        }
        for (i in 0 until compCnt) {
            // add comp i's safeEdge to F
            val e = safeEdges[i]!!
            val u = e.either()
            val v = e.other(u)
            val compMin = Math.min(comp[u], comp[v])
            val compMax = Math.max(comp[u], comp[v])
            // safeEdge e connects compMin and compMax
            if (i == compMin) {
                F.addEdge(e)
            } else {
                val compMinEdge = safeEdges[compMin]!!
                val s = compMinEdge.either()
                val t = compMinEdge.other(s)
                if (comp[s] == compMin) {
                    if (comp[t] != compMax) {
                        // e has not been added yet as compMin's safeEdge. add here
                        F.addEdge(e)
                    }
                } else {
                    // comp[t] == compMin
                    if (comp[s] != compMax) {
                        // e has not been added yet as compMin's safeEdge. add here
                        F.addEdge(e)
                    }
                }
            }
        }
    }

    private fun initComp(comp: IntArray) {
        for (i in 0 until comp.size) {
            comp[i] = -1
        }
    }
    private fun computeMST(): Iterable<Edge> {
        val F = EdgeWeightedGraph(g.V())
        val comp = IntArray(g.V()){ -1 }
        var count = dfsAndLabel(F, comp)
        while (count > 1) {
            addSafeEdge(F, comp, count)
            initComp(comp)
            count = dfsAndLabel(F, comp)
        }
        return F.edges()
    }
}

fun main() {
    val g = EdgeWeightedGraph(In("/Users/shimura233/IdeaProjects/algorithms/test/algs4-data/largeEWG.txt"))
    val mst = BoruvkaMST(g)
    for (edge in mst.edges) {
        println(edge)
    }
    println("weight: ${mst.weight}")
}