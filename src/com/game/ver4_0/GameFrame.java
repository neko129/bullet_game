package com.game.ver4_0;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * v1.0 
 * �Լ������߶� 
 * �����õ�ͼ 
 * ʵ���Լ������ƶ��Ĺ���
 * 
 * v2.0 
 * ʵ�ֹ������� 
 * ʵ��������� 
 * ����ͼƬ�زģ��Ż����� 
 * ʵ�ֵ��˵Ļ�������  ʵ�ֵ���AI 
 * ���Լ���˫����(*)(��˵paint()��ʵ��˫����
 * 
 * v3.0
 * ������������
 * �����¶׹���
 * ����ѡ�ع��� 
 * �Ż���Ϸ����
 * �޸�����bug
 * ��������AI
 * 
 *
 *
 *v4.0
 *�����������
 *�ɼ�ʰ����
 *��������������
 *��Ϸ����չʾ���ӷḻ
 *������Ч(*)
 */

// ��Ϸ����
public class GameFrame extends JFrame implements KeyListener {
	public static void main(String[] args) {
		GameFrame gf = new GameFrame();
	}

	GamePanel gp; // ��Ϸ���
	Interface it;// ��ʼ����
	GameImg gi; // ��ϷͼƬ���ڿ�ʼ��Ϸʱ��Ԥ������ϷͼƬ���ٴ��뵽��Ϸ���

	public GameFrame() {
		gi = new GameImg();
		
		it = new Interface(this);
		Thread t2 = new Thread(it);
		t2.start();

		// ��ӿ�ʼ���Ϊ�����¼�������
		this.addKeyListener(it);

		this.add(it);

		
		this.setTitle("ʱ���ع�");
		
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
