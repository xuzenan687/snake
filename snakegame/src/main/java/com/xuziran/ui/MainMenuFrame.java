package com.xuziran.ui;

import com.xuziran.pojo.Data;
import com.xuziran.util.UIStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainMenuFrame extends JFrame {

    public MainMenuFrame() {
        setTitle("游戏大厅");
        setSize(450, 350);
        setLocationRelativeTo(null); // 居中

        // 阻止窗口直接关闭，改为手动保存数据
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // 监听关闭事件
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // 保存数据
                Data.save();
                // 退出程序
                System.exit(0);
            }
        });

        JPanel main = UIStyle.card();
        main.setLayout(new BorderLayout(20, 20));

        JLabel title = new JLabel("多人竞技大厅", SwingConstants.CENTER);
        title.setFont(new Font("Microsoft YaHei", Font.BOLD, 26));
        main.add(title, BorderLayout.NORTH);

        JButton btnStart   = UIStyle.bigButton("开始游戏");
        JButton btnRank    = UIStyle.bigButton("排行榜");
        JButton btnFriend  = UIStyle.bigButton("好友管理");

        btnStart.addActionListener(e -> new SnakeGame().setVisible(true));
        btnRank.addActionListener(e -> new LeaderboardFrame().setVisible(true));
        btnFriend.addActionListener(e -> new FriendManagerFrame().setVisible(true));

        JPanel mid = new JPanel(new GridLayout(3,1,10,10));
        mid.add(btnStart);
        mid.add(btnRank);
        mid.add(btnFriend);

        main.add(mid, BorderLayout.CENTER);
        add(main);
    }
}
