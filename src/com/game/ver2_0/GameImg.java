package com.game.ver2_0;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

// ��ϷͼƬ��
// �Ÿ��ָ���ͼƬ
// ��μ��ظ���ͼƬ
// 1.�������getResourceAsStream()���ͼƬ��ַ����InputStream��
// 2.��ͼƬ��InputStream������ImageIO.read()
// 3.��read()������ȡ��ͼƬ�Ž�Image�����
class GameImg {
	private Image[] barrierImg; // ��ͼ�ϰ���ͼƬ
	private Image backgroundImg; // ����ͼƬ

	// �������ҵ�С��ͼƬ
	private Image standLeftImg; // վ������ͼƬ
	private Image standRightImg; // վ������ͼƬ
	private Image[] walkRightImg; // ������ͼƬ
	private Image[] walkLeftImg; // ������ͼƬ
	private Image jumpLeftImg; // ��Ծ����ͼƬ
	private Image jumpRightImg; // ��Ծ����ͼƬ
	private Image fallLeftImg; // ��������ͼƬ
	private Image fallRightImg; // ��������ͼƬ
	private Image[] attackLeftImg; // �����ȭ����ͼƬ
	private Image[] attackRightImg; // ���ҳ�ȭ����ͼƬ

	private Image[] aimWalkLeftImg; // ��׼״̬��·����ͼƬ
	private Image[] aimWalkRightImg; // ��׼״̬��·����ͼƬ
	private Image aimStandLeftImg; // ��׼״̬վ������ͼƬ
	private Image aimStandRightImg; // ��׼״̬վ������ͼƬ
	private Image aimJumpLeftImg; // ��׼״̬��Ծ����ͼƬ
	private Image aimJumpRightImg; // ��׼״̬��Ծ����ͼƬ
	private Image aimFallLeftImg; // ��׼״̬��������ͼƬ
	private Image aimFallRightImg; // ��׼״̬��������ͼƬ
	private Image aimHandLeftImg; // ��׼״̬���ֱ�ͼƬ
	private Image aimHandRightImg; // ��׼״̬���ֱ�ͼƬ
	private Image aimHandgunLeftImg; // ��׼״̬��ǹ����ͼƬ
	private Image aimHandgunRightImg; // ��׼״̬��ǹ����ͼƬ

	// �����ǵ���С��ͼƬ
	private Image epStandLeftImg; // վ������ͼƬ
	private Image epStandRightImg; // վ������ͼƬ
	private Image[] epWalkRightImg; // ������ͼƬ
	private Image[] epWalkLeftImg; // ������ͼƬ
	private Image epJumpLeftImg; // ��Ծ����ͼƬ
	private Image epJumpRightImg; // ��Ծ����ͼƬ
	private Image epFallLeftImg; // ��������ͼƬ
	private Image epFallRightImg; // ��������ͼƬ

	private Image[] epAttackLeftImg; // �����ȭ����ͼƬ
	private Image[] epAttackRightImg; // ���ҳ�ȭ����ͼƬ

	private Image[] epAimWalkLeftImg; // ��׼״̬��·����ͼƬ
	private Image[] epAimWalkRightImg; // ��׼״̬��·����ͼƬ
	private Image epAimStandLeftImg; // ��׼״̬վ������ͼƬ
	private Image epAimStandRightImg; // ��׼״̬վ������ͼƬ
	private Image epAimJumpLeftImg; // ��׼״̬��Ծ����ͼƬ
	private Image epAimJumpRightImg; // ��׼״̬��Ծ����ͼƬ
	private Image epAimFallLeftImg; // ��׼״̬��������ͼƬ
	private Image epAimFallRightImg; // ��׼״̬��������ͼƬ
	private Image epAimHandLeftImg; // ��׼״̬���ֱ�ͼƬ
	private Image epAimHandRightImg; // ��׼״̬���ֱ�ͼƬ
	private Image epAimHandgunLeftImg; // ��׼״̬��ǹ����ͼƬ
	private Image epAimHandgunRightImg; // ��׼״̬��ǹ����ͼƬ

	private InputStream is; // ������
	private String imgName; // ͼƬ����������ͼƬ��ַ

