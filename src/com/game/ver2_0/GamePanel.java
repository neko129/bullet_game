package com.game.ver2_0;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

// 游戏面板
class GamePanel extends JPanel implements Runnable, MouseListener, KeyListener {
	MyPerson mp; // 我的小人
	EnemyPerson ep; // 敌人小人
	Vector<EnemyPerson> vt_Ep; // 向量放敌人
	Map map; // 地图
	GameImg gi; // 游戏图片，保存各种各样游戏图片
	boolean isAlive; // 是否活着

	// int imgTime_walk; // 走路图片时间
	// int imgId_walk; // 走路图片id，对应第几张图片
	//
	// int imgTime_attack; // 攻击图片时间
	// int imgId_attack; // 攻击图片id


	public GamePanel() {
		// 游戏图片对象
		gi = new GameImg();

		// 地图
		map = new Map();

		// 注册鼠标事件监听者,用于确定地图障碍物坐标
		this.addMouseListener(this);

		// 我的小人
		mp = new MyPerson(600, 450, this);
		Thread t1 = new Thread(mp);
		t1.start();

		// 我的小人注册鼠标事件监听者
		this.addMouseListener(mp);
		this.addMouseMotionListener(mp);

		
		// 敌人小人
		
		vt_Ep = new Vector<EnemyPerson>(); // 敌人小人向量
		
		// 创建敌人小人
		ep = new EnemyPerson(300, 210, this);
		// 启动线程
		Thread t2 = new Thread(ep);
		t2.start();
		vt_Ep.add(ep); // 放进敌人小人向量
		
//		// 创建敌人小人
		ep = new EnemyPerson(300, 420, this);
		// 启动线程
		Thread t3 = new Thread(ep);
		t3.start();
		vt_Ep.add(ep); // 放进敌人小人向量
		

		// 创建敌人小人
		ep = new EnemyPerson(300, 310, this);
		// 启动线程
		Thread t4 = new Thread(ep);
		t4.start();
		vt_Ep.add(ep); // 放进敌人小人向量
		
		// 创建敌人小人
		ep = new EnemyPerson(800, 210, this);
		// 启动线程
		Thread t5 = new Thread(ep);
		t5.start();
		vt_Ep.add(ep); // 放进敌人小人向量
		
		// 创建敌人小人
//		ep = new EnemyPerson(800, 410, this);
//		// 启动线程
//		Thread t6 = new Thread(ep);
//		t6.start();
//		vt_Ep.add(ep); // 放进敌人小人向量
		
		// 创建敌人小人
		ep = new EnemyPerson(800, 310, this);
		// 启动线程
		Thread t7 = new Thread(ep);
		t7.start();
		vt_Ep.add(ep); // 放进敌人小人向量
		
		
		
		// 设置默认活着 
		isAlive  = true;
	}

	public void paint(Graphics g) {
		super.paint(g);

		// 画背景图片
		g.drawImage(gi.getBackgroundImg(), 0, 0, 1300, 700, this);

		// 画我的武器
		// 先判断是否是瞄准状态
		if (mp.isAim()) {
			this.drawWeapon(g, mp);
		}

		// 画我的小人
		// (测试用
		// g.fillRect(mp.getX(), mp.getY(), mp.getWidth(), mp.getHeight());
		if (mp.isAlive()) {
			// 判断是否活着
			this.drawPerson(g, mp);
		}
		// 画攻击对象(测试用
		// g.drawRect(mp.getX() - 10, mp.getY() + 10, 11, 6);

		// 画敌人小人，敌人小人的子弹,敌人小人武器
		// 混在一起会有些乱，不过只需遍历一遍敌人小人
		for (int i = 0; i < vt_Ep.size(); i++) {
			// 取出敌人小人
			EnemyPerson ep = vt_Ep.get(i);

			// 先判断敌人小人是否活着
			// 如果活着
			if (ep.isAlive()) {

				// 判断是否进入瞄准状态
				if (ep.isAim()) {

					// 画出敌人小人武器
					this.drawWeapon(g, ep);

					// 画出敌人小人射击倒计时
					g.drawString(ep.getShootTime() + "", ep.getX() + 10, ep.getY() - 5);
				}
				// 画出敌人小人
				this.drawPerson(g, ep);

				// 然后再画出敌人小人子弹
				for (int j = 0; j < ep.getWeapon().getVt_bullet().size(); j++) {
					// 取出子弹
					Bullet bullet = ep.getWeapon().getVt_bullet().get(j);

					// 判断子弹是否活着
					// 如果活着，画子弹
					if (bullet.isAlive()) {
						// 画子弹
						this.drawBullet(g, bullet);
					} else {
						// 否则，在子弹向量中删除子弹
						ep.getWeapon().getVt_bullet().remove(bullet);
					}
				}

			} else {
				// 如果敌人死了，从敌人小人向量中删除
				this.vt_Ep.remove(ep);
			}
		}

		// 画地图障碍物
		this.drawBarrier(g);

		// 试着画子弹
		// 我的小人的子弹
		// 先判断我的小人是否活着
		if (mp.isAlive()) {
			for (int i = 0; i < mp.getWeapon().getVt_bullet().size(); i++) {
				// 取出子弹
				Bullet bullet = mp.getWeapon().getVt_bullet().get(i);

				// 判断子弹是否活着
				// 如果活着，画子弹
				if (bullet.isAlive()) {
					// 画子弹
					this.drawBullet(g, bullet);
				} else {
					// 否则，在子弹向量中删除子弹
					mp.getWeapon().getVt_bullet().remove(bullet);
				}
			}
		}
	}

