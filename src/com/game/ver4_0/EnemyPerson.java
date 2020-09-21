package com.game.ver4_0;

import java.awt.Rectangle;
import java.util.Vector;

// ����С�ˣ�0Ϊ��ͨ���� 1Ϊ��·�ٶȼӿ� 2Ϊ��ǹ�ٶȼӿ� 3ΪBOSS����
class EnemyPerson extends Person implements Runnable {
	private boolean isSmartMove; // �Ƿ������ƶ�


	private CheckBullet cb; // ����ӵ�
	private boolean isInLine; // �жϵ���С�˺��ҵ�С���Ƿ���һ��ֱ�ߣ� û���ϰ��ﵲס
	private int shootTime; // ���ʱ��, ʱ����㼴����ӵ�
	private int sumShootTime; // �����ʱ��
	private double shootTimeSpeed; // ���ʱ������ٶ�
	private static int shootConstant = 1; // �������������Խ�����ʱ������ٶ�ԽС����Խ����ǹ

	// offset ƫ���� ʹ���˵���·������ȱ仯���������ֵ���
	private int offset_walkPaces; // ��·����ƫ����
	private int offset_shootTimeSpeed; // ���ʱ���ٶ�ƫ����

	private int type; // �������� ��0Ϊ��ͨ���� 1Ϊ��·�ٶȼӿ� 2Ϊ��ǹ�ٶȼӿ� 3Ϊ�������������ʱ 4ΪBOSS����
	

