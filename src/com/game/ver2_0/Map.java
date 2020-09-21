package com.game.ver2_0;

import java.io.*;
import java.util.Vector;
// 地图资源配置类,获取地图关卡信息
// 1.读取地图关卡文件
// 2.获得关卡等级信息，地图障碍物个数，各个障碍物的编号，坐标，大小
// 3.传给游戏面板进行一系列操作

class Map {
	private Vector<Barrier> vt_Barrier; // 向量放地图障碍物(Barrier)
	private int level; // 关卡等级
	private int number; // 障碍物个数
	
	public Map() {
		vt_Barrier = new Vector<Barrier>();
		// 读取关卡文件
		InputStream fr = null;
		BufferedReader br = null;
		
		String str1; // 存储地图障碍物信息
		String[] str2; // 存储一个个信息
		
		try {
			
			// 获取关卡文件
			fr = this.getClass().getClassLoader().getResourceAsStream("level_info/level_1.txt");
			br = new BufferedReader(new InputStreamReader(fr));
			
			str1 = br.readLine(); // 读取第一行，关卡等级，障碍物个数
			str2 = str1.split(" "); // 按空格分开
			level = Integer.parseInt(str2[0]);
			number = Integer.parseInt(str2[1]);
			
			// 读取接下来的行，记录地图障碍物信息
			while((str1 = br.readLine()) != null) {
				str2 = str1.split(" "); // 按空格分开
				
				// 根据信息创建地图障碍物(五参数，编号, x, y, width, height)
				Barrier barrier = new Barrier(Integer.parseInt(str2[0]), Integer.parseInt(str2[1]), 
						Integer.parseInt(str2[2]), Integer.parseInt(str2[3]), Integer.parseInt(str2[4]));
				// 放进向量
				vt_Barrier.add(barrier);
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
	
	public Vector<Barrier> getVt_Barrier() {
		return vt_Barrier;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public int getNumber() {
		return number;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}
}
