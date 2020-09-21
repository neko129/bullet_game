package com.game.ver2_0;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

// ��Ϸ���
class GamePanel extends JPanel implements Runnable, MouseListener, KeyListener {
	MyPerson mp; // �ҵ�С��
	EnemyPerson ep; // ����С��
	Vector<EnemyPerson> vt_Ep; // �����ŵ���
	Map map; // ��ͼ
	GameImg gi; // ��ϷͼƬ��������ָ�����ϷͼƬ
	boolean isAlive; // �Ƿ����

	// int imgTime_walk; // ��·ͼƬʱ��
	// int imgId_walk; // ��·ͼƬid����Ӧ�ڼ���ͼƬ
	//
	// int imgTime_attack; // ����ͼƬʱ��
	// int imgId_attack; // ����ͼƬid


	public GamePanel() {
		// ��ϷͼƬ����
		gi = new GameImg();

		// ��ͼ
		map = new Map();

		// ע������¼�������,����ȷ����ͼ�ϰ�������
		this.addMouseListener(this);

		// �ҵ�С��
		mp = new MyPerson(600, 450, this);
		Thread t1 = new Thread(mp);
		t1.start();

		// �ҵ�С��ע������¼�������
		this.addMouseListener(mp);
		this.addMouseMotionListener(mp);

		
		// ����С��
		
		vt_Ep = new Vector<EnemyPerson>(); // ����С������
		
		// ��������С��
		ep = new EnemyPerson(300, 210, this);
		// �����߳�
		Thread t2 = new Thread(ep);
		t2.start();
		vt_Ep.add(ep); // �Ž�����С������
		
//		// ��������С��
		ep = new EnemyPerson(300, 420, this);
		// �����߳�
		Thread t3 = new Thread(ep);
		t3.start();
		vt_Ep.add(ep); // �Ž�����С������
		

		// ��������С��
		ep = new EnemyPerson(300, 310, this);
		// �����߳�
		Thread t4 = new Thread(ep);
		t4.start();
		vt_Ep.add(ep); // �Ž�����С������
		
		// ��������С��
		ep = new EnemyPerson(800, 210, this);
		// �����߳�
		Thread t5 = new Thread(ep);
		t5.start();
		vt_Ep.add(ep); // �Ž�����С������
		
		// ��������С��
//		ep = new EnemyPerson(800, 410, this);
//		// �����߳�
//		Thread t6 = new Thread(ep);
//		t6.start();
//		vt_Ep.add(ep); // �Ž�����С������
		
		// ��������С��
		ep = new EnemyPerson(800, 310, this);
		// �����߳�
		Thread t7 = new Thread(ep);
		t7.start();
		vt_Ep.add(ep); // �Ž�����С������
		
		
		
		// ����Ĭ�ϻ��� 
		isAlive  = true;
	}

