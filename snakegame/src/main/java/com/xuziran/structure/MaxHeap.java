package com.xuziran.structure;

import com.xuziran.pojo.Player;

import java.util.ArrayList;
import java.util.List;

public class MaxHeap {
    private List<Player> heap; // 用动态数组存储堆元素（完全二叉树的层序遍历）

    public MaxHeap() {
        heap = new ArrayList<>();
    }

    public void insert(Player player) {
        heap.add(player); // 新增元素到堆尾
        siftUp(heap.size() - 1); // 从最后一个索引上浮调整
    }

    // 上浮调整（维护大顶堆：子节点>父节点则交换）
    private void siftUp(int index) {
        while (index > 0) { // 未到根节点
            int parentIndex = (index - 1) / 2; // 父节点索引（完全二叉树特性）
            // 子节点<=父节点，满足大顶堆，退出
            if (heap.get(index).getScore() <= heap.get(parentIndex).getScore()) break;
            // 交换子节点和父节点
            swap(index, parentIndex);
            index = parentIndex; // 继续向上调整
        }
    }

    // 下沉调整（维护大顶堆：父节点<子节点则交换，选较大子节点）
    private void siftDown(int index) {
        int n = heap.size();
        while (true) {
            int leftChild = 2 * index + 1; // 左子节点索引
            int rightChild = 2 * index + 2; // 右子节点索引
            int largest = index; // 初始化最大值为当前节点

            // 左子节点存在且大于当前最大值
            if (leftChild < n && heap.get(leftChild).getScore() > heap.get(largest).getScore()) {
                largest = leftChild;
            }
            // 右子节点存在且大于当前最大值
            if (rightChild < n && heap.get(rightChild).getScore() > heap.get(largest).getScore()) {
                largest = rightChild;
            }
            // 最大值就是当前节点，满足大顶堆，退出
            if (largest == index) break;
            // 交换当前节点与最大值子节点
            swap(index, largest);
            index = largest; // 继续向下调整
        }
    }

    public Player removeTop() {
        if (heap.isEmpty()) throw new RuntimeException("堆为空！");
        Player top = heap.get(0); // 保存堆顶（最大值）
        Player lastVal = heap.remove(heap.size() - 1); // 删除堆尾元素
        if (!heap.isEmpty()) {
            heap.set(0, lastVal); // 堆尾元素放到堆顶
            siftDown(0); // 从根节点下沉调整
        }
        return top;
    }

    private void swap(int i, int j) {
        Player temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    public Player getTop() {
        if (heap.isEmpty()) throw new RuntimeException("堆为空！");
        return heap.get(0);
    }

    public int size() {
        return heap.size();
    }

    // 6. 获取按分数从大到小排序的列表（不会破坏原堆）
    public List<Player> getSortedList() {
        // 克隆一份堆，避免破坏原堆
        MaxHeap tempHeap = new MaxHeap();
        for (Player p : heap) {
            tempHeap.insert(p);
        }

        List<Player> sorted = new ArrayList<>();
        while (tempHeap.size() > 0) {
            sorted.add(tempHeap.removeTop()); // 每次取最大值
        }
        return sorted;
    }

    public void clear(){
        heap.clear();
    }

    public Player find(String nickname) {
        for (Player p : heap) {
            if (p.getNickname().equals(nickname)) {
                return p;
            }
        }
        return null;
    }

}
