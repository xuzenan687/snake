package com.xuziran.structure;

import com.xuziran.pojo.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


// 哈希表类（泛型：键String，值Player）
public class MyHashMap {
    // 1. 核心参数（可调整初始容量和负载因子）
    private static final int DEFAULT_CAPACITY = 16; // 初始数组大小（2的幂，优化哈希分布）
    private static final float LOAD_FACTOR = 0.75f; // 负载因子（扩容阈值）
    private LinkedList<Entry<String, Player>>[] table; // 哈希表数组（每个元素是链表，存储冲突元素）
    private int size; // 实际存储的键值对数量

    // 2. 键值对实体类（链表节点）
    public static class Entry<String, Player> {
        public String key;
        public Player value;
        Entry<String, Player> next; // 下一个节点（解决冲突）
        Entry(String key, Player value) {
            this.key = key;
            this.value = value;
        }
    }

    // 3. 构造器（初始化数组）
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
        LinkedList<Entry<String, Player>>[] newTable = new LinkedList[newCapacity];

        // 遍历原数组，将所有元素重新哈希到新数组
        for (LinkedList<Entry<String, Player>> list : table) {
            if (list != null) {
                for (Entry<String, Player> entry : list) {
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

    // 6. 新增/修改键值对（key存在则更新value，不存在则新增）
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
        for (Entry<String, Player> entry : table[index]) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }
        table[index].add(new Entry<>(key, value));
        size++;
    }

    // 7. 根据key获取value（不存在返回null）
    public Player get(String key) {
        int index = hash(key);
        LinkedList<Entry<String, Player>> list = table[index];
        if (list != null) {
            for (Entry<String, Player> entry : list) {
                if (entry.key.equals(key)) {
                    return entry.value;
                }
            }
        }
        return null;
    }

    // 8. 根据key删除键值对（删除成功返回value，不存在返回null）
    public Player remove(String Stringey) {
        int index = hash(Stringey);
        LinkedList<Entry<String, Player>> list = table[index];
        if (list != null) {
            for (Entry<String, Player> entry : list) {
                if (entry.key.equals(Stringey)) {
                    Player value = entry.value;
                    list.remove(entry);
                    size--;
                    return value;
                }
            }
        }
        return null;
    }

    // 9. 获取哈希表大小
    public int size() {
        return size;
    }

    // 10. 打印哈希表（查看存储结构）
    public void print() {
        for (int i = 0; i < table.length; i++) {
            System.out.print("索引" + i + ": ");
            if (table[i] != null) {
                for (Entry<String, Player> entry : table[i]) {
                    System.out.print(entry.key + "=" + entry.value + " → ");
                }
            }
            System.out.println("null");
        }
    }

    public List<Player> getSortedList() {
        List<Player> sortedList = new ArrayList<>();
        for (LinkedList<Entry<String, Player>> list : table) {
            if (list != null) {
                for (Entry<String, Player> entry : list) {
                    sortedList.add(entry.value);
                }
            }
        }
        sortedList.sort((p1, p2) -> p2.getScore() - p1.getScore());
        return sortedList;
    }

    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }
}
