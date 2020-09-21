package com.game.ver3_0;

import java.awt.Rectangle;

// 敌人小人
class EnemyPerson extends Person implements Runnable {

	private int jumpTime_now; // 当前跳跃时间
	private int jumpTime_sum; // 总跳跃时间
	private int jumpCount_now; // 当前跳跃次数
	private int jumpCount_sum; // 总跳跃次数
	 
	private CheckBullet cb; // 检测子弹
	private boolean isInLine; // 判断敌人小人和我的小人是否在一条直线， 没有障碍物挡住
	private int shootTime; // 射击时间, 时间归零即射击子弹
	private int shootTimeSpeed; // 射击时间减少速度
	private static int shootConstant = 1; // 射击常数，常数越大，射击时间减少速度越慢

	public EnemyPerson(int x, int y, GamePanel gp) {

		// 敌人小人获取面板指针,取出障碍物信息，与障碍物判断碰撞，进一步传给武器，子弹，子弹需要判断是否与敌人，障碍物碰撞
		this.setGp(gp);

		this.setFalling(true); // 设置默认为下落
		jumpTime_now = 0; // 当前跳跃时间设为0
		jumpTime_sum = 8; // 总跳跃时间
		
		jumpCount_now = 0; // 当前跳跃次数
		jumpCount_sum = 2; // 总跳跃次数

		isInLine = false; // 设置是否一条直线

		shootTime = 100; // 初始化射击时间为100
		shootTimeSpeed = 4; // 射击时间减少速度

		this.setAlive(true); // 设置是否活着

		this.setAim(false); // 设置是否为瞄准状态

		this.setAttack(false); // 设置是否为攻击状态

		this.setHasWeapon(false); // 设置是否有武器

		// 测试
		// 创建武器
		Weapon weapon = new HandGun(x + this.getWidth() - 13, y + 11, 26, 8, "handgun", (int) (Math.random() * 5), gp);
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
						// 创建敌人矩形
						Rectangle epRtg = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());

