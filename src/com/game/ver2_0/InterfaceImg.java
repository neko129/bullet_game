package com.game.ver2_0;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;



class InterfaceImg {
	private Image Black;
	private Image background;
	private InputStream is;
	private String imgName;
	public InterfaceImg()
	{
		//���غ�ɫ͸����Ƭ
		Black=this.loadImg("/image/interfaceimg/Black.png");
		background=this.loadImg("/image/interfaceimg/background1.jpg");
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public Image getBlack()
	{
		return Black;
	}
	public Image getBackground1()
	{
		return background;
	}
	public Image loadImg(String imgPath) {
		Image image = null;
	
		// ���ͼƬ��Ӧ��������
		is = this.getClass().getResourceAsStream(imgPath);
		
		// ���������뵽ͼƬ�����
		try {
			image = ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	//public void setBackground1()
	//{
	//	this.background1=Toolkit.getDefaultToolkit().getImage("/BulletGame/image/interface/background1.jpg");
	//}
}