	public void paint(Graphics g) {
		super.paint(g);

		// ������ͼƬ
		g.drawImage(gi.getBackgroundImg(), 0, 0, 1300, 700, this);

		// ���ҵ�����
		// ���ж��Ƿ�����׼״̬
		if (mp.isAim()) {
			this.drawWeapon(g, mp);
		}

		// ���ҵ�С��
		// (������
		// g.fillRect(mp.getX(), mp.getY(), mp.getWidth(), mp.getHeight());
		if (mp.isAlive()) {
			// �ж��Ƿ����
			this.drawPerson(g, mp);
		}
		// ����������(������
		// g.drawRect(mp.getX() - 10, mp.getY() + 10, 11, 6);

		// ������С�ˣ�����С�˵��ӵ�,����С������
		// ����һ�����Щ�ң�����ֻ�����һ�����С��
		for (int i = 0; i < vt_Ep.size(); i++) {
			// ȡ������С��
			EnemyPerson ep = vt_Ep.get(i);

			// ���жϵ���С���Ƿ����
			// �������
			if (ep.isAlive()) {

				// �ж��Ƿ������׼״̬
				if (ep.isAim()) {

					// ��������С������
					this.drawWeapon(g, ep);

					// ��������С���������ʱ
					g.drawString(ep.getShootTime() + "", ep.getX() + 10, ep.getY() - 5);
				}
				// ��������С��
				this.drawPerson(g, ep);

				// Ȼ���ٻ�������С���ӵ�
				for (int j = 0; j < ep.getWeapon().getVt_bullet().size(); j++) {
					// ȡ���ӵ�
					Bullet bullet = ep.getWeapon().getVt_bullet().get(j);

					// �ж��ӵ��Ƿ����
					// ������ţ����ӵ�
					if (bullet.isAlive()) {
						// ���ӵ�
						this.drawBullet(g, bullet);
					} else {
						// �������ӵ�������ɾ���ӵ�
						ep.getWeapon().getVt_bullet().remove(bullet);
					}
				}

			} else {
				// ����������ˣ��ӵ���С��������ɾ��
				this.vt_Ep.remove(ep);
			}
		}

		// ����ͼ�ϰ���
		this.drawBarrier(g);

		// ���Ż��ӵ�
		// �ҵ�С�˵��ӵ�
		// ���ж��ҵ�С���Ƿ����
		if (mp.isAlive()) {
			for (int i = 0; i < mp.getWeapon().getVt_bullet().size(); i++) {
				// ȡ���ӵ�
				Bullet bullet = mp.getWeapon().getVt_bullet().get(i);

				// �ж��ӵ��Ƿ����
				// ������ţ����ӵ�
				if (bullet.isAlive()) {
					// ���ӵ�
					this.drawBullet(g, bullet);
				} else {
					// �������ӵ�������ɾ���ӵ�
					mp.getWeapon().getVt_bullet().remove(bullet);
				}
			}
		}
	}

	// ����ͼ�ϰ��﷽��
	public void drawBarrier(Graphics g) {
		for (int i = 0; i < map.getNumber(); i++) {
			// ȡ����ͼ�ϰ���
			Barrier barrier = map.getVt_Barrier().get(i);
			g.drawImage(gi.getBarrierImg()[barrier.getId()], barrier.getX(), barrier.getY(), barrier.getWidth(),
					barrier.getHeight(), this);
		}
	}

	// ���ӵ�����
	public void drawBullet(Graphics g, Bullet bullet) {
		g.fillRect((int) bullet.getX(), (int) bullet.getY(), bullet.getWidth(), bullet.getHeight());
	}

