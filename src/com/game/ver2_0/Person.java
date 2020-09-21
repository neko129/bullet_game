package com.game.ver2_0;

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

	private boolean isAlive; // �ж��Ƿ����
	private boolean isAttack; // �ж��Ƿ��ȭ����
	private boolean isAim; // �ж��Ƿ���׼����ס����Ҽ�������׼״̬���ɳ�ǹ����
	private boolean hasWeapon; // �ж��Ƿ�����������������������ӵ�

	private boolean isLeft; // �Ƿ�����
	private boolean isRight; // �Ƿ�����

	private boolean isFalling; // �Ƿ���������
	private boolean isStanding; // �Ƿ�����վ���ϰ�����
	private boolean isJumping; // �Ƿ�������Ծ
	
	private int imgTime_walk; // ��·ͼƬʱ��
	private int imgId_walk; // ��·ͼƬid����Ӧ�ڼ���ͼƬ

	private int imgTime_attack; // ����ͼƬʱ��
	private int imgId_attack; // ����ͼƬid
	
	int goalX; // Ŀ��x���꣬һ�����ӵ�������ָ������꣬�ͻ�����ת����Ҫ������
	int goalY; // Ŀ��y���꣬һ�����ӵ�������ָ������꣬�ͻ�����ת����Ҫ������

	private int collisionY; // ��ײ�ϰ����Y���꣬����С��������ײ�ϰ���ʱ��С�˵�����պ����ϰ������棬��ֹ�����ϰ�����

	private Weapon weapon; // ����

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
