package com.game.ver1_0;

import java.awt.Rectangle;
import java.awt.event.*;

// 我的小人
class MyPerson extends Person implements Runnable, KeyListener {
	GamePanel gp; // 指针，指向游戏面板
	private boolean isLeft; // 是否向左
	private boolean isRight; // 是否向右
	private boolean isFalling; // 是否正在下落
	private boolean isStanding; // 是否正在站在障碍物上
	private boolean isJumping; // 是否正在跳跃
	private int jumpTime_now; // 当前跳跃时间
	private int jumpTime_sum; // 总跳跃时间

	public MyPerson(int x, int y) {
		isFalling = true; // 设置默认为下落
		jumpTime_now = 0;  //当前跳跃时间设为0
		jumpTime_sum = 6; // 总跳跃时间设为6
		this.setX(x);
		this.setY(y);
		this.setWidth(20);
		this.setHeight(20);
		this.setLeftSpeed(Recorder.leftSpeed_mp);
		this.setRightSpeed(Recorder.rightSpeed_mp);
		this.setDirection("right");
	}

	public void keyPressed(KeyEvent e) {
		if ((e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)) {
			this.isLeft = true;
			this.setDirection("left");
		} else if ((e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)) {
			this.isRight = true;
			this.setDirection("right");
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			// 按下跳跃键
			this.isJumping = true;
			this.isFalling = false;
			this.isStanding = false;
			
		}
	}

	public void keyReleased(KeyEvent e) {
		if ((e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)) {
			this.isLeft = false;
		} else if ((e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)) {
			this.isRight = false;
		}
	}

	public void keyTyped(KeyEvent arg0) {

	}

	// 移动
	// 跳跃和下落过程移动要考虑是否碰到障碍物侧面
	// 在跳跃和下落过程移动的条件下，在障碍物侧面设置小矩形，如果检测到与小矩形碰撞，设置速度为0，
	// 防止出现与障碍物侧面碰撞导致卡在障碍物侧面的情况
	public void move() {
		if (isLeft) {
			this.setLeftSpeed(Recorder.leftSpeed_mp); // 每次都恢复速度

			// 如果正在下落或正在跳跃
			if (isFalling || isJumping) {
				// 如果移动方向向左
				if (this.getDirection().equals("left")) {
					// 与障碍物右边小矩形检测碰撞
					// 创建我的矩形
					Rectangle myRtg = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());

					// 创建障碍物侧面小矩形并判断
					for (int i = 0; i < gp.map.getNumber(); i++) {

						// 取出障碍物
						Barrier br = gp.map.getVt_Barrier().get(i);

						// 创建
						Rectangle brRtg = new Rectangle(br.getX() + br.getWidth(), br.getY(), 2, br.getHeight());

						// 判断
						if (myRtg.intersects(brRtg)) {
							// 如果碰撞,对应的速度设为0
							this.setLeftSpeed(0);
						}
					}
				}
			}
			this.moveLeft();
		} else if (isRight) {
			this.setRightSpeed(Recorder.rightSpeed_mp); // 每次都恢复速度

			// 如果正在下落或正在跳跃
			if (isFalling || isJumping) {
				// 如果移动方向向右
				if (this.getDirection().equals("right")) {
					// 与障碍物左边小矩形检测碰撞
					// 创建我的矩形
					Rectangle myRtg = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());

					// 创建障碍物侧面小矩形并判断
					for (int i = 0; i < gp.map.getNumber(); i++) {

						// 取出障碍物
						Barrier br = gp.map.getVt_Barrier().get(i);

						// 创建
						Rectangle brRtg = new Rectangle(br.getX() - 2, br.getY(), 2, br.getHeight());

						// 判断
						if (myRtg.intersects(brRtg)) {
							// 如果碰撞,对应的速度设为0
							this.setRightSpeed(0);
						}
					}
				}
			}
			this.moveRight();
		}
	}

	// 下落
	public void fall() {
		if (isFalling) {
			this.moveDown();
		}
	}

	// 跳跃
	public void jump() {
		if (isJumping && (this.jumpTime_now < this.jumpTime_sum)) {
			
			this.moveUp();
			this.jumpTime_now++;
		} else if (isJumping && (this.jumpTime_now == this.jumpTime_sum)) {
			// 跳跃结束
			this.isJumping = false;
			this.isFalling = true;
			this.jumpTime_now = 0; // 恢复当前跳跃时间
		}
	}

	// 判断我是否与地图障碍物碰撞
	// 根据我的小人和地图障碍物创建Rectangle对象
	// 借用Rectangle类的intersects()
	public boolean isCollision_myPerson() {
		boolean isCollision = false;

		// 创建我的小人对应的rectangle
		Rectangle myRtg = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());

		// 遍历与障碍物测试碰撞
		for (int i = 0; i < gp.map.getNumber(); i++) {
			// 取出障碍物
			Barrier barrier = gp.map.getVt_Barrier().get(i);

			// 创建障碍物对应的rectangle
			Rectangle brRtg = new Rectangle(barrier.getX(), barrier.getY(), barrier.getWidth(), barrier.getHeight());

			// 判断碰撞
			if (myRtg.intersects(brRtg)) {
				// 如果碰撞
				isCollision = true;
				break;
			}
		}

		return isCollision;
	}

	// 在跳跃过程中使用
	// 判断我是否与障碍物下面小矩形碰撞
	public boolean isCollision_jump() {
		boolean isCollision_jump = false;
		// 创建我的小人对应的rectangle
		Rectangle myRtg = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		
		// 遍历与障碍物测试碰撞
				for (int i = 0; i < gp.map.getNumber(); i++) {
					// 取出障碍物
					Barrier barrier = gp.map.getVt_Barrier().get(i);

					// 创建障碍物下面对应的rectangle
					Rectangle brRtg = new Rectangle(barrier.getX(), barrier.getY() + barrier.getHeight(), 
							barrier.getWidth(), 5);

					// 判断碰撞
					if (myRtg.intersects(brRtg)) {
						// 如果碰撞
						isCollision_jump = true;
						break;
					}
				}
				return isCollision_jump;
	}
	
	public void run() {
		while (true) {
			this.move();

			// 判断是否正在下落
			if (isFalling) {

				// 判断是否碰撞
				// 如果不碰撞，
				if (!this.isCollision_myPerson()) {
					this.fall();
				} else {
					// 如果碰撞,isFalling设为false,isStanding设为true;
					this.isFalling = false;
					this.isStanding = true;
				}
			}

			// 判断是否站在障碍物上
			if (isStanding) {

				// 判断是否碰撞
				// 如果不碰撞, 开始往下落
				if (!this.isCollision_myPerson()) {
					isFalling = true;
				}
			}
			
			// 判断是否正在跳跃
			if(isJumping) {
				// 判断是否碰到障碍物下面小矩形
				// 如果碰到
				if(this.isCollision_jump()) {
					isJumping = false;
					isFalling = true;
				} else {
					this.jump();
				}
				
			}

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}