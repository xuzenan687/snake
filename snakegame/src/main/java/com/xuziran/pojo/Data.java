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
    private static Integer primaryKey;//主键

    public static Integer getPrimaryKey() {
        primaryKey=getUserList().size();
        return primaryKey;
    }
    public static MaxHeap getLocalRanking() {
        return localRanking;
    }
    public static MaxHeap getGlobalRanking() {return globalRanking;}
    public static MyHashMap getFriends() {return friends;}
    public static MyHashMap getUserList() {return userList;}
    public static Player getCurrentUser() {return currentUser;}
    public static Graph getSocialNetwork() {return socialNetwork;}


    public static void save() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            Map<String, Object> map = new HashMap<>();

            map.put("userList", userList.getSortedList());
            map.put("globalRanking", globalRanking.getSortedList());
            map.put("socialNetwork", socialNetwork.toSerializableMap());

            String path = "snakegame/src/main/resources/data.json";
            FileWriter fw = new FileWriter(path);

            gson.toJson(map, fw);
            fw.close();

            System.out.println("数据保存成功！");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void load() {
        String path = "snakegame/src/main/resources/data.json";
        File file = new File(path);

        if (!file.exists()) {
            System.out.println("第一次运行，初始化默认数据");
            //init();
            return;
        }
        try {
            Gson gson = new Gson();
            FileReader fr = new FileReader(file);

            Type type = new TypeToken<Map<String, Object>>(){}.getType();
            Map<String, Object> data = gson.fromJson(fr, type);
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
            e.printStackTrace();
            //init();
        }
    }
    public  static void loadCurrentUser(String myName) {
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
        Set<String> nicknames = socialNetwork.getNeighbors(myName);
        for(String nickname : nicknames){
            if(userList.containsKey(nickname)){
                friends.put(nickname,userList.get(nickname));
            }else{
                new Exception("用户不存在");
            }
        }
    }
    public static void loadLocalRanking(String myName) {
        Set<String> nicknames = new HashSet<>(socialNetwork.getNeighbors(myName));//返回一个副本
        nicknames.add(myName);//添加自己
        for(String nickname : nicknames){
            if(userList.containsKey(nickname)){
                localRanking.insert(userList.get(nickname));
            }
        }
    }
//    public static void init(){
//        //当前用户信息
//        currentUser.setId(getPrimaryKey());
//        currentUser.setNickname("Tom");
//        currentUser.setScore(10);
//
//        //创建用户列表
//        Player Jerry = new Player(getPrimaryKey(),"Jerry",20);
//        Player Mike = new Player(getPrimaryKey(),"Mike",50);
//        Player Lucy = new Player(getPrimaryKey(),"Lucy",60);
//        Player Lily = new Player(getPrimaryKey(),"Lily",10);
//        Player Lucas = new Player(getPrimaryKey(),"Lucas",40);
//        userList.put("Tom",currentUser);
//        userList.put("Jerry",Jerry);
//        userList.put("Mike",Mike);
//        userList.put("Lucy",Lucy);
//        userList.put("Lily",Lily);
//        userList.put("Lucas",Lucas);
//
//
//        //创建好友列表
//        friends.put(currentUser.getNickname(), currentUser);
//        friends.put("Jerry",Jerry);
//        friends.put("Mike",Mike);
//
//        //创建社交网络
//        socialNetwork.addVertex("Tom");
//        socialNetwork.addVertex("Jerry");
//        socialNetwork.addVertex("Mike");
//        socialNetwork.addVertex("Lucy");
//        socialNetwork.addVertex("Lily");
//        socialNetwork.addVertex("Lucas");
//        socialNetwork.addEdge("Tom", "Jerry");
//        socialNetwork.addEdge("Tom", "Mike");
//        socialNetwork.addEdge("Jerry", "Mike");
//        socialNetwork.addEdge("Jerry", "Lucy");
//        socialNetwork.addEdge("Mike", "Lucas");
//        socialNetwork.addEdge("Lucy", "Lily");
//        socialNetwork.addEdge("Lucy", "Lucas");
//
//
//
//        //创建排行榜
//        //本地排行榜
//        for (Player player:friends.getSortedList()){
//            localRanking.insert(player);
//        }
//        //全球排行榜
//        for (Player player:userList.getSortedList()){
//            globalRanking.insert(player);
//        }
//
//
//    }


}
