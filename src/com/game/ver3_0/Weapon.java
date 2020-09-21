package com.game.ver3_0;

import java.util.Vector;

// ������
class Weapon {
	private int x;
	private int y;
	private int width;
	private int height;
	private String weaponType; // ��������
	private Vector<Bullet> vt_bullet; // �ӵ�����
	private int bulletNumber; // �ӵ�����
	private GamePanel gp; // ��Ϸ���ָ��

	public Weapon() {
		vt_bullet = new Vector<Bullet>();
	}
	
	// ����
	// ��������ӵ�
	public void fire(int goalX, int goalY, int belongto) {
		if(this.bulletNumber != 0) {
			// ���ж��Ƿ����ӵ�
			// ������ӵ��������ӵ�
			Bullet bullet = new Bullet(x, y, goalX, goalY, 4, 4, gp, belongto);
			Thread t1 = new Thread(bullet);
			t1.start();
			// �Ž��ӵ�����
			vt_bullet.add(bullet);
			
			bulletNumber--; // �ӵ�����
			
			System.out.println("�ӵ��ɳ�");
		} else {
			System.out.println("û�ӵ�");
		}
	}
	
	
	public void setGp(GamePanel gp) {
		this.gp = gp;
	}
	
	public Vector<Bullet> getVt_bullet() {
		return vt_bullet;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getWeaponType() {
		return weaponType;
	}

	public void setWeaponType(String weaponType) {
		this.weaponType = weaponType;
	}


	public int getBulletNumber() {
		return bulletNumber;
	}

	public void setBulletNumber(int bulletNumber) {
		this.bulletNumber = bulletNumber;
	}
}
