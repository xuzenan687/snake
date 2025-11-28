package com.xuziran.structure;

import com.xuziran.pojo.Data;
import com.xuziran.pojo.Player;

import java.util.*;

public class Graph {

    // 玩家昵称 → 好友昵称集合
    private Map<String, Set<String>> adjList = new HashMap<>();

    // 添加顶点
    public void addVertex(String name) {
        adjList.putIfAbsent(name, new HashSet<>());
    }

    // 添加好友关系（无向）
    public void addEdge(String name1, String name2) {
        if (name1.equals(name2)) return;

        addVertex(name1);
        addVertex(name2);

        boolean added1 = adjList.get(name1).add(name2);
        boolean added2 = adjList.get(name2).add(name1);

        if (added1 && added2) {
            System.out.println(name1 + " 和 " + name2 + " 成为好友！");
        }
    }

    // 删除边
    public void removeEdge(String name1, String name2) {
        adjList.getOrDefault(name1, new HashSet<>()).remove(name2);
        adjList.getOrDefault(name2, new HashSet<>()).remove(name1);
    }

    // 获取邻居（直接好友）
    public Set<String> getNeighbors(String name) {
        return adjList.getOrDefault(name, Collections.emptySet());
    }

    public void clear() {
        adjList.clear();
    }

    // ---------------- DFS ----------------
    public List<String> dfs(String start) {
        List<String> res = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        dfsHelper(start, visited, res);
        return res;
    }

    private void dfsHelper(String cur, Set<String> visited, List<String> res) {
        if (visited.contains(cur)) return;

        visited.add(cur);
        res.add(cur);

        for (String neighbor : getNeighbors(cur)) {
            dfsHelper(neighbor, visited, res);
        }
    }

    // ---------------- BFS ----------------
    public List<String> bfs(String start) {
        List<String> res = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Queue<String> q = new LinkedList<>();

        q.offer(start);
        visited.add(start);

        while (!q.isEmpty()) {
            String cur = q.poll();
            res.add(cur);

            for (String neighbor : getNeighbors(cur)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    q.offer(neighbor);
                }
            }
        }
        return res;
    }

    // ---------------- BFS 获取直接好友（第一层） ----------------
    public Set<String> bfsFriends(String start) {
        return new HashSet<>(getNeighbors(start));
    }

    // ---------------- BFS 求共同好友 ----------------
    public Set<String> commonFriendsBFS(String a, String b) {
        Set<String> A = bfsFriends(a);
        Set<String> B = bfsFriends(b);

        Set<String> res = new HashSet<>();
        for (String x : A) {
            if (B.contains(x)) res.add(x);
        }
        return res;
    }

    // ---------------- DFS 获取直接好友 ----------------
    public Set<String> dfsFriends(String start) {
        return new HashSet<>(getNeighbors(start));
    }

    // ---------------- DFS 求共同好友 ----------------
    public Set<String> commonFriendsDFS(String a, String b) {
        Set<String> A = dfsFriends(a);
        Set<String> B = dfsFriends(b);

        Set<String> res = new HashSet<>();
        for (String x : A) {
            if (B.contains(x)) res.add(x);
        }
        return res;
    }

    // ---------------- 推荐好友 ----------------
    public List<Player> recommendFriends() {
        String myName = Data.getCurrentUser().getNickname();
        Set<String> myFriends = getNeighbors(myName);
        List<PlayerRecommend> candidates = new ArrayList<>();

        for (Player p : Data.getUserList().getSortedList()) {
            String name = p.getNickname();
            if (name.equals(myName) || myFriends.contains(name)) continue;

            Set<String> friendsOfP = getNeighbors(name);

            // 计算共同好友
            int common = 0;
            for (String f : friendsOfP) {
                if (myFriends.contains(f)) common++;
            }
            if (common == 0) continue;

            int scoreDiff = Math.abs(p.getScore() - Data.getCurrentUser().getScore());
            if (scoreDiff > 100) continue;

            candidates.add(new PlayerRecommend(p, common, scoreDiff));
        }

        // 排序：共同好友多优先，积分差小优先
        candidates.sort((a, b) -> {
            if (a.commonFriends != b.commonFriends)
                return b.commonFriends - a.commonFriends;
            return a.scoreDiff - b.scoreDiff;
        });

        List<Player> result = new ArrayList<>();
        for (PlayerRecommend r : candidates) result.add(r.player);
        return result;
    }

    class PlayerRecommend {
        Player player;
        int commonFriends;
        int scoreDiff;

        PlayerRecommend(Player player, int commonFriends, int scoreDiff) {
            this.player = player;
            this.commonFriends = commonFriends;
            this.scoreDiff = scoreDiff;
        }
    }

    // ---------------- 序列化整个邻接表 ----------------
    public Map<String, Set<String>> toSerializableMap() {
        Map<String, Set<String>> result = new HashMap<>();
        for (Map.Entry<String, Set<String>> entry : adjList.entrySet()) {
            result.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }
        return result;
    }
}
