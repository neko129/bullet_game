package com.game.ver3_0;

import java.awt.Rectangle;

// 攻击类
// 小人出拳攻击时创建攻击对象
// 攻击对象与敌人判断是否碰撞
// 如果碰撞，将敌人打死
class Attack extends Rectangle {	
	
	// Rectangle类已有成员变量x, y等
	public Attack(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	// 判断攻击对象是否与小人碰撞
	public boolean isHit(Person p) {
		boolean isHit = false;
		
		// 创建小人对应的矩形
		Rectangle pRtg = new Rectangle(p.getX(), p.getY(), p.getWidth(), p.getHeight());
		
		if(this.intersects(pRtg)) {
			isHit = true;
		}
		
		return isHit;
	}
}
