package com.duansky.wordcount;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * date 2016年6月1日 上午11:17:59
 * @author DuanSky
 */

public class WordCount {
	
	public static List<String> WORD_HOUSE = Arrays.asList("happy", "fear", "worry", "nervous", "anxious", "upset", "hope");
	
	public Map<Time,Map<String,Integer>> map = new HashMap<>();
	
	public WordCount(){	}
	
	public void add(String[] data){
		if(data == null || data.length != 2) return;
		String date = data[0];
		String content = data[1];
		//time
		Time time = Time.getTime(date);
		if(time == null) return; //废弃这条数据
		//words
		String[] words = content.toLowerCase().split(" ");
		for(String word : words){
			if(WORD_HOUSE.contains(word)){
				add(time,word);
			}
		}
	}
	
	private void add(Time time,String word){
		if(WORD_HOUSE.contains(word)){
			if(map.containsKey(time)){
				Map<String,Integer> m = map.get(time);
				if(m.containsKey(word)){
					m.put(word, m.get(word)+1);
				}else{
					m.put(word, 1);
				}
			}else{
				Map<String,Integer> m = new HashMap<String,Integer>();
				m.put(word, 1);
				map.put(time, m);
			}
		}
	}
	
	public Map<Time,Map<String,Integer>> getMap(){
		return map;
	}
	
	public WordCount merge(WordCount wc){
		for(Entry<Time,Map<String,Integer>> entry : wc.map.entrySet()){//for the second map.
			Time secondTime = entry.getKey();
			Map<String,Integer> secondWordMap = entry.getValue();
			if(map.containsKey(secondTime)){
				mergeMap(map.get(secondTime),secondWordMap);//if the first map contains this key, we merge their count.
			}else{
				map.put(secondTime, secondWordMap); //if not, we add this key-value pair to the first map.
			}
		}
		return this;
	}

	private void mergeMap(Map<String,Integer> first,Map<String,Integer> second){
		for(Entry<String,Integer> entry : second.entrySet()){
			String secondWord = entry.getKey();
			Integer secondCount = entry.getValue();
			if(first.containsKey(secondWord)){
				first.put(secondWord, first.get(secondWord)+secondCount);
			}else
				first.put(secondWord, secondCount);
		}
	}
}
