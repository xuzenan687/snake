package com.xuziran.ui;

import com.xuziran.pojo.Data;
import com.xuziran.pojo.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JFrame {
    private SnakePanel gamePanel;
    private static final Random random = new SecureRandom();

    public SnakeGame() {
        setTitle("贪吃蛇");
        setSize(800, 600);
        setLocationRelativeTo(null);

        // 关闭窗口时保存数据
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Data.save();
            }
        });

        gamePanel = new SnakePanel();
        add(gamePanel);

        // 键盘监听
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                gamePanel.handleKey(e.getKeyCode());
            }
        });
    }

    class SnakePanel extends JPanel {
        // 类成员
        private LinkedList<Point> snake;
        private Point food;
        private int direction; // 1上、2下、3左、4右
        private boolean isGameOver;
        private boolean isPaused;
        private int score;
        private final int gridSize = 20;
        private Timer timer;
        private int timerDelay; // 初始速度，单位ms

        private final int topOffset = 30; // 顶部留给得分显示

        public SnakePanel() {
            setBackground(Color.BLACK);
            snake = new LinkedList<>();
            score = 0;
            timerDelay = 150;
            initGame(); // 初始化蛇和食物，但不调整timer

            // 初始化Timer
            timer = new Timer(timerDelay, e -> {

                if (!isGameOver && !isPaused) moveSnake();
                repaint();
            });
            timer.start();
        }

        private void initGame() {
            snake.clear();
            int cols = Math.max(800 / gridSize, 1);
            int rows = Math.max((600 - topOffset) / gridSize, 1);
            int startX = random.nextInt(Math.max(cols - 3, 1)-10) + 5;
            int startY = random.nextInt(rows-10)+5;
            if (startX == 0) startX = 10;
            if (startY == 0) startY = 10;

            snake.add(new Point(startX, startY));
            snake.add(new Point(startX - 1, startY));
            snake.add(new Point(startX - 2, startY));

            direction = 4; // 初始向右
            isGameOver = false;
            isPaused = false;
            score = 0;
            timerDelay = 150; // 重置速度
            if (timer != null) timer.setDelay(timerDelay); // 安全修改Timer速度
            generateFood();
        }


        private void generateFood() {

            int cols = Math.max(800 / gridSize, 1);
            int rows = Math.max((600 - topOffset) / gridSize, 30);

            int x, y;
            do {
                x = random.nextInt(cols-10)+5;
                y = random.nextInt(rows-10)+5;
            } while (snake.contains(new Point(x, y)));

            food = new Point(x, y);
        }
        public void handleKey(int keyCode) {

            if (isGameOver) {
                initGame();
                return;
            }

            if (keyCode == KeyEvent.VK_SPACE) {
                if (!isGameOver) isPaused = !isPaused; // 空格暂停/继续
                return;
            }



            switch (keyCode) {
                case KeyEvent.VK_UP -> { if (direction != 2) direction = 1; }
                case KeyEvent.VK_DOWN -> { if (direction != 1) direction = 2; }
                case KeyEvent.VK_LEFT -> { if (direction != 4) direction = 3; }
                case KeyEvent.VK_RIGHT -> { if (direction != 3) direction = 4; }
            }
        }

        private void moveSnake() {
            if (snake.isEmpty()) return;
            Point head = snake.getFirst();
            Point newHead = new Point(head);

            switch (direction) {
                case 1 -> newHead.y--;
                case 2 -> newHead.y++;
                case 3 -> newHead.x--;
                case 4 -> newHead.x++;
            }

            if (newHead.x < 0 || newHead.x >= getWidth() / gridSize ||
                    newHead.y < 0 || newHead.y >= (getHeight() - topOffset) / gridSize ||
                    snake.contains(newHead)) {
                isGameOver = true;
                return;
            }

            //更新分数
            if (Data.getCurrentUser() != null) {
                int newScore = Math.max(score, Data.getCurrentUser().getScore());
                Data.getCurrentUser().setScore(newScore);

                // HashMap（用户列表）
                if (Data.getUserList().get(Data.getCurrentUser().getNickname()) != null) {
                    Data.getUserList().get(Data.getCurrentUser().getNickname()).setScore(newScore);
                }

                // 好友表
                if (Data.getFriends().get(Data.getCurrentUser().getNickname()) != null) {
                    Data.getFriends().get(Data.getCurrentUser().getNickname()).setScore(newScore);
                }

                // 本地排行榜
                Player lp = Data.getLocalRanking().find(Data.getCurrentUser().getNickname());
                if (lp != null) lp.setScore(newScore);

                // 全球排行榜
                Player gp = Data.getGlobalRanking().find(Data.getCurrentUser().getNickname());
                if (gp != null) gp.setScore(newScore);
            }


            snake.addFirst(newHead);

            if (newHead.equals(food)) {
                score+=5;
                generateFood();
                // 提升速度，每吃一颗食物速度加快一点
                timerDelay = Math.max(40, 150 - score * 2);
                timer.setDelay(timerDelay);
            } else {
                snake.removeLast();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {// 绘制游戏面板
            super.paintComponent(g);

            // 绘制得分
            g.setColor(Color.WHITE);
            g.setFont(new Font("微软雅黑", Font.BOLD, 18));
            g.drawString("分数: " + score +"   历史最高分: " + Data.getCurrentUser().getScore()+ "  按空格键暂停", 10, 20);


            // 绘制背景网格（游戏区域，排除顶部留白）
            g.setColor(Color.DARK_GRAY);
            int rows = (getHeight() - topOffset) / gridSize;
            int cols = getWidth() / gridSize;
            for (int x = 0; x <= cols; x++) {
                g.drawLine(x * gridSize, topOffset, x * gridSize, topOffset + rows * gridSize);
            }
            for (int y = 0; y <= rows; y++) {
                g.drawLine(0, topOffset + y * gridSize, cols * gridSize, topOffset + y * gridSize);
            }

            // 绘制蛇（区分头和身体）
            for (int i = 0; i < snake.size(); i++) {
                Point p = snake.get(i);
                if (i == 0) g.setColor(Color.GREEN.brighter()); // 蛇头亮绿色
                else g.setColor(Color.GREEN.darker());         // 蛇身深绿色
                g.fillRect(p.x * gridSize, topOffset + p.y * gridSize, gridSize - 1, gridSize - 1);
            }

            // 绘制食物
            g.setColor(Color.RED);
            g.fillRect(food.x * gridSize, topOffset + food.y * gridSize, gridSize - 1, gridSize - 1);

            // 游戏状态提示
            g.setColor(Color.WHITE);
            if (isPaused) {
                g.setFont(new Font("微软雅黑", Font.BOLD, 30));
                g.drawString("暂停中", getWidth()/2 - 60, getHeight()/2);
            }

            if (isGameOver) {
                g.setFont(new Font("微软雅黑", Font.BOLD, 30));
                g.drawString("游戏结束! 按任意键继续", getWidth()/2 -160, getHeight()/2);
            }
        }
    }
}
