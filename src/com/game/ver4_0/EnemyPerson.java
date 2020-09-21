package com.game.ver4_0;

import java.awt.Rectangle;
import java.util.Vector;

// 敌人小人（0为普通敌人 1为走路速度加快 2为开枪速度加快 3为BOSS级）
class EnemyPerson extends Person implements Runnable {
	private boolean isSmartMove; // 是否智能移动


	private CheckBullet cb; // 检测子弹
	private boolean isInLine; // 判断敌人小人和我的小人是否在一条直线， 没有障碍物挡住
	private int shootTime; // 射击时间, 时间归零即射击子弹
	private int sumShootTime; // 总射击时间
	private double shootTimeSpeed; // 射击时间减少速度
	private static int shootConstant = 1; // 射击常数，常数越大，射击时间减少速度越小，即越慢开枪

	// offset 偏移量 使敌人的走路，射击等变化，产生多种敌人
	private int offset_walkPaces; // 走路步数偏移量
	private int offset_shootTimeSpeed; // 射击时间速度偏移量

	private int type; // 敌人种类 （0为普通敌人 1为走路速度加快 2为开枪速度加快 3为看不见射击倒计时 4为BOSS级）
	

	public EnemyPerson(int x, int y, int type, int weaponType, int bulletNumber, GamePanel gp) {

		// 敌人小人获取面板指针,取出障碍物信息，与障碍物判断碰撞，进一步传给武器，子弹，子弹需要判断是否与敌人，障碍物碰撞
		this.setGp(gp);

		this.type = type; // 设置敌人种类
		this.setWeaponType(weaponType); // 设置武器类型
		this.setBulletNumber(bulletNumber); // 设置武器的子弹数量

		this.initOffset(); // 初始化偏移量(调整敌人类型)
		
		this.initWeapon(); // 创建武器

		this.setFalling(true); // 设置默认为下落
		this.setJumpTime_now(0); // 当前跳跃时间设为0
		this.setJumpTime_sum(7);; // 总跳跃时间

		this.setJumpCount_now(0); // 当前跳跃次数为0
		this.setJumpCount_sum(2); // 总跳跃次数为2

		isInLine = false; // 设置是否一条直线

		sumShootTime = 130; // 初始化总射击时间
		shootTime = sumShootTime; // 初始化射击时间
		shootTimeSpeed = 4; // 射击时间减少速度

		this.setAlive(true); // 设置是否活着

		this.setAim(false); // 设置是否为瞄准状态

		this.setAttack(false); // 设置是否为攻击状态

		this.setHasWeapon(false); // 设置是否有武器

		
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
		this.setJumpPaces(5);

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

			for (int i = 0; i < this.getLeftPaces() + this.offset_walkPaces; i++) {
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

				// 实时更新武器坐标，与小人保持相对位置
				this.updateWeaponXY();
			}

		} else if (this.isRight()) {
			this.setRightSpeed(1); // 每次恢复向左速度

			for (int i = 0; i < this.getRightPaces() + this.offset_walkPaces; i++) {

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

				// 实时更新武器坐标，与小人保持相对位置
				this.updateWeaponXY();

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
		if (this.getJumpTime_now() < this.getJumpTime_sum() * Person.jumpSlowConstant * this.getJumpCount_now()) {
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

			this.setJumpTime_now(this.getJumpTime_now() + 1);
		} else if (this.getJumpTime_now() >= this.getJumpTime_sum() * Person.jumpSlowConstant * this.getJumpCount_now()) {
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

		if (this.getY() - 67 - (int) (Math.random() * 10) > this.getGp().mp.getY()) {
			// 跳跃次数少于等于总跳跃时间时才能跳跃
			if (this.getJumpCount_now() < this.getJumpCount_sum()) {
				this.setJumpCount_now(this.getJumpCount_now() + 1);
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
			int sumShootTimeSpeed = (int) Math
					.ceil((this.shootTimeSpeed + this.offset_shootTimeSpeed) / EnemyPerson.shootConstant);
			this.shootTime -= sumShootTimeSpeed;
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
			if (this.getX() - 11 < mp.getX() + mp.getWidth() && this.getX() - 11 > mp.getX()) {
				this.setAttack(true);
			}
		} else {
			if (this.getX() + this.getWidth() > mp.getX() && this.getX() + this.getWidth() < mp.getX() + mp.getWidth()) {
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

	// 掉落武器方法
	// 敌人小人的武器放进面板的武器向量,去掉敌人小人的武器
	public void dropWeapon() {
		Weapon weapon = this.getWeapon(); // 取出武器
		Thread t1; // 声明一个武器线程
		
		// 武器传向游戏面板的武器向量 
		weapon.setPicked(false);
		this.getGp().vt_wp.add(weapon);
		t1 = new Thread(weapon);
		t1.start();
		
		// 判断武器类型,调整武器宽高
		if (weapon.getWeaponType().equals("handgun")) {
			weapon.setWidth(18);
			weapon.setHeight(12);
		} else if (weapon.getWeaponType().equals("shotgun")) {
			weapon.setWidth(23);
			weapon.setHeight(8);
		}
		
		this.setWeapon(new Weapon(1, 1, 1, 1, "hand", 0, this.getGp()));
	}

	// 初始化偏移量方法
	public void initOffset() {
		// 对应敌人种类，设置偏移量的不同
		// 1为走路加快
		if (type == 1) {
			offset_walkPaces = 1;
			offset_shootTimeSpeed = 0;
		} else if (type == 2) {
			// 2为开枪速度加快
			offset_walkPaces = 0;
			offset_shootTimeSpeed = 2;
		} else if(type==4) 
		{
			offset_walkPaces = 1;
			offset_shootTimeSpeed = 2;
		}
	}
	
	//  初始化武器
	public void initWeapon() {
		Weapon weapon = null; // 声明武器
		
		// 如果武器类型为1，是手枪
		if (this.getWeaponType() == 1) {
			weapon = new Weapon(this.getX() + this.getWidth() - 13, this.getY() + 11, 26, 8, "handgun",
					this.getBulletNumber(), this.getGp());
		} else if (this.getWeaponType() == 2) {
			weapon = new Weapon(this.getX() + this.getWidth() - 13, this.getY() + 11, 33, 8, "shotgun",
					this.getBulletNumber(), this.getGp());
		}

		this.setWeapon(weapon);
		this.setHasWeapon(true);
	}

	// 智能跳跃方法（boss级敌人或某些强力敌人可以使用
	// 敌人检测我的子弹与自身的距离，检测到将要被击中的时候跳跃躲避
	public void smartJump() {

		// 取出我的小人的子弹向量
		Vector<Bullet> vt_bullet = this.getGp().mp.getWeapon().getVt_bullet();

		for (int i = 0; i < vt_bullet.size(); i++) {
			// 取出子弹
			Bullet bullet = vt_bullet.get(i);

			// 计算距离判断是否跳跃
			// 如果距离少于140且子弹不在敌人小人头上，在总跳跃次数内，跳跃
			if (bullet.getY() >= (this.getY() + 3) && Math.sqrt(Math.pow(this.getX() + 17 - bullet.getX(), 2)
					+ Math.pow(this.getY() + 10 - bullet.getY(), 2)) < 140) {
				if (this.getJumpCount_now() < this.getJumpCount_sum()) {
					this.setJumpCount_now(this.getJumpCount_now() + 1);
					this.setJumping(true);
					this.setStanding(false);
					this.setFalling(false);
				}
				break;
			}

		}
	}

	// 智能移动方法（boss级敌人或某些强力敌人可以使用
	// 敌人检测我的子弹与自身的距离，检测到将要被击中的时候移动
	public void smartMove() {
		isSmartMove = false; // 一开始是否智能移动设为false

		// 取出我的小人的子弹向量
		Vector<Bullet> vt_bullet = this.getGp().mp.getWeapon().getVt_bullet();

		for (int i = 0; i < vt_bullet.size(); i++) {
			// 取出子弹
			Bullet bullet = vt_bullet.get(i);

			// 计算距离判断是否移动
			// 如果距离少于250，判断子弹来的方向，反向移动
			if (Math.sqrt(Math.pow(this.getX() + 17 - bullet.getX(), 2)
					+ Math.pow(this.getY() + 10 - bullet.getY(), 2)) < 150) {

				isSmartMove = true;

				if (this.getX() > bullet.getX()) {
					// 如果从左侧来,向右走
					this.setRight(true);
					this.setLeft(false);
					this.setDirection("right");
				} else {
					// 否则
					this.setLeft(true);
					this.setRight(false);
					this.setDirection("left");
				}
				break;
			}

		}
	}

	public void run() {
		while (true) {

			// 如果死亡
			// 结束跳跃状态，开始下落状态，掉落武器
			if (!this.isAlive()) {
				this.setJumping(false);
				this.setFalling(true);
				this.dropWeapon();
			}
			
			// 判断是否结束线程
			// 如果已经死亡，且下落到障碍物上，子弹都死亡, 敌人线程死亡
			if (!this.isAlive() && this.isStanding()) {
				for (int i = 0; i < this.getWeapon().getVt_bullet().size(); i++) {
					Bullet bullet = this.getWeapon().getVt_bullet().get(i); // 取出子弹

					bullet.setAlive(false);
					
				}

				break;
			}
			
			// 测试用，增加难度方法
			// 智能移动
//			this.smartMove();
//			
//			if (type == 4 || (((int) (Math.random() * 40) == 0) && this.getGp().mp.isBulletTime())
//					|| (((int) (Math.random() * 20) == 0) && !this.getGp().mp.isBulletTime())) {
//				System.out.println("进来");
//				
//				// 智能跳跃
//				this.smartJump();
//				
//			}
			

			
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

					this.setJumpTime_now(0); // 跳跃时间恢复为0
					this.setJumpCount_now(0); // 恢复跳跃次数

					// 将障碍物Y坐标对应放进小人Y坐标
					this.setY(this.getCollisionY() - this.getHeight() + 1);

				}
			}

			

			// 如果敌人活着才进行下列逻辑判断
			if (this.isAlive()) {

				// 测试用，增加难度方法
				if (type == 4) {
					// 智能跳跃
					// 智能移动
					this.smartJump();
					this.smartMove();
				}
				
				
				// 判断敌人是否掉落出地图
				// 如果掉落出地图，敌人死亡
				this.isOverFall();


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
				
				// 如果不在瞄准状态，智能移动状态，向我的小人移动
				if (!this.isSmartMove) {
					this.moveToMe();
				}

				// 进入瞄准状态
				// 如果在一条直线, 进入瞄准状态，站在障碍物上且不在智能移动状态时不走路
				// 如果不在一条直线，离开瞄准状态，恢复少量射击时间
				if (this.isInLine) {
					this.intoAim();

					if (this.isStanding() && !this.isSmartMove) {
						this.setLeft(false);
						this.setRight(false);
					}
				} else {
					this.setAim(false);
					this.shootTime += 10;
					// 不能超过射击时间上限
					if(this.shootTime > sumShootTime) {
						shootTime = sumShootTime;
					}
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
						this.shootTime = sumShootTime;
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
				if (this.isJumping() && this.getJumpCount_now() <= this.getJumpCount_sum()) {
					this.jump();
				}

				// 判断是否攻击和到达对应的攻击图片时间(到达对应的攻击图片才攻击 )
				if (this.isAttack() && this.getImgId_attack() == 3) {
					this.attack();
				}
			}

			try {
				Thread.sleep(30);
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getOffset_walkPaces() {
		return offset_walkPaces;
	}

	public void setOffset_walkPaces(int offset_walkPaces) {
		this.offset_walkPaces = offset_walkPaces;
	}

	public int getOffset_shootTimeSpeed() {
		return offset_shootTimeSpeed;
	}

	public void setOffset_shootTimeSpeed(int offset_shootTimeSpeed) {
		this.offset_shootTimeSpeed = offset_shootTimeSpeed;
	}
}
