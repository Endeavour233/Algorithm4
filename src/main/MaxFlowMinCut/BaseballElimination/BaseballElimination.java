package main.MaxFlowMinCut.BaseballElimination;

import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FlowNetwork;


import java.util.HashMap;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

public class BaseballElimination {
    private int[] wins;
    private int[] losses;
    private int[] remainings;
    private int[][] schedules;
    private Set<String>[] Rs;
    private boolean[] eliminated;
    private boolean[] computed;
    private HashMap<String, Integer> teamInd;
    private String[] teamNames;
    private int teamNum;

    private void compute(String teamName) {
        int targetTeam = teamInd.get(teamName);
        if (computed[targetTeam]) return;
        int maxWin = wins[targetTeam] + remainings[targetTeam];
        Set<String> r = null;
        for (int i = 0; i < teamNum; i ++) {
            if (wins[i] > maxWin) {
                eliminated[targetTeam] = true;
                if (r == null) {
                    r = new HashSet<>();
                }
                r.add(teamNames[i]);
            }
        }
        // s, t, teamNum, teamNum - 1 + teamNum - 2 + ... + 1 = teamNum + 2 + teamNum(teamNum - 1)/2
        int v = 2 + teamNum*(teamNum + 1)/2;
        int t = v - 1;
        FlowNetwork network = new FlowNetwork(v);
        int teamNodeStart = (teamNum + 1) * teamNum / 2 - teamNum + 1;
        for (int i = 0; i < teamNum; i ++) {
            if (i == targetTeam) continue;
            for (int j = i + 1; j < teamNum; j ++) {
                if (j == targetTeam) continue;
                // i team - 1 + ... + team - i = (2 * team - 1 -i) * i / 2 + 1
                int node = (2 * teamNum - 1 - i) * i / 2  + j - i;
                network.addEdge(new FlowEdge(0, node, schedules[i][j]));
                network.addEdge(new FlowEdge(node, teamNodeStart + i, schedules[i][j]));
                network.addEdge(new FlowEdge(node, teamNodeStart + j, schedules[i][j]));
            }
        }
        for (int i = 0; i < teamNum; i ++) {
            if (i == targetTeam) continue;
            network.addEdge(new FlowEdge(teamNodeStart + i, t, Math.max(0, maxWin - wins[i])));
        }
        FordFulkerson fordFulkerson = new FordFulkerson(network, 0, t);
        for (int i = 0; i < teamNum; i ++) {
            if (i == targetTeam) continue;
            if (fordFulkerson.inCut(i + teamNodeStart)) {
                if (r == null) {
                    r = new HashSet<>();

                }
                r.add(teamNames[i]);
                eliminated[targetTeam] = true;
            }
        }
        Rs[targetTeam] = r;
        computed[targetTeam] = true;
    }

    public BaseballElimination(String filename) {
        In in = new In(filename);
        try {
            int teamNum = in.readInt();
            this.teamNum = teamNum;
            teamInd = new HashMap<>();
            wins = new int[teamNum];
            remainings = new int[teamNum];
            losses = new int[teamNum];
            schedules = new int[teamNum][teamNum];
            computed = new boolean[teamNum];
            eliminated = new boolean[teamNum];
            teamNames = new String[teamNum];
            Rs = (Set<String>[]) new Set[teamNum];
            for (int i = 0; i < teamNum; i ++) {
                String teamName = in.readString();
                teamInd.put(teamName, i);
                teamNames[i] = teamName;
                wins[i] = in.readInt();
                losses[i] = in.readInt();
                remainings[i] = in.readInt();
                for (int j = 0; j < teamNum; j ++) {
                    schedules[i][j] = in.readInt();
                }
            }
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid input format");
        }

    }
    // number of teams
    public int numberOfTeams() {
        return teamNum;
    }
    // all teams
    public Iterable<String> teams() {
        return teamInd.keySet();
    }
    // number of wins for given team
    public  int wins(String team) {
        validTeam(team);
        return wins[teamInd.get(team)];
    }
    // number of losses for given team
    public  int losses(String team) {
        validTeam(team);
        return losses[teamInd.get(team)];
    }
    // number of remaining games for given team
    public  int remaining(String team) {
        validTeam(team);
        return remainings[teamInd.get(team)];
    }
    public  int against(String team1, String team2) {
        validTeam(team1);
        validTeam(team2);
        return schedules[teamInd.get(team1)][teamInd.get(team2)];
    }
    // is given team eliminated?
    public  boolean isEliminated(String team) {
        validTeam(team);
        if (teamNum == 1) return false;
        compute(team);
        return eliminated[teamInd.get(team)];

    }
    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        validTeam(team);
        if (!isEliminated(team)) return null;
        return Rs[teamInd.get(team)];
    }

    private void validTeam(String team) {
        if (!teamInd.containsKey(team)) {
            throw new IllegalArgumentException(String.format("%s is not a valid team", team));
        }
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
