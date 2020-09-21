package com.game.ver3_0;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.InputStream;

import javax.swing.JPanel;

import java.awt.*;

class Interface extends JPanel implements Runnable, MouseListener, KeyListener {
	private int Panelwidth;//���ڿ��
	private int Panelheight;//���ڸ߶�
	private InterfaceImg Ifi;
	private int stair;// ��¼�������
	private int time_1;// һ������λ���ж�
	private int level;//��¼ָ���ؿ�
	private int level_now;//һ����������ж��ٹ�
	private int level_min;//�ӵڼ��ؿ�ʼ
	private int level_show;//�ؿ��������level_max�Ĺؿ�����X����ʾ level_show��level_max+X�ؿ�
	private int level_max;//�ؿ��ܵĽ���
	private boolean isAlive;
	private GameFrame gf;


	public Interface(GameFrame gf) {
		Panelwidth = 1300;
		Panelheight = 700;
		Ifi = new InterfaceImg();
		stair = 1;
		time_1 = 0;
		level=1;
		level_max=20;
		level_now=level_max;
		level_min=1;
		isAlive = true;
		
		this.gf = gf;
		
		if(level_now/10.0>level_now/10) {
			level_show=level_max/10*10+10;
		}
		else {
			level_show=level_max/10*10;
		}
		this.addKeyListener(this);
		this.addMouseListener(this);
	}

	public void paint(Graphics g) {
		super.paint(g);

		if (stair == 1) {
			drawFirstInterface(g);
		} else if (stair == 2 && time_1 == 1) {
			drawFirstInterface(g);
			drawSecondInterface2(g);
		}
		else if(stair==2&&time_1==0) {
			drawFirstInterface(g);
			drawSecondInterface1(g);
		}
		/*
		 * else if(stair==2) { drawSecondInterface(g); }
		 */
	}

	public void drawFirstInterface(Graphics g)// �ķ���������Ϸһ��ʼ�Ľ���
	{
		Color out = Color.white;//���ڽ���δ��ѡ��ʱ��ɫ
		Color over = new Color(10, 103, 139);//���ڽ���ѡ��ʱ��ɫ
		Color title = new Color(0, 107, 232);//ʱ���ع�������ɫ
		g.drawImage(Ifi.getBackground1(), 0, 0, Panelwidth, Panelheight, this);
		g.setFont(new Font("΢���ź�", Font.BOLD, 200));// ���������ͺʹ�С�ʹ�ϸ
		g.setColor(title);
		g.drawString("ʱ���ع�", 130, 300);// �����������ݺ���ɫ
		g.setColor(out);// δ��ѡ��ѡ����Ϣ
		g.setFont(new Font("����", Font.BOLD, 30));
		g.drawString("��ʼ��Ϸ", 1036, 435);
		g.drawString("������Ա����", 1036, 505);
		g.drawString("�˳�", 1036, 575);
		if (time_1 == 0) {//ѡ��ʼ��Ϸѡ��
			g.setColor(over);
			g.drawString("��ʼ��Ϸ", 1036, 435);
		} else if (time_1 == 1) {//ѡ��������Ա����ѡ��
			g.setColor(over);
			g.drawString("������Ա����", 1036, 505);
		} else if (time_1 == 2) {//ѡ���˳�ѡ��
			g.setColor(over);
			g.drawString("�˳�", 1036, 575);
		}
	}


