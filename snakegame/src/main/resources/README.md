# 多人竞技贪吃蛇游戏

这是一个基于Java Swing开发的多人竞技贪吃蛇游戏，支持用户登录、好友管理、排行榜查看以及好友推荐等社交功能，同时包含经典的贪吃蛇游戏玩法。

## 功能特点

- **用户系统**：支持用户登录，记录用户游戏分数
- **贪吃蛇游戏**：经典玩法，通过方向键控制蛇移动，吃到食物得分，撞到墙壁或自身则游戏结束
- **排行榜系统**：
    - 本地排名：展示当前用户的得分排名
    - 全球排名：展示所有用户的得分排名
- **社交功能**：
    - 好友管理：添加、查找好友，查看好友列表
    - 共同好友：查看与指定好友的共同好友
    - 好友推荐：基于共同好友数量和分数差距推荐潜在好友
    - 发起挑战：向好友发起游戏挑战

## 项目结构

- `main/java/com/xuziran/ui`：包含所有UI界面类
    - `LoginFrame.java`：登录界面
    - `MainMenuFrame.java`：主菜单界面
    - `SnakeGame.java`：贪吃蛇游戏界面
    - `LeaderboardFrame.java`：排行榜界面
    - `FriendManagerFrame.java`：好友管理界面
    - 其他社交相关界面：`AddFriendDialog.java`、`CommonFriendFrame.java`等

- `main/java/com/xuziran/pojo`：数据模型类
    - `Data.java`：数据管理类
    - `Player.java`：玩家信息类

- `main/java/com/xuziran/structure`：数据结构类
    - `Graph.java`：用于好友关系管理的图结构
    - `MaxHeap.java`：用于排行榜排序的最大堆

- `main/java/com/xuziran/util`：工具类
    - `UIStyle.java`：UI样式统一管理

- `main/resources`：数据文件
    - `data.json`：用户数据、排行榜和好友关系数据
    - `backup.json`：数据备份文件

## 如何运行

1. 确保已安装Java开发环境（JDK 8及以上）
2. 克隆或下载项目到本地
3. 编译并运行`Main.java`作为程序入口

```bash
# 编译命令示例
javac com/xuziran/Main.java

# 运行命令示例
java com.xuziran.Main
```

## 操作说明

1. **登录**：在登录界面输入昵称即可登录
2. **游戏控制**：
    - 方向键：控制蛇的移动方向
    - 空格键：暂停/继续游戏
3. **社交功能**：在主菜单点击"好友管理"可进行好友相关操作
4. **排行榜**：在主菜单点击"排行榜"可查看本地和全球排名

## 数据存储

用户数据、分数和好友关系存储在`data.json`文件中，程序退出时会自动保存数据，确保数据不会丢失。

## 设计亮点

- 使用图数据结构(`Graph.java`)管理好友关系，高效支持好友添加、删除和共同好友查询
- 使用最大堆(`MaxHeap.java`)实现排行榜功能，支持高效的分数排序
- 统一的UI样式管理，确保界面美观一致
- 完善的数据持久化机制，保证用户数据安全