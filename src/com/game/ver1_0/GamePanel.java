package com.game.ver1_0;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

// ��Ϸ���
class GamePanel extends JPanel implements Runnable,MouseListener {
	MyPerson mp; // �ҵ�С��
	EnemyPerson ep; // ����С��
	Map map; // ��ͼ
	
	public GamePanel() {
		
		ep = new EnemyPerson(300,300);
//		Thread t2 = new Thread(ep);
//		t2.start();
//		ep.setMyPerson(mp); // ����С�˻�ȡ�ҵ�С��
		
		// ��ͼ
		map = new Map();
		
		// ע������¼�������,����ȷ����ͼ�ϰ�������
		this.addMouseListener(this);
		
		// �ҵ�С��
		mp = new MyPerson(40,300);
		Thread t1 = new Thread(mp);
		t1.start();
		mp.gp = this; // �ҵ�С�˻�ȡ���ָ��
		
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		// ���ҵ�С��
		g.fillRect(mp.getX(), mp.getY(), mp.getWidth(), mp.getHeight());
		
		// ������С��
		g.fillRect(ep.getX(), ep.getY(), ep.getWidth(), ep.getHeight());
		
		// ����ͼ
		for(int i = 0; i < map.getNumber(); i++) {
			// ȡ����ͼ�ϰ���
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
