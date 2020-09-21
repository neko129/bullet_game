package com.game.ver4_0;

import java.awt.Rectangle;

abstract class Person {
	private int x;
	private int y;
	private int width; // 宽度
	private int height; // 高度

	private static int leftPaces; // 向左移动步数
	private static int rightPaces; // 向右移动步数
	private int leftSpeed; // 向左移动速度
	private int rightSpeed; // 向右移动速度

	private String direction; // 移动方向

	private int fallPaces; // 下落步数
	private static int fallSpeed; // 下落速度

	private static int jumpPaces; // 跳跃步数
	private int jumpSpeed; // 跳跃速度
	
	private int jumpTime_now; // 当前跳跃时间
	private int jumpTime_sum; // 总跳跃时间
	private int jumpCount_now; // 当前跳跃次数
	private int jumpCount_sum; // 总跳跃次数

	private boolean isAlive; // 判断是否活着
	private boolean isAttack; // 判断是否出拳攻击
	private boolean isAim; // 判断是否瞄准，摁住鼠标右键进入瞄准状态，可持枪攻击
	private boolean hasWeapon; // 判断是否有武器，有武器才能射出子弹

	private boolean isLeft; // 是否向左
	private boolean isRight; // 是否向右

	private boolean isFalling; // 是否正在下落
	private boolean isStanding; // 是否正在站在障碍物上
	private boolean isJumping; // 是否正在跳跃
	private boolean isSquat; // 是否下蹲

	private int imgTime_walk; // 走路图片时间
	private int imgId_walk; // 走路图片id，对应第几张图片

	private int imgTime_attack; // 攻击图片时间
	private int imgId_attack; // 攻击图片id

	private int imgTime_die; // 死亡图片时间
	private int imgId_die; // 死亡图片id

	int goalX; // 目标x坐标，一般是子弹飞行所指向的坐标，和画武器转动需要的坐标
	int goalY; // 目标y坐标，一般是子弹飞行所指向的坐标，和画武器转动需要的坐标

	private int collisionY; // 碰撞障碍物的Y坐标，用于小人下落碰撞障碍物时让小人的坐标刚好在障碍物上面，防止卡在障碍物里

	private Weapon weapon; // 武器
	private int weaponType; // 武器类型(0为空手,1为手枪，2为霰弹)
	private int bulletNumber; // 子弹数量

	private GamePanel gp; // 指针，指向游戏面板

	static int slowConstant = 1; // 变慢常数，用于画小人图片，子弹时间下一切变慢，画小人运动也变慢。默认常数为1
	static int jumpSlowConstant = 1; // 跳跃变慢常数,默认为1

	
	// 判断小人是否与地图障碍物碰撞
	// 根据小人和地图障碍物创建Rectangle对象
	// 借用Rectangle类的intersects()
	public boolean isCollision_Person(int collisionY) {
		boolean isCollision = false;

		// 取出游戏面板
		GamePanel gp = this.getGp();

		// 创建小人对应的rectangle
		Rectangle pRtg = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());

		// 遍历与障碍物测试碰撞
		for (int i = 0; i < gp.map.getNumber(); i++) {
			// 取出障碍物
			Barrier barrier = gp.map.getVt_Barrier().get(i);

			// 创建障碍物对应的rectangle
			Rectangle brRtg = new Rectangle(barrier.getX(), barrier.getY(), barrier.getWidth(), barrier.getHeight());

			// 判断碰撞
			if (pRtg.intersects(brRtg)) {
				// 如果碰撞
				// 得到对应障碍物的坐标
				this.collisionY = (int) brRtg.getY();
				isCollision = true;
				break;
			}
		}

