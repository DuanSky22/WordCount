package com.duansky.wordcount;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * date 2016年6月1日 下午2:27:38
 * @author DuanSky
 */

public class WordCountEngine {
	
	final static String inputUrl= "E:\\sundary\\ljy\\tweetstream.2013. Jan22-29.csv";
	final static String outputUrl = "E:\\sundary\\ljy\\output.txt";
	static List<BlockingQueue<String[]>> queues;
	
	public static void main(String args[]) throws InterruptedException, ExecutionException{
		
		long startTime = System.currentTimeMillis();
		
		//1. we use a thread to read data.
		System.out.println("1. we use a thread to read data.");
		Thread readThread = new Thread(new Runnable(){
			@Override
			public void run() {
				ReadEngine re = new ReadEngine(inputUrl);
				queues = re.getQueues();
				re.read();
			}
		});
		readThread.setPriority(Thread.MAX_PRIORITY);
		readThread.start();
		
		//2. then we wait 10s for data read.
		System.out.println("2. then we wait 10s for data read.");
		Thread.sleep(10*1000);
		
		//3. next we start word count.
		System.out.println("3. next we start word count.");
		int size =  queues.size();
		ExecutorService mapPool = Executors.newFixedThreadPool(size);
		List<Future<WordCount>> futures = new ArrayList<>(size); 
		List<WordCount> wcs = new ArrayList<>(size);
		for(int i = 0; i < size; i++)
			futures.add(mapPool.submit(new MapJob(queues.get(i))));
 		for(int i = 0; i < size; i++)
			wcs.add(futures.get(i).get());
 		mapPool.shutdown();
 		
		//4. then we merge the result.
		System.out.println("4. then we merge the result.");
		ExecutorService reducePool = Executors.newFixedThreadPool(1);
		Future<WordCount> reduceFuture = reducePool.submit(new ReduceJob(wcs));
		WordCount fwc = reduceFuture.get();
		reducePool.shutdown();
		
		//5. finally we write them in the file
		System.out.println("5. finally we write them in the file");
		WriteEngine we = new WriteEngine(outputUrl,fwc);
		we.write();
		
		System.out.println("all job have done, we use " + (System.currentTimeMillis() - startTime) / 1000 + " seconds, and find " + 
		Time.ERROR_COUNT.get() + " wrong data, we just ignore them.");
	}

}
