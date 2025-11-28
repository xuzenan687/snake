package com.xuziran.ui;

import com.xuziran.pojo.Data;
import com.xuziran.pojo.Player;
import com.xuziran.util.UIStyle;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LeaderboardFrame extends JFrame {
    JTable table;
    DefaultTableModel model;

    public LeaderboardFrame() {
        setTitle("排行榜");
        setSize(520, 480);
        setLocationRelativeTo(null);

        // ======== 顶部功能按钮 ========
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton localRankBtn = new JButton("本地排名");
        JButton globalRankBtn = new JButton("全球排名");
        topBar.add(localRankBtn);
        topBar.add(globalRankBtn);

        // ======== 表格初始化 ========
        model = new DefaultTableModel();
        table = new JTable(model);
        // 行高
        table.setRowHeight(35);

// 设置表格字体
        table.setFont(new Font("Microsoft YaHei", Font.PLAIN, 22));

// 设置表头字体
        table.getTableHeader().setFont(new Font("Microsoft YaHei", Font.BOLD, 22));

// 表头高度
        table.getTableHeader().setPreferredSize(new Dimension(0, 35));

        // 放进通用卡片 Panel
        JPanel panel = UIStyle.card();
        panel.setLayout(new BorderLayout());
        panel.add(topBar, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        add(panel);

        // ======== 按钮事件 ========
        localRankBtn.addActionListener(e -> loadLocalRank());
        globalRankBtn.addActionListener(e -> loadGlobalRank());

        // 默认显示本地排名
        loadLocalRank();
    }

    // ---------- 加载本地排行榜 ----------
    private void loadLocalRank() {
        model.setRowCount(0);
        model.setColumnIdentifiers(new String[]{"昵称", "历史最高分"});

        for (Player player : Data.getLocalRanking().getSortedList()) {
            model.addRow(new Object[]{player.getNickname(), player.getScore()});
        }

        // 移除按钮渲染器和编辑器
        table.getColumnModel().getColumnCount();
        if (table.getColumnCount() > 2) {
            table.getColumnModel().getColumn(2).setCellRenderer(null);
            table.getColumnModel().getColumn(2).setCellEditor(null);
        }
    }

    // ---------- 加载全球排行榜 ----------
    private void loadGlobalRank() {
        model.setRowCount(0);
        model.setColumnIdentifiers(new String[]{"昵称", "历史最高分", "操作"});

        for (Player player : Data.getGlobalRanking().getSortedList()) { // 这里可换成全球玩家列表
            model.addRow(new Object[]{player.getNickname(), player.getScore(), "添加好友"});
        }

        // 设置按钮渲染器和编辑器
        table.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor(new JCheckBox(), this));
    }

    // -------- 渲染器：外观是按钮 ----------
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText("添加好友");
            return this;
        }
    }

    // -------- 编辑器：点击真的触发事件 ----------
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private LeaderboardFrame parentFrame;

        public ButtonEditor(JCheckBox checkBox, LeaderboardFrame parentFrame) {
            super(checkBox);
            this.parentFrame = parentFrame;

            button = new JButton("添加好友");
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new AddFriendDialog(parentFrame).setVisible(true);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table, Object value, boolean isSelected, int row, int column) {
            return button;
        }
    }
}
