package com.game.ver4_0;

import java.io.*;
import java.util.Vector;
// 地图资源配置类,初始化地图（包括地图障碍物和敌人和我的小人）
// 1.读取地图关卡文件
// 2.获得地图障碍物个数，各个障碍物的编号，坐标，大小，敌人坐标等
// 3.传给游戏面板进行一系列操作

class Map {
	private Vector<Barrier> vt_Barrier; // 向量放地图障碍物(Barrier)
	private int number; // 障碍物个数
	private Vector<EnemyPerson> vt_Ep; // 向量放敌人小人
	private MyPerson mp; // 我的小人
	

	public Map(int level, GamePanel gp) {
		vt_Barrier = new Vector<Barrier>();
		vt_Ep = new Vector<EnemyPerson>();
		
		
		// 读取关卡文件
		InputStream fr = null;
		BufferedReader br = null;
		
		String str1; // 存储地图障碍物和敌人信息
		String[] str2; // 存储一个个信息
		
		try {
			
			// 获取关卡文件
			fr = this.getClass().getClassLoader().getResourceAsStream("level_info/level_" + level + ".txt");
			br = new BufferedReader(new InputStreamReader(fr));
			
			str1 = br.readLine(); // 读取第一行 障碍物个数
			str2 = str1.split(" "); // 按空格分开
			number = Integer.parseInt(str2[0]);
			
			// 读取接下来的行到敌人信息行之前，记录地图障碍物信息
			while(!(str1 = br.readLine()).equals("ep_info")) {
				str2 = str1.split(" "); // 按空格分开
				
				// 根据信息创建地图障碍物(五参数，编号, x, y, width, height)
				Barrier barrier = new Barrier(Integer.parseInt(str2[0]), Integer.parseInt(str2[1]), 
						Integer.parseInt(str2[2]), Integer.parseInt(str2[3]), Integer.parseInt(str2[4]));
				// 放进向量
				vt_Barrier.add(barrier);
			}
			
			
			// 读取接下来的行，记录敌人信息
			while (!(str1 = br.readLine()).equals("mp_info")) {
				str2 = str1.split(" "); // 按空格分开

				// 根据信息创建敌人小人(x, y, type, weaponType, bulletNumber, GamePanel)
				EnemyPerson ep = new EnemyPerson(Integer.parseInt(str2[0]), Integer.parseInt(str2[1]),
						Integer.parseInt(str2[2]), Integer.parseInt(str2[3]), Integer.parseInt(str2[4]), gp);
				// 放进向量
				vt_Ep.add(ep);
				
			} 
			
			// 读取接下来的行，记录我的小人信息
			while ((str1 = br.readLine()) != null) {
				str2 = str1.split(" "); // 按空格分开

				// 根据信息创建我的小人(x, y, weaponType, bulletNumber, GamePanel)
				 mp = new MyPerson(Integer.parseInt(str2[0]), Integer.parseInt(str2[1]),
						Integer.parseInt(str2[2]), Integer.parseInt(str2[3]), gp);
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			// 最后关闭输入流
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}
	
	public MyPerson getMp() {
		return mp;
	}

	public Vector<EnemyPerson> getVt_ep() {
		return vt_Ep;
	}
	
	public Vector<Barrier> getVt_Barrier() {
		return vt_Barrier;
	}

	
	public int getNumber() {
		return number;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}
}
