package com.game.ver4_0;

import java.awt.Rectangle;
import java.util.Vector;

// 武器类
// 将武器设为线程，在武器掉落时启动线程，掉落到障碍物上时结束线程
class Weapon implements Runnable {
	private int x;
	private int y;
	private int width;
	private int height;
	private String weaponType; // 武器类型
	private Vector<Bullet> vt_bullet; // 子弹向量
	private int bulletNumber; // 子弹向量
	private GamePanel gp; // 游戏面板指针
	private boolean isPicked; // 是否被捡起
	private int fallSpeed; // 下落速度
	private boolean isStanding; // 是否站在障碍物上
	private int collisionBarrierY; // 碰撞障碍物的y坐标
	private int eachFireBulletNumber; // 每次射击子弹数

	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param weaponType
	 * @param bulletNumber
	 * @param gp
	 */
	public Weapon(int x, int y, int width,int height, String weaponType,  int bulletNumber, GamePanel gp) {
		vt_bullet = new Vector<Bullet>();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.weaponType = weaponType;
		this.bulletNumber = bulletNumber;
		this.gp = gp;
		this.isStanding = false; // 默认没有站在地上
		this.fallSpeed = 2; // 设置下落速度
		
		if(weaponType.equals("handgun")) {
			// 如果是手枪，每次射出一颗子弹
			this.eachFireBulletNumber = 1; 
		} else if (weaponType.equals("shotgun")) {
			// 如果是霰弹，每次射出三颗子弹
			this.eachFireBulletNumber = 3;
		}
	}
	
	// 开火
	// 武器射出子弹
	// 手枪一次一颗，霰弹一次最多三颗
	public void fire(int goalX, int goalY, int belongto) {
		if (this.bulletNumber != 0) {
			// 先判断是否有子弹
			for (int i = 0; i < this.eachFireBulletNumber && this.bulletNumber != 0; i++) {
				// 如果有子弹，创建子弹
				Bullet bullet = new Bullet(x, y, goalX + (int) (Math.random() * 100 - 50),
						goalY + (int) (Math.random() * 100 - 50), 4, 4, gp, belongto);
				Thread t1 = new Thread(bullet);
				t1.start();
				// 放进子弹向量
				vt_bullet.add(bullet);

				bulletNumber--; // 子弹减少
			}
		} else {
			System.out.println("没子弹");
		}
	}
	
	
	//  下落
	public void fall() {
		this.y += this.fallSpeed;
	}
	
	// 检测碰撞方法
	public boolean isCollision() {
		boolean isCollision = false;

		// 创建武器对应的rectangle
		Rectangle wpRtg = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());

		// 遍历与障碍物测试碰撞
		for (int i = 0; i < gp.map.getNumber(); i++) {
			// 取出障碍物
			Barrier barrier = gp.map.getVt_Barrier().get(i);

			// 创建障碍物对应的rectangle
			Rectangle brRtg = new Rectangle(barrier.getX(), barrier.getY(), barrier.getWidth(), barrier.getHeight());

			// 判断碰撞
			if (wpRtg.intersects(brRtg)) {
				// 如果碰撞
				// 得到对应障碍物的坐标
				this.collisionBarrierY = (int) brRtg.getY();
				isCollision = true;
				break;
			}
		}

		return isCollision;
	}
	
	public void run() {
		while(true) {
			
			// 如果站在障碍物上或被捡起，线程结束
			if(this.isStanding || this.isPicked) {
				break;
			}
			
			// 判断是否碰撞障碍物
			// 如果没碰撞，下落
			if(!this.isCollision()) {
				this.fall();
			} else {
				// 如果碰撞障碍物
				// 则站在障碍物上
				this.isStanding = true;
				// 将碰撞障碍物的y坐标传给武器，画出武器不会卡进障碍物
				this.y = this.collisionBarrierY - this.height + 1;
			}
			
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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

	public boolean isPicked() {
		return isPicked;
	}

	public void setPicked(boolean isPicked) {
		this.isPicked = isPicked;
	}

	
}
