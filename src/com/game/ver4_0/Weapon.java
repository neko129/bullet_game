package com.game.ver4_0;

import java.awt.Rectangle;
import java.util.Vector;

// ������
// ��������Ϊ�̣߳�����������ʱ�����̣߳����䵽�ϰ�����ʱ�����߳�
class Weapon implements Runnable {
	private int x;
	private int y;
	private int width;
	private int height;
	private String weaponType; // ��������
	private Vector<Bullet> vt_bullet; // �ӵ�����
	private int bulletNumber; // �ӵ�����
	private GamePanel gp; // ��Ϸ���ָ��
	private boolean isPicked; // �Ƿ񱻼���
	private int fallSpeed; // �����ٶ�
	private boolean isStanding; // �Ƿ�վ���ϰ�����
	private int collisionBarrierY; // ��ײ�ϰ����y����
	private int eachFireBulletNumber; // ÿ������ӵ���

	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param weaponType
	 * @param bulletNumber
	 * @param gp
	 */
	public Weapon(int x, int y, int width,int height, String weaponType,  int bulletNumber, GamePanel gp) {
		vt_bullet = new Vector<Bullet>();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.weaponType = weaponType;
		this.bulletNumber = bulletNumber;
		this.gp = gp;
		this.isStanding = false; // Ĭ��û��վ�ڵ���
		this.fallSpeed = 2; // ���������ٶ�
		
		if(weaponType.equals("handgun")) {
			// �������ǹ��ÿ�����һ���ӵ�
			this.eachFireBulletNumber = 1; 
		} else if (weaponType.equals("shotgun")) {
			// �����������ÿ����������ӵ�
			this.eachFireBulletNumber = 3;
		}
	}
	
	// ����
	// ��������ӵ�
	// ��ǹһ��һ�ţ�����һ���������
	public void fire(int goalX, int goalY, int belongto) {
		if (this.bulletNumber != 0) {
			// ���ж��Ƿ����ӵ�
			for (int i = 0; i < this.eachFireBulletNumber && this.bulletNumber != 0; i++) {
				// ������ӵ��������ӵ�
				Bullet bullet = new Bullet(x, y, goalX + (int) (Math.random() * 100 - 50),
						goalY + (int) (Math.random() * 100 - 50), 4, 4, gp, belongto);
				Thread t1 = new Thread(bullet);
				t1.start();
				// �Ž��ӵ�����
				vt_bullet.add(bullet);

				bulletNumber--; // �ӵ�����
			}
		} else {
			System.out.println("û�ӵ�");
		}
	}
	
	
	//  ����
	public void fall() {
		this.y += this.fallSpeed;
	}
	
	// �����ײ����
	public boolean isCollision() {
		boolean isCollision = false;

		// ����������Ӧ��rectangle
		Rectangle wpRtg = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());

		// �������ϰ��������ײ
		for (int i = 0; i < gp.map.getNumber(); i++) {
			// ȡ���ϰ���
			Barrier barrier = gp.map.getVt_Barrier().get(i);

			// �����ϰ����Ӧ��rectangle
			Rectangle brRtg = new Rectangle(barrier.getX(), barrier.getY(), barrier.getWidth(), barrier.getHeight());

			// �ж���ײ
			if (wpRtg.intersects(brRtg)) {
				// �����ײ
				// �õ���Ӧ�ϰ��������
				this.collisionBarrierY = (int) brRtg.getY();
				isCollision = true;
				break;
			}
		}

		return isCollision;
	}
	
	public void run() {
		while(true) {
			
			// ���վ���ϰ����ϻ򱻼����߳̽���
			if(this.isStanding || this.isPicked) {
				break;
			}
			
			// �ж��Ƿ���ײ�ϰ���
			// ���û��ײ������
			if(!this.isCollision()) {
				this.fall();
			} else {
				// �����ײ�ϰ���
				// ��վ���ϰ�����
				this.isStanding = true;
				// ����ײ�ϰ����y���괫�������������������Ῠ���ϰ���
				this.y = this.collisionBarrierY - this.height + 1;
			}
			
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void setGp(GamePanel gp) {
		this.gp = gp;
	}
	
	public Vector<Bullet> getVt_bullet() {
		return vt_bullet;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
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

	public String getWeaponType() {
		return weaponType;
	}

	public void setWeaponType(String weaponType) {
		this.weaponType = weaponType;
	}


	public int getBulletNumber() {
		return bulletNumber;
	}

	public void setBulletNumber(int bulletNumber) {
		this.bulletNumber = bulletNumber;
	}

	public boolean isPicked() {
		return isPicked;
	}

	public void setPicked(boolean isPicked) {
		this.isPicked = isPicked;
	}

	
}
