package com.game.ver2_0;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * v1.0 �Լ������߶� �����õ�ͼ ʵ���Լ������ƶ��Ĺ���
 * 
 * v2.0 ʵ�ֹ������� ʵ��������� ����ͼƬ�زģ��Ż����� ʵ�ֵ��˵Ļ������� ʵ�ֵ���AI ���Լ���˫����(*)
 * 
 * @author Administrator
 *
 */

// ��Ϸ����
public class GameFrame extends JFrame implements KeyListener {
	public static void main(String[] args) {
		GameFrame gf = new GameFrame();
	}

	GamePanel gp; // ��Ϸ���
	Interface it;// ��Ϸ����

	public GameFrame() {
		// gp = new GamePanel();
		// Thread t1 = new Thread(gp);
		// t1.start();
		it = new Interface();
		Thread t2 = new Thread(it);
		t2.start();

		this.addKeyListener(this);

		this.add(it);

		// ע���ҵ�С�˼����¼�������
		// this.addKeyListener(gp.mp);

		// ע����Ϸ���Ϊ�����¼�������
		// this.addKeyListener(gp);

		// this.add(gp);
		
		this.setTitle("ʱ���ع�");
		
		this.setSize(1300, 700);
		this.setLocation(40, 30);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public void keyPressed(KeyEvent e) {

		// ��ʼ���ѡ�����
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
					// ��ʼ��Ϸ
					// ��ɾ����ʼ���

					this.remove(it);
					it.setAlive(false);

					gp = new GamePanel();
					Thread t1 = new Thread(gp);
					t1.start();
					// ע���ҵ�С�˼����¼�������
					this.addKeyListener(gp.mp);

					// ע����Ϸ���Ϊ�����¼�������
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
		
		 // R���ؿ���Ϸ
		if(e.getKeyCode() == KeyEvent.VK_R) {
			
			// ɾ��ԭ������Ϸ���
			this.remove(gp);
			gp.isAlive = false;

			gp = new GamePanel();
			Thread t1 = new Thread(gp);
			t1.start();
			// ע���ҵ�С�˼����¼�������
			this.addKeyListener(gp.mp);

			// ע����Ϸ���Ϊ�����¼�������
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