	public EnemyPerson(int x, int y, int type, int weaponType, int bulletNumber, GamePanel gp) {

		// ����С�˻�ȡ���ָ��,ȡ���ϰ�����Ϣ�����ϰ����ж���ײ����һ�������������ӵ����ӵ���Ҫ�ж��Ƿ�����ˣ��ϰ�����ײ
		this.setGp(gp);

		this.type = type; // ���õ�������
		this.setWeaponType(weaponType); // ������������
		this.setBulletNumber(bulletNumber); // �����������ӵ�����

		this.initOffset(); // ��ʼ��ƫ����(������������)
		
		this.initWeapon(); // ��������

		this.setFalling(true); // ����Ĭ��Ϊ����
		this.setJumpTime_now(0); // ��ǰ��Ծʱ����Ϊ0
		this.setJumpTime_sum(7);; // ����Ծʱ��

		this.setJumpCount_now(0); // ��ǰ��Ծ����Ϊ0
		this.setJumpCount_sum(2); // ����Ծ����Ϊ2

		isInLine = false; // �����Ƿ�һ��ֱ��

		sumShootTime = 130; // ��ʼ�������ʱ��
		shootTime = sumShootTime; // ��ʼ�����ʱ��
		shootTimeSpeed = 4; // ���ʱ������ٶ�

		this.setAlive(true); // �����Ƿ����

		this.setAim(false); // �����Ƿ�Ϊ��׼״̬

		this.setAttack(false); // �����Ƿ�Ϊ����״̬

		this.setHasWeapon(false); // �����Ƿ�������

		
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
		this.setJumpPaces(5);

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

			for (int i = 0; i < this.getLeftPaces() + this.offset_walkPaces; i++) {
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

				// ʵʱ�����������꣬��С�˱������λ��
				this.updateWeaponXY();
			}

		} else if (this.isRight()) {
			this.setRightSpeed(1); // ÿ�λָ������ٶ�

			for (int i = 0; i < this.getRightPaces() + this.offset_walkPaces; i++) {

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

				// ʵʱ�����������꣬��С�˱������λ��
				this.updateWeaponXY();

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
		if (this.getJumpTime_now() < this.getJumpTime_sum() * Person.jumpSlowConstant * this.getJumpCount_now()) {
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
					break;
				}
			}

			this.setJumpTime_now(this.getJumpTime_now() + 1);
		} else if (this.getJumpTime_now() >= this.getJumpTime_sum() * Person.jumpSlowConstant * this.getJumpCount_now()) {
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

		if (this.getY() - 67 - (int) (Math.random() * 10) > this.getGp().mp.getY()) {
			// ��Ծ�������ڵ�������Ծʱ��ʱ������Ծ
			if (this.getJumpCount_now() < this.getJumpCount_sum()) {
				this.setJumpCount_now(this.getJumpCount_now() + 1);
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
			int sumShootTimeSpeed = (int) Math
					.ceil((this.shootTimeSpeed + this.offset_shootTimeSpeed) / EnemyPerson.shootConstant);
			this.shootTime -= sumShootTimeSpeed;
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
			if (this.getX() - 11 < mp.getX() + mp.getWidth() && this.getX() - 11 > mp.getX()) {
				this.setAttack(true);
			}
		} else {
			if (this.getX() + this.getWidth() > mp.getX() && this.getX() + this.getWidth() < mp.getX() + mp.getWidth()) {
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

	// ������������
	// ����С�˵������Ž�������������,ȥ������С�˵�����
	public void dropWeapon() {
		Weapon weapon = this.getWeapon(); // ȡ������
		Thread t1; // ����һ�������߳�
		
		// ����������Ϸ������������ 
		weapon.setPicked(false);
		this.getGp().vt_wp.add(weapon);
		t1 = new Thread(weapon);
		t1.start();
		
		// �ж���������,�����������
		if (weapon.getWeaponType().equals("handgun")) {
			weapon.setWidth(18);
			weapon.setHeight(12);
		} else if (weapon.getWeaponType().equals("shotgun")) {
			weapon.setWidth(23);
			weapon.setHeight(8);
		}
		
		this.setWeapon(new Weapon(1, 1, 1, 1, "hand", 0, this.getGp()));
	}

	// ��ʼ��ƫ��������
	public void initOffset() {
		// ��Ӧ�������࣬����ƫ�����Ĳ�ͬ
		// 1Ϊ��·�ӿ�
		if (type == 1) {
			offset_walkPaces = 1;
			offset_shootTimeSpeed = 0;
		} else if (type == 2) {
			// 2Ϊ��ǹ�ٶȼӿ�
			offset_walkPaces = 0;
			offset_shootTimeSpeed = 2;
		} else if(type==4) 
		{
			offset_walkPaces = 1;
			offset_shootTimeSpeed = 2;
		}
	}
	
	//  ��ʼ������
	public void initWeapon() {
		Weapon weapon = null; // ��������
		
		// �����������Ϊ1������ǹ
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

	// ������Ծ������boss�����˻�ĳЩǿ�����˿���ʹ��
	// ���˼���ҵ��ӵ�������ľ��룬��⵽��Ҫ�����е�ʱ����Ծ���
	public void smartJump() {

		// ȡ���ҵ�С�˵��ӵ�����
		Vector<Bullet> vt_bullet = this.getGp().mp.getWeapon().getVt_bullet();

		for (int i = 0; i < vt_bullet.size(); i++) {
			// ȡ���ӵ�
			Bullet bullet = vt_bullet.get(i);

			// ��������ж��Ƿ���Ծ
			// �����������140���ӵ����ڵ���С��ͷ�ϣ�������Ծ�����ڣ���Ծ
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

	// �����ƶ�������boss�����˻�ĳЩǿ�����˿���ʹ��
	// ���˼���ҵ��ӵ�������ľ��룬��⵽��Ҫ�����е�ʱ���ƶ�
	public void smartMove() {
		isSmartMove = false; // һ��ʼ�Ƿ������ƶ���Ϊfalse

		// ȡ���ҵ�С�˵��ӵ�����
		Vector<Bullet> vt_bullet = this.getGp().mp.getWeapon().getVt_bullet();

		for (int i = 0; i < vt_bullet.size(); i++) {
			// ȡ���ӵ�
			Bullet bullet = vt_bullet.get(i);

			// ��������ж��Ƿ��ƶ�
			// �����������250���ж��ӵ����ķ��򣬷����ƶ�
			if (Math.sqrt(Math.pow(this.getX() + 17 - bullet.getX(), 2)
					+ Math.pow(this.getY() + 10 - bullet.getY(), 2)) < 150) {

				isSmartMove = true;

				if (this.getX() > bullet.getX()) {
					// ����������,������
					this.setRight(true);
					this.setLeft(false);
					this.setDirection("right");
				} else {
					// ����
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

			// �������
			// ������Ծ״̬����ʼ����״̬����������
			if (!this.isAlive()) {
				this.setJumping(false);
				this.setFalling(true);
				this.dropWeapon();
			}
			
			// �ж��Ƿ�����߳�
			// ����Ѿ������������䵽�ϰ����ϣ��ӵ�������, �����߳�����
			if (!this.isAlive() && this.isStanding()) {
				for (int i = 0; i < this.getWeapon().getVt_bullet().size(); i++) {
					Bullet bullet = this.getWeapon().getVt_bullet().get(i); // ȡ���ӵ�

					bullet.setAlive(false);
					
				}

				break;
			}
			
			// �����ã������Ѷȷ���
			// �����ƶ�
//			this.smartMove();
//			
//			if (type == 4 || (((int) (Math.random() * 40) == 0) && this.getGp().mp.isBulletTime())
//					|| (((int) (Math.random() * 20) == 0) && !this.getGp().mp.isBulletTime())) {
//				System.out.println("����");
//				
//				// ������Ծ
//				this.smartJump();
//				
//			}
			

			
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

					this.setJumpTime_now(0); // ��Ծʱ��ָ�Ϊ0
					this.setJumpCount_now(0); // �ָ���Ծ����

					// ���ϰ���Y�����Ӧ�Ž�С��Y����
					this.setY(this.getCollisionY() - this.getHeight() + 1);

				}
			}

			

			// ������˻��ŲŽ��������߼��ж�
			if (this.isAlive()) {

				// �����ã������Ѷȷ���
				if (type == 4) {
					// ������Ծ
					// �����ƶ�
					this.smartJump();
					this.smartMove();
				}
				
				
				// �жϵ����Ƿ�������ͼ
				// ����������ͼ����������
				this.isOverFall();


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
				
				// ���������׼״̬�������ƶ�״̬�����ҵ�С���ƶ�
				if (!this.isSmartMove) {
					this.moveToMe();
				}

				// ������׼״̬
				// �����һ��ֱ��, ������׼״̬��վ���ϰ������Ҳ��������ƶ�״̬ʱ����·
				// �������һ��ֱ�ߣ��뿪��׼״̬���ָ��������ʱ��
				if (this.isInLine) {
					this.intoAim();

					if (this.isStanding() && !this.isSmartMove) {
						this.setLeft(false);
						this.setRight(false);
					}
				} else {
					this.setAim(false);
					this.shootTime += 10;
					// ���ܳ������ʱ������
					if(this.shootTime > sumShootTime) {
						shootTime = sumShootTime;
					}
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
						this.shootTime = sumShootTime;
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
				if (this.isJumping() && this.getJumpCount_now() <= this.getJumpCount_sum()) {
					this.jump();
				}

				// �ж��Ƿ񹥻��͵����Ӧ�Ĺ���ͼƬʱ��(�����Ӧ�Ĺ���ͼƬ�Ź��� )
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
