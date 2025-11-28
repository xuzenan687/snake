package com.xuziran.ui;

import com.xuziran.pojo.Data;
import com.xuziran.pojo.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SearchFriendFrame extends JFrame {

    public SearchFriendFrame() {

        setTitle("查找好友");
        setSize(400, 150);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextField textField = new JTextField();
        JButton searchButton = new JButton("搜索");

        panel.add(textField, BorderLayout.CENTER);
        panel.add(searchButton, BorderLayout.EAST);
        add(panel);

        searchButton.addActionListener(e -> {
            String target = textField.getText().trim();
            if (target.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入昵称！");
                return;
            }
            Player found = null;
            for (Player f : Data.getFriends().getSortedList()) {
                if (f.getNickname().equals(target)) {
                    found = f;
                    break;
                }
            }
            if (found != null) {
                JOptionPane.showMessageDialog(this,
                        "昵称：" + found.getNickname() + "\n得分：" + found.getScore());
            } else {
                JOptionPane.showMessageDialog(this, "未找到该好友！");
            }
        });

        setVisible(true);
    }
}
