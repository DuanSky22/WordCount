package com.duansky.wordcount;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;

/**
 * date 2016年6月1日 上午10:55:30
 * @author DuanSky
 * 2013-01-22 14:34:43
 */

public class Time implements Comparable<Time>{
	
	private int year;
	private int month;
	private int day;
	
	//should worry about multi-thread visit.
	public static SortedSet<Time> TIME_HOUSE = Collections.synchronizedSortedSet(new TreeSet<Time>()); 
	public static AtomicLong ERROR_COUNT = new AtomicLong(0);
	
	private Time(int year,int month,int day){
		this.year = year;
		this.month = month;
		this.day = day;
	}

	private static Time getTime0(String time){
		if(time == null) {
			ERROR_COUNT.incrementAndGet();
			return null;
		}
		int year,month,day;
		try{
			String[] t = time.split(" ");
			if(t == null || t.length == 0) {
				ERROR_COUNT.incrementAndGet();
				System.out.println("Error time :" + time);
				return null;
			}
			String[] s = t[0].split("-");
			if(s == null || s.length < 3) {
				ERROR_COUNT.incrementAndGet();
				System.out.println("Error time :" + time);
				return null;
			}
			year = Integer.parseInt(s[0]);
			month = Integer.parseInt(s[1]);
			day = Integer.parseInt(s[2]);
			
		}catch(NullPointerException | NumberFormatException e){
			ERROR_COUNT.incrementAndGet();
			System.out.println("Error time :" + time);
			return null;
		}
		return new Time(year,month,day);
	}
	
	public static Time getTime(String time){
		Time t = getTime0(time);
		if(t != null) TIME_HOUSE.add(t);
		return t;
	}
	
	@Override
	public int compareTo(Time t) {
		if(this.year != t.year) return this.year > t.year ? 1 : -1;
		else{
			if(this.month != t.month) return this.month > t.month ? 1 : -1;
			else{
				if(this.day != t.day) return this.day > t.day ? 1 : -1;
				else return 0;
			}
		}
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Time){
			Time t = (Time) o;
			if(t.year == this.year && t.month == this.month && t.day == this.day)
				return true;
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return day + month * 100 + year * 10000;
	}
	
	@Override
	public String toString(){
		return year + "-" + 
				(month < 10 ? "0" : "") + month + "-" +
				(day < 10 ? "0" : "") + day;
				
	}
}
