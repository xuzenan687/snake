package com.xuziran.structure;

import com.xuziran.pojo.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//链地址法
public class MyHashMap {
    // 1. 核心参数（可调整初始容量和负载因子）
    private static final int DEFAULT_CAPACITY = 16; // 初始数组大小（2的幂，优化哈希分布）
    private static final float LOAD_FACTOR = 0.75f; // 负载因子（扩容阈值）
    private LinkedList<Entry>[] table; // 哈希表数组（每个元素是链表，存储冲突元素）
    private int size; // 实际存储的键值对数量

    // static作用：逻辑解耦，不关联外部MyHashMap的逻辑
    public static class Entry {
        public String key;
        public Player value;
        Entry next; // 下一个节点（解决冲突）
        Entry(String key, Player value) {
            this.key = key;
            this.value = value;
        }
    }

    public MyHashMap() {
        table = new LinkedList[DEFAULT_CAPACITY];
        size = 0;
    }

    // 4. 哈希函数：计算键对应的数组索引（核心）
    private int hash(String key) {
        if (key == null) return 0;
        // 取key的哈希码，再通过位运算确保索引在数组范围内（避免负数）
        return (key.hashCode() % table.length + table.length) % table.length;
    }

    // 5. 扩容（当size/数组长度 > 负载因子时触发）
    private void resize() {
        int newCapacity = table.length * 2; // 扩容为原来的2倍（2的幂）
        LinkedList<Entry>[] newTable = new LinkedList[newCapacity];

        // 遍历原数组，将所有元素重新哈希到新数组
        for (LinkedList<Entry> list : table) {
            if (list != null) {
                for (Entry entry : list) {
                    int newIndex = (entry.key.hashCode() % newCapacity + newCapacity) % newCapacity;
                    if (newTable[newIndex] == null) {
                        newTable[newIndex] = new LinkedList<>();
                    }
                    newTable[newIndex].add(entry);
                }
            }
        }
        table = newTable; // 替换为新数组
    }

    public void put(String key, Player value) {
        // 检查是否需要扩容
        if ((float) size / table.length > LOAD_FACTOR) {
            resize();
        }

        int index = hash(key);
        if (table[index] == null) {
            table[index] = new LinkedList<>(); // 链表为空则创建
        }

        // 遍历链表：存在key则更新value，不存在则新增节点
        for (Entry entry : table[index]) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }
        table[index].add(new Entry(key, value));
        size++;
    }

    public Player get(String key) {
        int index = hash(key);
        LinkedList<Entry> list = table[index];
        if (list != null) {
            for (Entry entry : list) {
                if (entry.key.equals(key)) {
                    return entry.value;
                }
            }
        }
        return null;
    }

    public Player remove(String key) {
        int index = hash(key);
        LinkedList<Entry> list = table[index];
        if (list != null) {
            for (Entry entry : list) {
                if (entry.key.equals(key)) {
                    Player value = entry.value;
                    list.remove(entry);
                    size--;
                    return value;
                }
            }
        }
        return null;
    }

    public int size() {
        return size;
    }


    public List<Player> getSortedList() {
        List<Player> sortedList = new ArrayList<>();
        for (LinkedList<Entry> list : table) {
            if (list != null) {
                for (Entry  entry : list) {
                    sortedList.add(entry.value);
                }
            }
        }
        sortedList.sort((p1, p2) -> p2.getScore() - p1.getScore());//自定义比较器
        return sortedList;
    }

    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }
    public boolean containsKey(String key) {
        return get(key) != null;
    }
}
