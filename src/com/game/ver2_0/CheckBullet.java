package com.game.ver2_0;

import java.awt.Rectangle;

// ����ӵ���
// ���ڼ�����С�˺��ҵ�С��֮���ֱ���Ƿ����ϰ���
// ������ϰ������ӵ�������û��ײ���ҵ�С��
// ���û���ϰ������ӵ������������ҵ�С��
// ���ݺ��ӵ��������ͬ
class CheckBullet implements Runnable {
	private double x;
	private double y;
	private int width;
	private int height;
	private double angle; // �Ƕ�
	private int cbSpeedP; // ���������p���ٶȣ������ӵ������ٶ�
	private GamePanel gp; // ָ�룬ָ����Ϸ��壬��ȡ�ҵ�С��
	private EnemyPerson ep; // ָ�룬ָ�򴴽�����ӵ��ĵ���С��
	private boolean isAlive; // �жϼ���ӵ��Ƿ����

	public CheckBullet(double x, double y, int goalX, int goalY, int width, int height, GamePanel gp, EnemyPerson ep) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.gp = gp;
		this.ep = ep;
		this.cbSpeedP = 24;
		angle = Math.atan2(goalY - y, goalX - x);
		isAlive = true; // �����Ƿ����
	}

	// �ӵ����з���
	public void fly() {
		x += cbSpeedP * Math.cos(angle);
		y += cbSpeedP * Math.sin(angle);
	}

	// �ж��ӵ��Ƿ�����ϰ���
	public boolean isHitBarrier() {
		boolean isHitBarrier = false;

		// �����ӵ���Ӧ����
		Rectangle bulletRtg = new Rectangle((int) this.x, (int) this.y, this.width, this.height);

		for (int i = 0; i < this.gp.map.getNumber(); i++) {
			// ȡ���ϰ���
			Barrier barrier = this.gp.map.getVt_Barrier().get(i);

			// �����ϰ����Ӧ����
			Rectangle barrierRtg = new Rectangle(barrier.getX(), barrier.getY(), barrier.getWidth(),
					barrier.getHeight());

			// �ж��Ƿ���ײ
			// �����ײ
			if (bulletRtg.intersects(barrierRtg)) {
				isHitBarrier = true;
				break;
			}
		}

		return isHitBarrier;
	}

	// �ж��ӵ��Ƿ�����ҵ�С�˷���
	public void isHitMp() {

		// �����ӵ���Ӧ����
		Rectangle bulletRtg = new Rectangle((int) this.x, (int) this.y, this.width, this.height);

		// ȡ���ҵ�С��
		MyPerson mp = this.gp.mp;

		// �����ҵ�С�˾���
		Rectangle mpRtg = new Rectangle(mp.getX(), mp.getY(), mp.getWidth(), mp.getHeight());

		// �ж��Ƿ���ײ
		// �����ײ���ӵ���������ײ���ҵ�С�ˣ�����С��isInLine = true
		if (bulletRtg.intersects(mpRtg)) {
			this.ep.setInLine(true);
			this.isAlive = false;

		}
	}

	// �ж��ӵ��Ƿ�ɳ���Ϸ���
	public void isOverFly() {
		// ����ӵ���x,y����Ϸ����⣬���Ƿɳ�
		// (�Ƚϼ򵥣�����Rectangle��
		if (this.x < 0 || this.x > 1300 || this.y < 0 || this.y > 700) {
			this.isAlive = false;
		}
	}

	public void run() {
		while (true) {
			// ����ӵ��������߳̽���
			if (!this.isAlive) {
				break;
			}

			// �ӵ�����
			this.fly();

			// �ж��ӵ��Ƿ�����ҵ�С��
			// ������У��ӵ�����������С�˵��ж��Ƿ���ҵ�С��ͬһ��ֱ��Ϊ true
			this.isHitMp();

			// �ж��Ƿ�����ϰ���
			// ����У�����С�˺��ҵ�С��֮�����ϰ������ͬһ����
			// ���ж��ӵ��Ƿ����
			if (this.isAlive) {
				if (this.isHitBarrier()) {

					this.ep.setInLine(false);
					
					this.isAlive = false;
				}
			}

			// �ж��ӵ��Ƿ�ɳ���Ϸ���
			// �ڷ�����ʵ��������
			this.isOverFly();

			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
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

}
