package com.game.ver4_0;

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
	Vector<EnemyPerson> vt_Ep; // 敌人小人向量
	int restEpNumbers; // 敌人小人剩余人数
	Map map; // 地图
	GameImg gi; // 游戏图片，保存各种各样游戏图片
	boolean isAlive; // 是否活着
	GameFrame gf; // 指针，指向游戏框架，用于在游戏框架中添加新的游戏面板等
	int level; // 关卡数
	Vector<Weapon> vt_wp; // 武器向量
	int score; // 分数，每关的分数，定一个最高分，随着游戏进行，分数减少，通过分数高低来表示通关的快慢

	public GamePanel(int level, GameFrame gf) {
		// 游戏框架指针传入
		this.gf = gf;

		// 关卡数
		this.level = level;

		// 注册鼠标事件监听者,用于确定地图障碍物坐标
		this.addMouseListener(this);

		// 地图
		map = new Map(level, this);

		// 我的小人(x, y, weaponType, bulletNumber)
		mp = map.getMp();
		Thread t1 = new Thread(mp);
		t1.start();

		// 敌人小人
		vt_Ep = map.getVt_ep(); // 获得敌人小人向量
		// 启动敌人小人线程
		for (int i = 0; i < vt_Ep.size(); i++) {
			Thread t2 = new Thread(vt_Ep.get(i));
			t2.start();
		}
		
		// 敌人小人剩余
		restEpNumbers = vt_Ep.size();

		// 我的小人注册鼠标事件监听者
		this.addMouseListener(mp);
		this.addMouseMotionListener(mp);

		// 设置默认活着
		isAlive = true;

		// 测试，创建武器
		vt_wp = new Vector<Weapon>();
		// Weapon weapon = new Weapon(200, 480, 18, 12, "handgun", 2, this);
		// vt_wp.add(weapon);
		
		// 初始化分数
		score = 10000;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// 画背景图片
		g.drawImage(gi.getBackgroundImg(), 0, 0, 1300, 700, this);
		
		//画游戏提示
		this.drawTip(g);
		
		// 测试
		// 画敌人小人数
		g.drawString(restEpNumbers + "", 100, 100);
		
		// 画分数
		g.drawString(this.score + "", 30, 30);

		// 画我的武器
		// 先判断是否是瞄准状态，且是否活着
		if (mp.isAim() && mp.isAlive()) {
			this.drawWeapon(g, mp);
		}

		// 画我的小人
		// g.fillRect(mp.getX(), mp.getY(), mp.getWidth(), mp.getHeight()); // (测试用
		this.drawPerson(g, mp);

		// 画攻击对象(测试用
		// g.drawRect(mp.getX() - 10, mp.getY() + 10, 11, 6);

		// 画敌人小人，敌人小人的子弹,敌人小人武器
		// 混在一起画会有些乱，不过只需遍历一遍敌人小人
		for (int i = 0; i < vt_Ep.size(); i++) {
			// 取出敌人小人
			EnemyPerson ep = vt_Ep.get(i);

			// 判断是否进入瞄准状态,且活着
			if (ep.isAim() && ep.isAlive()) {

				// 画出敌人小人武器
				this.drawWeapon(g, ep);

				// 画出敌人小人射击倒计时
				// 第三种敌人不显示倒计时，不画出
				if(ep.getType()!=3) {
					g.drawString(ep.getShootTime() + "", ep.getX() + 10, ep.getY() - 5);
				}
			}

			// 画出敌人小人
			this.drawPerson(g, ep);

			// 画出敌人小人种类标志
			// 如果敌人小人活着
			if (ep.isAlive()) {
				this.drawEpTypeLogo(g, ep);
			}

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

		}

		// 画游戏面板的武器和面板的武器的子弹
		for (int i = 0; i < vt_wp.size(); i++) {
			// 取出武器
			Weapon weapon = vt_wp.get(i);

			// 判断是否武器被捡起
			// 如果不被捡起才画
			if (!weapon.isPicked()) {
				this.drawGamePanelWeapon(weapon, g);

				// 画出武器的子弹
				for (int j = 0; j < weapon.getVt_bullet().size(); j++) {
					// 取出子弹
					Bullet bullet = weapon.getVt_bullet().get(j);

					// 判断子弹是否活着
					// 活着
					if (bullet.isAlive()) {
						// 画出子弹
						this.drawBullet(g, bullet);
					} else {
						// 否则，从子弹向量中删除
						weapon.getVt_bullet().remove(bullet);
					}
				}

			} else {
				// 否则，将武器移除出武器向量
				vt_wp.remove(weapon);
			}
		}

		// 画地图障碍物
		this.drawBarrier(g);

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
		int personImg_x;
		int personImg_y;
		int personImg_width;
		int personImg_height;

		if (!p.isSquat()) {
			// 如果不在下蹲状态，对应的小人图片参数
			personImg_x = p.getX() - 8; // 得到小人图片的x
			personImg_y = p.getY(); // 得到小人图片的y
			personImg_width = p.getHeight(); // 得到小人图片的宽
			personImg_height = p.getHeight(); // 得到小人图片的高
		} else {
			// 如果处在下蹲状态，对应的小人图片参数
			personImg_x = p.getX() - 8; // 得到小人图片的x
			personImg_y = p.getY() - 16; // 得到小人图片的y
			personImg_width = 50; // 得到小人图片的宽
			personImg_height = 50; // 得到小人图片的高
		}
		// 如果活着
		if (p.isAlive()) {

			// 画走路
			// 如果走路,只在站立状态走路，且不在攻击状态，不在瞄准状态,不在下蹲状态
			if (p.isStanding() && !p.isAttack() && !p.isAim() && !p.isSquat()) {
				this.drawPersonWalk(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
			}

			// 画瞄准状态走路
			// 只在站立状态，不在攻击状态，不在下蹲状态，在瞄准状态,向左或向右运动
			else if (p.isStanding() && !p.isAttack() && p.isAim() && (p.isLeft() || p.isRight()) && !p.isSquat()) {
				this.drawPersonAimWalk(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
			}

			// 画站立
			// 如果站立,处于站立状态且不走动，且不在攻击状态,不在瞄准状态，不在下蹲状态
			if (p.isStanding() && !p.isLeft() && !p.isRight() && !p.isAttack() && !p.isAim() && !p.isSquat()) {
				this.drawPersonStand(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
			}

			// 画瞄准状态站立
			// 处于站立状态且不走动，且不在攻击状态,在瞄准状态
			else if (p.isStanding() && !p.isLeft() && !p.isRight() && !p.isAttack() && p.isAim()) {
				this.drawPersonAimStand(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
			}

			// 画跳跃
			// 如果跳跃，只在跳跃状态，且不在攻击状态,不在瞄准状态,不在下蹲状态
			if (p.isJumping() && !p.isAttack() && !p.isAim() && !p.isSquat()) {
				this.drawPersonJump(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
			}

			// 画瞄准状态跳跃
			// 如果跳跃，在跳跃状态，瞄准状态，且不在攻击状态,不在下蹲状态
			else if (p.isJumping() && p.isAim() && !p.isAttack() && !p.isSquat()) {
				this.drawPersonAimJump(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
			}

			// 画瞄准状态下落
			// 如果下落，在下落状态，在瞄准状态,且不在攻击状态，不在下蹲状态
			if (p.isFalling() && !p.isAttack() && p.isAim() && !p.isSquat()) {
				this.drawPersonAimFall(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
			}

			// 画下落
			// 如果下落，只在下落状态，且不在攻击状态，不在瞄准状态,不在下蹲状态
			else if (p.isFalling() && !p.isAttack() && !p.isSquat()) {
				this.drawPersonFall(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
			}

			// 画攻击
			if (p.isAttack()) {
				this.drawPersonAttack(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
			}

			// 画下蹲
			// 如果下蹲，处于下蹲状态
			if (p.isSquat()) {
				this.drawPersonSquat(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
			}
		}

		// 画死亡
		// 如果不活着
		if (!p.isAlive()) {
			this.drawPersonDie(g, personImg_x, personImg_y, personImg_width, personImg_height, p);
		}

	}

	// 画小人左右移动方法
	public void drawPersonWalk(Graphics g, int personImg_x, int personImg_y, int personImg_width, int personImg_height,
			Person p) {
		// 左右走情况
		// 不包括跳跃和下落
		// 如果向左走
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

			// 判断小人是我的小人类还是敌人小人类，两种小人图片不同
			// 如果是我的小人
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
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

			// 判断小人是我的小人类还是敌人小人类，两种小人图片不同
			// 如果是我的小人
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
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
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
				g.drawImage(gi.getStandLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			} else {
				g.drawImage(gi.getEpStandLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			}

		} else if (p.getDirection().equals("right")) {
			// 向右站立
			// 判断是我的小人还是敌人小人
			// 如果是我的小人
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
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
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
				g.drawImage(gi.getJumpLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			} else {
				// 如果是敌人小人
				g.drawImage(gi.getEpJumpLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			}

		} else if (p.getDirection().equals("right")) {
			// 如果向右

			// 判断是我的小人还是敌人小人
			// 如果是我的小人
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
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
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
				g.drawImage(gi.getFallLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			} else {
				// 敌人小人
				g.drawImage(gi.getEpFallLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			}

		} else if (p.getDirection().equals("right")) {
			// 如果向右

			// 判断是我的小人还是敌人小人
			// 如果是我的小人
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
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
			} else if (p.getImgTime_attack() < 12 * Person.slowConstant) {
				p.setImgId_attack(3);
			} else {
				p.setImgTime_attack(0); // 攻击图片时间恢复
				p.setImgId_attack(0); // 攻击图片Id恢复
				p.setAttack(false); // 攻击结束
			}

			// 判断是我的小人还是敌人小人
			// 如果是我的小人
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
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
			} else if (p.getImgTime_attack() < 12 * Person.slowConstant) {
				p.setImgId_attack(3);
			} else {
				p.setImgTime_attack(0); // 攻击图片时间恢复
				p.setImgId_attack(0); // 攻击图片Id恢复
				p.setAttack(false); // 攻击结束
			}

			// 判断是我的小人还是敌人小人
			// 如果是我的小人
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {

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

			// 判断是我的小人还是敌人小人
			// 如果是我的小人
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
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

			// 判断是我的小人还是敌人小人
			// 如果是我的小人
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
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
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
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
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
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
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
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
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
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
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
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
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
				g.drawImage(gi.getAimFallRightImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
			} else {
				// 敌人小人
				g.drawImage(gi.getEpAimFallRightImg(), personImg_x, personImg_y, personImg_width, personImg_height,
						this);
			}
		}
	}

	// 画小人死亡方法
	public void drawPersonDie(Graphics g, int personImg_x, int personImg_y, int personImg_width, int personImg_height,
			Person p) {
		// 如果向左
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

			// 如果是我的小人
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
				g.drawImage(gi.getDieLeftImg()[p.getImgId_die()], personImg_x, personImg_y, personImg_width,
						personImg_height, this);
			} else {
				// 如果是敌人小人
				g.drawImage(gi.getEpDieLeftImg()[p.getImgId_die()], personImg_x, personImg_y, personImg_width,
						personImg_height, this);
			}

			// System.out.println("time = " + p.getImgTime_die() + " i = " + i++);
			// System.out.println("Id = " + p.getImgId_die());

			p.setImgTime_die(p.getImgTime_die() + 1);

		} else {
			// 如果向右
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

			// 如果是我的小人
			if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
				g.drawImage(gi.getDieRightImg()[p.getImgId_die()], personImg_x, personImg_y, personImg_width,
						personImg_height, this);
			} else {
				// 如果是敌人小人
				g.drawImage(gi.getEpDieRightImg()[p.getImgId_die()], personImg_x, personImg_y, personImg_width,
						personImg_height, this);
			}

			// System.out.println("time = " + p.getImgTime_die() + " i = " + i++);
			// System.out.println("Id = " + p.getImgId_die());

			p.setImgTime_die(p.getImgTime_die() + 1);
		}
	}

	// 画小人下蹲方法
	public void drawPersonSquat(Graphics g, int personImg_x, int personImg_y, int personImg_width, int personImg_height,
			Person p) {

		// 先判断小人面对方向
		// 如果向左
		if (p.getDirection().equals("left")) {
			g.drawImage(gi.getSquatLeftImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
		} else {
			// 如果向右
			g.drawImage(gi.getSquatRightImg(), personImg_x, personImg_y, personImg_width, personImg_height, this);
		}

	}

	
	
	public void drawWeapon(Graphics g, Person p) {
		double angle; // 保存转动的角度，转换之后转回来

		// 取出武器
		Weapon weapon = p.getWeapon();
		Graphics2D g2 = (Graphics2D) g;

		int weaponImg_x = weapon.getX();// 得到武器图片的x
		int weaponImg_y = weapon.getY(); // 得到武器图片的y
		int weaponImg_width = weapon.getWidth(); // 得到武器图片的宽
		int weaponImg_height = weapon.getHeight(); // 得到武器图片的高

		// 先判断小人方向
		// 如果向右
		if (p.getDirection().equals("right")) {
			// 调用方法让武器随鼠标转动
			angle = this.Imgrotate(g2, weapon.getX(), weapon.getY() + 3, p.goalX, p.goalY, "right");

			// 判断武器类型，不同武器图片不同
			// 如果是手枪
			if (weapon.getWeaponType().equals("handgun")) {
				// 判断是我的小人还是敌人小人,两种小人拿武器的手臂颜色不同，即图片不同
				// 如果是我的小人
				if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
					g2.drawImage(gi.getAimHandgunRightImg(), weaponImg_x, weaponImg_y, weaponImg_width,
							weaponImg_height, this);
				} else {
					// 敌人小人
					g2.drawImage(gi.getEpAimHandgunRightImg(), weaponImg_x, weaponImg_y, weaponImg_width,
							weaponImg_height, this);
				}
				
			} else if (weapon.getWeaponType().equals("hand")) {
				// 如果是空手
				//  如果是我的小人
				if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
					g2.drawImage(gi.getAimHandRightImg(), weaponImg_x, weaponImg_y, weaponImg_width, weaponImg_height,
							this);
				} else {
					// 敌人小人
					g2.drawImage(gi.getEpAimHandRightImg(), weaponImg_x, weaponImg_y, weaponImg_width, weaponImg_height,
							this);
				}

			} else if (weapon.getWeaponType().equals("shotgun")) {
				// 如果是霰弹
				// 如果是我的小人
				if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
					g2.drawImage(gi.getAimShotgunRightImg(), weaponImg_x, weaponImg_y, weaponImg_width, weaponImg_height,
							this);
				} else {
					// 敌人小人
					g2.drawImage(gi.getEpAimShotgunRightImg(), weaponImg_x, weaponImg_y, weaponImg_width, weaponImg_height,
							this);
				}
			}
			// 画完武器转换来
			g2.rotate(-angle, weaponImg_x, weaponImg_y + 3);

		} else if (p.getDirection().equals("left")) {
			// 如果向左

			// 调用方法让武器随鼠标转动
			angle = this.Imgrotate(g2, weapon.getX() + weapon.getWidth(), weapon.getY() + 3, p.goalX, p.goalY, "left");

			// 如果是手枪
			if (weapon.getWeaponType().equals("handgun")) {
				// 判断是我的小人还是敌人小人,两种小人拿武器的手臂颜色不同，即图片不同
				// 如果是我的小人
				if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
					g2.drawImage(gi.getAimHandgunLeftImg(), weaponImg_x, weaponImg_y, weaponImg_width, weaponImg_height,
							this);

				} else {
					// 敌人小人
					g2.drawImage(gi.getEpAimHandgunLeftImg(), weaponImg_x, weaponImg_y, weaponImg_width,
							weaponImg_height, this);
				}
				
			} else if (weapon.getWeaponType().equals("hand")) {
				// 如果是空手
				if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
					g2.drawImage(gi.getAimHandLeftImg(), weaponImg_x, weaponImg_y, weaponImg_width, weaponImg_height,
							this);

				} else {
					// 敌人小人
					g2.drawImage(gi.getEpAimHandLeftImg(), weaponImg_x, weaponImg_y, weaponImg_width, weaponImg_height,
							this);
				}
			} else if (weapon.getWeaponType().equals("shotgun")) {
				// 如果是霰弹
				// 如果是我的小人
				if (p.getClass().getName().equals("com.game.ver4_0.MyPerson")) {
					g2.drawImage(gi.getAimShotgunLeftImg(), weaponImg_x, weaponImg_y, weaponImg_width, weaponImg_height,
							this);
				} else {
					// 敌人小人
					g2.drawImage(gi.getEpAimShotgunLeftImg(), weaponImg_x, weaponImg_y, weaponImg_width, weaponImg_height,
							this);
				}
			}
			// 画完武器转换来
			g2.rotate(-angle, weapon.getX() + weapon.getWidth(), weapon.getY() + 3);
		}
	}
			

	// 图片转动方法(返回转动的弧度
	// 需判断武器位置，只能转动右一半，需要加PI转到左一半
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

	// 画游戏面板武器的方法
	public void drawGamePanelWeapon(Weapon weapon, Graphics g) {
		// 判断武器类型
		// 如果是手枪
		if (weapon.getWeaponType().equals("handgun")) {
			g.drawImage(gi.getWeaponHandgunImg(), weapon.getX(), weapon.getY(), weapon.getWidth(), weapon.getHeight(),
					this);
		} else if (weapon.getWeaponType().equals("shotgun")) {
			//  如果是霰弹
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
	//画地图里面的游戏提示
		public void drawTip(Graphics g) {
			if(level==1) {
				this.drawTip_level_1(g);
			}
		}
	//画游戏第一关提示
	public void drawTip_level_1(Graphics g) {
		int buttom_width_height=30;
		int interval=35;
		int all_move_X=0;//移动画出物体x轴（方便整体改动）
		int all_move_Y=0;//移动画出物体y轴 （方便整体改动）
		Color buttom=new Color(81,81,81);
		Color white=Color.WHITE;
		Color primaryColor=new Color(51,51,51);//Graphics的初始颜色
		Font primaryFont=new Font("Dialog",Font.PLAIN,12);//Graphics的初始字体
		Font buttomword_1=new Font("等线", Font.BOLD, 20);
		Font buttomword_2=new Font("等线",Font.CENTER_BASELINE,17);
		Font buttomword_3=new Font("等线", Font.BOLD, 40);
		Font text=new Font("微软雅黑",Font.CENTER_BASELINE,20);
		
		g.setColor(buttom);
		g.fillRect(985+all_move_X, interval+all_move_Y, buttom_width_height, buttom_width_height);//画A键方块
		g.fillRect(1025+all_move_X, interval+all_move_Y, buttom_width_height, buttom_width_height);//画D键方块
		g.fillRect(1005+all_move_X, interval*2+all_move_Y, buttom_width_height, buttom_width_height);//画S键方块
		g.fillRect(960+all_move_X, interval*3+all_move_Y, buttom_width_height*4, buttom_width_height);//画SPACE键方块
		g.fillRect(1005+all_move_X, interval*4+all_move_Y, buttom_width_height, buttom_width_height);//画E键方块
		g.fillRect(990+all_move_X, interval*5+all_move_Y, buttom_width_height*2, buttom_width_height);//画ESC键方块
		
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
		g.drawString("左右移动", 1093+all_move_X, 22+interval+all_move_Y);
		g.drawString("下蹲", 1113+all_move_X, 22+interval*2+all_move_Y);
		g.drawString("跳跃", 1113+all_move_X, 22+interval*3+all_move_Y);
		g.drawString("捡武器", 1104+all_move_X, 22+interval*4+all_move_Y);
		g.drawString("退出", 1113+all_move_X, 22+interval*5+all_move_Y);
		g.drawString("举枪", 1113+all_move_X, 22+interval*6+all_move_Y);
		g.drawString("出拳", 1113+all_move_X, 22+interval*7+all_move_Y);
		g.drawString("射击", 1113+all_move_X, 22+interval*8+all_move_Y);
		
		g.setFont(primaryFont);
		g.setColor(primaryColor);
	}

	public void run() {
		while (true) {

			// 判断是否活着
			// 如果死亡，停止游戏面板的所有线程
			// 最后结束游戏面板线程
			if (!this.isAlive) {
				this.mp.setAlive(false); // 设置我的小人死亡
				this.mp.setStanding(true); // 设置站在障碍物上

				for (int i = 0; i < this.vt_Ep.size(); i++) {
					EnemyPerson ep = this.vt_Ep.get(i); // 取出敌人小人

					ep.setAlive(false); // 设置死亡
					ep.setStanding(true); // 设置站在障碍物上，配合起来才是结束线程
				}

				// 游戏面板的武器的子弹全部死亡
				for(int j = 0; j < this.vt_wp.size(); j++) {
					// 取出武器
					Weapon weapon = this.vt_wp.get(j);
					
					for(int k = 0; k < weapon.getVt_bullet().size(); k++) {
						// 设置死亡
						Bullet bullet = weapon.getVt_bullet().get(k);
						bullet.setAlive(false);
					}
				}
				
				break;
			}

			this.repaint();

			// 当有敌人时
			if(this.restEpNumbers != 0) {
				// 每循环一次，分数减少
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
		// 测试用功能，按1键添加敌人,只在第一二关用
		if (arg0.getKeyCode() == KeyEvent.VK_1) {
			if (this.level == 1 || this.level == 2) {
				restEpNumbers++; // 剩余敌人数增加

				EnemyPerson ep = new EnemyPerson(300, 440, 0, 1, 2, this);
				Thread t1 = new Thread(ep);
				t1.start();

				this.vt_Ep.add(ep);
			}
		}

		// R键重开功能
		else if (arg0.getKeyCode() == KeyEvent.VK_R) {
			// 创建新的游戏面板
			GamePanel gp = new GamePanel(this.level, gf);
			// 游戏图片类对象传入
			gp.setGi(gi);
			// 启动
			Thread t1 = new Thread(gp);
			t1.start();
			// 添加新的游戏面板的事件监听
			gf.addKeyListener(gp);
			// 添加新的游戏面板我的小人的事件监听
			gf.addKeyListener(gp.mp);

			// 删除旧的游戏面板
			gf.remove(this);
			// 设置旧的游戏面板为死亡
			this.isAlive = false;
			// 删除旧的游戏面板的事件监听
			gf.removeKeyListener(this);
			// 删除旧的游戏面板我的小人的事件监听
			gf.removeKeyListener(this.mp);

			// 添加新的游戏框架
			gf.add(gp);
			gf.setVisible(true);
		} 
		else if (arg0.getKeyCode() == KeyEvent.VK_G) {
			// 当没有敌人时按F键进入下一关。
			if (this.restEpNumbers == 0) {
				// 创建新的游戏面板
				GamePanel gp = new GamePanel(this.level + 1, gf);
				// 游戏图片类对象传入
				gp.setGi(gi);
				// 启动
				Thread t1 = new Thread(gp);
				t1.start();
				// 添加新的游戏面板的事件监听
				gf.addKeyListener(gp);
				// 添加新的游戏面板我的小人的事件监听
				gf.addKeyListener(gp.mp);

				// 删除旧的游戏面板
				gf.remove(this);
				// 设置旧的游戏面板为死亡
				this.isAlive = false;
				// 删除旧的游戏面板的事件监听
				gf.removeKeyListener(this);
				// 删除旧的游戏面板我的小人的事件监听
				gf.removeKeyListener(this.mp);

				// 添加新的游戏框架
				gf.add(gp);
				gf.setVisible(true);
			}
		}
		// 按esc键退出游戏面板
		else if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {

			// 删除游戏面板
			gf.remove(this);
			// 设置游戏面板为死亡
			this.isAlive = false;
			// 删除游戏面板的事件监听
			gf.removeKeyListener(this);
			// 删除游戏面板我的小人的事件监听
			gf.removeKeyListener(this.mp);

			// 添加开始面板
			gf.add(gf.it);
			// 添加开始面板的键盘事件监听
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
