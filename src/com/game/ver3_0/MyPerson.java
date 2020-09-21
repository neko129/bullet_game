package com.game.ver3_0;

import java.awt.Rectangle;
import java.awt.event.*;

// 我的小人
class MyPerson extends Person implements Runnable, KeyListener, MouseListener, MouseMotionListener {

	private int jumpTime_now; // 当前跳跃时间
	private int jumpTime_sum; // 总跳跃时间
	 private int jumpCount_now; // 当前跳跃次数
	 private int jumpCount_sum; // 总跳跃次数

	// 构造方法初始化
	public MyPerson(int x, int y, GamePanel gp) {

		// 我的小人获取面板指针,取出障碍物信息，与障碍物判断碰撞，进一步传给武器，子弹，子弹需要判断是否与敌人，障碍物碰撞
		this.setGp(gp);

		this.setFalling(true); // 设置默认为下落
		jumpTime_now = 0; // 当前跳跃时间设为0
		jumpTime_sum = 8; // 总跳跃时间

		jumpCount_now = 0; // 当前跳跃次数为0
		jumpCount_sum = 2; // 总跳跃次数为2

		this.setAlive(true); // 设置是否活着

		this.setAim(false); // 设置是否为瞄准状态

		this.setAttack(false); // 设置是否为攻击状态

		this.setHasWeapon(false); // 设置是否有武器

		this.setSquat(false); // 设置是否下蹲

		// 测试
		// 创建武器
		Weapon weapon = new HandGun(x + this.getWidth() - 13, y + 11, 26, 8, "handgun", 4, gp);
		this.setWeapon(weapon);
		this.setHasWeapon(true);

		this.setX(x);
		this.setY(y);
		this.setWidth(34);
		this.setHeight(50);

		this.setLeftPaces(8);
		this.setRightPaces(8);

		this.setLeftSpeed(1); // move()里面每次都会恢复移动速度
		this.setRightSpeed(1); // 如要修改，还需要修改move()里面的速度

		this.setDirection("right");

		this.setFallSpeed(8);
		this.setJumpSpeed(2);

		this.setFallPaces(8);
		this.setJumpPaces(6);

		this.setImgTime_walk(0); // 设置走路图片时间
		this.setImgId_walk(0); // 设置走路图片ID

		this.setImgTime_attack(0); // 设置攻击图片时间
		this.setImgId_attack(0); // 设置攻击图片ID

		this.setImgTime_die(0); // 设置死亡图片时间
		this.setImgId_die(0); // 设置死亡图片ID
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {

		// 先判断是否活着
		if (this.isAlive()) {

			// 按下鼠标右键，不处于下蹲状态，进入瞄准状态
			if (arg0.getButton() == MouseEvent.BUTTON3 && !this.isSquat()) {
				this.setAim(true);
				System.out.println("进入瞄准状态");

				// 判断小人是否转向
				// 如果鼠标指的坐标在小人坐标左边，转左
				// 如果鼠标指的坐标在小人坐标右边，转右
				if (arg0.getX() < this.getX()) {
					this.setDirection("left");
					this.updateWeaponXY();
				} else if (arg0.getX() > this.getX()) {
					this.setDirection("right");
					this.updateWeaponXY();
				}

				// 记录鼠标所指坐标信息记录
				this.goalX = arg0.getX();
				this.goalY = arg0.getY();

			}

			// 不处于瞄准状态，不处于下蹲状态，点击鼠标左键，出拳攻击
			if (arg0.getButton() == MouseEvent.BUTTON1 && !this.isAim() && !this.isSquat()) {
				// 出拳攻击(到对应的攻击图片时间时判断是否攻击到敌人
				this.setAttack(true);

			} else if (arg0.getButton() == MouseEvent.BUTTON1 && this.isAim() && this.hasWeapon()
					&& this.getWeapon().getBulletNumber() != 0) {
				// 处于瞄准状态，持有武器，且武器有子弹，点击鼠标左键，开枪射出子弹

				System.out.println("shoot!");
				this.shoot(arg0.getX(), arg0.getY());
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

		// 释放鼠标右键，离开瞄准状态
		if (arg0.getButton() == MouseEvent.BUTTON3) {
			this.setAim(false);
			System.out.println("离开瞄准状态");
		}

	}

	public void mouseDragged(MouseEvent arg0) {
		// 按住鼠标右键拖动，将坐标传给面板用于画出可旋转的武器
		// 先判断是否是鼠标右键
		// 判断不了鼠标右键！！
		// 那就判断是否瞄准状态吧。。。
		// 先判断是否活着
		if (this.isAlive()) {
			if (this.isAim()) {
				
				// 判断小人是否转向
				// 如果鼠标指的坐标在小人坐标左边，转左
				// 如果鼠标指的坐标在小人坐标右边，转右
				if (arg0.getX() < this.getX()) {
					this.setDirection("left");
					this.updateWeaponXY();
				} else if (arg0.getX() > this.getX()) {
					this.setDirection("right");
					this.updateWeaponXY();
				}

				// 记录鼠标所指坐标信息记录
				this.goalX = arg0.getX();
				this.goalY = arg0.getY();
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {

	}

	// 键按下
	// 处于下蹲状态时，不可移动，不可跳跃
	public void keyPressed(KeyEvent e) {
		// 先判断是否活着
		if (this.isAlive()) {
			// 再判断是否处于下蹲状态,不处于下蹲状态才进行下列操作 
			if (!this.isSquat()) {
				
				if ((e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)) {
					// 向左移动
					this.setLeft(true);
					this.setRight(false);

					if (!this.isAim()) {
						// 在瞄准状态，向左走不会面向左，由鼠标位置决定面向哪，所以不处于瞄准状态才改变面向方向
						this.setDirection("left");
					}
				} else if ((e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)) {
					// 向右移动
					this.setRight(true);
					this.setLeft(false);

					if (!this.isAim()) {
						this.setDirection("right");
					}
				} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					// 跳跃
					// 跳跃次数少于等于总跳跃时间时才能跳跃
					if(this.jumpCount_now < this.jumpCount_sum) {
						this.jumpCount_now++;
						this.setJumping(true);
						this.setFalling(false);
						this.setStanding(false);
					}
				}
			}
			
			if (e.getKeyCode() == KeyEvent.VK_S) {
				// 下蹲
				// 判断如果不是下蹲状态时，设置为下蹲状态,调用下蹲方法，改变小人大小和坐标，结束瞄准状态
				if (!this.isSquat()) {
					this.setSquat(true);
					this.squat();
					this.setAim(false);
				}
			}
		}
	}

	// 键释放
	public void keyReleased(KeyEvent e) {
		if (this.isAlive()) {
			if ((e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)) {
				this.setLeft(false);
			} else if ((e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)) {
				this.setRight(false);
			} else if (e.getKeyCode() == KeyEvent.VK_S) {
				this.setSquat(false);
				this.squatRecovery();
			}
		}
	}

	public void keyTyped(KeyEvent arg0) {

	}

	// 移动
	// 移动注意事项
	// 跳跃和下落过程移动要考虑是否碰到障碍物侧面
	// 在跳跃和下落条件下移动，在障碍物侧面设置小矩形，如果检测到与小矩形碰撞，设置移动速度为0，
	// 防止出现与障碍物侧面碰撞导致卡在障碍物侧面的情况
	// 移动逻辑
	// 每次移动一个移动速度，设置一个移动步数，移动移动步数次
	// 小人在跳跃或下落与障碍物侧面小矩形碰撞，速度变成0
	// *********
	// 注意在移动方法里每次移动1像素都让武器获取小人坐标
	public void move() {
		// 取出游戏面板
		GamePanel gp = this.getGp();

		if (this.isLeft()) {
			this.setLeftSpeed(1); // 每次恢复向左速度

			for (int i = 0; i < this.getLeftPaces(); i++) {
				// 如果正在下落或正在跳跃
				if (this.isFalling() || this.isJumping()) {
					// 如果移动方向向左
					if (this.isLeft()) {
						// 与障碍物右边小矩形检测碰撞
						// 创建我的矩形
						Rectangle myRtg = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());

						// 创建障碍物侧面小矩形并判断
						for (int j = 0; j < gp.map.getNumber(); j++) {

							// 取出障碍物
							Barrier br = gp.map.getVt_Barrier().get(j);

							// 创建侧面小矩形
							Rectangle brRtg = new Rectangle(br.getX() + br.getWidth(), br.getY(), 2, br.getHeight());

							// 判断
							if (myRtg.intersects(brRtg)) {
								// 如果碰撞,对应的移动速度设为0
								this.setLeftSpeed(0);
							}
						}
					}
				}

				this.moveLeft();

				// 处于瞄准状态，实时更新武器坐标，与小人保持相对位置
				if (this.isAim()) {
					this.updateWeaponXY();
				}
			}

		} else if (this.isRight()) {
			this.setRightSpeed(1); // 每次恢复向左速度

			for (int i = 0; i < this.getRightPaces(); i++) {

				// 如果正在下落或正在跳跃
				if (this.isFalling() || this.isJumping()) {
					// 如果移动方向向右
					if (this.isRight()) {
						// 与障碍物左边小矩形检测碰撞
						// 创建我的矩形
						Rectangle myRtg = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());

						// 创建障碍物侧面小矩形并判断
						for (int j = 0; j < gp.map.getNumber(); j++) {

							// 取出障碍物
							Barrier br = gp.map.getVt_Barrier().get(j);

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

				// 处于瞄准状态，实时更新武器坐标，与小人保持相对位置
				if (this.isAim()) {
					this.updateWeaponXY();
				}
			}
		}
	}

	// 下落
	// *************
	// 下落过程让武器获取小人坐标保证武器在对应位置
	public void fall() {
		this.moveDown();
		this.updateWeaponXY();
	}

	// 跳跃
	// ************
	// 跳跃每步都让武器获取小人坐标保证武器在对应位置
	public void jump() {

		if (this.jumpTime_now < this.jumpTime_sum * Person.jumpSlowConstant * this.jumpCount_now) {
			for (int i = 0; i < this.getJumpPaces(); i++) {

				// 判断是否碰撞到障碍物下面小矩形
				// 如果没碰到
				if (!this.isCollision_jump()) {
					this.moveUp();
					this.updateWeaponXY();
				} else {
					// 如果碰到
					this.setFalling(true);
					this.setJumping(false);
					break;
				}
			}

			this.jumpTime_now++;
		} else if (this.jumpTime_now >= this.jumpTime_sum * Person.jumpSlowConstant * this.jumpCount_now) {
			// 跳跃结束
			this.setJumping(false);
			this.setFalling(true);
		}
	}

	// 出拳攻击
	// 1.在我的小人面前创建一个出拳攻击对象
	// 2.攻击对象与敌人判断是否碰撞
	public void attack() {
		Attack atk = null;

		// 取出游戏面板
		GamePanel gp = this.getGp();

		// 先判断小人方向
		// 如果是右
		if (this.getDirection().equals("right")) {
			// 创建出对应的攻击对象
			atk = new Attack(this.getX() + this.getWidth() - 3, this.getY() + 10, 14, 6);

		} else {
			// 如果是左
			// 创建出对应的攻击对象
			atk = new Attack(this.getX() - 11, this.getY() + 10, 11, 6);
		}

		// 与敌人判断
		for (int i = 0; i < gp.vt_Ep.size(); i++) {
			// 取出敌人小人
			EnemyPerson ep = gp.vt_Ep.get(i);

			// 开始判断
			// 如果碰撞
			if (atk.isHit(ep)) {
				ep.setAlive(false);
			}
		}
	}

	// 射击
	// 武器射出子弹
	// 将鼠标指的x, y传入，用于子弹飞行
	public void shoot(int goalX, int goalY) {
		this.getWeapon().fire(goalX, goalY, 0);
	}

	// 下蹲
	// 我的小人的大小改变，坐标位置改变
	public void squat() {
		this.setY(this.getY() + 16);
		this.setHeight(this.getWidth());
	}

	// 下蹲恢复
	// 恢复 我的小人的大小，坐标位置
	public void squatRecovery() {
		this.setY(this.getY() - 16);
		this.setHeight(50);
	}

	// 更新武器的坐标，保证武器和小人保持相对位置
	public void updateWeaponXY() {
		// 判断小人状态，不同状态武器坐标不同
		// 如果面向右
		if (this.getDirection().equals("right")) {
			// 如果站立状态
			if (this.isStanding() && !this.isLeft() && !this.isRight()) {
				this.getWeapon().setX(this.getX() + this.getWidth() - 13);
				this.getWeapon().setY(this.getY() + 11);
			} else if (this.isStanding()) {

				// 如果是走路
				this.getWeapon().setX(this.getX() + this.getWidth() - 6);
				this.getWeapon().setY(this.getY() + 17);

			} else if (this.isJumping()) {

				// 如果是跳跃
				this.getWeapon().setX(this.getX() + this.getWidth() - 13);
				this.getWeapon().setY(this.getY() + 8);
			} else if (this.isFalling()) {

				// 如果是下落
				this.getWeapon().setX(this.getX() + this.getWidth() - 15);
				this.getWeapon().setY(this.getY() + 8);
			}
		}

		if (this.getDirection().equals("left")) {
			// 如果面向左

			// 如果站立状态
			if (this.isStanding() && !this.isLeft() && !this.isRight()) {
				this.getWeapon().setX(this.getX() - 13);
				this.getWeapon().setY(this.getY() + 11);
			} else if (this.isStanding()) {
				// 如果是走路
				this.getWeapon().setX(this.getX() - 26 + 6);
				this.getWeapon().setY(this.getY() + 17);

			} else if (this.isJumping()) {

				// 如果是跳跃
				this.getWeapon().setX(this.getX() + 13 - 26);
				this.getWeapon().setY(this.getY() + 8);
			} else if (this.isFalling()) {

				// 如果是下落
				this.getWeapon().setX(this.getX() + 15 - 26);
				this.getWeapon().setY(this.getY() + 8);
			}
		}
	}

	// 子弹时间方法,我的小人站立不动时进入子弹时间，一切变慢
	public void bulletTime() {
		// 变慢常数增加
		Person.slowConstant = 12;

		// 移动速度变慢
		this.setLeftPaces(1);
		this.setRightPaces(1);

		// 跳跃变慢
		this.setJumpPaces(1);
		Person.jumpSlowConstant = 6;

		// 下落变慢
		this.setFallSpeed(1);

		// 子弹飞行变慢
		Bullet.speedP = 1;

		// 敌人射击倒计时变慢
		EnemyPerson.setShootConstant(4);
	}

	// 结束子弹时间
	public void endBulletTime() {
		// 变慢常数恢复
		Person.slowConstant = 1;

		// 移动速度恢复
		this.setLeftPaces(8);
		this.setRightPaces(8);

		// 跳跃恢复
		this.setJumpPaces(6);
		Person.jumpSlowConstant = 1;

		// 下落恢复
		this.setFallSpeed(8);

		// 子弹飞行恢复
		Bullet.speedP = 16;

		// 敌人射击倒计时恢复
		EnemyPerson.setShootConstant(1);
	}

	public void run() {
		while (true) {

			
			// 如果死亡
			// 结束子弹时间，结束跳跃状态，开始下落状态
			if (!this.isAlive()) {
				this.endBulletTime();
				this.setJumping(false);
				this.setFalling(true);
			}
			
			
			// 如果死亡且落到障碍物上
			// 结束线程
			if (!this.isAlive() && this.isStanding()) {
				break;
			}

		

			// 判断是否正在下落
			if (this.isFalling()) {

				// 判断是否碰撞
				// 如果不碰撞，
				if (!this.isCollision_Person(this.getCollisionY())) {
					this.fall();
				} else {
					// 如果碰撞,isFalling设为false,isStanding设为true;
					this.setFalling(false);
					this.setStanding(true);
					this.jumpTime_now = 0; // 跳跃时间恢复为0
					this.jumpCount_now = 0; // 跳跃次数恢复为0
					// 将障碍物Y坐标对应放进小人Y坐标
					this.setY(this.getCollisionY() - this.getHeight() + 1);

				}
			}

			// 判断是否活着，活着才进行下列判断
			if (this.isAlive()) {
				// 我的小人站立不动，不在攻击状态,进入子弹时间
				if (this.isStanding() && !this.isLeft() && !this.isRight() && !this.isAttack()) {
					this.bulletTime();
				}

				// 我的小人移动，出拳攻击，结束子弹时间
				if (this.isLeft() || this.isRight() || this.isAttack() || this.isFalling() || this.isJumping()) {
					this.endBulletTime();
				}

				// 处于瞄准状态，实时更新武器坐标，与小人保持相对位置
				if (this.isAim()) {
					this.updateWeaponXY();
				}

				if (!this.isAttack()) {
					this.move();
				}

				// 判断是否站在障碍物上
				if (this.isStanding()) {

					// 判断是否碰撞
					// 如果不碰撞, 开始往下落
					if (!this.isCollision_Person(this.getCollisionY())) {
						this.setFalling(true);
						this.setStanding(false);
					}
				}

				// 判断是否正在跳跃
				// 跳跃最多可二段跳
				if (this.isJumping() && this.jumpCount_now <= this.jumpCount_sum) {
					this.jump();
				}

				// 判断是否攻击和到达对应的攻击图片时间(到达对应的时间才攻击 )
				if (this.isAttack() && (this.getImgTime_attack() == 10 || this.getImgTime_attack() == 11)) {
					this.attack();
				}
			}
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}