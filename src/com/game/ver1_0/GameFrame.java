package com.game.ver1_0;

import javax.swing.*;
import java.awt.*;
/**
 * v1.0
 * �Լ������߶�
 * �����õ�ͼ
 * ʵ���Լ������ƶ��Ĺ���
 * @author Administrator
 *
 */

// ��Ϸ����
public class GameFrame extends JFrame {
	public static void main(String[] args) {
		GameFrame gf = new GameFrame();
	}

	GamePanel gp; // ��Ϸ���

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
