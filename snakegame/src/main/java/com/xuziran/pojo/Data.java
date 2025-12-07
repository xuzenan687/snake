package com.xuziran.pojo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.xuziran.structure.Graph;
import com.xuziran.structure.MaxHeap;
import com.xuziran.structure.MyHashMap;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class Data {

    private static Player currentUser = new Player();//当前用户
    private static MyHashMap userList = new MyHashMap();// 用户列表
    private static MyHashMap friends = new MyHashMap();//好友列表
    private static Graph socialNetwork = new Graph();//社交网络
    private static MaxHeap localRanking = new MaxHeap();//本地排行榜
    private static MaxHeap globalRanking = new MaxHeap();//全球排行榜
    public static Integer getPrimaryKey() {return getUserList().size();}
    public static MaxHeap getLocalRanking() {
        return localRanking;
    }
    public static MaxHeap getGlobalRanking() {return globalRanking;}
    public static MyHashMap getFriends() {return friends;}
    public static MyHashMap getUserList() {return userList;}
    public static Player getCurrentUser() {return currentUser;}
    public static Graph getSocialNetwork() {return socialNetwork;}


    public static void save() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Map<String, Object> map = new HashMap<>();

        map.put("userList", userList.getSortedList());
        map.put("globalRanking", globalRanking.getSortedList());
        map.put("socialNetwork", socialNetwork.toSerializableMap());

        String path = "snakegame/src/main/resources/data.json";
        File real = new File(path);

        try (FileWriter fw = new FileWriter(real)) {
            gson.toJson(map, fw);
        } catch (Exception e) {
            System.err.println("数据保存失败：" + e.getMessage());
        }
        System.out.println("数据保存成功！");
    }
    public static void load() {
        String path = "snakegame/src/main/resources/data.json";
        File file = new File(path);

        // 如果不存在，创建空文件并写入初始数据
        if (!file.exists()) {
            System.out.println("首次运行：data.json 不存在，自动创建空数据文件...");
            saveEmptyData(path);
        }

        try (FileReader fr = new FileReader(file)) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Object>>(){}.getType();
            Map<String, Object> data = gson.fromJson(fr, type);

            if (data == null) {
                System.err.println("数据解析失败：JSON为空或损坏");
                return;
            }

            fr.close();

            // 清空原数据
            userList.clear();
            friends.clear();
            localRanking.clear();
            globalRanking.clear();
            socialNetwork.clear();


            // 加载社交网络
            Map<String, List<String>> sn = (Map<String, List<String>>) data.get("socialNetwork");
            if (sn != null) {
                for (String vertex : sn.keySet()) {
                    socialNetwork.addVertex(vertex);
                }
                for (Map.Entry<String, List<String>> entry : sn.entrySet()) {
                    String v1 = entry.getKey();
                    for (String v2 : entry.getValue()) {
                        socialNetwork.addEdge(v1, v2);
                    }
                }
            }
            // 用户列表
            List<Map> users = (List<Map>) data.get("userList");
            for (Map m : users) {
                Player p = new Player(
                        ((Double)m.get("id")).intValue(),
                        (String)m.get("nickname"),
                        ((Double)m.get("score")).intValue()
                );
                userList.put(p.getNickname(), p);
            }

            // 全球排行榜
            List<Map> glob = (List<Map>) data.get("globalRanking");
            for (Map m : glob) {
                Player p = new Player(
                        ((Double)m.get("id")).intValue(),
                        (String)m.get("nickname"),
                        ((Double)m.get("score")).intValue()
                );
                globalRanking.insert(p);
            }
            System.out.println("数据加载成功！");

        } catch (Exception e) {
            System.err.println("数据加载失败：" + e.getMessage());
        }
    }
    public  static void loadCurrentUser(String myName) {
        if (myName == null || myName.isEmpty()) return;
        if(userList.containsKey(myName)){
            currentUser = userList.get(myName);
        }else{
            currentUser=new Player(getPrimaryKey(),myName,0);
            userList.put(myName,currentUser);
            globalRanking.insert(currentUser);
            socialNetwork.addVertex(myName);
        }
    }
    public static void loadFriendList(String myName) {
        friends.clear();
        Set<String> nicknames = socialNetwork.getNeighbors(myName);
        if (nicknames == null) return;
        for(String nickname : nicknames){
            if(userList.containsKey(nickname)){
                friends.put(nickname,userList.get(nickname));
            }else{
                System.err.println("警告：社交网络中的用户不存在：" + nickname);
            }
        }
    }
    public static void loadLocalRanking(String myName) {
        localRanking.clear();
        Set<String> nicknames = new HashSet<>(socialNetwork.getNeighbors(myName));//返回一个副本
        nicknames.add(myName);//添加自己
        for(String nickname : nicknames){
            if(userList.containsKey(nickname)){
                localRanking.insert(userList.get(nickname));
            }
        }
    }
    private static void saveEmptyData(String path) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            Map<String, Object> empty = new HashMap<>();
            empty.put("userList", new ArrayList<>());
            empty.put("globalRanking", new ArrayList<>());
            empty.put("socialNetwork", new HashMap<>());

            File f = new File(path);

            // 确保目录存在
            File parent = f.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }

            try (FileWriter writer = new FileWriter(f)) {
                gson.toJson(empty, writer);
            }

            System.out.println("成功创建空 data.json");
        } catch (Exception e) {
            System.err.println("创建空 data.json 失败：" + e.getMessage());
        }
    }

}
