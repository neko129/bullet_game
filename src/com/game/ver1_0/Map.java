package com.game.ver1_0;

import java.io.*;
import java.util.Vector;
// ��ͼ��Դ������,��ȡ��ͼ�ؿ���Ϣ
// 1.��ȡ��ͼ�ؿ��ļ�
// 2.��õ�ͼ�ϰ�����������꣬��С
// 3.������Ϸ������һϵ�в���

class Map {
	private Vector<Barrier> vt_Barrier; // �����ŵ�ͼ�ϰ���(Barrier)
	private int level; // �ؿ��ȼ�
	private int number; // �ϰ������
	
	public Map() {
		vt_Barrier = new Vector<Barrier>();
		// ��ȡ�ؿ��ļ�
		InputStream fr = null;
		BufferedReader br = null;
		
		String str1; // �洢��ͼ�ϰ�����Ϣ
		String[] str2;
		
		try {
			
			// ��ȡ�ؿ��ļ�
			fr = this.getClass().getClassLoader().getResourceAsStream("level_info/level_1.txt");
			br = new BufferedReader(new InputStreamReader(fr));
			
			str1 = br.readLine(); // ��ȡ��һ�У��ؿ��ȼ����ϰ������
			str2 = str1.split(" "); // ���ո�ֿ�
			level = Integer.parseInt(str2[0]);
			number = Integer.parseInt(str2[1]);
			
			// ��ȡ���������У���¼��ͼ�ϰ�����Ϣ
			while((str1 = br.readLine()) != null) {
				str2 = str1.split(" "); // ���ո�ֿ�
				
				// ������Ϣ������ͼ�ϰ���
				Barrier barrier = new Barrier(Integer.parseInt(str2[0]), Integer.parseInt(str2[1]), 
						Integer.parseInt(str2[2]), Integer.parseInt(str2[3]));
				// �Ž�����
				vt_Barrier.add(barrier);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// ���ر�������
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
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
