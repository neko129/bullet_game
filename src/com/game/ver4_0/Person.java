package com.game.ver4_0;

import java.awt.Rectangle;

abstract class Person {
	private int x;
	private int y;
	private int width; // ���
	private int height; // �߶�

	private static int leftPaces; // �����ƶ�����
	private static int rightPaces; // �����ƶ�����
	private int leftSpeed; // �����ƶ��ٶ�
	private int rightSpeed; // �����ƶ��ٶ�

	private String direction; // �ƶ�����

	private int fallPaces; // ���䲽��
	private static int fallSpeed; // �����ٶ�

	private static int jumpPaces; // ��Ծ����
	private int jumpSpeed; // ��Ծ�ٶ�
	
	private int jumpTime_now; // ��ǰ��Ծʱ��
	private int jumpTime_sum; // ����Ծʱ��
	private int jumpCount_now; // ��ǰ��Ծ����
	private int jumpCount_sum; // ����Ծ����

	private boolean isAlive; // �ж��Ƿ����
	private boolean isAttack; // �ж��Ƿ��ȭ����
	private boolean isAim; // �ж��Ƿ���׼����ס����Ҽ�������׼״̬���ɳ�ǹ����
	private boolean hasWeapon; // �ж��Ƿ�����������������������ӵ�

	private boolean isLeft; // �Ƿ�����
	private boolean isRight; // �Ƿ�����

	private boolean isFalling; // �Ƿ���������
	private boolean isStanding; // �Ƿ�����վ���ϰ�����
	private boolean isJumping; // �Ƿ�������Ծ
	private boolean isSquat; // �Ƿ��¶�

	private int imgTime_walk; // ��·ͼƬʱ��
	private int imgId_walk; // ��·ͼƬid����Ӧ�ڼ���ͼƬ

	private int imgTime_attack; // ����ͼƬʱ��
	private int imgId_attack; // ����ͼƬid

	private int imgTime_die; // ����ͼƬʱ��
	private int imgId_die; // ����ͼƬid

	int goalX; // Ŀ��x���꣬һ�����ӵ�������ָ������꣬�ͻ�����ת����Ҫ������
	int goalY; // Ŀ��y���꣬һ�����ӵ�������ָ������꣬�ͻ�����ת����Ҫ������

	private int collisionY; // ��ײ�ϰ����Y���꣬����С��������ײ�ϰ���ʱ��С�˵�����պ����ϰ������棬��ֹ�����ϰ�����

	private Weapon weapon; // ����
	private int weaponType; // ��������(0Ϊ����,1Ϊ��ǹ��2Ϊ����)
	private int bulletNumber; // �ӵ�����

	private GamePanel gp; // ָ�룬ָ����Ϸ���

	static int slowConstant = 1; // �������������ڻ�С��ͼƬ���ӵ�ʱ����һ�б�������С���˶�Ҳ������Ĭ�ϳ���Ϊ1
	static int jumpSlowConstant = 1; // ��Ծ��������,Ĭ��Ϊ1

	
	// �ж�С���Ƿ����ͼ�ϰ�����ײ
	// ����С�˺͵�ͼ�ϰ��ﴴ��Rectangle����
	// ����Rectangle���intersects()
	public boolean isCollision_Person(int collisionY) {
		boolean isCollision = false;

		// ȡ����Ϸ���
		GamePanel gp = this.getGp();

		// ����С�˶�Ӧ��rectangle
		Rectangle pRtg = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());

		// �������ϰ��������ײ
		for (int i = 0; i < gp.map.getNumber(); i++) {
			// ȡ���ϰ���
			Barrier barrier = gp.map.getVt_Barrier().get(i);

			// �����ϰ����Ӧ��rectangle
			Rectangle brRtg = new Rectangle(barrier.getX(), barrier.getY(), barrier.getWidth(), barrier.getHeight());

			// �ж���ײ
			if (pRtg.intersects(brRtg)) {
				// �����ײ
				// �õ���Ӧ�ϰ��������
				this.collisionY = (int) brRtg.getY();
				isCollision = true;
				break;
			}
		}

		return isCollision;
	}

	// ����Ծ������ʹ��
	// �ж�С���Ƿ����ϰ�������С������ײ
	public boolean isCollision_jump() {
		boolean isCollision_jump = false;

		// ȡ����Ϸ���
		GamePanel gp = this.getGp();

		// ����С�˶�Ӧ��rectangle
		Rectangle pRtg = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());

		// �������ϰ��������ײ
		for (int i = 0; i < gp.map.getNumber(); i++) {
			// ȡ���ϰ���
			Barrier barrier = gp.map.getVt_Barrier().get(i);

			// �����ϰ��������Ӧ��rectangle
			Rectangle brRtg = new Rectangle(barrier.getX(), barrier.getY() + barrier.getHeight(), barrier.getWidth(),
					2);

			// �ж���ײ
			if (pRtg.intersects(brRtg)) {
				// �����ײ
				isCollision_jump = true;
				break;
			}
		}
		return isCollision_jump;
	}

	// �������������꣬��֤������С�˱������λ��
	public void updateWeaponXY() {
		// ���ж�С��״̬����ͬ״̬�����겻ͬ
		// ��������������һ�£�����������������ɴ��������Ƴ������Բ��ÿ�����������
		// ���������(������ȫ��������һ��)
		if (this.getDirection().equals("right")) {
			// ���վ��״̬
			if (this.isStanding() && !this.isLeft() && !this.isRight()) {
				this.getWeapon().setX(this.getX() + this.getWidth() - 13);
				this.getWeapon().setY(this.getY() + 11);
			} else if (this.isStanding()) {

				// �������·
				this.getWeapon().setX(this.getX() + this.getWidth() - 6);
				this.getWeapon().setY(this.getY() + 17);

			} else if (this.isJumping()) {

				// �������Ծ
				this.getWeapon().setX(this.getX() + this.getWidth() - 13);
				this.getWeapon().setY(this.getY() + 8);
			} else if (this.isFalling()) {

				// ���������
				this.getWeapon().setX(this.getX() + this.getWidth() - 15);
				this.getWeapon().setY(this.getY() + 8);
			}
		}

		else if (this.getDirection().equals("left")) {
			// ���������

			// ���վ��״̬
			if (this.isStanding() && !this.isLeft() && !this.isRight()) {
				this.getWeapon().setX(this.getX() + 13 - this.weapon.getWidth());
				this.getWeapon().setY(this.getY() + 11);
			} else if (this.isStanding()) {
				// �������·
				this.getWeapon().setX(this.getX() - this.weapon.getWidth() + 6);
				this.getWeapon().setY(this.getY() + 17);

			} else if (this.isJumping()) {

				// �������Ծ
				this.getWeapon().setX(this.getX() + 13 - this.weapon.getWidth());
				this.getWeapon().setY(this.getY() + 8);
			} else if (this.isFalling()) {

				// ���������
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