	// 画地图障碍物方法
	public void drawBarrier(Graphics g) {
		for (int i = 0; i < map.getNumber(); i++) {
			// 取出地图障碍物
			Barrier barrier = map.getVt_Barrier().get(i);
			g.drawImage(gi.getBarrierImg()[barrier.getId()], barrier.getX(), barrier.getY(), barrier.getWidth(),
					barrier.getHeight(), this);
		}
	}

	// 画子弹方法
	public void drawBullet(Graphics g, Bullet bullet) {
		g.fillRect((int) bullet.getX(), (int) bullet.getY(), bullet.getWidth(), bullet.getHeight());
	}

	// 画小人方法
	public void drawPerson(Graphics g, Person p) {
		int personImg_x = p.getX() - 8; // 得到小人图片的x
		int personImg_y = p.getY(); // 得到小人图片的y
		int personImg_width = p.getHeight(); // 得到小人图片的宽
		int personImg_height = p.getHeight(); // 得到小人图片的高

		// 画走路
		// 如果走路,只在站立状态走路，且不在攻击状态，不在瞄准状态
		if (p.isStanding() && !p.isAttack() && !p.isAim()) {
			this.drawPersonWalk(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
		}

		// 画瞄准状态走路
		// 只在站立状态，不在攻击状态，在瞄准状态,向左或向右运动
		else if (p.isStanding() && !p.isAttack() && p.isAim() && (p.isLeft() || p.isRight())) {
			this.drawPersonAimWalk(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
		}

		// 画站立
		// 如果站立,处于站立状态且不走动，且不在攻击状态,不在瞄准状态
		if (p.isStanding() && !p.isLeft() && !p.isRight() && !p.isAttack() && !p.isAim()) {
			this.drawPersonStand(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
		}

		// 画瞄准状态站立
		// 处于站立状态且不走动，且不在攻击状态,在瞄准状态
		else if (p.isStanding() && !p.isLeft() && !p.isRight() && !p.isAttack() && p.isAim()) {
			this.drawPersonAimStand(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
		}

		// 画跳跃
		// 如果跳跃，只在跳跃状态，且不在攻击状态,不在瞄准状态
		if (p.isJumping() && !p.isAttack() && !p.isAim()) {
			this.drawPersonJump(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
		}

		// 画瞄准状态跳跃
		// 如果跳跃，在跳跃状态，瞄准状态，且不在攻击状态
		else if (p.isJumping() && p.isAim() && !p.isAttack()) {
			this.drawPersonAimJump(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
		}

		// 画瞄准状态下落
		// 如果下落，在下落状态，在瞄准状态,且不在攻击状态
		if (p.isFalling() && !p.isAttack() && p.isAim()) {
			this.drawPersonAimFall(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
		}

		// 画下落
		// 如果下落，只在下落状态，且不在攻击状态，不在瞄准状态
		else if (p.isFalling() && !p.isAttack()) {
			this.drawPersonFall(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
		}

		// 画攻击
		if (p.isAttack()) {
			this.drawPersonAttack(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
		}

	}

	// 画小人左右移动方法
	public void drawPersonWalk(Graphics g, int personImg_x, int personImg_y, int personImg_width, int personImg_height,
			Person p) {
		// 左右走情况
		// 不包括跳跃和下落
		// 如果向左走
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

			// 判断小人是我的小人类还是敌人小人类，两种小人图片不同
			// 如果是我的小人
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getWalkLeftImg()[p.getImgId_walk()], personImg_x, personImg_y, personImg_width,
						personImg_height, this);
			} else {
				// 如果是敌人小人
				g.drawImage(gi.getEpWalkLeftImg()[p.getImgId_walk()], personImg_x, personImg_y, personImg_width,
						personImg_height, this);
			}

			p.setImgTime_walk(p.getImgTime_walk() + 1);
		}

		// 如果向右走
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

			// 判断小人是我的小人类还是敌人小人类，两种小人图片不同
			// 如果是我的小人
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getWalkRightImg()[p.getImgId_walk()], personImg_x, personImg_y, personImg_width,
						personImg_height, this);
			} else {
				// 如果是敌人小人
				g.drawImage(gi.getEpWalkRightImg()[p.getImgId_walk()], personImg_x, personImg_y, personImg_width,
						personImg_height, this);
			}
			p.setImgTime_walk(p.getImgTime_walk() + 1);
		}
	}

	// 画小人站立方法
	public void drawPersonStand(Graphics g, int personImg_x, int personImg_y, int personImg_width, int personImg_height,
			Person p) {
		// 向左站立
		if (p.getDirection().equals("left")) {

			// 判断是我的小人还是敌人小人
			// 如果是我的小人
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getStandLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			} else {
				g.drawImage(gi.getEpStandLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			}

		} else if (p.getDirection().equals("right")) {
			// 向右站立
			// 判断是我的小人还是敌人小人
			// 如果是我的小人
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getStandRightImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			} else {
				// 如果是敌人小人
				g.drawImage(gi.getEpStandRightImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			}
		}
	}

	// 画小人跳跃方法
	public void drawPersonJump(Graphics g, int personImg_x, int personImg_y, int personImg_width, int personImg_height,
			Person p) {
		// 如果向左
		if (p.getDirection().equals("left")) {

			// 判断是我的小人还是敌人小人
			// 如果是我的小人
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getJumpLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			} else {
				// 如果是敌人小人
				g.drawImage(gi.getEpJumpLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			}

		} else if (p.getDirection().equals("right")) {
			// 如果向右

			// 判断是我的小人还是敌人小人
			// 如果是我的小人
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getJumpRightImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			} else {
				// 敌人小人
				g.drawImage(gi.getEpJumpRightImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			}
		}
	}

	// 画小人下落方法
	public void drawPersonFall(Graphics g, int personImg_x, int personImg_y, int personImg_width, int personImg_height,
			Person p) {
		// 如果向左
		if (p.getDirection().equals("left")) {

			// 判断是我的小人还是敌人小人
			// 如果是我的小人
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getFallLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			} else {
				// 敌人小人
				g.drawImage(gi.getEpFallLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			}

		} else if (p.getDirection().equals("right")) {
			// 如果向右

			// 判断是我的小人还是敌人小人
			// 如果是我的小人
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getFallRightImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			} else {
				// 敌人小人
				g.drawImage(gi.getEpFallRightImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			}
		}
	}

	// 画小人出拳攻击方法
	public void drawPersonAttack(Graphics g, int personImg_x, int personImg_y, int personImg_width,
			int personImg_height, Person p) {
		// 先判断方向
		// 如果向左
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
				p.setImgTime_attack(0); // 攻击图片时间恢复
				p.setImgId_attack(0); // 攻击图片Id恢复
				p.setAttack(false); // 攻击结束
			}

			// 判断是我的小人还是敌人小人
			// 如果是我的小人
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getAttackLeftImg()[p.getImgId_attack()], personImg_x, personImg_y, personImg_width,
						personImg_height, this);
			} else {
				// 敌人小人
				g.drawImage(gi.getEpAttackLeftImg()[p.getImgId_attack()], personImg_x, personImg_y, personImg_width,
						personImg_height, this);
			}
			p.setImgTime_attack(p.getImgTime_attack() + 1);
		}

