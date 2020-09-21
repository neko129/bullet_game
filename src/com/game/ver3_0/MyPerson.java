package com.game.ver3_0;

import java.awt.Rectangle;
import java.awt.event.*;

// �ҵ�С��
class MyPerson extends Person implements Runnable, KeyListener, MouseListener, MouseMotionListener {

	private int jumpTime_now; // ��ǰ��Ծʱ��
	private int jumpTime_sum; // ����Ծʱ��
	 private int jumpCount_now; // ��ǰ��Ծ����
	 private int jumpCount_sum; // ����Ծ����

	// ���췽����ʼ��
	public MyPerson(int x, int y, GamePanel gp) {

		// �ҵ�С�˻�ȡ���ָ��,ȡ���ϰ�����Ϣ�����ϰ����ж���ײ����һ�������������ӵ����ӵ���Ҫ�ж��Ƿ�����ˣ��ϰ�����ײ
		this.setGp(gp);

		this.setFalling(true); // ����Ĭ��Ϊ����
		jumpTime_now = 0; // ��ǰ��Ծʱ����Ϊ0
		jumpTime_sum = 8; // ����Ծʱ��

		jumpCount_now = 0; // ��ǰ��Ծ����Ϊ0
		jumpCount_sum = 2; // ����Ծ����Ϊ2

		this.setAlive(true); // �����Ƿ����

		this.setAim(false); // �����Ƿ�Ϊ��׼״̬

		this.setAttack(false); // �����Ƿ�Ϊ����״̬

		this.setHasWeapon(false); // �����Ƿ�������

		this.setSquat(false); // �����Ƿ��¶�

		// ����
		// ��������
		Weapon weapon = new HandGun(x + this.getWidth() - 13, y + 11, 26, 8, "handgun", 4, gp);
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

		// ���ж��Ƿ����
		if (this.isAlive()) {

			// ��������Ҽ����������¶�״̬��������׼״̬
			if (arg0.getButton() == MouseEvent.BUTTON3 && !this.isSquat()) {
				this.setAim(true);
				System.out.println("������׼״̬");

				// �ж�С���Ƿ�ת��
				// ������ָ��������С��������ߣ�ת��
				// ������ָ��������С�������ұߣ�ת��
				if (arg0.getX() < this.getX()) {
					this.setDirection("left");
					this.updateWeaponXY();
				} else if (arg0.getX() > this.getX()) {
					this.setDirection("right");
					this.updateWeaponXY();
				}

				// ��¼�����ָ������Ϣ��¼
				this.goalX = arg0.getX();
				this.goalY = arg0.getY();

			}

			// ��������׼״̬���������¶�״̬���������������ȭ����
			if (arg0.getButton() == MouseEvent.BUTTON1 && !this.isAim() && !this.isSquat()) {
				// ��ȭ����(����Ӧ�Ĺ���ͼƬʱ��ʱ�ж��Ƿ񹥻�������
				this.setAttack(true);

			} else if (arg0.getButton() == MouseEvent.BUTTON1 && this.isAim() && this.hasWeapon()
					&& this.getWeapon().getBulletNumber() != 0) {
				// ������׼״̬���������������������ӵ����������������ǹ����ӵ�

				System.out.println("shoot!");
				this.shoot(arg0.getX(), arg0.getY());
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

		// �ͷ�����Ҽ����뿪��׼״̬
		if (arg0.getButton() == MouseEvent.BUTTON3) {
			this.setAim(false);
			System.out.println("�뿪��׼״̬");
		}

	}

	public void mouseDragged(MouseEvent arg0) {
		// ��ס����Ҽ��϶��������괫��������ڻ�������ת������
		// ���ж��Ƿ�������Ҽ�
		// �жϲ�������Ҽ�����
		// �Ǿ��ж��Ƿ���׼״̬�ɡ�����
		// ���ж��Ƿ����
		if (this.isAlive()) {
			if (this.isAim()) {
				
				// �ж�С���Ƿ�ת��
				// ������ָ��������С��������ߣ�ת��
				// ������ָ��������С�������ұߣ�ת��
				if (arg0.getX() < this.getX()) {
					this.setDirection("left");
					this.updateWeaponXY();
				} else if (arg0.getX() > this.getX()) {
					this.setDirection("right");
					this.updateWeaponXY();
				}

				// ��¼�����ָ������Ϣ��¼
				this.goalX = arg0.getX();
				this.goalY = arg0.getY();
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {

	}

	// ������
	// �����¶�״̬ʱ�������ƶ���������Ծ
	public void keyPressed(KeyEvent e) {
		// ���ж��Ƿ����
		if (this.isAlive()) {
			// ���ж��Ƿ����¶�״̬,�������¶�״̬�Ž������в��� 
			if (!this.isSquat()) {
				
				if ((e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)) {
					// �����ƶ�
					this.setLeft(true);
					this.setRight(false);

					if (!this.isAim()) {
						// ����׼״̬�������߲��������������λ�þ��������ģ����Բ�������׼״̬�Ÿı�������
						this.setDirection("left");
					}
				} else if ((e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)) {
					// �����ƶ�
					this.setRight(true);
					this.setLeft(false);

					if (!this.isAim()) {
						this.setDirection("right");
					}
				} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					// ��Ծ
					// ��Ծ�������ڵ�������Ծʱ��ʱ������Ծ
					if(this.jumpCount_now < this.jumpCount_sum) {
						this.jumpCount_now++;
						this.setJumping(true);
						this.setFalling(false);
						this.setStanding(false);
					}
				}
			}
			
			if (e.getKeyCode() == KeyEvent.VK_S) {
				// �¶�
				// �ж���������¶�״̬ʱ������Ϊ�¶�״̬,�����¶׷������ı�С�˴�С�����꣬������׼״̬
				if (!this.isSquat()) {
					this.setSquat(true);
					this.squat();
					this.setAim(false);
				}
			}
		}
	}

	// ���ͷ�
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
						// �����ҵľ���
						Rectangle myRtg = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());

						// �����ϰ������С���β��ж�
						for (int j = 0; j < gp.map.getNumber(); j++) {

							// ȡ���ϰ���
							Barrier br = gp.map.getVt_Barrier().get(j);

							// ��������С����
							Rectangle brRtg = new Rectangle(br.getX() + br.getWidth(), br.getY(), 2, br.getHeight());

							// �ж�
							if (myRtg.intersects(brRtg)) {
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
						// �����ҵľ���
						Rectangle myRtg = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());

						// �����ϰ������С���β��ж�
						for (int j = 0; j < gp.map.getNumber(); j++) {

							// ȡ���ϰ���
							Barrier br = gp.map.getVt_Barrier().get(j);

							// ����
							Rectangle brRtg = new Rectangle(br.getX() - 2, br.getY(), 2, br.getHeight());

							// �ж�
							if (myRtg.intersects(brRtg)) {
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
	// 1.���ҵ�С����ǰ����һ����ȭ��������
	// 2.��������������ж��Ƿ���ײ
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

		// ������ж�
		for (int i = 0; i < gp.vt_Ep.size(); i++) {
			// ȡ������С��
			EnemyPerson ep = gp.vt_Ep.get(i);

			// ��ʼ�ж�
			// �����ײ
			if (atk.isHit(ep)) {
				ep.setAlive(false);
			}
		}
	}

	// ���
	// ��������ӵ�
	// �����ָ��x, y���룬�����ӵ�����
	public void shoot(int goalX, int goalY) {
		this.getWeapon().fire(goalX, goalY, 0);
	}

	// �¶�
	// �ҵ�С�˵Ĵ�С�ı䣬����λ�øı�
	public void squat() {
		this.setY(this.getY() + 16);
		this.setHeight(this.getWidth());
	}

	// �¶׻ָ�
	// �ָ� �ҵ�С�˵Ĵ�С������λ��
	public void squatRecovery() {
		this.setY(this.getY() - 16);
		this.setHeight(50);
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

	// �ӵ�ʱ�䷽��,�ҵ�С��վ������ʱ�����ӵ�ʱ�䣬һ�б���
	public void bulletTime() {
		// ������������
		Person.slowConstant = 12;

		// �ƶ��ٶȱ���
		this.setLeftPaces(1);
		this.setRightPaces(1);

		// ��Ծ����
		this.setJumpPaces(1);
		Person.jumpSlowConstant = 6;

		// �������
		this.setFallSpeed(1);

		// �ӵ����б���
		Bullet.speedP = 1;

		// �����������ʱ����
		EnemyPerson.setShootConstant(4);
	}

	// �����ӵ�ʱ��
	public void endBulletTime() {
		// ���������ָ�
		Person.slowConstant = 1;

		// �ƶ��ٶȻָ�
		this.setLeftPaces(8);
		this.setRightPaces(8);

		// ��Ծ�ָ�
		this.setJumpPaces(6);
		Person.jumpSlowConstant = 1;

		// ����ָ�
		this.setFallSpeed(8);

		// �ӵ����лָ�
		Bullet.speedP = 16;

		// �����������ʱ�ָ�
		EnemyPerson.setShootConstant(1);
	}

	public void run() {
		while (true) {

			
			// �������
			// �����ӵ�ʱ�䣬������Ծ״̬����ʼ����״̬
			if (!this.isAlive()) {
				this.endBulletTime();
				this.setJumping(false);
				this.setFalling(true);
			}
			
			
			// ����������䵽�ϰ�����
			// �����߳�
			if (!this.isAlive() && this.isStanding()) {
				break;
			}

		

			// �ж��Ƿ���������
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
					this.jumpCount_now = 0; // ��Ծ�����ָ�Ϊ0
					// ���ϰ���Y�����Ӧ�Ž�С��Y����
					this.setY(this.getCollisionY() - this.getHeight() + 1);

				}
			}

			// �ж��Ƿ���ţ����ŲŽ��������ж�
			if (this.isAlive()) {
				// �ҵ�С��վ�����������ڹ���״̬,�����ӵ�ʱ��
				if (this.isStanding() && !this.isLeft() && !this.isRight() && !this.isAttack()) {
					this.bulletTime();
				}

				// �ҵ�С���ƶ�����ȭ�����������ӵ�ʱ��
				if (this.isLeft() || this.isRight() || this.isAttack() || this.isFalling() || this.isJumping()) {
					this.endBulletTime();
				}

				// ������׼״̬��ʵʱ�����������꣬��С�˱������λ��
				if (this.isAim()) {
					this.updateWeaponXY();
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
				// ��Ծ���ɶ�����
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

}