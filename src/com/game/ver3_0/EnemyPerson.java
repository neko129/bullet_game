package com.game.ver3_0;

import java.awt.Rectangle;

// ����С��
class EnemyPerson extends Person implements Runnable {

	private int jumpTime_now; // ��ǰ��Ծʱ��
	private int jumpTime_sum; // ����Ծʱ��
	private int jumpCount_now; // ��ǰ��Ծ����
	private int jumpCount_sum; // ����Ծ����
	 
	private CheckBullet cb; // ����ӵ�
	private boolean isInLine; // �жϵ���С�˺��ҵ�С���Ƿ���һ��ֱ�ߣ� û���ϰ��ﵲס
	private int shootTime; // ���ʱ��, ʱ����㼴����ӵ�
	private int shootTimeSpeed; // ���ʱ������ٶ�
	private static int shootConstant = 1; // �������������Խ�����ʱ������ٶ�Խ��

	public EnemyPerson(int x, int y, GamePanel gp) {

		// ����С�˻�ȡ���ָ��,ȡ���ϰ�����Ϣ�����ϰ����ж���ײ����һ�������������ӵ����ӵ���Ҫ�ж��Ƿ�����ˣ��ϰ�����ײ
		this.setGp(gp);

		this.setFalling(true); // ����Ĭ��Ϊ����
		jumpTime_now = 0; // ��ǰ��Ծʱ����Ϊ0
		jumpTime_sum = 8; // ����Ծʱ��
		
		jumpCount_now = 0; // ��ǰ��Ծ����
		jumpCount_sum = 2; // ����Ծ����

		isInLine = false; // �����Ƿ�һ��ֱ��

		shootTime = 100; // ��ʼ�����ʱ��Ϊ100
		shootTimeSpeed = 4; // ���ʱ������ٶ�

		this.setAlive(true); // �����Ƿ����

		this.setAim(false); // �����Ƿ�Ϊ��׼״̬

		this.setAttack(false); // �����Ƿ�Ϊ����״̬

		this.setHasWeapon(false); // �����Ƿ�������

		// ����
		// ��������
		Weapon weapon = new HandGun(x + this.getWidth() - 13, y + 11, 26, 8, "handgun", (int) (Math.random() * 5), gp);
		this.setWeapon(weapon);
		this.setHasWeapon(true);

		this.setX(x);
		this.setY(y);
		this.setWidth(34);
		this.setHeight(50);

		this.setLeftPaces(8);
		this.setRightPaces(8);

		this.setLeftSpeed(1); // move()����ÿ�ζ���ָ��ƶ��ٶ�
		this.setRightSpeed(1); // ��Ҫ�޸ģ�����Ҫ�޸�move()������ٶ�

		this.setDirection("right");

		this.setFallSpeed(8);
		this.setJumpSpeed(2);

		this.setFallPaces(8);
		this.setJumpPaces(6);
		
		this.setImgTime_walk(0); // ������·ͼƬʱ��
		this.setImgId_walk(0); // ������·ͼƬID

		this.setImgTime_attack(0); // ���ù���ͼƬʱ��
		this.setImgId_attack(0); // ���ù���ͼƬID
		
		this.setImgTime_die(0); // ��������ͼƬʱ��
		this.setImgId_die(0); // ��������ͼƬID
	}

