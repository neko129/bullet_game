package com.game.ver1_0;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

// 游戏面板
class GamePanel extends JPanel implements Runnable,MouseListener {
	MyPerson mp; // 我的小人
	EnemyPerson ep; // 敌人小人
	Map map; // 地图
	
	public GamePanel() {
		
		ep = new EnemyPerson(300,300);
//		Thread t2 = new Thread(ep);
//		t2.start();
//		ep.setMyPerson(mp); // 敌人小人获取我的小人
		
		// 地图
		map = new Map();
		
		// 注册鼠标事件监听者,用于确定地图障碍物坐标
		this.addMouseListener(this);
		
		// 我的小人
		mp = new MyPerson(40,300);
		Thread t1 = new Thread(mp);
		t1.start();
		mp.gp = this; // 我的小人获取面板指针
		
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		// 画我的小人
		g.fillRect(mp.getX(), mp.getY(), mp.getWidth(), mp.getHeight());
		
		// 画敌人小人
		g.fillRect(ep.getX(), ep.getY(), ep.getWidth(), ep.getHeight());
		
		// 画地图
		for(int i = 0; i < map.getNumber(); i++) {
			// 取出地图障碍物
			Barrier barrier = map.getVt_Barrier().get(i);
			g.fill3DRect(barrier.getX(), barrier.getY(), barrier.getWidth(), barrier.getHeight(), false);
		}
	}
	
	public void run() {
		while(true) {
			this.repaint();
			
			try {
				Thread.sleep(25);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		System.out.println("x = " + arg0.getX());
		System.out.println("y = " + arg0.getY());
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