	// ��С�˷���
	public void drawPerson(Graphics g, Person p) {
		int personImg_x = p.getX() - 8; // �õ�С��ͼƬ��x
		int personImg_y = p.getY(); // �õ�С��ͼƬ��y
		int personImg_width = p.getHeight(); // �õ�С��ͼƬ�Ŀ�
		int personImg_height = p.getHeight(); // �õ�С��ͼƬ�ĸ�

		// ����·
		// �����·,ֻ��վ��״̬��·���Ҳ��ڹ���״̬��������׼״̬
		if (p.isStanding() && !p.isAttack() && !p.isAim()) {
			this.drawPersonWalk(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
		}

		// ����׼״̬��·
		// ֻ��վ��״̬�����ڹ���״̬������׼״̬,����������˶�
		else if (p.isStanding() && !p.isAttack() && p.isAim() && (p.isLeft() || p.isRight())) {
			this.drawPersonAimWalk(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
		}

		// ��վ��
		// ���վ��,����վ��״̬�Ҳ��߶����Ҳ��ڹ���״̬,������׼״̬
		if (p.isStanding() && !p.isLeft() && !p.isRight() && !p.isAttack() && !p.isAim()) {
			this.drawPersonStand(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
		}

		// ����׼״̬վ��
		// ����վ��״̬�Ҳ��߶����Ҳ��ڹ���״̬,����׼״̬
		else if (p.isStanding() && !p.isLeft() && !p.isRight() && !p.isAttack() && p.isAim()) {
			this.drawPersonAimStand(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
		}

		// ����Ծ
		// �����Ծ��ֻ����Ծ״̬���Ҳ��ڹ���״̬,������׼״̬
		if (p.isJumping() && !p.isAttack() && !p.isAim()) {
			this.drawPersonJump(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
		}

		// ����׼״̬��Ծ
		// �����Ծ������Ծ״̬����׼״̬���Ҳ��ڹ���״̬
		else if (p.isJumping() && p.isAim() && !p.isAttack()) {
			this.drawPersonAimJump(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
		}

		// ����׼״̬����
		// ������䣬������״̬������׼״̬,�Ҳ��ڹ���״̬
		if (p.isFalling() && !p.isAttack() && p.isAim()) {
			this.drawPersonAimFall(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
		}

		// ������
		// ������䣬ֻ������״̬���Ҳ��ڹ���״̬��������׼״̬
		else if (p.isFalling() && !p.isAttack()) {
			this.drawPersonFall(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
		}

		// ������
		if (p.isAttack()) {
			this.drawPersonAttack(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
		}

	}

	// ��С�������ƶ�����
	public void drawPersonWalk(Graphics g, int personImg_x, int personImg_y, int personImg_width, int personImg_height,
			Person p) {
		// ���������
		// ��������Ծ������
		// ���������
		if (p.isLeft()) {
			if (p.getImgTime_walk() < 2 * Person.slowConstant) {
				p.setImgId_walk(0);
			} else if (p.getImgTime_walk() < 4 * Person.slowConstant) {
				p.setImgId_walk(1);
			} else if (p.getImgTime_walk() < 6 * Person.slowConstant) {
				p.setImgId_walk(2);
			} else if (p.getImgTime_walk() < 8 * Person.slowConstant) {
				p.setImgId_walk(3);
			} else if (p.getImgTime_walk() < 10 * Person.slowConstant) {
				p.setImgId_walk(4);
			} else if (p.getImgTime_walk() < 12 * Person.slowConstant) {
				p.setImgId_walk(5);
			} else if (p.getImgTime_walk() < 14 * Person.slowConstant) {
				p.setImgId_walk(6);
			} else if (p.getImgTime_walk() < 16 * Person.slowConstant) {
				p.setImgId_walk(7);
			} else if (p.getImgTime_walk() < 18 * Person.slowConstant) {
				p.setImgId_walk(8);
			} else if (p.getImgTime_walk() < 20 * Person.slowConstant) {
				p.setImgId_walk(9);
			} else {
				p.setImgTime_walk(0);
			}

			// �ж�С�����ҵ�С���໹�ǵ���С���࣬����С��ͼƬ��ͬ
			// ������ҵ�С��
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getWalkLeftImg()[p.getImgId_walk()], personImg_x, personImg_y, personImg_width,
						personImg_height, this);
			} else {
				// ����ǵ���С��
				g.drawImage(gi.getEpWalkLeftImg()[p.getImgId_walk()], personImg_x, personImg_y, personImg_width,
						personImg_height, this);
			}

			p.setImgTime_walk(p.getImgTime_walk() + 1);
		}

		// ���������
		if (p.isRight()) {
			if (p.getImgTime_walk() < 2 * Person.slowConstant) {
				p.setImgId_walk(0);
			} else if (p.getImgTime_walk() < 4 * Person.slowConstant) {
				p.setImgId_walk(1);
			} else if (p.getImgTime_walk() < 6 * Person.slowConstant) {
				p.setImgId_walk(2);
			} else if (p.getImgTime_walk() < 8 * Person.slowConstant) {
				p.setImgId_walk(3);
			} else if (p.getImgTime_walk() < 10 * Person.slowConstant) {
				p.setImgId_walk(4);
			} else if (p.getImgTime_walk() < 12 * Person.slowConstant) {
				p.setImgId_walk(5);
			} else if (p.getImgTime_walk() < 14 * Person.slowConstant) {
				p.setImgId_walk(6);
			} else if (p.getImgTime_walk() < 16 * Person.slowConstant) {
				p.setImgId_walk(7);
			} else if (p.getImgTime_walk() < 18 * Person.slowConstant) {
				p.setImgId_walk(8);
			} else if (p.getImgTime_walk() < 20 * Person.slowConstant) {
				p.setImgId_walk(9);
			} else {
				p.setImgTime_walk(0);
			}

			// �ж�С�����ҵ�С���໹�ǵ���С���࣬����С��ͼƬ��ͬ
			// ������ҵ�С��
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getWalkRightImg()[p.getImgId_walk()], personImg_x, personImg_y, personImg_width,
						personImg_height, this);
			} else {
				// ����ǵ���С��
				g.drawImage(gi.getEpWalkRightImg()[p.getImgId_walk()], personImg_x, personImg_y, personImg_width,
						personImg_height, this);
			}
			p.setImgTime_walk(p.getImgTime_walk() + 1);
		}
	}

	// ��С��վ������
	public void drawPersonStand(Graphics g, int personImg_x, int personImg_y, int personImg_width, int personImg_height,
			Person p) {
		// ����վ��
		if (p.getDirection().equals("left")) {

			// �ж����ҵ�С�˻��ǵ���С��
			// ������ҵ�С��
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getStandLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			} else {
				g.drawImage(gi.getEpStandLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			}

		} else if (p.getDirection().equals("right")) {
			// ����վ��
			// �ж����ҵ�С�˻��ǵ���С��
			// ������ҵ�С��
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getStandRightImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			} else {
				// ����ǵ���С��
				g.drawImage(gi.getEpStandRightImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			}
		}
	}

	// ��С����Ծ����
	public void drawPersonJump(Graphics g, int personImg_x, int personImg_y, int personImg_width, int personImg_height,
			Person p) {
		// �������
		if (p.getDirection().equals("left")) {

			// �ж����ҵ�С�˻��ǵ���С��
			// ������ҵ�С��
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getJumpLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			} else {
				// ����ǵ���С��
				g.drawImage(gi.getEpJumpLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			}

		} else if (p.getDirection().equals("right")) {
			// �������

			// �ж����ҵ�С�˻��ǵ���С��
			// ������ҵ�С��
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getJumpRightImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			} else {
				// ����С��
				g.drawImage(gi.getEpJumpRightImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			}
		}
	}

	// ��С�����䷽��
	public void drawPersonFall(Graphics g, int personImg_x, int personImg_y, int personImg_width, int personImg_height,
			Person p) {
		// �������
		if (p.getDirection().equals("left")) {

			// �ж����ҵ�С�˻��ǵ���С��
			// ������ҵ�С��
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getFallLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			} else {
				// ����С��
				g.drawImage(gi.getEpFallLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			}

		} else if (p.getDirection().equals("right")) {
			// �������

			// �ж����ҵ�С�˻��ǵ���С��
			// ������ҵ�С��
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getFallRightImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			} else {
				// ����С��
				g.drawImage(gi.getEpFallRightImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			}
		}
	}

	// ��С�˳�ȭ��������
	public void drawPersonAttack(Graphics g, int personImg_x, int personImg_y, int personImg_width,
			int personImg_height, Person p) {
		// ���жϷ���
		// �������
		if (p.getDirection().equals("left")) {
			if (p.getImgTime_attack() < 3 * Person.slowConstant) {
				p.setImgId_attack(0);
			} else if (p.getImgTime_attack() < 6 * Person.slowConstant) {
				p.setImgId_attack(1);
			} else if (p.getImgTime_attack() < 9 * Person.slowConstant) {
				p.setImgId_attack(2);
			} else if (p.getImgTime_attack() < 13 * Person.slowConstant) {
				p.setImgId_attack(3);
			} else {
				p.setImgTime_attack(0); // ����ͼƬʱ��ָ�
				p.setImgId_attack(0); // ����ͼƬId�ָ�
				p.setAttack(false); // ��������
			}

			// �ж����ҵ�С�˻��ǵ���С��
			// ������ҵ�С��
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getAttackLeftImg()[p.getImgId_attack()], personImg_x, personImg_y, personImg_width,
						personImg_height, this);
			} else {
				// ����С��
				g.drawImage(gi.getEpAttackLeftImg()[p.getImgId_attack()], personImg_x, personImg_y, personImg_width,
						personImg_height, this);
			}
			p.setImgTime_attack(p.getImgTime_attack() + 1);
		}

		// �������
		if (p.getDirection().equals("right")) {
			if (p.getImgTime_attack() < 3 * Person.slowConstant) {
				p.setImgId_attack(0);
			} else if (p.getImgTime_attack() < 6 * Person.slowConstant) {
				p.setImgId_attack(1);
			} else if (p.getImgTime_attack() < 9 * Person.slowConstant) {
				p.setImgId_attack(2);
			} else if (p.getImgTime_attack() < 13 * Person.slowConstant) {
				p.setImgId_attack(3);
			} else {
				p.setImgTime_attack(0); // ����ͼƬʱ��ָ�
				p.setImgId_attack(0); // ����ͼƬId�ָ�
				p.setAttack(false); // ��������
			}

			// �ж����ҵ�С�˻��ǵ���С��
			// ������ҵ�С��
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {

				g.drawImage(gi.getAttackRightImg()[p.getImgId_attack()], personImg_x, personImg_y, personImg_width,
						personImg_height, this);
			} else {
				// ����С��
				g.drawImage(gi.getEpAttackRightImg()[p.getImgId_attack()], personImg_x, personImg_y, personImg_width,
						personImg_height, this);
			}

			p.setImgTime_attack(p.getImgTime_attack() + 1);
		}
	}

	// ��С����׼״̬��·����
	public void drawPersonAimWalk(Graphics g, int personImg_x, int personImg_y, int personImg_width,
			int personImg_height, Person p) {
		// ���������
		if (p.getDirection().equals("left")) {
			if (p.getImgTime_walk() < 2 * Person.slowConstant) {
				p.setImgId_walk(0);
			} else if (p.getImgTime_walk() < 4 * Person.slowConstant) {
				p.setImgId_walk(1);
			} else if (p.getImgTime_walk() < 6 * Person.slowConstant) {
				p.setImgId_walk(2);
			} else if (p.getImgTime_walk() < 8 * Person.slowConstant) {
				p.setImgId_walk(3);
			} else if (p.getImgTime_walk() < 10 * Person.slowConstant) {
				p.setImgId_walk(4);
			} else if (p.getImgTime_walk() < 12 * Person.slowConstant) {
				p.setImgId_walk(5);
			} else if (p.getImgTime_walk() < 14 * Person.slowConstant) {
				p.setImgId_walk(6);
			} else if (p.getImgTime_walk() < 16 * Person.slowConstant) {
				p.setImgId_walk(7);
			} else if (p.getImgTime_walk() < 18 * Person.slowConstant) {
				p.setImgId_walk(8);
			} else if (p.getImgTime_walk() < 20 * Person.slowConstant) {
				p.setImgId_walk(9);
			} else {
				p.setImgTime_walk(0);
			}

			// �ж����ҵ�С�˻��ǵ���С��
			// ������ҵ�С��
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getAimWalkLeftImg()[p.getImgId_walk()], personImg_x, personImg_y, personImg_width,
						personImg_height, this);
			} else {
				// ����С��
				g.drawImage(gi.getEpAimWalkLeftImg()[p.getImgId_walk()], personImg_x, personImg_y, personImg_width,
						personImg_height, this);

			}
			p.setImgTime_walk(p.getImgTime_walk() + 1);
		}

		// ���������
		if (p.getDirection().equals("right")) {
			if (p.getImgTime_walk() < 2 * Person.slowConstant) {
				p.setImgId_walk(0);
			} else if (p.getImgTime_walk() < 4 * Person.slowConstant) {
				p.setImgId_walk(1);
			} else if (p.getImgTime_walk() < 6 * Person.slowConstant) {
				p.setImgId_walk(2);
			} else if (p.getImgTime_walk() < 8 * Person.slowConstant) {
				p.setImgId_walk(3);
			} else if (p.getImgTime_walk() < 10 * Person.slowConstant) {
				p.setImgId_walk(4);
			} else if (p.getImgTime_walk() < 12 * Person.slowConstant) {
				p.setImgId_walk(5);
			} else if (p.getImgTime_walk() < 14 * Person.slowConstant) {
				p.setImgId_walk(6);
			} else if (p.getImgTime_walk() < 16 * Person.slowConstant) {
				p.setImgId_walk(7);
			} else if (p.getImgTime_walk() < 18 * Person.slowConstant) {
				p.setImgId_walk(8);
			} else if (p.getImgTime_walk() < 20 * Person.slowConstant) {
				p.setImgId_walk(9);
			} else {
				p.setImgTime_walk(0);
			}

			// �ж����ҵ�С�˻��ǵ���С��
			// ������ҵ�С��
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getAimWalkRightImg()[p.getImgId_walk()], personImg_x, personImg_y, personImg_width,
						personImg_height, this);
			} else {
				// ����С��
				g.drawImage(gi.getEpAimWalkRightImg()[p.getImgId_walk()], personImg_x, personImg_y, personImg_width,
						personImg_height, this);
			}

			p.setImgTime_walk(p.getImgTime_walk() + 1);
		}
	}

	// ��С����׼״̬վ������
	public void drawPersonAimStand(Graphics g, int personImg_x, int personImg_y, int personImg_width,
			int personImg_height, Person p) {
		// ����վ��
		if (p.getDirection().equals("left")) {

			// �ж����ҵ�С�˻��ǵ���С��
			// ������ҵ�С��
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getAimStandLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			} else {
				// ����С��
				g.drawImage(gi.getEpAimStandLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height,
						this);
			}

		} else if (p.getDirection().equals("right")) {
			// ����վ��

			// �ж����ҵ�С�˻��ǵ���С��
			// ������ҵ�С��
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getAimStandRightImg(), personImg_x, personImg_y, personImg_width, personImg_height,
						this);
			} else {
				// ����С��
				g.drawImage(gi.getEpAimStandRightImg(), personImg_x, personImg_y, personImg_width, personImg_height,
						this);
			}
		}
	}

	// ��С����׼״̬��Ծ����
	public void drawPersonAimJump(Graphics g, int personImg_x, int personImg_y, int personImg_width,
			int personImg_height, Person p) {

		// ���������
		if (p.getDirection().equals("left")) {

			// �ж����ҵ�С�˻��ǵ���С��
			// ������ҵ�С��
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getAimJumpLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			} else {
				// ����С��
				g.drawImage(gi.getEpAimJumpLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height,
						this);
			}

		} else if (p.getDirection().equals("right")) {
			// ���������

			// �ж����ҵ�С�˻��ǵ���С��
			// ������ҵ�С��
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getAimJumpRightImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			} else {
				// ����С��
				g.drawImage(gi.getEpAimJumpRightImg(), personImg_x, personImg_y, personImg_width, personImg_height,
						this);
			}
		}
	}

	// ��С����׼״̬���䷽��
	public void drawPersonAimFall(Graphics g, int personImg_x, int personImg_y, int personImg_width,
			int personImg_height, Person p) {
		// �������
		if (p.getDirection().equals("left")) {

			// �ж����ҵ�С�˻��ǵ���С��
			// ������ҵ�С��
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getAimFallLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			} else {
				// ����С��
				g.drawImage(gi.getEpAimFallLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height,
						this);
			}

		} else if (p.getDirection().equals("right")) {
			// �������

			// �ж����ҵ�С�˻��ǵ���С��
			// ������ҵ�С��
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getAimFallRightImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			} else {
				// ����С��
				g.drawImage(gi.getEpAimFallRightImg(), personImg_x, personImg_y, personImg_width, personImg_height,
						this);
			}
		}
	}

	// ����������
	public void drawWeapon(Graphics g, Person p) {
		double angle; // ����ת���ĽǶȣ�ת��֮��ת����

		// ȡ������
		Weapon weapon = p.getWeapon();
		Graphics2D g2 = (Graphics2D) g;

		int weaponImg_x = weapon.getX();// �õ�����ͼƬ��x
		int weaponImg_y = weapon.getY(); // �õ�����ͼƬ��y
		int weaponImg_width = weapon.getWidth(); // �õ�����ͼƬ�Ŀ�
		int weaponImg_height = weapon.getHeight(); // �õ�����ͼƬ�ĸ�

		// ���ж���������
		// �������ǹ
		if (weapon.getWeaponType().equals("handgun")) {
			// ���ж�С�˷���
			// �������
			if (p.getDirection().equals("right")) {
				// ���÷��������������ת��
				angle = this.Imgrotate(g2, weapon.getX(), weapon.getY() + 3, p.goalX, p.goalY, "right");

				// �ж����ҵ�С�˻��ǵ���С��,����С�����������ֱ���ɫ��ͬ����ͼƬ��ͬ
				// ������ҵ�С��
				if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
					g2.drawImage(gi.getAimHandgunRightImg(), weaponImg_x, weaponImg_y, weaponImg_width,
							weaponImg_height, this);
				} else {
					// ����С��
					g2.drawImage(gi.getEpAimHandgunRightImg(), weaponImg_x, weaponImg_y, weaponImg_width,
							weaponImg_height, this);
				}

				// ��������ת����
				g2.rotate(-angle, weaponImg_x, weaponImg_y + 3);

			} else if (p.getDirection().equals("left")) {
				// �������

				// ���÷��������������ת��
				angle = this.Imgrotate(g2, weapon.getX() + weapon.getWidth(), weapon.getY() + 3, p.goalX, p.goalY,
						"left");

				// �ж����ҵ�С�˻��ǵ���С��,����С�����������ֱ���ɫ��ͬ����ͼƬ��ͬ
				// ������ҵ�С��
				if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
					g2.drawImage(gi.getAimHandgunLeftImg(), weaponImg_x, weaponImg_y, weaponImg_width, weaponImg_height,
							this);

				} else {
					// ����С��
					g2.drawImage(gi.getEpAimHandgunLeftImg(), weaponImg_x, weaponImg_y, weaponImg_width,
							weaponImg_height, this);
				}

				// ��������ת����
				g2.rotate(-angle, weapon.getX() + weapon.getWidth(), weapon.getY() + 3);
			}
		}

	}

	// ͼƬת������(����ת���Ļ���
	// ���ж�����λ�ã�ֻ��ת��һ�룬��Ҫ��PIת����һ��
	public double Imgrotate(Graphics2D g2, int x, int y, int goalX, int goalY, String direction) {
		double angle = 0; // ת���Ƕ�

		// ���ת������
		if (direction.equals("right")) {
			angle = Math.atan2(goalY - y, goalX - x);
		} else if (direction.equals("left")) {
			angle = Math.atan2(goalY - y, goalX - x) + Math.PI;
		}

		// ��ʼת��
		g2.rotate(angle, x, y);

		return angle;
	}

	public void run() {
		while (true) {
			
			// �ж��Ƿ����
			// ���������ֹͣ��Ϸ���������߳�
			// ��������Ϸ����߳�
			if(!this.isAlive) {
				this.mp.setAlive(false); // �����ҵ�С������
				
				for(int i = 0; i < this.vt_Ep.size(); i++) {
					EnemyPerson ep = this.vt_Ep.get(i); // ȡ������С��
					
					ep.setAlive(false); // ��������
				}

				break;
			}
			
			this.repaint();

			try {
				Thread.sleep(20);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// �����ù��ܣ���1����ӵ���
		if (arg0.getKeyCode() == KeyEvent.VK_1) {
			EnemyPerson ep = new EnemyPerson(300, 440, this);
			Thread t1 = new Thread(ep);
			t1.start();

			this.vt_Ep.add(ep);
		}

		if (arg0.getKeyCode() == KeyEvent.VK_2) {

		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}
