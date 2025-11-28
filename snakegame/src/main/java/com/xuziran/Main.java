package com.xuziran;

import com.xuziran.pojo.Data;
import com.xuziran.ui.MainMenuFrame;
import com.xuziran.util.UIStyle;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(Data::save));//在程序退出时保存数据

        Data.load();

        SwingUtilities.invokeLater(() -> {
            UIStyle.applyGlobalStyle();
            new MainMenuFrame().setVisible(true);
        });
    }
}
