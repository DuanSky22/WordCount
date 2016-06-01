package com.duansky.wordcount;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * date 2016年6月1日 上午11:32:12
 * @author DuanSky
 */

public class ReadEngine {

	private BufferedReader reader;
	
	private int coreSize = Runtime.getRuntime().availableProcessors();
	private int curr = 0;
	private volatile boolean isFinished = false;
	
	List<BlockingQueue<String[]>> list = new ArrayList<>(coreSize);
	
	public ReadEngine(String url){
		try {
			reader = new BufferedReader(new FileReader(new File(url)));
			for(int i = 0; i < coreSize; i++)
				list.add(new LinkedBlockingQueue<String[]>());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void read(){
		String data = "";
		try {
			while((data = reader.readLine())!=null){
				String[] s = data.split(",");
				if(s.length <= 4) continue;
				String[] content = {s[2],s[4]};
//				System.out.println(content[0]+"\t"+content[1]);
				list.get(getNextIndex()).put(content);
				data = null;
			}
			isFinished = true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private int getNextIndex(){
		curr = (curr+1) % coreSize;
		return curr;
	}
	
	public List<BlockingQueue<String[]>> getQueues(){
		return list;
	}
	
	public BlockingQueue<String[]> getQueue(int index){
		return list.get(index);
	}
	
	public boolean isFinished(){
		return isFinished;
	}
}
