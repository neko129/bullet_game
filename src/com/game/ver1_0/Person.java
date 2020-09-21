package com.game.ver1_0;

abstract class Person {
	private int x;
	private int y;
	private int width; // ���
	private int height; // �߶�
	private int leftSpeed; // �����ƶ��ٶ�
	private int rightSpeed; // �����ƶ��ٶ�
	private String direction; // �ƶ�����
	
	public String getDirection() {
		return direction;
	}
	
	public void setDirection(String dir) {
		this.direction = dir;
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
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getX() {
		return x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getY() {
		return y;
	}
	
	public int getLeftSpeed() {
		return leftSpeed;
	}
	
	public void setLeftSpeed(int leftSpeed) {
		this.leftSpeed = leftSpeed;
	}
	
	public int getRightSpeed() {
		return rightSpeed;
	}
	
	public void setRightSpeed(int rightSpeed) {
		this.rightSpeed = rightSpeed;
	}
	
	public void moveLeft() {
		x -= leftSpeed;
	}
	
	public void moveRight() {
		x += rightSpeed;
	}
	
	public void moveDown() {
		y += 3;
	}
	
	public void moveUp() {
		y -= 5;
	}
}
