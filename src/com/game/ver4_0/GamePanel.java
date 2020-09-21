package com.game.ver4_0;

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
	Vector<EnemyPerson> vt_Ep; // ����С������
	int restEpNumbers; // ����С��ʣ������
	Map map; // ��ͼ
	GameImg gi; // ��ϷͼƬ��������ָ�����ϷͼƬ
	boolean isAlive; // �Ƿ����
	GameFrame gf; // ָ�룬ָ����Ϸ��ܣ���������Ϸ���������µ���Ϸ����
	int level; // �ؿ���
	Vector<Weapon> vt_wp; // ��������
	int score; // ������ÿ�صķ�������һ����߷֣�������Ϸ���У��������٣�ͨ�������ߵ�����ʾͨ�صĿ���

	public GamePanel(int level, GameFrame gf) {
		// ��Ϸ���ָ�봫��
		this.gf = gf;

		// �ؿ���
		this.level = level;

		// ע������¼�������,����ȷ����ͼ�ϰ�������
		this.addMouseListener(this);

		// ��ͼ
		map = new Map(level, this);

		// �ҵ�С��(x, y, weaponType, bulletNumber)
		mp = map.getMp();
		Thread t1 = new Thread(mp);
		t1.start();

		// ����С��
		vt_Ep = map.getVt_ep(); // ��õ���С������
		// ��������С���߳�
		for (int i = 0; i < vt_Ep.size(); i++) {
			Thread t2 = new Thread(vt_Ep.get(i));
			t2.start();
		}
		
		// ����С��ʣ��
		restEpNumbers = vt_Ep.size();

		// �ҵ�С��ע������¼�������
		this.addMouseListener(mp);
		this.addMouseMotionListener(mp);

		// ����Ĭ�ϻ���
		isAlive = true;

		// ���ԣ���������
		vt_wp = new Vector<Weapon>();
		// Weapon weapon = new Weapon(200, 480, 18, 12, "handgun", 2, this);
		// vt_wp.add(weapon);
		
		// ��ʼ������
		score = 10000;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// ������ͼƬ
		g.drawImage(gi.getBackgroundImg(), 0, 0, 1300, 700, this);
		
		//����Ϸ��ʾ
		this.drawTip(g);
		
		// ����
		// ������С����
		g.drawString(restEpNumbers + "", 100, 100);
		
		// ������
		g.drawString(this.score + "", 30, 30);

		// ���ҵ�����
		// ���ж��Ƿ�����׼״̬�����Ƿ����
		if (mp.isAim() && mp.isAlive()) {
			this.drawWeapon(g, mp);
		}

		// ���ҵ�С��
		// g.fillRect(mp.getX(), mp.getY(), mp.getWidth(), mp.getHeight()); // (������
		this.drawPerson(g, mp);

		// ����������(������
		// g.drawRect(mp.getX() - 10, mp.getY() + 10, 11, 6);

		// ������С�ˣ�����С�˵��ӵ�,����С������
		// ����һ�𻭻���Щ�ң�����ֻ�����һ�����С��
		for (int i = 0; i < vt_Ep.size(); i++) {
			// ȡ������С��
			EnemyPerson ep = vt_Ep.get(i);

			// �ж��Ƿ������׼״̬,�һ���
			if (ep.isAim() && ep.isAlive()) {

				// ��������С������
				this.drawWeapon(g, ep);

				// ��������С���������ʱ
				// �����ֵ��˲���ʾ����ʱ��������
				if(ep.getType()!=3) {
					g.drawString(ep.getShootTime() + "", ep.getX() + 10, ep.getY() - 5);
				}
			}

			// ��������С��
			this.drawPerson(g, ep);

			// ��������С�������־
			// �������С�˻���
			if (ep.isAlive()) {
				this.drawEpTypeLogo(g, ep);
			}

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

		}

		// ����Ϸ���������������������ӵ�
		for (int i = 0; i < vt_wp.size(); i++) {
			// ȡ������
			Weapon weapon = vt_wp.get(i);

			// �ж��Ƿ�����������
			// �����������Ż�
			if (!weapon.isPicked()) {
				this.drawGamePanelWeapon(weapon, g);

				// �����������ӵ�
				for (int j = 0; j < weapon.getVt_bullet().size(); j++) {
					// ȡ���ӵ�
					Bullet bullet = weapon.getVt_bullet().get(j);

					// �ж��ӵ��Ƿ����
					// ����
					if (bullet.isAlive()) {
						// �����ӵ�
						this.drawBullet(g, bullet);
					} else {
						// ���򣬴��ӵ�������ɾ��
						weapon.getVt_bullet().remove(bullet);
					}
				}

			} else {
				// ���򣬽������Ƴ�����������
				vt_wp.remove(weapon);
			}
		}

		// ����ͼ�ϰ���
		this.drawBarrier(g);

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
		int personImg_x;
		int personImg_y;
		int personImg_width;
		int personImg_height;

		if (!p.isSquat()) {
			// ��������¶�״̬����Ӧ��С��ͼƬ����
			personImg_x = p.getX() - 8; // �õ�С��ͼƬ��x
			personImg_y = p.getY(); // �õ�С��ͼƬ��y
			personImg_width = p.getHeight(); // �õ�С��ͼƬ�Ŀ�
			personImg_height = p.getHeight(); // �õ�С��ͼƬ�ĸ�
		} else {
			// ��������¶�״̬����Ӧ��С��ͼƬ����
			personImg_x = p.getX() - 8; // �õ�С��ͼƬ��x
			personImg_y = p.getY() - 16; // �õ�С��ͼƬ��y
			personImg_width = 50; // �õ�С��ͼƬ�Ŀ�
			personImg_height = 50; // �õ�С��ͼƬ�ĸ�
		}
		// �������
		if (p.isAlive()) {

			// ����·
			// �����·,ֻ��վ��״̬��·���Ҳ��ڹ���״̬��������׼״̬,�����¶�״̬
			if (p.isStanding() && !p.isAttack() && !p.isAim() && !p.isSquat()) {
				this.drawPersonWalk(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
			}

			// ����׼״̬��·
			// ֻ��վ��״̬�����ڹ���״̬�������¶�״̬������׼״̬,����������˶�
			else if (p.isStanding() && !p.isAttack() && p.isAim() && (p.isLeft() || p.isRight()) && !p.isSquat()) {
				this.drawPersonAimWalk(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
			}

			// ��վ��
			// ���վ��,����վ��״̬�Ҳ��߶����Ҳ��ڹ���״̬,������׼״̬�������¶�״̬
			if (p.isStanding() && !p.isLeft() && !p.isRight() && !p.isAttack() && !p.isAim() && !p.isSquat()) {
				this.drawPersonStand(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
			}

			// ����׼״̬վ��
			// ����վ��״̬�Ҳ��߶����Ҳ��ڹ���״̬,����׼״̬
			else if (p.isStanding() && !p.isLeft() && !p.isRight() && !p.isAttack() && p.isAim()) {
				this.drawPersonAimStand(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
			}

			// ����Ծ
			// �����Ծ��ֻ����Ծ״̬���Ҳ��ڹ���״̬,������׼״̬,�����¶�״̬
			if (p.isJumping() && !p.isAttack() && !p.isAim() && !p.isSquat()) {
				this.drawPersonJump(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
			}

			// ����׼״̬��Ծ
			// �����Ծ������Ծ״̬����׼״̬���Ҳ��ڹ���״̬,�����¶�״̬
			else if (p.isJumping() && p.isAim() && !p.isAttack() && !p.isSquat()) {
				this.drawPersonAimJump(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
			}

			// ����׼״̬����
			// ������䣬������״̬������׼״̬,�Ҳ��ڹ���״̬�������¶�״̬
			if (p.isFalling() && !p.isAttack() && p.isAim() && !p.isSquat()) {
				this.drawPersonAimFall(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
			}

			// ������
			// ������䣬ֻ������״̬���Ҳ��ڹ���״̬��������׼״̬,�����¶�״̬
			else if (p.isFalling() && !p.isAttack() && !p.isSquat()) {
				this.drawPersonFall(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
			}

			// ������
			if (p.isAttack()) {
				this.drawPersonAttack(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
			}

			// ���¶�
			// ����¶ף������¶�״̬
			if (p.isSquat()) {
				this.drawPersonSquat(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
			}
		}

		// ������
		// ���������
		if (!p.isAlive()) {
			this.drawPersonDie(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
		}

	}

	// ��С�������ƶ�����
	public void drawPersonWalk(Graphics g, int personImg_x, int personImg_y, int personImg_width, int personImg_height,
			Person p) {
		// ���������
		// ��������Ծ������
		// ���������
		if (p.isLeft()) {
			if (p.getImgTime_walk() < 3 * Person.slowConstant) {
				p.setImgId_walk(0);
			} else if (p.getImgTime_walk() < 6 * Person.slowConstant) {
				p.setImgId_walk(1);
			} else if (p.getImgTime_walk() < 9 * Person.slowConstant) {
				p.setImgId_walk(2);
			} else if (p.getImgTime_walk() < 12 * Person.slowConstant) {
				p.setImgId_walk(3);
			} else if (p.getImgTime_walk() < 15 * Person.slowConstant) {
				p.setImgId_walk(4);
			} else if (p.getImgTime_walk() < 18 * Person.slowConstant) {
				p.setImgId_walk(5);
			} else if (p.getImgTime_walk() < 21 * Person.slowConstant) {
				p.setImgId_walk(6);
			} else if (p.getImgTime_walk() < 24 * Person.slowConstant) {
				p.setImgId_walk(7);
			} else if (p.getImgTime_walk() < 27 * Person.slowConstant) {
				p.setImgId_walk(8);
			} else if (p.getImgTime_walk() < 30 * Person.slowConstant) {
				p.setImgId_walk(9);
			} else {
				p.setImgTime_walk(0);
			}

			// �ж�С�����ҵ�С���໹�ǵ���С���࣬����С��ͼƬ��ͬ
			// ������ҵ�С��
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
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
			if (p.getImgTime_walk() < 3 * Person.slowConstant) {
				p.setImgId_walk(0);
			} else if (p.getImgTime_walk() < 6 * Person.slowConstant) {
				p.setImgId_walk(1);
			} else if (p.getImgTime_walk() < 9 * Person.slowConstant) {
				p.setImgId_walk(2);
			} else if (p.getImgTime_walk() < 12 * Person.slowConstant) {
				p.setImgId_walk(3);
			} else if (p.getImgTime_walk() < 15 * Person.slowConstant) {
				p.setImgId_walk(4);
			} else if (p.getImgTime_walk() < 18 * Person.slowConstant) {
				p.setImgId_walk(5);
			} else if (p.getImgTime_walk() < 21 * Person.slowConstant) {
				p.setImgId_walk(6);
			} else if (p.getImgTime_walk() < 24 * Person.slowConstant) {
				p.setImgId_walk(7);
			} else if (p.getImgTime_walk() < 27 * Person.slowConstant) {
				p.setImgId_walk(8);
			} else if (p.getImgTime_walk() < 30 * Person.slowConstant) {
				p.setImgId_walk(9);
			} else {
				p.setImgTime_walk(0);
			}

			// �ж�С�����ҵ�С���໹�ǵ���С���࣬����С��ͼƬ��ͬ
			// ������ҵ�С��
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
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
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
				g.drawImage(gi.getStandLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			} else {
				g.drawImage(gi.getEpStandLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			}

		} else if (p.getDirection().equals("right")) {
			// ����վ��
			// �ж����ҵ�С�˻��ǵ���С��
			// ������ҵ�С��
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
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
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
				g.drawImage(gi.getJumpLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			} else {
				// ����ǵ���С��
				g.drawImage(gi.getEpJumpLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			}

		} else if (p.getDirection().equals("right")) {
			// �������

			// �ж����ҵ�С�˻��ǵ���С��
			// ������ҵ�С��
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
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
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
				g.drawImage(gi.getFallLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			} else {
				// ����С��
				g.drawImage(gi.getEpFallLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			}

		} else if (p.getDirection().equals("right")) {
			// �������

			// �ж����ҵ�С�˻��ǵ���С��
			// ������ҵ�С��
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
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
			} else if (p.getImgTime_attack() < 12 * Person.slowConstant) {
				p.setImgId_attack(3);
			} else {
				p.setImgTime_attack(0); // ����ͼƬʱ��ָ�
				p.setImgId_attack(0); // ����ͼƬId�ָ�
				p.setAttack(false); // ��������
			}

			// �ж����ҵ�С�˻��ǵ���С��
			// ������ҵ�С��
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
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
			} else if (p.getImgTime_attack() < 12 * Person.slowConstant) {
				p.setImgId_attack(3);
			} else {
				p.setImgTime_attack(0); // ����ͼƬʱ��ָ�
				p.setImgId_attack(0); // ����ͼƬId�ָ�
				p.setAttack(false); // ��������
			}

			// �ж����ҵ�С�˻��ǵ���С��
			// ������ҵ�С��
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {

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
			if (p.getImgTime_walk() < 3 * Person.slowConstant) {
				p.setImgId_walk(0);
			} else if (p.getImgTime_walk() < 6 * Person.slowConstant) {
				p.setImgId_walk(1);
			} else if (p.getImgTime_walk() < 9 * Person.slowConstant) {
				p.setImgId_walk(2);
			} else if (p.getImgTime_walk() < 12 * Person.slowConstant) {
				p.setImgId_walk(3);
			} else if (p.getImgTime_walk() < 15 * Person.slowConstant) {
				p.setImgId_walk(4);
			} else if (p.getImgTime_walk() < 18 * Person.slowConstant) {
				p.setImgId_walk(5);
			} else if (p.getImgTime_walk() < 21 * Person.slowConstant) {
				p.setImgId_walk(6);
			} else if (p.getImgTime_walk() < 24 * Person.slowConstant) {
				p.setImgId_walk(7);
			} else if (p.getImgTime_walk() < 27 * Person.slowConstant) {
				p.setImgId_walk(8);
			} else if (p.getImgTime_walk() < 30 * Person.slowConstant) {
				p.setImgId_walk(9);
			} else {
				p.setImgTime_walk(0);
			}

			// �ж����ҵ�С�˻��ǵ���С��
			// ������ҵ�С��
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
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
			if (p.getImgTime_walk() < 3 * Person.slowConstant) {
				p.setImgId_walk(0);
			} else if (p.getImgTime_walk() < 6 * Person.slowConstant) {
				p.setImgId_walk(1);
			} else if (p.getImgTime_walk() < 9 * Person.slowConstant) {
				p.setImgId_walk(2);
			} else if (p.getImgTime_walk() < 12 * Person.slowConstant) {
				p.setImgId_walk(3);
			} else if (p.getImgTime_walk() < 15 * Person.slowConstant) {
				p.setImgId_walk(4);
			} else if (p.getImgTime_walk() < 18 * Person.slowConstant) {
				p.setImgId_walk(5);
			} else if (p.getImgTime_walk() < 21 * Person.slowConstant) {
				p.setImgId_walk(6);
			} else if (p.getImgTime_walk() < 24 * Person.slowConstant) {
				p.setImgId_walk(7);
			} else if (p.getImgTime_walk() < 27 * Person.slowConstant) {
				p.setImgId_walk(8);
			} else if (p.getImgTime_walk() < 30 * Person.slowConstant) {
				p.setImgId_walk(9);
			} else {
				p.setImgTime_walk(0);
			}

			// �ж����ҵ�С�˻��ǵ���С��
			// ������ҵ�С��
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
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
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
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
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
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
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
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
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
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
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
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
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
				g.drawImage(gi.getAimFallRightImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			} else {
				// ����С��
				g.drawImage(gi.getEpAimFallRightImg(), personImg_x, personImg_y, personImg_width, personImg_height,
						this);
			}
		}
	}

	// ��С����������
	public void drawPersonDie(Graphics g, int personImg_x, int personImg_y, int personImg_width, int personImg_height,
			Person p) {
		// �������
		if (p.getDirection().equals("left")) {
			if (p.getImgTime_die() < 4) {
				p.setImgId_die(0);
			} else if (p.getImgTime_die() < 8) {
				p.setImgId_die(1);
			} else if (p.getImgTime_die() < 12) {
				p.setImgId_die(2);
			} else if (p.getImgTime_die() < 16) {
				p.setImgId_die(3);
			} else if (p.getImgTime_die() < 20) {
				p.setImgId_die(4);
			} else if (p.getImgTime_die() < 24) {
				p.setImgId_die(5);
			} else if (p.getImgTime_die() < 28) {
				p.setImgId_die(6);
			} else if (p.getImgTime_die() < 32) {
				p.setImgId_die(7);
			} else if (p.getImgTime_die() < 36) {
				p.setImgId_die(8);
			} else if (p.getImgTime_die() < 40) {
				p.setImgId_die(9);
			} else {
				p.setImgId_die(10);
			}

			// ������ҵ�С��
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
				g.drawImage(gi.getDieLeftImg()[p.getImgId_die()], personImg_x, personImg_y, personImg_width,
						personImg_height, this);
			} else {
				// ����ǵ���С��
				g.drawImage(gi.getEpDieLeftImg()[p.getImgId_die()], personImg_x, personImg_y, personImg_width,
						personImg_height, this);
			}

			// System.out.println("time = " + p.getImgTime_die() + " i = " + i++);
			// System.out.println("Id = " + p.getImgId_die());

			p.setImgTime_die(p.getImgTime_die() + 1);

		} else {
			// �������
			if (p.getImgTime_die() < 4) {
				p.setImgId_die(0);
			} else if (p.getImgTime_die() < 8) {
				p.setImgId_die(1);
			} else if (p.getImgTime_die() < 12) {
				p.setImgId_die(2);
			} else if (p.getImgTime_die() < 16) {
				p.setImgId_die(3);
			} else if (p.getImgTime_die() < 20) {
				p.setImgId_die(4);
			} else if (p.getImgTime_die() < 24) {
				p.setImgId_die(5);
			} else if (p.getImgTime_die() < 28) {
				p.setImgId_die(6);
			} else if (p.getImgTime_die() < 32) {
				p.setImgId_die(7);
			} else if (p.getImgTime_die() < 36) {
				p.setImgId_die(8);
			} else if (p.getImgTime_die() < 40) {
				p.setImgId_die(9);
			} else {
				p.setImgId_die(10);
			}

			// ������ҵ�С��
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
				g.drawImage(gi.getDieRightImg()[p.getImgId_die()], personImg_x, personImg_y, personImg_width,
						personImg_height, this);
			} else {
				// ����ǵ���С��
				g.drawImage(gi.getEpDieRightImg()[p.getImgId_die()], personImg_x, personImg_y, personImg_width,
						personImg_height, this);
			}

			// System.out.println("time = " + p.getImgTime_die() + " i = " + i++);
			// System.out.println("Id = " + p.getImgId_die());

			p.setImgTime_die(p.getImgTime_die() + 1);
		}
	}

	// ��С���¶׷���
	public void drawPersonSquat(Graphics g, int personImg_x, int personImg_y, int personImg_width, int personImg_height,
			Person p) {

		// ���ж�С����Է���
		// �������
		if (p.getDirection().equals("left")) {
			g.drawImage(gi.getSquatLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
		} else {
			// �������
			g.drawImage(gi.getSquatRightImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
		}

	}

	
	
	public void drawWeapon(Graphics g, Person p) {
		double angle; // ����ת���ĽǶȣ�ת��֮��ת����

		// ȡ������
		Weapon weapon = p.getWeapon();
		Graphics2D g2 = (Graphics2D) g;

		int weaponImg_x = weapon.getX();// �õ�����ͼƬ��x
		int weaponImg_y = weapon.getY(); // �õ�����ͼƬ��y
		int weaponImg_width = weapon.getWidth(); // �õ�����ͼƬ�Ŀ�
		int weaponImg_height = weapon.getHeight(); // �õ�����ͼƬ�ĸ�

		// ���ж�С�˷���
		// �������
		if (p.getDirection().equals("right")) {
			// ���÷��������������ת��
			angle = this.Imgrotate(g2, weapon.getX(), weapon.getY() + 3, p.goalX, p.goalY, "right");

			// �ж��������ͣ���ͬ����ͼƬ��ͬ
			// �������ǹ
			if (weapon.getWeaponType().equals("handgun")) {
				// �ж����ҵ�С�˻��ǵ���С��,����С�����������ֱ���ɫ��ͬ����ͼƬ��ͬ
				// ������ҵ�С��
				if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
					g2.drawImage(gi.getAimHandgunRightImg(), weaponImg_x, weaponImg_y, weaponImg_width,
							weaponImg_height, this);
				} else {
					// ����С��
					g2.drawImage(gi.getEpAimHandgunRightImg(), weaponImg_x, weaponImg_y, weaponImg_width,
							weaponImg_height, this);
				}
				
			} else if (weapon.getWeaponType().equals("hand")) {
				// ����ǿ���
				//  ������ҵ�С��
				if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
					g2.drawImage(gi.getAimHandRightImg(), weaponImg_x, weaponImg_y, weaponImg_width, weaponImg_height,
							this);
				} else {
					// ����С��
					g2.drawImage(gi.getEpAimHandRightImg(), weaponImg_x, weaponImg_y, weaponImg_width, weaponImg_height,
							this);
				}

			} else if (weapon.getWeaponType().equals("shotgun")) {
				// ���������
				// ������ҵ�С��
				if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
					g2.drawImage(gi.getAimShotgunRightImg(), weaponImg_x, weaponImg_y, weaponImg_width, weaponImg_height,
							this);
				} else {
					// ����С��
					g2.drawImage(gi.getEpAimShotgunRightImg(), weaponImg_x, weaponImg_y, weaponImg_width, weaponImg_height,
							this);
				}
			}
			// ��������ת����
			g2.rotate(-angle, weaponImg_x, weaponImg_y + 3);

		} else if (p.getDirection().equals("left")) {
			// �������

			// ���÷��������������ת��
			angle = this.Imgrotate(g2, weapon.getX() + weapon.getWidth(), weapon.getY() + 3, p.goalX, p.goalY, "left");

			// �������ǹ
			if (weapon.getWeaponType().equals("handgun")) {
				// �ж����ҵ�С�˻��ǵ���С��,����С�����������ֱ���ɫ��ͬ����ͼƬ��ͬ
				// ������ҵ�С��
				if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
					g2.drawImage(gi.getAimHandgunLeftImg(), weaponImg_x, weaponImg_y, weaponImg_width, weaponImg_height,
							this);

				} else {
					// ����С��
					g2.drawImage(gi.getEpAimHandgunLeftImg(), weaponImg_x, weaponImg_y, weaponImg_width,
							weaponImg_height, this);
				}
				
			} else if (weapon.getWeaponType().equals("hand")) {
				// ����ǿ���
				if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
					g2.drawImage(gi.getAimHandLeftImg(), weaponImg_x, weaponImg_y, weaponImg_width, weaponImg_height,
							this);

				} else {
					// ����С��
					g2.drawImage(gi.getEpAimHandLeftImg(), weaponImg_x, weaponImg_y, weaponImg_width, weaponImg_height,
							this);
				}
			} else if (weapon.getWeaponType().equals("shotgun")) {
				// ���������
				// ������ҵ�С��
				if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
					g2.drawImage(gi.getAimShotgunLeftImg(), weaponImg_x, weaponImg_y, weaponImg_width, weaponImg_height,
							this);
				} else {
					// ����С��
					g2.drawImage(gi.getEpAimShotgunLeftImg(), weaponImg_x, weaponImg_y, weaponImg_width, weaponImg_height,
							this);
				}
			}
			// ��������ת����
			g2.rotate(-angle, weapon.getX() + weapon.getWidth(), weapon.getY() + 3);
		}
	}
			

	// ͼƬת������(����ת���Ļ���
	// ���ж�����λ�ã�ֻ��ת����һ�룬��Ҫ��PIת����һ��
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

	// ����Ϸ��������ķ���
	public void drawGamePanelWeapon(Weapon weapon, Graphics g) {
		// �ж���������
		// �������ǹ
		if (weapon.getWeaponType().equals("handgun")) {
			g.drawImage(gi.getWeaponHandgunImg(), weapon.getX(), weapon.getY(), weapon.getWidth(), weapon.getHeight(),
					this);
		} else if (weapon.getWeaponType().equals("shotgun")) {
			//  ���������
			g.drawImage(gi.getWeaponShotgunImg(), weapon.getX(), weapon.getY(), weapon.getWidth(), weapon.getHeight(), this);
		}
	}

	public void drawEpTypeLogo(Graphics g, EnemyPerson ep) {
		int drawLogoImg_x = ep.getX() + 5;
		int drawLogoImg_y = ep.getY() - 37;
		int drawLogoImg_width = 25;
		int drawLogoImg_height = 25;

		if (ep.getType() == 1) {
			if ((ep.isLeft() || ep.isStanding() || ep.isFalling() || ep.isJumping())
					&& ep.getDirection().equals("left")) {
				g.drawImage(gi.getEpTypeImg()[ep.getType() - 1], drawLogoImg_x - 3, drawLogoImg_y, drawLogoImg_width,
						drawLogoImg_height, this);
			} else if ((ep.isLeft() || ep.isStanding() || ep.isFalling() || ep.isJumping())
					&& ep.getDirection().equals("right")) {
				g.drawImage(gi.getEpTypeImg()[ep.getType() - 1], drawLogoImg_x + 4, drawLogoImg_y, drawLogoImg_width,
						drawLogoImg_height, this);
			}
		} else if (ep.getType() == 2) {
			if ((ep.isLeft() || ep.isStanding() || ep.isFalling() || ep.isJumping())
					&& ep.getDirection().equals("left")) {
				g.drawImage(gi.getEpTypeImg()[ep.getType() - 1], drawLogoImg_x - 3, drawLogoImg_y, drawLogoImg_width,
						drawLogoImg_height, this);
			} else if ((ep.isLeft() || ep.isStanding() || ep.isFalling() || ep.isJumping())
					&& ep.getDirection().equals("right")) {
				g.drawImage(gi.getEpTypeImg()[ep.getType() - 1], drawLogoImg_x + 4, drawLogoImg_y, drawLogoImg_width,
						drawLogoImg_height, this);
			}

		} else if (ep.getType() == 3) {
			if ((ep.isLeft() || ep.isStanding() || ep.isFalling() || ep.isJumping())
					&& ep.getDirection().equals("left")) {
				g.drawImage(gi.getEpTypeImg()[ep.getType() - 1], drawLogoImg_x - 5, drawLogoImg_y, drawLogoImg_width,
						drawLogoImg_height, this);
			} else if ((ep.isLeft() || ep.isStanding() || ep.isFalling() || ep.isJumping())
					&& ep.getDirection().equals("right")) {
				g.drawImage(gi.getEpTypeImg()[ep.getType() - 1], drawLogoImg_x + 6, drawLogoImg_y, drawLogoImg_width,
						drawLogoImg_height, this);
			}
		} else if (ep.getType() == 4) {
			if ((ep.isLeft() || ep.isStanding() || ep.isFalling() || ep.isJumping())
					&& ep.getDirection().equals("left")) {
				g.drawImage(gi.getEpTypeImg()[ep.getType() - 1], drawLogoImg_x - 5, drawLogoImg_y, drawLogoImg_width,
						drawLogoImg_height, this);
			} else if ((ep.isLeft() || ep.isStanding() || ep.isFalling() || ep.isJumping())
					&& ep.getDirection().equals("right")) {
				g.drawImage(gi.getEpTypeImg()[ep.getType() - 1], drawLogoImg_x + 6, drawLogoImg_y, drawLogoImg_width,
						drawLogoImg_height, this);
			}
		}
	}
	//����ͼ�������Ϸ��ʾ
		public void drawTip(Graphics g) {
			if(level==1) {
				this.drawTip_level_1(g);
			}
		}
	//����Ϸ��һ����ʾ
	public void drawTip_level_1(Graphics g) {
		int buttom_width_height=30;
		int interval=35;
		int all_move_X=0;//�ƶ���������x�ᣨ��������Ķ���
		int all_move_Y=0;//�ƶ���������y�� ����������Ķ���
		Color buttom=new Color(81,81,81);
		Color white=Color.WHITE;
		Color primaryColor=new Color(51,51,51);//Graphics�ĳ�ʼ��ɫ
		Font primaryFont=new Font("Dialog",Font.PLAIN,12);//Graphics�ĳ�ʼ����
		Font buttomword_1=new Font("����", Font.BOLD, 20);
		Font buttomword_2=new Font("����",Font.CENTER_BASELINE,17);
		Font buttomword_3=new Font("����", Font.BOLD, 40);
		Font text=new Font("΢���ź�",Font.CENTER_BASELINE,20);
		
		g.setColor(buttom);
		g.fillRect(985+all_move_X, interval+all_move_Y, buttom_width_height, buttom_width_height);//��A������
		g.fillRect(1025+all_move_X, interval+all_move_Y, buttom_width_height, buttom_width_height);//��D������
		g.fillRect(1005+all_move_X, interval*2+all_move_Y, buttom_width_height, buttom_width_height);//��S������
		g.fillRect(960+all_move_X, interval*3+all_move_Y, buttom_width_height*4, buttom_width_height);//��SPACE������
		g.fillRect(1005+all_move_X, interval*4+all_move_Y, buttom_width_height, buttom_width_height);//��E������
		g.fillRect(990+all_move_X, interval*5+all_move_Y, buttom_width_height*2, buttom_width_height);//��ESC������
		
		g.drawImage(gi.getTipImg()[1], 1005+all_move_X, interval*6+all_move_Y, buttom_width_height, buttom_width_height, this);
		g.drawImage(gi.getTipImg()[0], 1005+all_move_X, interval*7+all_move_Y, buttom_width_height, buttom_width_height, this);
		g.drawImage(gi.getTipImg()[1], 975+all_move_X, interval*8+all_move_Y, buttom_width_height, buttom_width_height, this);		
		g.drawImage(gi.getTipImg()[0], 1035+all_move_X, interval*8+all_move_Y, buttom_width_height, buttom_width_height, this);
		
//		g.drawString("+", 1008+all_move_X, 30+interval*7+all_move_Y);
		g.setFont(buttomword_3);;
		g.drawString("+", 1006+all_move_X, 28+interval*8+all_move_Y);

		
		g.setColor(white);
		g.setFont(buttomword_1);
		g.drawString("A", 994+all_move_X, 21+interval+all_move_Y);
		g.drawString("D", 1033+all_move_X, 21+interval+all_move_Y);
		g.drawString("S", 1014+all_move_X, 21+interval*2+all_move_Y);
		g.drawString("E",1014+all_move_X,21+interval*4+all_move_Y);
		g.setFont(buttomword_2);
		g.drawString("SPACE", 995+all_move_X, 21+interval*3+all_move_Y);
		g.drawString("ESC", 1007+all_move_X, 21+interval*5+all_move_Y);
		
		g.setFont(text);
		g.setColor(buttom);
		g.drawString("�����ƶ�", 1093+all_move_X, 22+interval+all_move_Y);
		g.drawString("�¶�", 1113+all_move_X, 22+interval*2+all_move_Y);
		g.drawString("��Ծ", 1113+all_move_X, 22+interval*3+all_move_Y);
		g.drawString("������", 1104+all_move_X, 22+interval*4+all_move_Y);
		g.drawString("�˳�", 1113+all_move_X, 22+interval*5+all_move_Y);
		g.drawString("��ǹ", 1113+all_move_X, 22+interval*6+all_move_Y);
		g.drawString("��ȭ", 1113+all_move_X, 22+interval*7+all_move_Y);
		g.drawString("���", 1113+all_move_X, 22+interval*8+all_move_Y);
		
		g.setFont(primaryFont);
		g.setColor(primaryColor);
	}

	public void run() {
		while (true) {

			// �ж��Ƿ����
			// ���������ֹͣ��Ϸ���������߳�
			// ��������Ϸ����߳�
			if (!this.isAlive) {
				this.mp.setAlive(false); // �����ҵ�С������
				this.mp.setStanding(true); // ����վ���ϰ�����

				for (int i = 0; i < this.vt_Ep.size(); i++) {
					EnemyPerson ep = this.vt_Ep.get(i); // ȡ������С��

					ep.setAlive(false); // ��������
					ep.setStanding(true); // ����վ���ϰ����ϣ�����������ǽ����߳�
				}

				// ��Ϸ�����������ӵ�ȫ������
				for(int j = 0; j < this.vt_wp.size(); j++) {
					// ȡ������
					Weapon weapon = this.vt_wp.get(j);
					
					for(int k = 0; k < weapon.getVt_bullet().size(); k++) {
						// ��������
						Bullet bullet = weapon.getVt_bullet().get(k);
						bullet.setAlive(false);
					}
				}
				
				break;
			}

			this.repaint();

			// ���е���ʱ
			if(this.restEpNumbers != 0) {
				// ÿѭ��һ�Σ���������
				score -= 10;
			}
			
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
		// �����ù��ܣ���1����ӵ���,ֻ�ڵ�һ������
		if (arg0.getKeyCode() == KeyEvent.VK_1) {
			if (this.level == 1 || this.level == 2) {
				restEpNumbers++; // ʣ�����������

				EnemyPerson ep = new EnemyPerson(300, 440, 0, 1, 2, this);
				Thread t1 = new Thread(ep);
				t1.start();

				this.vt_Ep.add(ep);
			}
		}

		// R���ؿ�����
		else if (arg0.getKeyCode() == KeyEvent.VK_R) {
			// �����µ���Ϸ���
			GamePanel gp = new GamePanel(this.level, gf);
			// ��ϷͼƬ�������
			gp.setGi(gi);
			// ����
			Thread t1 = new Thread(gp);
			t1.start();
			// ����µ���Ϸ�����¼�����
			gf.addKeyListener(gp);
			// ����µ���Ϸ����ҵ�С�˵��¼�����
			gf.addKeyListener(gp.mp);

			// ɾ���ɵ���Ϸ���
			gf.remove(this);
			// ���þɵ���Ϸ���Ϊ����
			this.isAlive = false;
			// ɾ���ɵ���Ϸ�����¼�����
			gf.removeKeyListener(this);
			// ɾ���ɵ���Ϸ����ҵ�С�˵��¼�����
			gf.removeKeyListener(this.mp);

			// ����µ���Ϸ���
			gf.add(gp);
			gf.setVisible(true);
		} 
		else if (arg0.getKeyCode() == KeyEvent.VK_G) {
			// ��û�е���ʱ��F��������һ�ء�
			if (this.restEpNumbers == 0) {
				// �����µ���Ϸ���
				GamePanel gp = new GamePanel(this.level + 1, gf);
				// ��ϷͼƬ�������
				gp.setGi(gi);
				// ����
				Thread t1 = new Thread(gp);
				t1.start();
				// ����µ���Ϸ�����¼�����
				gf.addKeyListener(gp);
				// ����µ���Ϸ����ҵ�С�˵��¼�����
				gf.addKeyListener(gp.mp);

				// ɾ���ɵ���Ϸ���
				gf.remove(this);
				// ���þɵ���Ϸ���Ϊ����
				this.isAlive = false;
				// ɾ���ɵ���Ϸ�����¼�����
				gf.removeKeyListener(this);
				// ɾ���ɵ���Ϸ����ҵ�С�˵��¼�����
				gf.removeKeyListener(this.mp);

				// ����µ���Ϸ���
				gf.add(gp);
				gf.setVisible(true);
			}
		}
		// ��esc���˳���Ϸ���
		else if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {

			// ɾ����Ϸ���
			gf.remove(this);
			// ������Ϸ���Ϊ����
			this.isAlive = false;
			// ɾ����Ϸ�����¼�����
			gf.removeKeyListener(this);
			// ɾ����Ϸ����ҵ�С�˵��¼�����
			gf.removeKeyListener(this.mp);

			// ��ӿ�ʼ���
			gf.add(gf.it);
			// ��ӿ�ʼ���ļ����¼�����
			gf.addKeyListener(gf.it);

			gf.setVisible(true);
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

	public void setGi(GameImg gi) {
		this.gi = gi;
	}
}
