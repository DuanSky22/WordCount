package com.duansky.wordcount;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * date 2016年6月1日 下午1:46:25
 * @author DuanSky
 */

public class MapJob implements Callable<WordCount>{
	
	private volatile boolean flag = true;
	private BlockingQueue<String[]> queue;
	
	public MapJob(BlockingQueue<String[]> queue){
		this.queue = queue;
	}

	@Override
	public WordCount call() throws Exception {
		WordCount wc = new WordCount();
		while(flag && !Thread.currentThread().isInterrupted()){
			
			if(queue.isEmpty()){
//				System.out.println(" ---------------------------------------------------- ");
//				System.out.println("|                                                    |");
//				System.out.println("|                                                    |");
//				System.out.println(" map job works so fast, we have to make them sleep...");
//				System.out.println("|                                                    |");
//				System.out.println("|                                                    |");
//				System.out.println(" ---------------------------------------------------- ");
				Thread.sleep(10 * 1000);
			}
			
			String[] data = queue.poll(30,TimeUnit.SECONDS);
			if(data == null) break;
			wc.add(data);

			
		}
		return wc;
	}

}
