package com.game.ver2_0;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * v1.0 自己可以走动 可配置地图 实现自己基本移动的功能
 * 
 * v2.0 实现攻击功能 实现射击功能 加入图片素材，优化画面 实现敌人的基本功能 实现敌人AI 尝试加入双缓冲(*)
 * 
 * @author Administrator
 *
 */

// 游戏窗体
public class GameFrame extends JFrame implements KeyListener {
	public static void main(String[] args) {
		GameFrame gf = new GameFrame();
	}

	GamePanel gp; // 游戏面板
	Interface it;// 游戏界面

	public GameFrame() {
		// gp = new GamePanel();
		// Thread t1 = new Thread(gp);
		// t1.start();
		it = new Interface();
		Thread t2 = new Thread(it);
		t2.start();

		this.addKeyListener(this);

		this.add(it);

		// 注册我的小人键盘事件监听者
		// this.addKeyListener(gp.mp);

		// 注册游戏面板为键盘事件监听者
		// this.addKeyListener(gp);

		// this.add(gp);
		
		this.setTitle("时间特工");
		
		this.setSize(1300, 700);
		this.setLocation(40, 30);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public void keyPressed(KeyEvent e) {

		// 开始面板选择相关
		if (it.getStair() == 1) {
			if ((e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S)) {
				it.setTime_1((it.getTime_1() + 1) % 3);
			} else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
				it.setTime_1((it.getTime_1() + 2) % 3);
			} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				if (it.getTime_1() == 1) {
					it.setStair(it.getStair() + 1);
				} else if (it.getTime_1() == 2) {
					System.exit(0);
				} else if (it.getTime_1() == 0) {
					// 开始游戏
					// 先删除开始面板

					this.remove(it);
					it.setAlive(false);

					gp = new GamePanel();
					Thread t1 = new Thread(gp);
					t1.start();
					// 注册我的小人键盘事件监听者
					this.addKeyListener(gp.mp);

					// 注册游戏面板为键盘事件监听者
					this.addKeyListener(gp);

					this.add(gp);

					this.setVisible(true);
				}
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (it.getStair() > 1) {
				it.setStair(it.getStair() - 1);
			}
		}
		
		 // R键重开游戏
		if(e.getKeyCode() == KeyEvent.VK_R) {
			
			// 删除原来的游戏面板
			this.remove(gp);
			gp.isAlive = false;

			gp = new GamePanel();
			Thread t1 = new Thread(gp);
			t1.start();
			// 注册我的小人键盘事件监听者
			this.addKeyListener(gp.mp);

			// 注册游戏面板为键盘事件监听者
			this.addKeyListener(gp);

			this.add(gp);

			this.setVisible(true);
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}
