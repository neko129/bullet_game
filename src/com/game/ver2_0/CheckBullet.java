package com.game.ver2_0;

import java.awt.Rectangle;

// 检测子弹类
// 用于检测敌人小人和我的小人之间的直线是否有障碍物
// 如果有障碍物，检测子弹死亡且没碰撞到我的小人
// 如果没有障碍物，检测子弹死亡且碰到我的小人
// 内容和子弹类基本相同
class CheckBullet implements Runnable {
	private double x;
	private double y;
	private int width;
	private int height;
	private double angle; // 角度
	private int cbSpeedP; // 极坐标距离p的速度，即是子弹飞行速度
	private GamePanel gp; // 指针，指向游戏面板，获取我的小人
	private EnemyPerson ep; // 指针，指向创建检测子弹的敌人小人
	private boolean isAlive; // 判断检测子弹是否活着

	public CheckBullet(double x, double y, int goalX, int goalY, int width, int height, GamePanel gp, EnemyPerson ep) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.gp = gp;
		this.ep = ep;
		this.cbSpeedP = 24;
		angle = Math.atan2(goalY - y, goalX - x);
		isAlive = true; // 设置是否活着
	}

	// 子弹飞行方法
	public void fly() {
		x += cbSpeedP * Math.cos(angle);
		y += cbSpeedP * Math.sin(angle);
	}

	// 判断子弹是否击中障碍物
	public boolean isHitBarrier() {
		boolean isHitBarrier = false;

		// 创建子弹对应矩形
		Rectangle bulletRtg = new Rectangle((int) this.x, (int) this.y, this.width, this.height);

		for (int i = 0; i < this.gp.map.getNumber(); i++) {
			// 取出障碍物
			Barrier barrier = this.gp.map.getVt_Barrier().get(i);

			// 创建障碍物对应矩形
			Rectangle barrierRtg = new Rectangle(barrier.getX(), barrier.getY(), barrier.getWidth(),
					barrier.getHeight());

			// 判断是否碰撞
			// 如果碰撞
			if (bulletRtg.intersects(barrierRtg)) {
				isHitBarrier = true;
				break;
			}
		}

		return isHitBarrier;
	}

	// 判断子弹是否击中我的小人方法
	public void isHitMp() {

		// 创建子弹对应矩形
		Rectangle bulletRtg = new Rectangle((int) this.x, (int) this.y, this.width, this.height);

		// 取出我的小人
		MyPerson mp = this.gp.mp;

		// 创建我的小人矩形
		Rectangle mpRtg = new Rectangle(mp.getX(), mp.getY(), mp.getWidth(), mp.getHeight());

		// 判断是否碰撞
		// 如果碰撞，子弹死亡，碰撞到我的小人，敌人小人isInLine = true
		if (bulletRtg.intersects(mpRtg)) {
			this.ep.setInLine(true);
			this.isAlive = false;

		}
	}

	// 判断子弹是否飞出游戏面板
	public void isOverFly() {
		// 如果子弹的x,y在游戏面板外，就是飞出
		// (比较简单，不用Rectangle类
		if (this.x < 0 || this.x > 1300 || this.y < 0 || this.y > 700) {
			this.isAlive = false;
		}
	}

	public void run() {
		while (true) {
			// 如果子弹死亡，线程结束
			if (!this.isAlive) {
				break;
			}

			// 子弹飞行
			this.fly();

			// 判断子弹是否击中我的小人
			// 如果击中，子弹死亡，敌人小人的判断是否和我的小人同一条直线为 true
			this.isHitMp();

			// 判断是否击中障碍物
			// 如果有，敌人小人和我的小人之间有障碍物，不在同一条线
			// 先判断子弹是否活着
			if (this.isAlive) {
				if (this.isHitBarrier()) {

					this.ep.setInLine(false);
					
					this.isAlive = false;
				}
			}

			// 判断子弹是否飞出游戏面板
			// 在方法里实现了死亡
			this.isOverFly();

			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
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

}
