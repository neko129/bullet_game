package com.game.ver3_0;

// ��ͼ�ϰ�����
class Barrier {
	private int x; 
	private int y;
	private int width; // ���
	private int height; // �߶�
	private int Id; // ��ţ���Ӧ���ֹ����ϰ���
	
	public Barrier(int Id, int x, int y, int width, int height) {
		this.Id = Id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public int getId() {
		return Id;
	}
	
	public void setID(int Id) {
		this.Id = Id;
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
	
}