		// 如果向右
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
				p.setImgTime_attack(0); // 攻击图片时间恢复
				p.setImgId_attack(0); // 攻击图片Id恢复
				p.setAttack(false); // 攻击结束
			}

			// 判断是我的小人还是敌人小人
			// 如果是我的小人
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {

				g.drawImage(gi.getAttackRightImg()[p.getImgId_attack()], personImg_x, personImg_y, personImg_width,
						personImg_height, this);
			} else {
				// 敌人小人
				g.drawImage(gi.getEpAttackRightImg()[p.getImgId_attack()], personImg_x, personImg_y, personImg_width,
						personImg_height, this);
			}

			p.setImgTime_attack(p.getImgTime_attack() + 1);
		}
	}

	// 画小人瞄准状态走路方法
	public void drawPersonAimWalk(Graphics g, int personImg_x, int personImg_y, int personImg_width,
			int personImg_height, Person p) {
		// 如果向左走
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

			// 判断是我的小人还是敌人小人
			// 如果是我的小人
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getAimWalkLeftImg()[p.getImgId_walk()], personImg_x, personImg_y, personImg_width,
						personImg_height, this);
			} else {
				// 敌人小人
				g.drawImage(gi.getEpAimWalkLeftImg()[p.getImgId_walk()], personImg_x, personImg_y, personImg_width,
						personImg_height, this);

			}
			p.setImgTime_walk(p.getImgTime_walk() + 1);
		}

		// 如果向右走
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

			// 判断是我的小人还是敌人小人
			// 如果是我的小人
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getAimWalkRightImg()[p.getImgId_walk()], personImg_x, personImg_y, personImg_width,
						personImg_height, this);
			} else {
				// 敌人小人
				g.drawImage(gi.getEpAimWalkRightImg()[p.getImgId_walk()], personImg_x, personImg_y, personImg_width,
						personImg_height, this);
			}

			p.setImgTime_walk(p.getImgTime_walk() + 1);
		}
	}

	// 画小人瞄准状态站立方法
	public void drawPersonAimStand(Graphics g, int personImg_x, int personImg_y, int personImg_width,
			int personImg_height, Person p) {
		// 向左站立
		if (p.getDirection().equals("left")) {

			// 判断是我的小人还是敌人小人
			// 如果是我的小人
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getAimStandLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			} else {
				// 敌人小人
				g.drawImage(gi.getEpAimStandLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height,
						this);
			}

		} else if (p.getDirection().equals("right")) {
			// 向右站立

			// 判断是我的小人还是敌人小人
			// 如果是我的小人
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getAimStandRightImg(), personImg_x, personImg_y, personImg_width, personImg_height,
						this);
			} else {
				// 敌人小人
				g.drawImage(gi.getEpAimStandRightImg(), personImg_x, personImg_y, personImg_width, personImg_height,
						this);
			}
		}
	}

	// 画小人瞄准状态跳跃方法
	public void drawPersonAimJump(Graphics g, int personImg_x, int personImg_y, int personImg_width,
			int personImg_height, Person p) {

		// 如果面向左
		if (p.getDirection().equals("left")) {

			// 判断是我的小人还是敌人小人
			// 如果是我的小人
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getAimJumpLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			} else {
				// 敌人小人
				g.drawImage(gi.getEpAimJumpLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height,
						this);
			}

		} else if (p.getDirection().equals("right")) {
			// 如果面向右

			// 判断是我的小人还是敌人小人
			// 如果是我的小人
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getAimJumpRightImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			} else {
				// 敌人小人
				g.drawImage(gi.getEpAimJumpRightImg(), personImg_x, personImg_y, personImg_width, personImg_height,
						this);
			}
		}
	}

	// 画小人瞄准状态下落方法
	public void drawPersonAimFall(Graphics g, int personImg_x, int personImg_y, int personImg_width,
			int personImg_height, Person p) {
		// 如果向左
		if (p.getDirection().equals("left")) {

			// 判断是我的小人还是敌人小人
			// 如果是我的小人
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getAimFallLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			} else {
				// 敌人小人
				g.drawImage(gi.getEpAimFallLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height,
						this);
			}

		} else if (p.getDirection().equals("right")) {
			// 如果向右

			// 判断是我的小人还是敌人小人
			// 如果是我的小人
			if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
				g.drawImage(gi.getAimFallRightImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			} else {
				// 敌人小人
				g.drawImage(gi.getEpAimFallRightImg(), personImg_x, personImg_y, personImg_width, personImg_height,
						this);
			}
		}
	}

	// 画武器方法
	public void drawWeapon(Graphics g, Person p) {
		double angle; // 保存转动的角度，转换之后转回来

		// 取出武器
		Weapon weapon = p.getWeapon();
		Graphics2D g2 = (Graphics2D) g;

		int weaponImg_x = weapon.getX();// 得到武器图片的x
		int weaponImg_y = weapon.getY(); // 得到武器图片的y
		int weaponImg_width = weapon.getWidth(); // 得到武器图片的宽
		int weaponImg_height = weapon.getHeight(); // 得到武器图片的高

		// 先判断武器类型
		// 如果是手枪
		if (weapon.getWeaponType().equals("handgun")) {
			// 再判断小人方向
			// 如果向右
			if (p.getDirection().equals("right")) {
				// 调用方法让武器随鼠标转动
				angle = this.Imgrotate(g2, weapon.getX(), weapon.getY() + 3, p.goalX, p.goalY, "right");

				// 判断是我的小人还是敌人小人,两种小人拿武器的手臂颜色不同，即图片不同
				// 如果是我的小人
				if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
					g2.drawImage(gi.getAimHandgunRightImg(), weaponImg_x, weaponImg_y, weaponImg_width,
							weaponImg_height, this);
				} else {
					// 敌人小人
					g2.drawImage(gi.getEpAimHandgunRightImg(), weaponImg_x, weaponImg_y, weaponImg_width,
							weaponImg_height, this);
				}

				// 画完武器转换来
				g2.rotate(-angle, weaponImg_x, weaponImg_y + 3);

			} else if (p.getDirection().equals("left")) {
				// 如果向左

				// 调用方法让武器随鼠标转动
				angle = this.Imgrotate(g2, weapon.getX() + weapon.getWidth(), weapon.getY() + 3, p.goalX, p.goalY,
						"left");

				// 判断是我的小人还是敌人小人,两种小人拿武器的手臂颜色不同，即图片不同
				// 如果是我的小人
				if (p.getClass().getName().equals("com.game.ver2_0.MyPerson")) {
					g2.drawImage(gi.getAimHandgunLeftImg(), weaponImg_x, weaponImg_y, weaponImg_width, weaponImg_height,
							this);

				} else {
					// 敌人小人
					g2.drawImage(gi.getEpAimHandgunLeftImg(), weaponImg_x, weaponImg_y, weaponImg_width,
							weaponImg_height, this);
				}

				// 画完武器转换来
				g2.rotate(-angle, weapon.getX() + weapon.getWidth(), weapon.getY() + 3);
			}
		}

	}

	// 图片转动方法(返回转动的弧度
	// 需判断武器位置，只能转动一半，需要加PI转到另一半
	public double Imgrotate(Graphics2D g2, int x, int y, int goalX, int goalY, String direction) {
		double angle = 0; // 转动角度

		// 算出转动弧度
		if (direction.equals("right")) {
			angle = Math.atan2(goalY - y, goalX - x);
		} else if (direction.equals("left")) {
			angle = Math.atan2(goalY - y, goalX - x) + Math.PI;
		}

		// 开始转动
		g2.rotate(angle, x, y);

		return angle;
	}

	public void run() {
		while (true) {
			
			// 判断是否活着
			// 如果死亡，停止游戏面板的所有线程
			// 最后结束游戏面板线程
			if(!this.isAlive) {
				this.mp.setAlive(false); // 设置我的小人死亡
				
				for(int i = 0; i < this.vt_Ep.size(); i++) {
					EnemyPerson ep = this.vt_Ep.get(i); // 取出敌人小人
					
					ep.setAlive(false); // 设置死亡
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
		// 测试用功能，按1键添加敌人
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