		return isCollision;
	}

	// 在跳跃过程中使用
	// 判断小人是否与障碍物下面小矩形碰撞
	public boolean isCollision_jump() {
		boolean isCollision_jump = false;

		// 取出游戏面板
		GamePanel gp = this.getGp();

		// 创建小人对应的rectangle
		Rectangle pRtg = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());

		// 遍历与障碍物测试碰撞
		for (int i = 0; i < gp.map.getNumber(); i++) {
			// 取出障碍物
			Barrier barrier = gp.map.getVt_Barrier().get(i);

			// 创建障碍物下面对应的rectangle
			Rectangle brRtg = new Rectangle(barrier.getX(), barrier.getY() + barrier.getHeight(), barrier.getWidth(),
					2);

			// 判断碰撞
			if (pRtg.intersects(brRtg)) {
				// 如果碰撞
				isCollision_jump = true;
				break;
			}
		}
		return isCollision_jump;
	}

	// 更新武器的坐标，保证武器和小人保持相对位置
	public void updateWeaponXY() {
		// 先判断小人状态，不同状态，坐标不同
		// 面向右武器坐标一致，而面向左武器坐标可从面向右推出，所以不用考虑武器类型
		// 如果面向右(面向右全武器坐标一致)
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

		else if (this.getDirection().equals("left")) {
			// 如果面向左

			// 如果站立状态
			if (this.isStanding() && !this.isLeft() && !this.isRight()) {
				this.getWeapon().setX(this.getX() + 13 - this.weapon.getWidth());
				this.getWeapon().setY(this.getY() + 11);
			} else if (this.isStanding()) {
				// 如果是走路
				this.getWeapon().setX(this.getX() - this.weapon.getWidth() + 6);
				this.getWeapon().setY(this.getY() + 17);

			} else if (this.isJumping()) {

				// 如果是跳跃
				this.getWeapon().setX(this.getX() + 13 - this.weapon.getWidth());
				this.getWeapon().setY(this.getY() + 8);
			} else if (this.isFalling()) {

				// 如果是下落
				this.getWeapon().setX(this.getX() + 15 - this.weapon.getWidth());
				this.getWeapon().setY(this.getY() + 8);
			}

		}

	}

	public boolean isSquat() {
		return isSquat;
	}

	public void setSquat(boolean isSquat) {
		this.isSquat = isSquat;
	}

	public int getImgTime_die() {
		return imgTime_die;
	}

	public void setImgTime_die(int imgTime_die) {
		this.imgTime_die = imgTime_die;
	}

	public void setImgId_die(int imgId_die) {
		this.imgId_die = imgId_die;
	}

	public int getImgId_die() {
		return imgId_die;
	}

	public int getImgTime_walk() {
		return imgTime_walk;
	}

	public void setImgTime_walk(int imgTime_walk) {
		this.imgTime_walk = imgTime_walk;
	}

	public int getImgId_walk() {
		return imgId_walk;
	}

	public void setImgId_walk(int imgId_walk) {
		this.imgId_walk = imgId_walk;
	}

	public int getImgTime_attack() {
		return imgTime_attack;
	}

	public void setImgTime_attack(int imgTime_attack) {
		this.imgTime_attack = imgTime_attack;
	}

	public int getImgId_attack() {
		return imgId_attack;
	}

	public void setImgId_attack(int imgId_attack) {
		this.imgId_attack = imgId_attack;
	}

	public int getCollisionY() {
		return collisionY;
	}

	public void setCollisionY(int collisionY) {
		this.collisionY = collisionY;
	}

	public GamePanel getGp() {
		return gp;
	}

	public void setGp(GamePanel gp) {
		this.gp = gp;
	}

	public boolean isLeft() {
		return isLeft;
	}

	public void setLeft(boolean isLeft) {
		this.isLeft = isLeft;
	}

	public boolean isRight() {
		return isRight;
	}

	public void setRight(boolean isRight) {
		this.isRight = isRight;
	}

	public boolean isFalling() {
		return isFalling;
	}

	public void setFalling(boolean isFalling) {
		this.isFalling = isFalling;
	}

	public boolean isStanding() {
		return isStanding;
	}

	public void setStanding(boolean isStanding) {
		this.isStanding = isStanding;
	}

	public boolean isJumping() {
		return isJumping;
	}

	public void setJumping(boolean isJumping) {
		this.isJumping = isJumping;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

	public boolean hasWeapon() {
		return hasWeapon;
	}

	public void setHasWeapon(boolean hasWeapon) {
		this.hasWeapon = hasWeapon;
	}

	public boolean isAim() {
		return isAim;
	}

	public void setAim(boolean isAim) {
		this.isAim = isAim;
	}

	public boolean isAttack() {
		return isAttack;
	}

	public void setAttack(boolean isAttack) {
		this.isAttack = isAttack;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public int getFallPaces() {
		return fallPaces;
	}

	public void setFallPaces(int fallPaces) {
		this.fallPaces = fallPaces;
	}

	public int getJumpPaces() {
		return jumpPaces;
	}

	public void setJumpPaces(int jumpPaces) {
		Person.jumpPaces = jumpPaces;
	}

	public int getLeftSpeed() {
		return leftSpeed;
	}

	public void setLeftSpeed(int leftSpeed) {
		this.leftSpeed = leftSpeed;
	}

	public int getRightSpeed() {
		return rightSpeed;
	}

	public void setRightSpeed(int rightSpeed) {
		this.rightSpeed = rightSpeed;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String dir) {
		this.direction = dir;
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

	public void setX(int x) {
		this.x = x;
	}

	public int getX() {
		return x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getY() {
		return y;
	}

	public int getLeftPaces() {
		return leftPaces;
	}

	public void setLeftPaces(int leftPaces) {
		Person.leftPaces = leftPaces;
	}

	public int getRightPaces() {
		return rightPaces;
	}

	public void setRightPaces(int rightPaces) {
		Person.rightPaces = rightPaces;
	}

	public void setJumpSpeed(int jumpSpeed) {
		this.jumpSpeed = jumpSpeed;
	}

	public int getJumpSpeed(int jumpSpeed) {
		return jumpSpeed;
	}

	public void setFallSpeed(int fallSpeed) {
		Person.fallSpeed = fallSpeed;
	}

	public int getFallSpeed(int fallSpeed) {
		return fallSpeed;
	}
	
	public int getWeaponType() {
		return weaponType;
	}

	public void setWeaponType(int weaponType) {
		this.weaponType = weaponType;
	}

	public int getJumpTime_now() {
		return jumpTime_now;
	}

	public void setJumpTime_now(int jumpTime_now) {
		this.jumpTime_now = jumpTime_now;
	}

	public int getJumpTime_sum() {
		return jumpTime_sum;
	}

	public void setJumpTime_sum(int jumpTime_sum) {
		this.jumpTime_sum = jumpTime_sum;
	}

	public int getJumpCount_now() {
		return jumpCount_now;
	}

	public void setJumpCount_now(int jumpCount_now) {
		this.jumpCount_now = jumpCount_now;
	}

	public int getJumpCount_sum() {
		return jumpCount_sum;
	}

	public void setJumpCount_sum(int jumpCount_sum) {
		this.jumpCount_sum = jumpCount_sum;
	}

	public int getBulletNumber() {
		return bulletNumber;
	}

	public void setBulletNumber(int bulletNumber) {
		this.bulletNumber = bulletNumber;
	}
	
	public void moveLeft() {
		x -= leftSpeed;
	}

	public void moveRight() {
		x += rightSpeed;
	}

	public void moveDown() {
		y += fallSpeed;
	}

	public void moveUp() {
		y -= jumpSpeed;
	}
}
