package com.game.ver1_0;

import javax.swing.*;
import java.awt.*;
/**
 * v1.0
 * 自己可以走动
 * 可配置地图
 * 实现自己基本移动的功能
 * @author Administrator
 *
 */

// 游戏窗体
public class GameFrame extends JFrame {
	public static void main(String[] args) {
		GameFrame gf = new GameFrame();
	}

	GamePanel gp; // 游戏面板

	public GameFrame() {
		gp = new GamePanel();
		Thread t1 = new Thread(gp);
		t1.start();

		this.addKeyListener(gp.mp);

		this.add(gp);

		this.setSize(1200, 700);
		this.setLocation(80, 30);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
}
