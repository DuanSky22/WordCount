package com.duansky.wordcount;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

/**
 * date 2016年6月1日 下午2:46:16
 * @author DuanSky
 */

public class WriteEngine {

	private BufferedWriter writer;
	private WordCount wc;
	
	public WriteEngine(String url,WordCount wc){
		try {
			File file;
			if(!(file = new File(url)).exists())
				file.createNewFile();
			this.writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(url)));
			this.wc = wc;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*
	 * ---------  word list ------
	 * |
	 * |
	 * |
	 * time
	 * list
	 * |
	 * |
	 * |
	 */
	public void write(){
		//1. first we should write head.
		writeHead();
		//2. then write each line.
		writeCoutent();
	}

	private void writeCoutent() {
		Map<Time,Map<String,Integer>> map = wc.getMap();
		for(Time time : Time.TIME_HOUSE){
			String timeS = time.toString();
			Map<String,Integer> content = map.get(time);
			StringBuilder sb = new StringBuilder();
			getLineData(sb,timeS,content);
			try {
				writer.write(sb.toString());
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private void getLineData(StringBuilder sb, String time,
			Map<String, Integer> content) {
		sb.append(time);
		for(String word : WordCountEngine.WORD_HOUSE){
			sb.append("\t" + content.get(word));
		}
		sb.append("\n");
	}
	
	private void writeHead() {
		List<String> words = WordCountEngine.WORD_HOUSE;
		StringBuilder sb = new StringBuilder("Time\\Word");
		for(String word : words){
			sb.append("\t" + word);
		}
		sb.append("\n");
		try {
			writer.write(sb.toString());
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
