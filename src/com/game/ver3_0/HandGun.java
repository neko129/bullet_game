package com.game.ver3_0;

// ��ǹ��
class HandGun extends Weapon {
	
	// ���췽����ʼ��
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