	// ���췽����ʼ��
	public GameImg() {
		barrierImg = new Image[5];

		
		// ��ʼ���ҵ�С��ͼƬ����
		walkRightImg = new Image[10];
		walkLeftImg = new Image[10];

		attackLeftImg = new Image[4];
		attackRightImg = new Image[4];

		aimWalkLeftImg = new Image[10];
		aimWalkRightImg = new Image[10];
		
		// ��ʼ������С��ͼƬ����
		epWalkRightImg = new Image[10];
		epWalkLeftImg = new Image[10];

		epAttackLeftImg = new Image[4];
		epAttackRightImg = new Image[4];

		epAimWalkLeftImg = new Image[10];
		epAimWalkRightImg = new Image[10];

		// ���ص�ͼ�ϰ���ͼƬ
		this.loadImg("/image/barrier/", barrierImg);

		// ���ر���ͼƬ
		backgroundImg = this.loadImg("/image/background/1.png");

		// �����ҵ�С��ͼƬ
		this.loadMpImg();
		
		// ���ص���С��ͼƬ
		this.loadEpImg();

		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ����ͼƬ����(����ͼƬ)
	// ע�������Image�෵��
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

	// ����ͼƬ����(����ͼƬ)
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

	// �����ҵ�С��ͼƬ����
	public void loadMpImg() {

		// ����������·ͼƬ
		this.loadImg("/image/walk/left/", walkLeftImg);

		// ����������·ͼƬ
		this.loadImg("/image/walk/right/", walkRightImg);

		// ����վ������ͼƬ
		standLeftImg = this.loadImg("/image/stand/left/0.png");

		// ����վ������ͼƬ
		standRightImg = this.loadImg("/image/stand/right/0.png");

		// ������Ծ����ͼƬ
		jumpLeftImg = this.loadImg("/image/jump/left/0.png");

		// ������Ծ����ͼƬ
		jumpRightImg = this.loadImg("/image/jump/right/0.png");

		// ������������ͼƬ
		fallLeftImg = this.loadImg("/image/fall/left/0.png");

		// ������������ͼƬ
		fallRightImg = this.loadImg("/image/fall/right/0.png");

		// �������󹥻�ͼƬ
		this.loadImg("/image/attack/left/", attackLeftImg);

		// �������ҹ���ͼƬ
		this.loadImg("/image/attack/right/", attackRightImg);

		// ������׼״̬������·ͼƬ
		this.loadImg("/image/aim/walk/left/", aimWalkLeftImg);

		// ������׼״̬������·ͼƬ
		this.loadImg("/image/aim/walk/right/", aimWalkRightImg);

		// ������׼״̬վ������ͼƬ
		aimStandLeftImg = this.loadImg("/image/aim/stand/left/0.png");

		// ������׼״̬վ������ͼƬ
		aimStandRightImg = this.loadImg("/image/aim/stand/right/0.png");

		// ������׼״̬���ֱ�ͼƬ
		aimHandLeftImg = this.loadImg("/image/aim/hand/left/0.png");

		// ������׼״̬���ֱ�ͼƬ
		aimHandLeftImg = this.loadImg("/image/aim/hand/right/0.png");

		// ������׼״̬��ǹ����ͼƬ
		aimHandgunLeftImg = this.loadImg("/image/aim/handgun/left/0.png");

		// ������׼״̬��ǹ����ͼƬ
		aimHandgunRightImg = this.loadImg("/image/aim/handgun/right/0.png");

		// ������׼״̬��Ծ����ͼƬ
		aimJumpLeftImg = this.loadImg("/image/aim/jump/left/0.png");

		// ������׼״̬��Ծ����ͼƬ
		aimJumpRightImg = this.loadImg("/image/aim/jump/right/0.png");

		// ������׼״̬��������ͼƬ
		aimFallLeftImg = this.loadImg("/image/aim/fall/left/0.png");

		// ������׼״̬��������ͼƬ
		aimFallRightImg = this.loadImg("/image/aim/fall/right/0.png");
	}

	
	
	// ���ص���С��ͼƬ����
	public void loadEpImg() {
		
		// ����������·ͼƬ
		this.loadImg("/image/epImg/walk/left/", epWalkLeftImg);

		// ����������·ͼƬ
		this.loadImg("/image/epImg/walk/right/", epWalkRightImg);

		// ����վ������ͼƬ
		epStandLeftImg = this.loadImg("/image/epImg/stand/left/0.png");

		// ����վ������ͼƬ
		epStandRightImg = this.loadImg("/image/epImg/stand/right/0.png");

		// ������Ծ����ͼƬ
		epJumpLeftImg = this.loadImg("/image/epImg/jump/left/0.png");

		// ������Ծ����ͼƬ
		epJumpRightImg = this.loadImg("/image/epImg/jump/right/0.png");

		// ������������ͼƬ
		epFallLeftImg = this.loadImg("/image/epImg/fall/left/0.png");

		// ������������ͼƬ
		epFallRightImg = this.loadImg("/image/epImg/fall/right/0.png");

		// �������󹥻�ͼƬ
		this.loadImg("/image/epImg/attack/left/", epAttackLeftImg);

		// �������ҹ���ͼƬ
		this.loadImg("/image/epImg/attack/right/", epAttackRightImg);

		// ������׼״̬������·ͼƬ
		this.loadImg("/image/epImg/aim/walk/left/", epAimWalkLeftImg);

		// ������׼״̬������·ͼƬ
		this.loadImg("/image/epImg/aim/walk/right/", epAimWalkRightImg);

		// ������׼״̬վ������ͼƬ
		epAimStandLeftImg = this.loadImg("/image/epImg/aim/stand/left/0.png");

		// ������׼״̬վ������ͼƬ
		epAimStandRightImg = this.loadImg("/image/epImg/aim/stand/right/0.png");

//		// ������׼״̬���ֱ�ͼƬ
//		epAimHandLeftImg = this.loadImg("/image/epImg/aim/hand/left/0.png");
//
//		// ������׼״̬���ֱ�ͼƬ
//		epAimHandLeftImg = this.loadImg("/image/epImg/aim/hand/right/0.png");

		// ������׼״̬��ǹ����ͼƬ
		epAimHandgunLeftImg = this.loadImg("/image/epImg/aim/handgun/left/0.png");

		// ������׼״̬��ǹ����ͼƬ
		epAimHandgunRightImg = this.loadImg("/image/epImg/aim/handgun/right/0.png");

		// ������׼״̬��Ծ����ͼƬ
		epAimJumpLeftImg = this.loadImg("/image/epImg/aim/jump/left/0.png");

		// ������׼״̬��Ծ����ͼƬ
		epAimJumpRightImg = this.loadImg("/image/epImg/aim/jump/right/0.png");

		// ������׼״̬��������ͼƬ
		epAimFallLeftImg = this.loadImg("/image/epImg/aim/fall/left/0.png");

		// ������׼״̬��������ͼƬ
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