	// �ƶ�
	// �ƶ�ע������
	// ��Ծ����������ƶ�Ҫ�����Ƿ������ϰ������
	// ����Ծ�������������ƶ������ϰ����������С���Σ������⵽��С������ײ�������ƶ��ٶ�Ϊ0��
	// ��ֹ�������ϰ��������ײ���¿����ϰ����������
	// �ƶ��߼�
	// ÿ���ƶ�һ���ƶ��ٶȣ�����һ���ƶ��������ƶ��ƶ�������
	// С������Ծ���������ϰ������С������ײ���ٶȱ��0
	// *********
	// ע�����ƶ�������ÿ���ƶ�1���ض���������ȡС������
	public void move() {
		// ȡ����Ϸ���
		GamePanel gp = this.getGp();

		if (this.isLeft()) {
			this.setLeftSpeed(1); // ÿ�λָ������ٶ�

			for (int i = 0; i < this.getLeftPaces(); i++) {
				// ������������������Ծ
				if (this.isFalling() || this.isJumping()) {
					// ����ƶ���������
					if (this.isLeft()) {
						// ���ϰ����ұ�С���μ����ײ
						// �������˾���
						Rectangle epRtg = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());

						// �����ϰ������С���β��ж�
						for (int j = 0; j < gp.map.getNumber(); j++) {

							// ȡ���ϰ���
							Barrier br = gp.map.getVt_Barrier().get(j);

							// ��������С����
							Rectangle brRtg = new Rectangle(br.getX() + br.getWidth(), br.getY(), 2, br.getHeight());

							// �ж�
							if (epRtg.intersects(brRtg)) {
								// �����ײ,��Ӧ���ƶ��ٶ���Ϊ0
								this.setLeftSpeed(0);
							}
						}
					}
				}

				this.moveLeft();

				// ������׼״̬��ʵʱ�����������꣬��С�˱������λ��
				if (this.isAim()) {
					this.updateWeaponXY();
				}
			}

		} else if (this.isRight()) {
			this.setRightSpeed(1); // ÿ�λָ������ٶ�

			for (int i = 0; i < this.getRightPaces(); i++) {

				// ������������������Ծ
				if (this.isFalling() || this.isJumping()) {
					// ����ƶ���������
					if (this.isRight()) {
						// ���ϰ������С���μ����ײ
						// �������˾���
						Rectangle epRtg = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());

						// �����ϰ������С���β��ж�
						for (int j = 0; j < gp.map.getNumber(); j++) {

							// ȡ���ϰ���
							Barrier br = gp.map.getVt_Barrier().get(j);

							// ����
							Rectangle brRtg = new Rectangle(br.getX() - 2, br.getY(), 2, br.getHeight());

							// �ж�
							if (epRtg.intersects(brRtg)) {
								// �����ײ,��Ӧ���ٶ���Ϊ0
								this.setRightSpeed(0);
							}
						}
					}
				}

				this.moveRight();

				// ������׼״̬��ʵʱ�����������꣬��С�˱������λ��
				if (this.isAim()) {
					this.updateWeaponXY();
				}
			}
		}
	}

	// ����
	// *************
	// ���������������ȡС�����걣֤�����ڶ�Ӧλ��
	public void fall() {
		this.moveDown();
		this.updateWeaponXY();
	}

	// ��Ծ
	// ************
	// ��Ծÿ������������ȡС�����걣֤�����ڶ�Ӧλ��
	public void jump() {
		if (this.jumpTime_now < this.jumpTime_sum * Person.jumpSlowConstant * this.jumpCount_now) {

			for (int i = 0; i < this.getJumpPaces(); i++) {

				// �ж��Ƿ���ײ���ϰ�������С����
				// ���û����
				if (!this.isCollision_jump()) {
					this.moveUp();
					this.updateWeaponXY();
				} else {
					// �������
					this.setFalling(true);
					this.setJumping(false);
					this.jumpTime_now = 0; // �ָ���ǰ��Ծʱ��
					break;
				}
			}

			this.jumpTime_now++;
		} else if (this.jumpTime_now >= this.jumpTime_sum * Person.jumpSlowConstant * this.jumpCount_now) {
			// ��Ծ����
			this.setJumping(false);
			this.setFalling(true);
		}
	}

	// ��ȭ����
	// 1.�ڵ���С����ǰ����һ����ȭ��������
	// 2.�����������ҵ�С���ж��Ƿ���ײ
	public void attack() {
		Attack atk = null;

		// ȡ����Ϸ���
		GamePanel gp = this.getGp();

		// ���ж�С�˷���
		// �������
		if (this.getDirection().equals("right")) {
			// ��������Ӧ�Ĺ�������
			atk = new Attack(this.getX() + this.getWidth() - 3, this.getY() + 10, 14, 6);

		} else {
			// �������
			// ��������Ӧ�Ĺ�������
			atk = new Attack(this.getX() - 11, this.getY() + 10, 11, 6);
		}

		// ���ҵ�С���ж�
		// �����ײ
		// �ҵ�С������
		if (atk.isHit(gp.mp)) {
			gp.mp.setAlive(false);
		}

	}

	// ���
	// ��������ӵ�
	// �����ָ��x, y���룬�����ӵ�����
	public void shoot(int goalX, int goalY) {
		this.getWeapon().fire(goalX, goalY, 1);
	}

	// �������������꣬��֤������С�˱������λ��
	public void updateWeaponXY() {
		// �ж�С��״̬����ͬ״̬�������겻ͬ
		// ���������
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

		if (this.getDirection().equals("left")) {
			// ���������

			// ���վ��״̬
			if (this.isStanding() && !this.isLeft() && !this.isRight()) {
				this.getWeapon().setX(this.getX() - 13);
				this.getWeapon().setY(this.getY() + 11);
			} else if (this.isStanding()) {
				// �������·
				this.getWeapon().setX(this.getX() - 26 + 6);
				this.getWeapon().setY(this.getY() + 17);

			} else if (this.isJumping()) {

				// �������Ծ
				this.getWeapon().setX(this.getX() + 13 - 26);
				this.getWeapon().setY(this.getY() + 8);
			} else if (this.isFalling()) {

				// ���������
				this.getWeapon().setX(this.getX() + 15 - 26);
				this.getWeapon().setY(this.getY() + 8);
			}
		}
	}

	// �������ҿ�������
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
			// ��Ծ�������ڵ�������Ծʱ��ʱ������Ծ
			if (this.jumpCount_now < this.jumpCount_sum) {
				this.jumpCount_now++;
				this.setJumping(true);
				this.setStanding(false);
				this.setFalling(false);
			}
		}
	}

	// ���˽�����׼״̬
	// ��ȡ�ҵ�С�����꣬������Ϸ��廭����
	// �жϵ���С�˺��ҵ�С���������λ�ã�ת����Է���
	public void intoAim() {
		// ȡ���ҵ�С��
		MyPerson mp = this.getGp().mp;

		// ��ȡ�ҵ�����
		this.goalX = mp.getX();
		this.goalY = mp.getY();

		// ���x��������ҵ�С��x���꣬������
		if (this.getX() > mp.getX()) {
			this.setDirection("left");
		} else {
			// ����������
			this.setDirection("right");
		}

		this.setAim(true);
	}

	// �жϵ���С���Ƿ������׼״̬
	// ˼·
	// ����С�˺��ҵ�С��֮���ֱ��û���ϰ��ﵲס����ʱ����ӵ����ᱻ��ס�������Խ�����׼״̬
	// �õ���С�˷��������Ƿ���ײ���ӵ�����û����ײ���ϰ�������Խ�����׼״̬
	public void isIntoAim() {
		// ȡ���ҵ�С��
		MyPerson mp = this.getGp().mp;
		// ��������ӵ�����Ƿ�����ҵ�С��,����С�˵�isInLine�Ѿ������ӵ������isInLine = true; �����Խ�����׼״̬
		cb = new CheckBullet(this.getX() + 17, this.getY() + 25, mp.getX() + 17, mp.getY() + 25, 3, 3, this.getGp(),
				this);
		Thread t1 = new Thread(cb);
		t1.start();

	}

	// �������ʱ����
	// ���ʱ���������
	public void shootCountdown() {
		if (shootTime != 0) {
			this.shootTime -= this.shootTimeSpeed / EnemyPerson.shootConstant;
		}
	}

	// ���˳�ȭ��������
	// ������û���ӵ������ҵ�С�˾���Ͻ�ʱ�����빥��״̬
	public void attackMe() {
		// ȡ���ҵ�С��
		MyPerson mp = this.getGp().mp;

		// ���жϵ���С�˷���
		// �������
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

	// �жϵ����Ƿ�������ͼ
	// ����������ͼ����������
	public void isOverFall() {
		if (this.getY() > 700) {
			this.setAlive(false);
		}
	}

	public void run() {
		while (true) {
		
			// �������
			// ������Ծ״̬����ʼ����״̬
			if (!this.isAlive()) {
				this.setJumping(false);
				this.setFalling(true);
			}

			// �ж��Ƿ�����
			// ����Ѿ������������䵽�ϰ����ϣ��ӵ��������������߳�����
			if (!this.isAlive() && this.isStanding()) {
				for (int i = 0; i < this.getWeapon().getVt_bullet().size(); i++) {
					Bullet bullet = this.getWeapon().getVt_bullet().get(i); // ȡ���ӵ�

					bullet.setAlive(false);
				}
				
				break;
			}
			
			// �ж��Ƿ���������
			// ���Ż��������������䣬���������䵽�ϰ����ϲ����߳�����
			if (this.isFalling()) {
				
				
				// �ж��Ƿ���ײ
				// �������ײ��
				if (!this.isCollision_Person(this.getCollisionY())) {
					this.fall();
				} else {
					// �����ײ,isFalling��Ϊfalse,isStanding��Ϊtrue;
					this.setFalling(false);
					this.setStanding(true);
					
					this.jumpTime_now = 0; // ��Ծʱ��ָ�Ϊ0
					this.jumpCount_now = 0; // �ָ���Ծ����
					
					// ���ϰ���Y�����Ӧ�Ž�С��Y����
					this.setY(this.getCollisionY() - this.getHeight() + 1);

				}
			}

			// ������˻��ŲŽ��������߼��ж�
			if (this.isAlive()) {
				
				// �жϵ����Ƿ�������ͼ
				// ����������ͼ����������
				this.isOverFall();

				// ���������׼״̬,���ҵ�С�˿���
				if (!this.isAim()) {
					this.moveToMe();
				}

				// ����ӵ�Ϊ0���˳���׼״̬
				if (this.getWeapon().getBulletNumber() == 0) {
					this.setAim(false);
					this.isInLine = false;
				}
				
				// ����ӵ�Ϊ0�������ҵ�С��
				if (this.getWeapon().getBulletNumber() == 0) {
					this.attackMe();
				}

				// �ж��Ƿ������׼״̬,�˷�����ı����С�˵�isInLine�������true������ͬһ��ֱ�ߣ�������������׼״̬
				// û���ӵ�ʱ���ж��Ƿ������׼״̬
				if (this.getWeapon().getBulletNumber() != 0) {
					this.isIntoAim();
				}

				// ������׼״̬
				// �����һ��ֱ��, ������׼״̬��վ���ϰ�����ʱ����·
				// �������һ��ֱ�ߣ��뿪��׼״̬
				if (this.isInLine) {
					this.intoAim();
					
					if(this.isStanding()) {
						this.setLeft(false);
						this.setRight(false);
					}
				} else {
					this.setAim(false);
				}

				// ������׼״̬��ʵʱ�����������꣬��С�˱������λ��
				if (this.isAim()) {
					this.updateWeaponXY();
				}

				// ������׼״̬,�һ���,�����������ʱ�����ʱ�䵽������
				if (this.isAim()) {
					this.shootCountdown();

					if (this.shootTime <= 0) {
						this.shoot(this.getGp().mp.getX(), this.getGp().mp.getY());

						// �����ָ����ʱ��
						this.shootTime = 100;
					}
				}

				if (!this.isAttack()) {
					this.move();
				}

				

				// �ж��Ƿ�վ���ϰ�����
				if (this.isStanding()) {

					// �ж��Ƿ���ײ
					// �������ײ, ��ʼ������
					if (!this.isCollision_Person(this.getCollisionY())) {
						this.setFalling(true);
						this.setStanding(false);
					}
				}

				// �ж��Ƿ�������Ծ
				if (this.isJumping() && this.jumpCount_now <= this.jumpCount_sum) {
					this.jump();
				}

				// �ж��Ƿ񹥻��͵����Ӧ�Ĺ���ͼƬʱ��(�����Ӧ��ʱ��Ź��� )
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
