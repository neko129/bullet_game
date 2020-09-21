package com.game.ver2_0;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

// 游戏图片类
// 放各种各样图片
// 如何加载各种图片
// 1.利用类的getResourceAsStream()获得图片地址并打开InputStream流
// 2.将图片的InputStream流传入ImageIO.read()
// 3.将read()方法读取的图片放进Image类对象
class GameImg {
	private Image[] barrierImg; // 地图障碍物图片
	private Image backgroundImg; // 背景图片

	// 以下是我的小人图片
	private Image standLeftImg; // 站立向左图片
	private Image standRightImg; // 站立向右图片
	private Image[] walkRightImg; // 向右走图片
	private Image[] walkLeftImg; // 向左走图片
	private Image jumpLeftImg; // 跳跃向左图片
	private Image jumpRightImg; // 跳跃向右图片
	private Image fallLeftImg; // 下落向左图片
	private Image fallRightImg; // 下落向右图片
	private Image[] attackLeftImg; // 向左出拳攻击图片
	private Image[] attackRightImg; // 向右出拳攻击图片

	private Image[] aimWalkLeftImg; // 瞄准状态走路向左图片
	private Image[] aimWalkRightImg; // 瞄准状态走路向右图片
	private Image aimStandLeftImg; // 瞄准状态站立向左图片
	private Image aimStandRightImg; // 瞄准状态站立向右图片
	private Image aimJumpLeftImg; // 瞄准状态跳跃向左图片
	private Image aimJumpRightImg; // 瞄准状态跳跃向右图片
	private Image aimFallLeftImg; // 瞄准状态下落向左图片
	private Image aimFallRightImg; // 瞄准状态下落向右图片
	private Image aimHandLeftImg; // 瞄准状态左手臂图片
	private Image aimHandRightImg; // 瞄准状态右手臂图片
	private Image aimHandgunLeftImg; // 瞄准状态手枪向左图片
	private Image aimHandgunRightImg; // 瞄准状态手枪向右图片

	// 以下是敌人小人图片
	private Image epStandLeftImg; // 站立向左图片
	private Image epStandRightImg; // 站立向右图片
	private Image[] epWalkRightImg; // 向右走图片
	private Image[] epWalkLeftImg; // 向左走图片
	private Image epJumpLeftImg; // 跳跃向左图片
	private Image epJumpRightImg; // 跳跃向右图片
	private Image epFallLeftImg; // 下落向左图片
	private Image epFallRightImg; // 下落向右图片

	private Image[] epAttackLeftImg; // 向左出拳攻击图片
	private Image[] epAttackRightImg; // 向右出拳攻击图片

	private Image[] epAimWalkLeftImg; // 瞄准状态走路向左图片
	private Image[] epAimWalkRightImg; // 瞄准状态走路向右图片
	private Image epAimStandLeftImg; // 瞄准状态站立向左图片
	private Image epAimStandRightImg; // 瞄准状态站立向右图片
	private Image epAimJumpLeftImg; // 瞄准状态跳跃向左图片
	private Image epAimJumpRightImg; // 瞄准状态跳跃向右图片
	private Image epAimFallLeftImg; // 瞄准状态下落向左图片
	private Image epAimFallRightImg; // 瞄准状态下落向右图片
	private Image epAimHandLeftImg; // 瞄准状态左手臂图片
	private Image epAimHandRightImg; // 瞄准状态右手臂图片
	private Image epAimHandgunLeftImg; // 瞄准状态手枪向左图片
	private Image epAimHandgunRightImg; // 瞄准状态手枪向右图片

	private InputStream is; // 输入流
	private String imgName; // 图片名，方便拿图片地址

