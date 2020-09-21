package com.game.ver2_0;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import java.awt.*;

class Interface extends JPanel implements Runnable, MouseListener, KeyListener {
	private int Panelwidth = 1300, Panelheight = 700;// ���ڸ߶�
	private Color out = new Color(133, 181, 247);
	private Color over = new Color(10, 103, 139);
	private Color title = new Color(0, 107, 232);
	private InterfaceImg Ifi = new InterfaceImg();
	private int stair = 1;// ��¼�������
	private int time_1 = 0;// һ������λ���ж�
	private boolean isAlive;

	// int iterface[][]=new int[4][4];



	public Interface() {
		isAlive = true;
		this.addKeyListener(this);
		this.addMouseListener(this);
	}

	public void paint(Graphics g) {
		super.paint(g);

		if (stair == 1) {
			drawFirstInterface(g);
		} else if (stair == 2 && time_1 == 1) {
			drawFirstInterface(g);
			drawSecondInterface2(g);
		}
		/*
		 * else if(stair==2) { drawSecondInterface(g); }
		 */
	}

	public void drawFirstInterface(Graphics g)// �ķ���������Ϸһ��ʼ�Ľ���
	{
		g.drawImage(Ifi.getBackground1(), 0, 0, Panelwidth, Panelheight, this);
		g.setFont(new Font("΢���ź�", Font.BOLD, 200));// ���������ͺʹ�С�ʹ�ϸ
		g.setColor(title);
		g.drawString("ʱ���ع�", 130, 300);// �����������ݺ���ɫ
		g.setColor(Color.white);// δ��ѡ��ѡ����Ϣ
		g.setFont(new Font("����", Font.BOLD, 30));
		g.drawString("��ʼ��Ϸ", 1036, 435);
		g.drawString("������Ա����", 1036, 505);
		g.drawString("�˳�", 1036, 575);
		if (time_1 == 0) {
			g.setColor(over);
			g.drawString("��ʼ��Ϸ", 1036, 435);
		} else if (time_1 == 1) {
			g.setColor(over);
			g.drawString("������Ա����", 1036, 505);
		} else if (time_1 == 2) {
			g.setColor(over);
			g.drawString("�˳�", 1036, 575);
		}

	}

	/*
	 * public void drawSecondInterface1(Graphics g)//δ��ɵĶ������ { g.setFont(new
	 * Font("΢���ź�", Font.BOLD, 25)); g.setColor(out); g.drawString("LEVEL1", 54,
	 * 47);
	 * 
	 * }
	 */
	public void drawSecondInterface2(Graphics g) {
		int Listwidth = 600, Listheight = 300;
		g.setColor(Color.white);
		g.drawImage(Ifi.getBlack(), 0, 0, Panelwidth, Panelheight, this);
		g.fillRect((Panelwidth - Listwidth) / 2, (Panelheight - Listheight) / 2, Listwidth, Listheight);
		g.setFont(new Font("����", Font.BOLD, 30));
		g.setColor(out);
		g.drawString("������Ա����", 560, 275);
		g.setFont(new Font("����", Font.BOLD, 20));
		g.drawString("��ΰ��", 537, 344);
		g.drawString("����", 707, 344);
		g.drawString("��ԣ��", 537, 430);
		g.drawString("ʩ����", 707, 430);
	}

	@Override
	public void run() {
		// TODO �Զ����ɵķ������
		while (true) {
			if(!this.isAlive) {
				break;
			}
			
			this.repaint();
			try {
				Thread.sleep(25);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO �Զ����ɵķ������
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO �Զ����ɵķ������

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO �Զ����ɵķ������

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// System.out.println("x="+arg0.getX()+"\ny="+arg0.getY());
		// TODO �Զ����ɵķ������
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO �Զ����ɵķ������

	}

	@Override
	public void keyPressed(KeyEvent e) {
	
	}
	
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO �Զ����ɵķ������

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO �Զ����ɵķ������

	}

	public int getTime_1() {
		return time_1;
	}

	public void setTime_1(int time_1) {
		this.time_1 = time_1;
	}

	public void setStair(int stair) {
		this.stair = stair;
	}

	public int getStair() {
		return stair;
	}

	public void setAlive(boolean b) {
		// TODO �Զ����ɵķ������
		
	}
	
	public boolean isAlive() {
		return isAlive;
	}
}
