package com.game.ver3_0;

import java.awt.Rectangle;

// �ӵ���
// ���ʱ����ӵ�
// ʵ���ӵ�����ֱ�߷���
// ���������꣬����Ƕȣ�x = pcosx , y = psinx

class Bullet implements Runnable {
	private double x;
	private double y;
	private int width;
	private int height;
	private double angle; // �Ƕ�
	
	static double speedP; // ���������p���ٶȣ������ӵ������ٶ�
	
	private GamePanel gp; // ָ�룬ָ����Ϸ��壬��Ҫ�ǻ�ȡ���˶���
	private boolean isAlive; // �ж��ӵ��Ƿ����
	private int belongto; // �ӵ�����˭

	// belongto ������ �ж��ӵ�����˭����������ж��У��ҵ�С�˵��ӵ����ж��Ƿ���е���
	// ����С�˻��ж��Ƿ�����ҵ�С��
	// 0���ҵ�С�ˣ�1�ǵ���С��
	public Bullet(double x, double y, int goalX, int goalY, int width, int height, GamePanel gp, int belongto) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.gp = gp;
		Bullet.speedP = 16;
		angle = Math.atan2(goalY - y, goalX - x);
		isAlive = true; // �����Ƿ����
		this.belongto = belongto;
	}

	// �ӵ����з���
	public void fly() {
		x += speedP * Math.cos(angle);
		y += speedP * Math.sin(angle);
	}

	// �ж��ӵ��Ƿ���е��˷���
	public void isHitEp() {

		// �����ӵ���Ӧ����
		Rectangle bulletRtg = new Rectangle((int) this.x, (int) this.y, this.width, this.height);

		// ȡ������
		for (int i = 0; i < gp.vt_Ep.size(); i++) {
			EnemyPerson ep = gp.vt_Ep.get(i);

			//  �жϵ����Ƿ����
			// ������ţ��Ž����ж���ײ
			
			if (ep.isAlive()) {
				// ������Ӧ�ĵ��˾���
				Rectangle epRtg = new Rectangle(ep.getX(), ep.getY(), ep.getWidth(), ep.getHeight());

				// �ж��Ƿ���ײ
				// �����ײ���ӵ���������������,ֱ������ѭ�������������������˼����ײ
				if (bulletRtg.intersects(epRtg)) {

					this.isAlive = false;
					ep.setAlive(false);
					break;
				}
			}
		}

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
		// �����ײ���ӵ��������ҵ�С������
		if (bulletRtg.intersects(mpRtg)) {
			mp.setAlive(false);
			this.isAlive = false;

		}
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

			//�ж��ӵ��Ƿ�����ҵ�С��
			// 1�ǵ���С��
			if(this.belongto == 1) {
				this.isHitMp();
			}
			
			// �ж��ӵ��Ƿ���е���
			// ������У��ӵ���������������(�ڷ�����ʵ��������)
			//  �ж��ӵ�����˭
			// 0���ҵ�С��
			if(this.belongto == 0) {
				this.isHitEp();
			}
			
			// �ж��ӵ��Ƿ�����ϰ���
			if(this.isHitBarrier()) {
				this.isAlive = false;
			}

			// �ж��ӵ��Ƿ�ɳ���Ϸ���
			// �ڷ�����ʵ��������
			this.isOverFly();

			try {
				Thread.sleep(40);
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