	// 构造方法初始化
	public GameImg() {
		barrierImg = new Image[5];

		
		// 初始化我的小人图片数组
		walkRightImg = new Image[10];
		walkLeftImg = new Image[10];

		attackLeftImg = new Image[4];
		attackRightImg = new Image[4];

		aimWalkLeftImg = new Image[10];
		aimWalkRightImg = new Image[10];
		
		// 初始化敌人小人图片数组
		epWalkRightImg = new Image[10];
		epWalkLeftImg = new Image[10];

		epAttackLeftImg = new Image[4];
		epAttackRightImg = new Image[4];

		epAimWalkLeftImg = new Image[10];
		epAimWalkRightImg = new Image[10];

		// 加载地图障碍物图片
		this.loadImg("/image/barrier/", barrierImg);

		// 加载背景图片
		backgroundImg = this.loadImg("/image/background/1.png");

		// 加载我的小人图片
		this.loadMpImg();
		
		// 加载敌人小人图片
		this.loadEpImg();

		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 加载图片方法(单张图片)
	// 注意这里的Image类返回
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

	// 加载图片方法(多张图片)
	public void loadImg(String imgPath, Image[] image) {
		for (int i = 0; i < image.length; i++) {
			imgName = i + ".png";

			is = this.getClass().getResourceAsStream(imgPath + imgName);

			try {
				image[i] = ImageIO.read(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 加载我的小人图片方法
	public void loadMpImg() {

		// 加载向左走路图片
		this.loadImg("/image/walk/left/", walkLeftImg);

		// 加载向右走路图片
		this.loadImg("/image/walk/right/", walkRightImg);

		// 加载站立向左图片
		standLeftImg = this.loadImg("/image/stand/left/0.png");

		// 加载站立向右图片
		standRightImg = this.loadImg("/image/stand/right/0.png");

		// 加载跳跃向左图片
		jumpLeftImg = this.loadImg("/image/jump/left/0.png");

		// 加载跳跃向右图片
		jumpRightImg = this.loadImg("/image/jump/right/0.png");

		// 加载下落向左图片
		fallLeftImg = this.loadImg("/image/fall/left/0.png");

		// 加载下落向右图片
		fallRightImg = this.loadImg("/image/fall/right/0.png");

		// 加载向左攻击图片
		this.loadImg("/image/attack/left/", attackLeftImg);

		// 加载向右攻击图片
		this.loadImg("/image/attack/right/", attackRightImg);

		// 加载瞄准状态向左走路图片
		this.loadImg("/image/aim/walk/left/", aimWalkLeftImg);

		// 加载瞄准状态向右走路图片
		this.loadImg("/image/aim/walk/right/", aimWalkRightImg);

		// 加载瞄准状态站立向左图片
		aimStandLeftImg = this.loadImg("/image/aim/stand/left/0.png");

		// 加载瞄准状态站立向右图片
		aimStandRightImg = this.loadImg("/image/aim/stand/right/0.png");

		// 加载瞄准状态左手臂图片
		aimHandLeftImg = this.loadImg("/image/aim/hand/left/0.png");

		// 加载瞄准状态右手臂图片
		aimHandLeftImg = this.loadImg("/image/aim/hand/right/0.png");

		// 加载瞄准状态手枪向左图片
		aimHandgunLeftImg = this.loadImg("/image/aim/handgun/left/0.png");

		// 加载瞄准状态手枪向右图片
		aimHandgunRightImg = this.loadImg("/image/aim/handgun/right/0.png");

		// 加载瞄准状态跳跃向左图片
		aimJumpLeftImg = this.loadImg("/image/aim/jump/left/0.png");

		// 加载瞄准状态跳跃向右图片
		aimJumpRightImg = this.loadImg("/image/aim/jump/right/0.png");

		// 加载瞄准状态下落向左图片
		aimFallLeftImg = this.loadImg("/image/aim/fall/left/0.png");

		// 加载瞄准状态下落向右图片
		aimFallRightImg = this.loadImg("/image/aim/fall/right/0.png");
	}

	
	
	// 加载敌人小人图片方法
	public void loadEpImg() {
		
		// 加载向左走路图片
		this.loadImg("/image/epImg/walk/left/", epWalkLeftImg);

		// 加载向右走路图片
		this.loadImg("/image/epImg/walk/right/", epWalkRightImg);

		// 加载站立向左图片
		epStandLeftImg = this.loadImg("/image/epImg/stand/left/0.png");

		// 加载站立向右图片
		epStandRightImg = this.loadImg("/image/epImg/stand/right/0.png");

		// 加载跳跃向左图片
		epJumpLeftImg = this.loadImg("/image/epImg/jump/left/0.png");

		// 加载跳跃向右图片
		epJumpRightImg = this.loadImg("/image/epImg/jump/right/0.png");

		// 加载下落向左图片
		epFallLeftImg = this.loadImg("/image/epImg/fall/left/0.png");

		// 加载下落向右图片
		epFallRightImg = this.loadImg("/image/epImg/fall/right/0.png");

		// 加载向左攻击图片
		this.loadImg("/image/epImg/attack/left/", epAttackLeftImg);

		// 加载向右攻击图片
		this.loadImg("/image/epImg/attack/right/", epAttackRightImg);

		// 加载瞄准状态向左走路图片
		this.loadImg("/image/epImg/aim/walk/left/", epAimWalkLeftImg);

		// 加载瞄准状态向右走路图片
		this.loadImg("/image/epImg/aim/walk/right/", epAimWalkRightImg);

		// 加载瞄准状态站立向左图片
		epAimStandLeftImg = this.loadImg("/image/epImg/aim/stand/left/0.png");

		// 加载瞄准状态站立向右图片
		epAimStandRightImg = this.loadImg("/image/epImg/aim/stand/right/0.png");

//		// 加载瞄准状态左手臂图片
//		epAimHandLeftImg = this.loadImg("/image/epImg/aim/hand/left/0.png");
//
//		// 加载瞄准状态右手臂图片
//		epAimHandLeftImg = this.loadImg("/image/epImg/aim/hand/right/0.png");

		// 加载瞄准状态手枪向左图片
		epAimHandgunLeftImg = this.loadImg("/image/epImg/aim/handgun/left/0.png");

		// 加载瞄准状态手枪向右图片
		epAimHandgunRightImg = this.loadImg("/image/epImg/aim/handgun/right/0.png");

		// 加载瞄准状态跳跃向左图片
		epAimJumpLeftImg = this.loadImg("/image/epImg/aim/jump/left/0.png");

		// 加载瞄准状态跳跃向右图片
		epAimJumpRightImg = this.loadImg("/image/epImg/aim/jump/right/0.png");

		// 加载瞄准状态下落向左图片
		epAimFallLeftImg = this.loadImg("/image/epImg/aim/fall/left/0.png");

		// 加载瞄准状态下落向右图片
		epAimFallRightImg = this.loadImg("/image/epImg/aim/fall/right/0.png");
	}

	
	
	public Image getEpStandLeftImg() {
		return epStandLeftImg;
	}

	public Image getEpStandRightImg() {
		return epStandRightImg;
	}

	public Image[] getEpWalkRightImg() {
		return epWalkRightImg;
	}

	public Image[] getEpWalkLeftImg() {
		return epWalkLeftImg;
	}

	public Image getEpJumpLeftImg() {
		return epJumpLeftImg;
	}

	public Image getEpJumpRightImg() {
		return epJumpRightImg;
	}

	public Image getEpFallLeftImg() {
		return epFallLeftImg;
	}

	public Image getEpFallRightImg() {
		return epFallRightImg;
	}

	public Image[] getEpAttackLeftImg() {
		return epAttackLeftImg;
	}

	public Image[] getEpAttackRightImg() {
		return epAttackRightImg;
	}

	public Image[] getEpAimWalkLeftImg() {
		return epAimWalkLeftImg;
	}

	public Image[] getEpAimWalkRightImg() {
		return epAimWalkRightImg;
	}

	public Image getEpAimStandLeftImg() {
		return epAimStandLeftImg;
	}

	public Image getEpAimStandRightImg() {
		return epAimStandRightImg;
	}

	public Image getEpAimJumpLeftImg() {
		return epAimJumpLeftImg;
	}

	public Image getEpAimJumpRightImg() {
		return epAimJumpRightImg;
	}

	public Image getEpAimFallLeftImg() {
		return epAimFallLeftImg;
	}

	public Image getEpAimFallRightImg() {
		return epAimFallRightImg;
	}

	public Image getEpAimHandLeftImg() {
		return epAimHandLeftImg;
	}

	public Image getEpAimHandRightImg() {
		return epAimHandRightImg;
	}

	public Image getEpAimHandgunLeftImg() {
		return epAimHandgunLeftImg;
	}

	public Image getEpAimHandgunRightImg() {
		return epAimHandgunRightImg;
	}

	public Image getAimJumpLeftImg() {
		return aimJumpLeftImg;
	}

	public Image getAimJumpRightImg() {
		return aimJumpRightImg;
	}

	public Image getAimFallLeftImg() {
		return aimFallLeftImg;
	}

	public Image getAimFallRightImg() {
		return aimFallRightImg;
	}

	public Image getAimStandLeftImg() {
		return aimStandLeftImg;
	}

	public Image getAimStandRightImg() {
		return aimStandRightImg;
	}

	public Image[] getAimWalkLeftImg() {
		return aimWalkLeftImg;
	}

	public Image getAimHandLeftImg() {
		return aimHandLeftImg;
	}

	public Image getAimHandRightImg() {
		return aimHandRightImg;
	}

	public Image getAimHandgunLeftImg() {
		return aimHandgunLeftImg;
	}

	public Image getAimHandgunRightImg() {
		return aimHandgunRightImg;
	}

	public Image[] getAimWalkRightImg() {
		return aimWalkRightImg;
	}

	public Image[] getAttackLeftImg() {
		return attackLeftImg;
	}

	public Image[] getAttackRightImg() {
		return attackRightImg;
	}

	public Image getFallLeftImg() {
		return fallLeftImg;
	}

	public Image getFallRightImg() {
		return fallRightImg;
	}

	public Image getJumpLeftImg() {
		return jumpLeftImg;
	}

	public Image getJumpRightImg() {
		return jumpRightImg;
	}

	public Image getStandLeftImg() {
		return standLeftImg;
	}

	public Image getStandRightImg() {
		return standRightImg;
	}

	public Image[] getWalkRightImg() {
		return walkRightImg;
	}

	public Image[] getWalkLeftImg() {
		return walkLeftImg;
	}

	public Image[] getBarrierImg() {
		return barrierImg;
	}

	public Image getBackgroundImg() {
		return backgroundImg;
	}

}
