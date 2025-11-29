package com.xuziran.ui;

import com.xuziran.pojo.Data;
import com.xuziran.pojo.Player;
import com.xuziran.util.UIStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginFrame extends JFrame {

    public LoginFrame() throws HeadlessException {
        setTitle("登入页面");
        setSize(450, 300);
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
        // 输入框
        JTextField tfUser = new JTextField();

        JPanel panel = new JPanel();
        tfUser.setPreferredSize(new Dimension(200,40));
        panel.add(new JLabel("昵称："));
        panel.add(tfUser);

        JButton btnLogin   = UIStyle.bigButton("登陆");

        btnLogin.addActionListener(e -> {
            Data.load();//原始数据加载完毕
            Data.loadCurrentUser(tfUser.getText());
            Data.loadFriendList(tfUser.getText());
            Data.loadLocalRanking(tfUser.getText());
            new MainMenuFrame().setVisible(true);
            this.dispose();
        });

        JPanel mid = new JPanel(new GridLayout(3,1,10,10));
        mid.add(panel);
        mid.add(btnLogin);

        main.add(mid, BorderLayout.CENTER);
        add(main);
    }
}
