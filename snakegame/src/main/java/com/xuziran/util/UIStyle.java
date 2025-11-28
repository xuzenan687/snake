package com.xuziran.util;

import javax.swing.*;
import java.awt.*;

public class UIStyle {

    public static void applyGlobalStyle() {
        Font font = new Font("Microsoft YaHei", Font.PLAIN, 24);
        UIManager.put("Button.font", font);
        UIManager.put("Label.font", font);
        UIManager.put("TextField.font", font);
        UIManager.put("TextArea.font", font);
        UIManager.put("Table.font", font);
        UIManager.put("OptionPane.font", font);
    }

    public static JPanel card() {
        JPanel p = new JPanel();
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return p;
    }

    public static JButton bigButton(String t) {
        JButton b = new JButton(t);
        b.setPreferredSize(new Dimension(200, 45));
        return b;
    }
}
