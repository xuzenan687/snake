package com.xuziran.ui;

import com.xuziran.pojo.Data;
import com.xuziran.util.UIStyle;

import javax.swing.*;
import java.awt.*;

public class AddFriendDialog extends JDialog {


    public AddFriendDialog(JFrame parent) {
        super(parent, "添加好友", true);

        setSize(350, 200);
        setLocationRelativeTo(parent);

        JPanel panel = UIStyle.card();
        panel.setLayout(new GridLayout(2, 2, 10, 10));

        JTextField tfFriend = new JTextField();

        panel.add(new JLabel("昵称："));
        panel.add(tfFriend);

        JButton btnOK = new JButton("确定");
        JButton btnCancel = new JButton("取消");

        btnOK.addActionListener(e -> {
            // 添加好友
            String friend = tfFriend.getText();
            if(Data.getUserList().containsKey(friend)){
                Data.getFriends().put(friend, Data.getUserList().get(friend));
                Data.getSocialNetwork().addEdge(Data.getCurrentUser().getNickname(), friend);
                JOptionPane.showMessageDialog(this, "添加成功！");
            }else{
                JOptionPane.showMessageDialog(this, "用户不存在！");
            }
            dispose();
        });

        btnCancel.addActionListener(e -> dispose());

        panel.add(btnOK);
        panel.add(btnCancel);

        add(panel);
    }
}