	public void drawSecondInterface1(Graphics g) {
		int Listwidth = 640, Listheight = 280;//�߿���
		int buttom=80,interval=40;//��ť��С�ͼ����С
		int move_x=28;//��ť�����ƶ������ĺ���
		int move_y=52;//��ť�����ƶ�����������
		Color list_background=Color.white;//���ñ�����ɫ
		Color list_buttom_out=new Color(81,81,81);//���ùؿ�ѡ��δ��ѡ��ɫ
		Color list_buttom_over=new Color(10, 103, 139);//���ùؿ�ѡ�ѡ��ɫ
		Color list_page_number=new Color(27, 195, 243);//����ѡ�ؽ���ҳ��������ɫ
		g.setColor(list_background);
		g.drawImage(Ifi.getBlack(), 0, 0, Panelwidth, Panelheight, this);
		g.fillRect((Panelwidth - Listwidth) / 2, (Panelheight - Listheight) / 2, Listwidth, Listheight);
		g.setColor(list_buttom_out);
		for(int t=0;t<level_show/10;t++) {
			if(level>t*10&&level<=(t+1)*10) {
				int m=t*10;
				for(int j=0;j<2;j++) {
					for(int i=0;i<5;i++) {
						m++;
						if(m==level) {
							g.setColor(list_buttom_over);//��ѡ��ʱ�ķ���
							g.fillRect((Panelwidth - Listwidth) / 2+(i+1)*interval+i*buttom,(Panelheight - Listheight) / 2+(j+1)*interval+j*buttom,buttom,buttom);
							g.setColor(list_buttom_out);
						}
						else{
							g.fillRect((Panelwidth - Listwidth) / 2+(i+1)*interval+i*buttom,(Panelheight - Listheight) / 2+(j+1)*interval+j*buttom,buttom,buttom);//��δѡ��ʱ�ķ���
						}
					}
				}
				g.setColor(list_background);
				g.setFont(new Font("����", Font.BOLD, 40));
				int n=t*10;
				for(int j=0;j<2;j++){
					for(int i=0;i<5;i++) {
						n++;
						if(n>level_now&&n<=level_max) {
							g.drawImage(Ifi.getLock(), (Panelwidth - Listwidth) / 2+(i+1)*interval+i*buttom+25, (Panelheight - Listheight) / 2+(j+1)*interval+j*buttom+20, 30, 36, this);
//							g.drawString("Y",(Panelwidth - Listwidth) / 2+(i+1)*interval+i*buttom+move_x,(Panelheight - Listheight) / 2+(j+1)*interval+j*buttom+move_y);
						}
						else if(n>level_max) {
							g.drawString("X",(Panelwidth - Listwidth) / 2+(i+1)*interval+i*buttom+move_x,(Panelheight - Listheight) / 2+(j+1)*interval+j*buttom+move_y);
						}
						else if(n<10){
							g.drawString(n+"",(Panelwidth - Listwidth) / 2+(i+1)*interval+i*buttom+move_x,(Panelheight - Listheight) / 2+(j+1)*interval+j*buttom+move_y);
						}
						else if(n>=10) {
							g.drawString(n+"",(Panelwidth - Listwidth) / 2+(i+1)*interval+i*buttom+move_x-10,(Panelheight - Listheight) / 2+(j+1)*interval+j*buttom+move_y);
						}
					}
				}
				g.setColor(list_page_number);
				g.setFont(new Font("����",Font.BOLD,20));
				g.drawString((t+1)+"/"+level_show/10, 935, 477);
			}
			
		}

	}
	
	
	public void drawSecondInterface2(Graphics g) {
		int Listwidth = 600, Listheight = 300;
		Color list_background=Color.white;//���ñ�����ɫ
		Color list_title=new Color(133, 181, 247);//������ɫ
		g.setColor(list_background);
		g.drawImage(Ifi.getBlack(), 0, 0, Panelwidth, Panelheight, this);
		g.fillRect((Panelwidth - Listwidth) / 2, (Panelheight - Listheight) / 2, Listwidth, Listheight);
		g.setFont(new Font("����", Font.BOLD, 30));
		g.setColor(list_title);
		g.drawString("������Ա����", 560, 275);
		g.setFont(new Font("����", Font.BOLD, 20));
		g.drawString("��ΰ��", 537, 344);
		g.drawString("����", 707, 344);
		g.drawString("��ԣ��", 537, 430);
		g.drawString("ʩ����", 707, 430);
	}

	@Override
	public void run() {

		while (true) {
			if (!this.isAlive) {
				break;
			}

			this.repaint();
			try {
				Thread.sleep(25);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO �Զ����ɵķ������
		System.out.println(arg0.getX()+" "+arg0.getY());
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO �Զ����ɵķ������

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO �Զ����ɵķ������

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// System.out.println("x="+arg0.getX()+"\ny="+arg0.getY());
		// TODO �Զ����ɵķ������
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO �Զ����ɵķ������

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// ��ʼ���ѡ�����
		if (stair == 1) {
			if ((e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S)) {
				time_1 = (time_1 + 1) % 3;
			} 
			else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
				time_1 = (time_1 + 2) % 3;
			} 
			else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				if (time_1 == 1||time_1==0) {
					stair++;
				} else if (time_1 == 2) {
					System.exit(0);
				} else if (time_1 == 0) {

				}
			}
		}
		else if(stair==2) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				stair--;
				level=1;
			}
			if(time_1==0) {
				int t;
				t=level/10;
				if ((e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)) {
					if(level>=level_min&&level<level_show) {
						if(level!=5+t*10&&level!=level_show-5) {
							level++;
						}
						else if(level==5+t*10&&level!=level_show-5) {
							level=(t+1)*10+1;
						}
					}
				}
				else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
					if(level>level_min&&level<=level_show) {
//						if(level!=1&&level!=6&&level!=11&&level!=16) {
//							level--;
//						}
//						else if(level==11||level==16) {
//							level=1;
//						}
						if(level!=t*10+6&&level!=t*10+1) {
							level--;
						}
						else if((level==t*10+6||level==t*10+1)&&level!=level_min+5) {
							level=(t-1)*10+1;
						}
					}
				}
				else if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
					
					if(level>t*10+5&&level<=(t+1)*10||level%10==0) {
						level-=5;
					}
				}
				else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
					if(level>t*10&&level<=t*10+5) {
						level+=5;
					}
				}
				else if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					// ɾ����ʼ���
					gf.remove(this);
					
					// ɾ����ʼ���ļ��̼�����
					gf.removeKeyListener(this);
					
					// ������Ϸ���
					GamePanel gp=new GamePanel(level, gf);
					// ����ϷͼƬ���������Ϸ���
					gp.setGi(gf.gi);
					// ������Ϸ���
					Thread t1=new Thread(gp);
					t1.start();
					
					//ע����Ϸ���Ϊ�����¼�����
					gf.addKeyListener(gp);
					
					// ע���ҵ�С��Ϊ�����¼�����
					gf.addKeyListener(gp.mp);
					
					
					gf.add(gp);
					gf.setVisible(true);
				}
			}
		}

	}

	

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO �Զ����ɵķ������
		
	}
	
	public int getTime_1() {
		return time_1;
	}

	public void setTime_1(int time_1) {
		this.time_1 = time_1;
	}

	public void setStair(int stair) {
		this.stair = stair;
	}

	public int getStair() {
		return stair;
	}

	public void setAlive(boolean b) {
		// TODO �Զ����ɵķ������

	}

	public boolean isAlive() {
		return isAlive;
	}
}