						// 创建障碍物侧面小矩形并判断
						for (int j = 0; j < gp.map.getNumber(); j++) {

							// 取出障碍物
							Barrier br = gp.map.getVt_Barrier().get(j);

							// 创建侧面小矩形
							Rectangle brRtg = new Rectangle(br.getX() + br.getWidth(), br.getY(), 2, br.getHeight());

							// 判断
							if (epRtg.intersects(brRtg)) {
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
						// 创建敌人矩形
						Rectangle epRtg = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());

						// 创建障碍物侧面小矩形并判断
						for (int j = 0; j < gp.map.getNumber(); j++) {

							// 取出障碍物
							Barrier br = gp.map.getVt_Barrier().get(j);

							// 创建
							Rectangle brRtg = new Rectangle(br.getX() - 2, br.getY(), 2, br.getHeight());

							// 判断
							if (epRtg.intersects(brRtg)) {
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
					this.jumpTime_now = 0; // 恢复当前跳跃时间
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
	// 1.在敌人小人面前创建一个出拳攻击对象
	// 2.攻击对象与我的小人判断是否碰撞
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

		// 与我的小人判断
		// 如果碰撞
		// 我的小人死亡
		if (atk.isHit(gp.mp)) {
			gp.mp.setAlive(false);
		}

	}

	// 射击
	// 武器射出子弹
	// 将鼠标指的x, y传入，用于子弹飞行
	public void shoot(int goalX, int goalY) {
		this.getWeapon().fire(goalX, goalY, 1);
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

	// 敌人向我靠近方法
	public void moveToMe() {
		if (this.getX() > this.getGp().mp.getX() + this.getGp().mp.getWidth()) {
			this.setLeft(true);
			this.setRight(false);
			this.setDirection("left");
		} else if (this.getX() + this.getWidth() < this.getGp().mp.getX()) {
			this.setLeft(false);
			this.setRight(true);
			this.setDirection("right");
		}

		if (this.getY() - 90 - (int)(Math.random() * 45) > this.getGp().mp.getY()) {
			// 跳跃次数少于等于总跳跃时间时才能跳跃
			if (this.jumpCount_now < this.jumpCount_sum) {
				this.jumpCount_now++;
				this.setJumping(true);
				this.setStanding(false);
				this.setFalling(false);
			}
		}
	}

	// 敌人进入瞄准状态
	// 获取我的小人坐标，用于游戏面板画武器
	// 判断敌人小人和我的小人坐标相对位置，转换面对方向
	public void intoAim() {
		// 取出我的小人
		MyPerson mp = this.getGp().mp;

		// 获取我的坐标
		this.goalX = mp.getX();
		this.goalY = mp.getY();

		// 如果x坐标大于我的小人x坐标，面向左
		if (this.getX() > mp.getX()) {
			this.setDirection("left");
		} else {
			// 否则面向右
			this.setDirection("right");
		}

		this.setAim(true);
	}

	// 判断敌人小人是否进入瞄准状态
	// 思路
	// 敌人小人和我的小人之间的直线没有障碍物挡住，此时射出子弹不会被挡住，即可以进入瞄准状态
	// 让敌人小人发射出检测是否碰撞的子弹，若没有碰撞到障碍物，即可以进入瞄准状态
	public void isIntoAim() {
		// 取出我的小人
		MyPerson mp = this.getGp().mp;
		// 创建检测子弹检测是否击中我的小人,敌人小人的isInLine已经传入子弹，如果isInLine = true; 即可以进入瞄准状态
		cb = new CheckBullet(this.getX() + 17, this.getY() + 25, mp.getX() + 17, mp.getY() + 25, 3, 3, this.getGp(),
				this);
		Thread t1 = new Thread(cb);
		t1.start();

	}

	// 射击倒计时方法
	// 射击时间归零后射击
	public void shootCountdown() {
		if (shootTime != 0) {
			this.shootTime -= this.shootTimeSpeed / EnemyPerson.shootConstant;
		}
	}

	// 敌人出拳攻击方法
	// 当敌人没有子弹，离我的小人距离较近时，进入攻击状态
	public void attackMe() {
		// 取出我的小人
		MyPerson mp = this.getGp().mp;

		// 先判断敌人小人方向
		// 如果是左
		if (this.getDirection().equals("left")) {
			if (this.getX() - 11 > mp.getX() + mp.getWidth() && this.getX() - 11 < mp.getX() + mp.getWidth() + 5) {
				this.setAttack(true);
			}
		} else {
			if (this.getX() + this.getWidth() + 11 > mp.getX() - 5 && this.getX() + this.getWidth() + 11 < mp.getX()) {
				this.setAttack(true);
			}
		}
	}

	// 判断敌人是否掉落出地图
	// 如果掉落出地图，敌人死亡
	public void isOverFall() {
		if (this.getY() > 700) {
			this.setAlive(false);
		}
	}

	public void run() {
		while (true) {
		
			// 如果死亡
			// 结束跳跃状态，开始下落状态
			if (!this.isAlive()) {
				this.setJumping(false);
				this.setFalling(true);
			}

			// 判断是否死亡
			// 如果已经死亡，且下落到障碍物上，子弹都死亡，敌人线程死亡
			if (!this.isAlive() && this.isStanding()) {
				for (int i = 0; i < this.getWeapon().getVt_bullet().size(); i++) {
					Bullet bullet = this.getWeapon().getVt_bullet().get(i); // 取出子弹

					bullet.setAlive(false);
				}
				
				break;
			}
			
			// 判断是否正在下落
			// 活着或死亡都可以下落，死亡后下落到障碍物上才算线程死亡
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
					this.jumpCount_now = 0; // 恢复跳跃次数
					
					// 将障碍物Y坐标对应放进小人Y坐标
					this.setY(this.getCollisionY() - this.getHeight() + 1);

				}
			}

			// 如果敌人活着才进行下列逻辑判断
			if (this.isAlive()) {
				
				// 判断敌人是否掉落出地图
				// 如果掉落出地图，敌人死亡
				this.isOverFall();

				// 如果不在瞄准状态,向我的小人靠近
				if (!this.isAim()) {
					this.moveToMe();
				}

				// 如果子弹为0，退出瞄准状态
				if (this.getWeapon().getBulletNumber() == 0) {
					this.setAim(false);
					this.isInLine = false;
				}
				
				// 如果子弹为0，攻击我的小人
				if (this.getWeapon().getBulletNumber() == 0) {
					this.attackMe();
				}

				// 判断是否进入瞄准状态,此方法会改变敌人小人的isInLine，如果是true，即在同一条直线，接下来进入瞄准状态
				// 没有子弹时不判断是否进入瞄准状态
				if (this.getWeapon().getBulletNumber() != 0) {
					this.isIntoAim();
				}

				// 进入瞄准状态
				// 如果在一条直线, 进入瞄准状态，站在障碍物上时不走路
				// 如果不在一条直线，离开瞄准状态
				if (this.isInLine) {
					this.intoAim();
					
					if(this.isStanding()) {
						this.setLeft(false);
						this.setRight(false);
					}
				} else {
					this.setAim(false);
				}

				// 处于瞄准状态，实时更新武器坐标，与小人保持相对位置
				if (this.isAim()) {
					this.updateWeaponXY();
				}

				// 处于瞄准状态,且活着,进入射击倒计时，射击时间到零后射击
				if (this.isAim()) {
					this.shootCountdown();

					if (this.shootTime <= 0) {
						this.shoot(this.getGp().mp.getX(), this.getGp().mp.getY());

						// 射击后恢复射击时间
						this.shootTime = 100;
					}
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

	public static void setShootConstant(int shootConstant) {
		EnemyPerson.shootConstant = shootConstant;
	}

	public int getShootTime() {
		return shootTime;
	}

	public void setShootTimeSpeed(int shootTimeSpeed) {
		this.shootTimeSpeed = shootTimeSpeed;
	}

	public void setInLine(boolean isInLine) {
		this.isInLine = isInLine;
	}

	public boolean getInLine() {
		return isInLine;
	}
}
