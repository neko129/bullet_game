package com.game.ver1_0;

import java.awt.Rectangle;
import java.awt.event.*;

// �ҵ�С��
class MyPerson extends Person implements Runnable, KeyListener {
	GamePanel gp; // ָ�룬ָ����Ϸ���
	private boolean isLeft; // �Ƿ�����
	private boolean isRight; // �Ƿ�����
	private boolean isFalling; // �Ƿ���������
	private boolean isStanding; // �Ƿ�����վ���ϰ�����
	private boolean isJumping; // �Ƿ�������Ծ
	private int jumpTime_now; // ��ǰ��Ծʱ��
	private int jumpTime_sum; // ����Ծʱ��

	public MyPerson(int x, int y) {
		isFalling = true; // ����Ĭ��Ϊ����
		jumpTime_now = 0;  //��ǰ��Ծʱ����Ϊ0
		jumpTime_sum = 6; // ����Ծʱ����Ϊ6
		this.setX(x);
		this.setY(y);
		this.setWidth(20);
		this.setHeight(20);
		this.setLeftSpeed(Recorder.leftSpeed_mp);
		this.setRightSpeed(Recorder.rightSpeed_mp);
		this.setDirection("right");
	}

	public void keyPressed(KeyEvent e) {
		if ((e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)) {
			this.isLeft = true;
			this.setDirection("left");
		} else if ((e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)) {
			this.isRight = true;
			this.setDirection("right");
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			// ������Ծ��
			this.isJumping = true;
			this.isFalling = false;
			this.isStanding = false;
			
		}
	}

	public void keyReleased(KeyEvent e) {
		if ((e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)) {
			this.isLeft = false;
		} else if ((e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)) {
			this.isRight = false;
		}
	}

	public void keyTyped(KeyEvent arg0) {

	}

	// �ƶ�
	// ��Ծ����������ƶ�Ҫ�����Ƿ������ϰ������
	// ����Ծ����������ƶ��������£����ϰ����������С���Σ������⵽��С������ײ�������ٶ�Ϊ0��
	// ��ֹ�������ϰ��������ײ���¿����ϰ����������
	public void move() {
		if (isLeft) {
			this.setLeftSpeed(Recorder.leftSpeed_mp); // ÿ�ζ��ָ��ٶ�

			// ������������������Ծ
			if (isFalling || isJumping) {
				// ����ƶ���������
				if (this.getDirection().equals("left")) {
					// ���ϰ����ұ�С���μ����ײ
					// �����ҵľ���
					Rectangle myRtg = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());

					// �����ϰ������С���β��ж�
					for (int i = 0; i < gp.map.getNumber(); i++) {

						// ȡ���ϰ���
						Barrier br = gp.map.getVt_Barrier().get(i);

						// ����
						Rectangle brRtg = new Rectangle(br.getX() + br.getWidth(), br.getY(), 2, br.getHeight());

						// �ж�
						if (myRtg.intersects(brRtg)) {
							// �����ײ,��Ӧ���ٶ���Ϊ0
							this.setLeftSpeed(0);
						}
					}
				}
			}
			this.moveLeft();
		} else if (isRight) {
			this.setRightSpeed(Recorder.rightSpeed_mp); // ÿ�ζ��ָ��ٶ�

			// ������������������Ծ
			if (isFalling || isJumping) {
				// ����ƶ���������
				if (this.getDirection().equals("right")) {
					// ���ϰ������С���μ����ײ
					// �����ҵľ���
					Rectangle myRtg = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());

					// �����ϰ������С���β��ж�
					for (int i = 0; i < gp.map.getNumber(); i++) {

						// ȡ���ϰ���
						Barrier br = gp.map.getVt_Barrier().get(i);

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
		}
	}

	// ����
	public void fall() {
		if (isFalling) {
			this.moveDown();
		}
	}

	// ��Ծ
	public void jump() {
		if (isJumping && (this.jumpTime_now < this.jumpTime_sum)) {
			
			this.moveUp();
			this.jumpTime_now++;
		} else if (isJumping && (this.jumpTime_now == this.jumpTime_sum)) {
			// ��Ծ����
			this.isJumping = false;
			this.isFalling = true;
			this.jumpTime_now = 0; // �ָ���ǰ��Ծʱ��
		}
	}

	// �ж����Ƿ����ͼ�ϰ�����ײ
	// �����ҵ�С�˺͵�ͼ�ϰ��ﴴ��Rectangle����
	// ����Rectangle���intersects()
	public boolean isCollision_myPerson() {
		boolean isCollision = false;

		// �����ҵ�С�˶�Ӧ��rectangle
		Rectangle myRtg = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());

		// �������ϰ��������ײ
		for (int i = 0; i < gp.map.getNumber(); i++) {
			// ȡ���ϰ���
			Barrier barrier = gp.map.getVt_Barrier().get(i);

			// �����ϰ����Ӧ��rectangle
			Rectangle brRtg = new Rectangle(barrier.getX(), barrier.getY(), barrier.getWidth(), barrier.getHeight());

			// �ж���ײ
			if (myRtg.intersects(brRtg)) {
				// �����ײ
				isCollision = true;
				break;
			}
		}

		return isCollision;
	}

	// ����Ծ������ʹ��
	// �ж����Ƿ����ϰ�������С������ײ
	public boolean isCollision_jump() {
		boolean isCollision_jump = false;
		// �����ҵ�С�˶�Ӧ��rectangle
		Rectangle myRtg = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		
		// �������ϰ��������ײ
				for (int i = 0; i < gp.map.getNumber(); i++) {
					// ȡ���ϰ���
					Barrier barrier = gp.map.getVt_Barrier().get(i);

					// �����ϰ��������Ӧ��rectangle
					Rectangle brRtg = new Rectangle(barrier.getX(), barrier.getY() + barrier.getHeight(), 
							barrier.getWidth(), 5);

					// �ж���ײ
					if (myRtg.intersects(brRtg)) {
						// �����ײ
						isCollision_jump = true;
						break;
					}
				}
				return isCollision_jump;
	}
	
	public void run() {
		while (true) {
			this.move();

			// �ж��Ƿ���������
			if (isFalling) {

				// �ж��Ƿ���ײ
				// �������ײ��
				if (!this.isCollision_myPerson()) {
					this.fall();
				} else {
					// �����ײ,isFalling��Ϊfalse,isStanding��Ϊtrue;
					this.isFalling = false;
					this.isStanding = true;
				}
			}

			// �ж��Ƿ�վ���ϰ�����
			if (isStanding) {

				// �ж��Ƿ���ײ
				// �������ײ, ��ʼ������
				if (!this.isCollision_myPerson()) {
					isFalling = true;
				}
			}
			
			// �ж��Ƿ�������Ծ
			if(isJumping) {
				// �ж��Ƿ������ϰ�������С����
				// �������
				if(this.isCollision_jump()) {
					isJumping = false;
					isFalling = true;
				} else {
					this.jump();
				}
				
			}

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}