package com.game.ver3_0;

import java.awt.Rectangle;

// 子弹类
// 射击时射出子弹
// 实现子弹任意直线飞行
// 建立极坐标，算出角度，x = pcosx , y = psinx

class Bullet implements Runnable {
	private double x;
	private double y;
	private int width;
	private int height;
	private double angle; // 角度
	
	static double speedP; // 极坐标距离p的速度，即是子弹飞行速度
	
	private GamePanel gp; // 指针，指向游戏面板，主要是获取敌人对象
	private boolean isAlive; // 判断子弹是否活着
	private int belongto; // 子弹属于谁

	// belongto 参数， 判断子弹属于谁，在下面的判断中，我的小人的子弹会判断是否击中敌人
	// 敌人小人会判断是否击中我的小人
	// 0是我的小人，1是敌人小人
	public Bullet(double x, double y, int goalX, int goalY, int width, int height, GamePanel gp, int belongto) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.gp = gp;
		Bullet.speedP = 16;
		angle = Math.atan2(goalY - y, goalX - x);
		isAlive = true; // 设置是否活着
		this.belongto = belongto;
	}

	// 子弹飞行方法
	public void fly() {
		x += speedP * Math.cos(angle);
		y += speedP * Math.sin(angle);
	}

	// 判断子弹是否击中敌人方法
	public void isHitEp() {

		// 创建子弹对应矩形
		Rectangle bulletRtg = new Rectangle((int) this.x, (int) this.y, this.width, this.height);

		// 取出敌人
		for (int i = 0; i < gp.vt_Ep.size(); i++) {
			EnemyPerson ep = gp.vt_Ep.get(i);

			//  判断敌人是否活着
			// 如果活着，才进行判断碰撞
			
			if (ep.isAlive()) {
				// 创建对应的敌人矩形
				Rectangle epRtg = new Rectangle(ep.getX(), ep.getY(), ep.getWidth(), ep.getHeight());

				// 判断是否碰撞
				// 如果碰撞，子弹死亡，敌人死亡,直接跳出循环，不能再与其他敌人检测碰撞
				if (bulletRtg.intersects(epRtg)) {

					this.isAlive = false;
					ep.setAlive(false);
					break;
				}
			}
		}

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
		// 如果碰撞，子弹死亡，我的小人死亡
		if (bulletRtg.intersects(mpRtg)) {
			mp.setAlive(false);
			this.isAlive = false;

		}
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

			//判断子弹是否击中我的小人
			// 1是敌人小人
			if(this.belongto == 1) {
				this.isHitMp();
			}
			
			// 判断子弹是否击中敌人
			// 如果击中，子弹死亡，敌人死亡(在方法里实现了死亡)
			//  判断子弹属于谁
			// 0是我的小人
			if(this.belongto == 0) {
				this.isHitEp();
			}
			
			// 判断子弹是否击中障碍物
			if(this.isHitBarrier()) {
				this.isAlive = false;
			}

			// 判断子弹是否飞出游戏面板
			// 在方法里实现了死亡
			this.isOverFly();

			try {
				Thread.sleep(40);
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
