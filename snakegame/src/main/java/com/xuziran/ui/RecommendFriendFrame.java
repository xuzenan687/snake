package com.xuziran.ui;

import com.xuziran.pojo.Data;
import com.xuziran.pojo.Player;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// 推荐好友窗口
public class RecommendFriendFrame extends JFrame {

    public RecommendFriendFrame() {

        setTitle("推荐好友");
        setSize(500, 400); // 稍微加大一点
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columnNames = {"昵称", "得分", "操作"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // 只有“操作”列可编辑（放按钮）
            }
        };

        JTable table = new JTable(tableModel);

        // 行高
        table.setRowHeight(35);

        // 设置表格字体
        table.setFont(new Font("Microsoft YaHei", Font.PLAIN, 18));

        // 设置表头字体
        table.getTableHeader().setFont(new Font("Microsoft YaHei", Font.BOLD, 18));

        // 表头高度
        table.getTableHeader().setPreferredSize(new Dimension(0, 35));

        // TODO: 添加数据
        for (Player f : Data.getSocialNetwork().recommendFriends()) {
            tableModel.addRow(new Object[]{f.getNickname(), f.getScore(), "添加好友"});
        }

        // 按钮渲染器
        table.getColumn("操作").setCellRenderer(new ButtonRenderer());
        table.getColumn("操作").setCellEditor(new ButtonEditor(new JCheckBox()));

        // 加滚动条
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);

        setVisible(true);
    }

    // 按钮渲染器
    static class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // 按钮编辑器（点击事件）
    static class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean clicked;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped(); // 停止编辑，否则按钮无法触发事件多次
                }
            });
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
                // 让弹窗相对于整个窗口居中
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(button);
                JOptionPane.showMessageDialog(parentFrame, "已发送好友请求！");
                // TODO: 发送好友请求
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
