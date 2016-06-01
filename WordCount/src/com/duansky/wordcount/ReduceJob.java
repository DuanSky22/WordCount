package com.duansky.wordcount;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * date 2016年6月1日 下午2:06:38
 * @author DuanSky
 */

public class ReduceJob implements Callable<WordCount>{
	
	private List<WordCount> list;

	public ReduceJob(List<WordCount> list){
		this.list = list;
	}
	
	@Override
	public WordCount call() throws Exception {
		WordCount res = new WordCount();
		for(WordCount wc : list)
			res.merge(wc);
		return res;
	}

}
