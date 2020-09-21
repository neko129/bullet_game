package com.game.ver3_0;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.InputStream;

import javax.swing.JPanel;

import java.awt.*;

class Interface extends JPanel implements Runnable, MouseListener, KeyListener {
	private int Panelwidth;//窗口宽度
	private int Panelheight;//窗口高度
	private InterfaceImg Ifi;
	private int stair;// 记录界面层数
	private int time_1;// 一级界面位置判断
	private int level;//记录指定关卡
	private int level_now;//一共可以玩的有多少关
	private int level_min;//从第几关开始
	private int level_show;//关卡界面大于level_max的关卡会用X来表示 level_show是level_max+X关卡
	private int level_max;//关卡总的界面
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

	public void drawFirstInterface(Graphics g)// 改方法画出游戏一开始的界面
	{
		Color out = Color.white;//窗口界面未被选择时颜色
		Color over = new Color(10, 103, 139);//窗口界面选择时颜色
		Color title = new Color(0, 107, 232);//时间特工大字颜色
		g.drawImage(Ifi.getBackground1(), 0, 0, Panelwidth, Panelheight, this);
		g.setFont(new Font("微软雅黑", Font.BOLD, 200));// 改字体类型和大小和粗细
		g.setColor(title);
		g.drawString("时间特工", 130, 300);// 设置字体内容和颜色
		g.setColor(out);// 未被选择选项信息
		g.setFont(new Font("等线", Font.BOLD, 30));
		g.drawString("开始游戏", 1036, 435);
		g.drawString("制作人员名单", 1036, 505);
		g.drawString("退出", 1036, 575);
		if (time_1 == 0) {//选择开始游戏选项
			g.setColor(over);
			g.drawString("开始游戏", 1036, 435);
		} else if (time_1 == 1) {//选择制作人员名单选项
			g.setColor(over);
			g.drawString("制作人员名单", 1036, 505);
		} else if (time_1 == 2) {//选择退出选项
			g.setColor(over);
			g.drawString("退出", 1036, 575);
		}
	}


	public void drawSecondInterface1(Graphics g) {
		int Listwidth = 640, Listheight = 280;//边框宽高
		int buttom=80,interval=40;//按钮大小和间隔大小
		int move_x=28;//按钮文字移动至中心横向
		int move_y=52;//按钮文字移动至中心纵向
		Color list_background=Color.white;//设置背景颜色
		Color list_buttom_out=new Color(81,81,81);//设置关卡选项未被选颜色
		Color list_buttom_over=new Color(10, 103, 139);//设置关卡选项被选颜色
		Color list_page_number=new Color(27, 195, 243);//设置选关界面页数字体颜色
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
							g.setColor(list_buttom_over);//画选择时的方块
							g.fillRect((Panelwidth - Listwidth) / 2+(i+1)*interval+i*buttom,(Panelheight - Listheight) / 2+(j+1)*interval+j*buttom,buttom,buttom);
							g.setColor(list_buttom_out);
						}
						else{
							g.fillRect((Panelwidth - Listwidth) / 2+(i+1)*interval+i*buttom,(Panelheight - Listheight) / 2+(j+1)*interval+j*buttom,buttom,buttom);//画未选择时的方块
						}
					}
				}
				g.setColor(list_background);
				g.setFont(new Font("等线", Font.BOLD, 40));
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
				g.setFont(new Font("等线",Font.BOLD,20));
				g.drawString((t+1)+"/"+level_show/10, 935, 477);
			}
			
		}

	}
	
	
	public void drawSecondInterface2(Graphics g) {
		int Listwidth = 600, Listheight = 300;
		Color list_background=Color.white;//设置背景颜色
		Color list_title=new Color(133, 181, 247);//字体颜色
		g.setColor(list_background);
		g.drawImage(Ifi.getBlack(), 0, 0, Panelwidth, Panelheight, this);
		g.fillRect((Panelwidth - Listwidth) / 2, (Panelheight - Listheight) / 2, Listwidth, Listheight);
		g.setFont(new Font("等线", Font.BOLD, 30));
		g.setColor(list_title);
		g.drawString("制作成员名单", 560, 275);
		g.setFont(new Font("等线", Font.BOLD, 20));
		g.drawString("邓伟杰", 537, 344);
		g.drawString("林月", 707, 344);
		g.drawString("吴裕泽", 537, 430);
		g.drawString("施译昶", 707, 430);
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
		// TODO 自动生成的方法存根
		System.out.println(arg0.getX()+" "+arg0.getY());
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// System.out.println("x="+arg0.getX()+"\ny="+arg0.getY());
		// TODO 自动生成的方法存根
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// 开始面板选择相关
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
					// 删除开始面板
					gf.remove(this);
					
					// 删除开始面板的键盘监听者
					gf.removeKeyListener(this);
					
					// 创建游戏面板
					GamePanel gp=new GamePanel(level, gf);
					// 将游戏图片类对象传入游戏面板
					gp.setGi(gf.gi);
					// 启动游戏面板
					Thread t1=new Thread(gp);
					t1.start();
					
					//注册游戏面板为键盘事件监听
					gf.addKeyListener(gp);
					
					// 注册我的小人为键盘事件监听
					gf.addKeyListener(gp.mp);
					
					
					gf.add(gp);
					gf.setVisible(true);
				}
			}
		}

	}

	

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO 自动生成的方法存根
		
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
		// TODO 自动生成的方法存根

	}

	public boolean isAlive() {
		return isAlive;
	}
}
