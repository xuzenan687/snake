package com.xuziran.ui;


import com.xuziran.pojo.Data;
import com.xuziran.pojo.Player;
import com.xuziran.util.UIStyle;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FriendManagerFrame extends JFrame {

    JTable table;
    DefaultTableModel model;

    public FriendManagerFrame() {
        setTitle("好友管理中心");
        setSize(600, 480);
        setLocationRelativeTo(null);

        // ========== 顶部按钮栏 ==========
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnAdd = new JButton("添加好友");
        JButton btnSearch = new JButton("查找好友");
        JButton btnRecommend = new JButton("推荐好友");
        JButton btnRefresh = new JButton("共同好友");

        btnAdd.addActionListener(e -> {
            new AddFriendDialog(this).setVisible(true);
            refreshTable();
        });
        btnSearch.addActionListener(e -> new SearchFriendFrame().setVisible( true));
        btnRecommend.addActionListener(e -> new RecommendFriendFrame().setVisible( true));
        btnRefresh.addActionListener(e -> new CommonFriendFrame().setVisible( true));

        topBar.add(btnAdd);
        topBar.add(btnSearch);
        topBar.add(btnRecommend);
        topBar.add(btnRefresh);

        // ========== 表格视图（好友列表） ==========
        String[] columns = {"好友昵称", "历史最高分", "操作"};

        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                return col == 2;
            }
        };

        table = new JTable(model);

        // 行高
        table.setRowHeight(35);

        table.setFont(new Font("Microsoft YaHei", Font.PLAIN, 22));
        table.getTableHeader().setFont(new Font("Microsoft YaHei", Font.BOLD, 22));
        table.getTableHeader().setPreferredSize(new Dimension(0, 35));



        loadMockFriendList();


        table.getColumnModel().getColumn(2).setCellEditor(new ChallengeButtonEditor(new JCheckBox(), this, model));

        table.getColumnModel().getColumn(2).setCellRenderer((tbl, val, selected, focus, row, col) -> {
            JButton btn = new JButton("发起挑战");
            return btn;
        });

        JScrollPane scroll = new JScrollPane(table);// 滚动条

        // ========== 整体布局 ==========
        JPanel root = UIStyle.card();
        root.setLayout(new BorderLayout(15, 15));

        root.add(topBar, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);

        add(root);
    }


    // ------------------------------
    // 将模拟好友数据加载到表格
    // ------------------------------
    private void loadMockFriendList() {
        model.setRowCount(0);
        for (Player friend : Data.getFriends().getSortedList()) {
            model.addRow(new Object[]{
                    friend.getNickname(),
                    friend.getScore(),
                    "发起挑战"
            });
        }
    }

    // 刷新（暂时重新加载模拟数据）
    private void refreshTable() {
        loadMockFriendList();
    }

    // ------------------------------
    // 统一输入弹窗功能
    // ------------------------------
    interface Action {
        void run(String user, String friend);
    }

    private void openDialog(String title, Action action) {
        JDialog d = new JDialog(this, title, true);
        d.setSize(350, 200);
        d.setLocationRelativeTo(this);

        JTextField tfUser = new JTextField();
        JTextField tfFriend = new JTextField();

        JPanel p = UIStyle.card();
        p.setLayout(new GridLayout(3, 2, 10, 10));

        p.add(new JLabel("用户："));
        p.add(tfUser);
        p.add(new JLabel("好友："));
        p.add(tfFriend);

        JButton ok = new JButton("确定");
        JButton cancel = new JButton("取消");

        ok.addActionListener(e -> {
            action.run(tfUser.getText(), tfFriend.getText());
            d.dispose();
        });

        cancel.addActionListener(e -> d.dispose());

        p.add(ok);
        p.add(cancel);

        d.add(p);
        d.setVisible(true);
    }
    // 表格单元格按钮编辑器 —— 负责触发点击事件
    class ChallengeButtonEditor extends DefaultCellEditor {

        private JButton button;
        private FriendManagerFrame parent;
        private DefaultTableModel model;
        private int row;

        public ChallengeButtonEditor(JCheckBox checkBox, FriendManagerFrame parent, DefaultTableModel model) {
            super(checkBox);
            this.parent = parent;
            this.model = model;

            button = new JButton("发起挑战");

            button.addActionListener(e -> {
                new SnakeGame().setVisible(true);
                fireEditingStopped(); // 结束编辑状态，否则表格会锁住
            });
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;  // 记录当前行
            return button;
        }
    }

}
