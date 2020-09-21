package com.game.ver3_0;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;



class InterfaceImg {
	private Image Black;
	private Image background;
	private Image Lock;
	private InputStream is;
	public InterfaceImg()
	{
		//加载黑色透明照片
		Black=this.loadImg("/image/interfaceimg/Black.png");
		background=this.loadImg("/image/interfaceimg/background1.jpg");
		Lock=this.loadImg("/image/interfaceimg/Lock.png");
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public Image loadImg(String imgPath) {
		Image image = null;
	
		// 获得图片对应的输入流
		is = this.getClass().getResourceAsStream(imgPath);
		
		// 输入流输入到图片类对象
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
	
	public Image getBlack()
	{
		return Black;
	}
	public Image getBackground1()
	{
		return background;
	}
	public Image getLock() {
		return Lock;
	}


	public void setLock(Image lock) {
		Lock = lock;
	}
}
