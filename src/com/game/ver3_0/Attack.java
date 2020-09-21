package com.game.ver3_0;

import java.awt.Rectangle;

// ������
// С�˳�ȭ����ʱ������������
// ��������������ж��Ƿ���ײ
// �����ײ�������˴���
class Attack extends Rectangle {	
	
	// Rectangle�����г�Ա����x, y��
	public Attack(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	// �жϹ��������Ƿ���С����ײ
	public boolean isHit(Person p) {
		boolean isHit = false;
		
		// ����С�˶�Ӧ�ľ���
		Rectangle pRtg = new Rectangle(p.getX(), p.getY(), p.getWidth(), p.getHeight());
		
		if(this.intersects(pRtg)) {
			isHit = true;
		}
		
		return isHit;
	}
}
