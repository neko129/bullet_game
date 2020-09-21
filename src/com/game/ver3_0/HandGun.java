package com.game.ver3_0;

// 手枪类
class HandGun extends Weapon {
	
	// 构造方法初始化
	public HandGun(int x, int y, int width, int height, String weaponType,  int bulletNumber, GamePanel gp) {
		this.setX(x);
		this.setY(y);
		this.setWidth(width);
		this.setHeight(height);
		this.setWeaponType(weaponType);
		this.setBulletNumber(bulletNumber);
		this.setGp(gp);
	}
}
