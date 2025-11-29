package com.xuziran.ui;

import com.xuziran.pojo.Data;
import com.xuziran.pojo.Player;
import com.xuziran.structure.MyHashMap;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class CommonFriendFrame extends JFrame {

    public CommonFriendFrame() {
        setTitle("共同好友");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        Player currentUser = Data.getCurrentUser();
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "请先登录！");
            dispose();
            return;
        }

        // 顶部选择好友的下拉框
        List<Player> myFriends = Data.getFriends().getSortedList();
        if (myFriends.isEmpty()) {
            JOptionPane.showMessageDialog(this, "你还没有好友！");
            dispose();
            return;
        }

        JComboBox<String> friendCombo = new JComboBox<>();
        for (Player f : myFriends) {
            if (!f.getNickname().equals(currentUser.getNickname())) { // 排除自己
                friendCombo.addItem(f.getNickname());
            }
        }

        // 设置字体大小
        friendCombo.setFont(new Font("Microsoft YaHei", Font.PLAIN, 22));

// 设置首选大小（宽，高）
        friendCombo.setPreferredSize(new Dimension(200, 40));

// 添加到顶部
        add(friendCombo, BorderLayout.NORTH);

        // 表格
        String[] columnNames = {"昵称", "得分", "操作"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };
        JTable table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Microsoft YaHei", Font.PLAIN, 22));
        table.getTableHeader().setFont(new Font("Microsoft YaHei", Font.BOLD, 18));
        table.getTableHeader().setPreferredSize(new Dimension(0, 35));

        table.getColumn("操作").setCellRenderer(new ButtonRenderer());
        table.getColumn("操作").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // 当选择好友时刷新共同好友列表
        friendCombo.addActionListener(e -> {
            String selectedFriend = (String) friendCombo.getSelectedItem();
            updateTable(tableModel, currentUser.getNickname(), selectedFriend);
        });
        friendCombo.setSelectedIndex(0); // 默认选择第一个好友
        setVisible(true);
    }

    // 更新表格数据
    private void updateTable(DefaultTableModel tableModel, String myName, String friendName) {
        tableModel.setRowCount(0);
        List<Player> commonFriends = new ArrayList<>();
        Set<String> commonFriendName = Data.getSocialNetwork().commonFriendsBFS(myName, friendName);
        for(String friend : commonFriendName){
            if(Data.getUserList().containsKey(friend)){
                commonFriends.add(Data.getUserList().get(friend));
            }
        }
        for (Player p : commonFriends) {
            tableModel.addRow(new Object[]{p.getNickname(), p.getScore(), "添加好友"});
        }
    }

    // 按钮渲染器
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() { setOpaque(true); }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // 按钮编辑器
    static class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean clicked;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            clicked = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(button);
                JOptionPane.showMessageDialog(parentFrame, "已发送好友请求！");
            }
            clicked = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
}
