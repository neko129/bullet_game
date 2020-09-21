package com.game.ver4_0;

import java.io.*;
import java.util.Vector;
// ��ͼ��Դ������,��ʼ����ͼ��������ͼ�ϰ���͵��˺��ҵ�С�ˣ�
// 1.��ȡ��ͼ�ؿ��ļ�
// 2.��õ�ͼ�ϰ�������������ϰ���ı�ţ����꣬��С�����������
// 3.������Ϸ������һϵ�в���

class Map {
	private Vector<Barrier> vt_Barrier; // �����ŵ�ͼ�ϰ���(Barrier)
	private int number; // �ϰ������
	private Vector<EnemyPerson> vt_Ep; // �����ŵ���С��
	private MyPerson mp; // �ҵ�С��
	

	public Map(int level, GamePanel gp) {
		vt_Barrier = new Vector<Barrier>();
		vt_Ep = new Vector<EnemyPerson>();
		
		
		// ��ȡ�ؿ��ļ�
		InputStream fr = null;
		BufferedReader br = null;
		
		String str1; // �洢��ͼ�ϰ���͵�����Ϣ
		String[] str2; // �洢һ������Ϣ
		
		try {
			
			// ��ȡ�ؿ��ļ�
			fr = this.getClass().getClassLoader().getResourceAsStream("level_info/level_" + level + ".txt");
			br = new BufferedReader(new InputStreamReader(fr));
			
			str1 = br.readLine(); // ��ȡ��һ�� �ϰ������
			str2 = str1.split(" "); // ���ո�ֿ�
			number = Integer.parseInt(str2[0]);
			
			// ��ȡ���������е�������Ϣ��֮ǰ����¼��ͼ�ϰ�����Ϣ
			while(!(str1 = br.readLine()).equals("ep_info")) {
				str2 = str1.split(" "); // ���ո�ֿ�
				
				// ������Ϣ������ͼ�ϰ���(����������, x, y, width, height)
				Barrier barrier = new Barrier(Integer.parseInt(str2[0]), Integer.parseInt(str2[1]), 
						Integer.parseInt(str2[2]), Integer.parseInt(str2[3]), Integer.parseInt(str2[4]));
				// �Ž�����
				vt_Barrier.add(barrier);
			}
			
			
			// ��ȡ���������У���¼������Ϣ
			while (!(str1 = br.readLine()).equals("mp_info")) {
				str2 = str1.split(" "); // ���ո�ֿ�

				// ������Ϣ��������С��(x, y, type, weaponType, bulletNumber, GamePanel)
				EnemyPerson ep = new EnemyPerson(Integer.parseInt(str2[0]), Integer.parseInt(str2[1]),
						Integer.parseInt(str2[2]), Integer.parseInt(str2[3]), Integer.parseInt(str2[4]), gp);
				// �Ž�����
				vt_Ep.add(ep);
				
			} 
			
			// ��ȡ���������У���¼�ҵ�С����Ϣ
			while ((str1 = br.readLine()) != null) {
				str2 = str1.split(" "); // ���ո�ֿ�

				// ������Ϣ�����ҵ�С��(x, y, weaponType, bulletNumber, GamePanel)
				 mp = new MyPerson(Integer.parseInt(str2[0]), Integer.parseInt(str2[1]),
						Integer.parseInt(str2[2]), Integer.parseInt(str2[3]), gp);
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			// ���ر�������
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
