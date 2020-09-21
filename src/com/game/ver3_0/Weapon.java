package com.game.ver3_0;

import java.util.Vector;

// 武器类
class Weapon {
	private int x;
	private int y;
	private int width;
	private int height;
	private String weaponType; // 武器类型
	private Vector<Bullet> vt_bullet; // 子弹向量
	private int bulletNumber; // 子弹向量
	private GamePanel gp; // 游戏面板指针

	public Weapon() {
		vt_bullet = new Vector<Bullet>();
	}
	
	// 开火
	// 武器射出子弹
	public void fire(int goalX, int goalY, int belongto) {
		if(this.bulletNumber != 0) {
			// 先判断是否有子弹
			// 如果有子弹，创建子弹
			Bullet bullet = new Bullet(x, y, goalX, goalY, 4, 4, gp, belongto);
			Thread t1 = new Thread(bullet);
			t1.start();
			// 放进子弹向量
			vt_bullet.add(bullet);
			
			bulletNumber--; // 子弹减少
			
			System.out.println("子弹飞出");
		} else {
			System.out.println("没子弹");
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
