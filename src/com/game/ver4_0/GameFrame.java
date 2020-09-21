package com.game.ver4_0;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * v1.0 
 * 自己可以走动 
 * 可配置地图 
 * 实现自己基本移动的功能
 * 
 * v2.0 
 * 实现攻击功能 
 * 实现射击功能 
 * 加入图片素材，优化画面 
 * 实现敌人的基本功能  实现敌人AI 
 * 尝试加入双缓冲(*)(据说paint()已实现双缓冲
 * 
 * v3.0
 * 加入死亡动画
 * 加入下蹲功能
 * 加入选关功能 
 * 优化游戏画面
 * 修复断手bug
 * 调整敌人AI
 * 
 *
 *
 *v4.0
 *加入更多武器
 *可捡拾武器
 *加入更多种类敌人
 *游戏画面展示更加丰富
 *加入音效(*)
 */

// 游戏窗体
public class GameFrame extends JFrame implements KeyListener {
	public static void main(String[] args) {
		GameFrame gf = new GameFrame();
	}

	GamePanel gp; // 游戏面板
	Interface it;// 开始界面
	GameImg gi; // 游戏图片，在开始游戏时先预加载游戏图片。再传入到游戏面板

	public GameFrame() {
		gi = new GameImg();
		
		it = new Interface(this);
		Thread t2 = new Thread(it);
		t2.start();

		// 添加开始面板为键盘事件监听者
		this.addKeyListener(it);

		this.add(it);

		
		this.setTitle("时间特工");
		
		this.setSize(1300, 700);
		this.setLocation(40, 30);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public void keyPressed(KeyEvent e) {

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
