package com.wbw.birthday.info;

import java.util.ArrayList;
import java.util.List;


public class BirthdayInfo {
	private String id;
	private String name;   //名字
	private String remark;  //备注
	private int kind;   //公历或阴历
	private int year;   //年
	private int month;  //月+1
	private int day;    //日
	private String timeofday;  //hh:MM:ss
	private int alarmkind;   //提示方式
	
	public static List<BirthdayInfo> binfo_list = new ArrayList<BirthdayInfo>();
	public String getName() {
		return name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getKind() {
		return kind;
	}
	public void setKind(int kind) {
		this.kind = kind;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public String getTimeofday() {
		return timeofday;
	}
	public void setTimeofday(String timeofday) {
		this.timeofday = timeofday;
	}
	public int getAlarmkind() {
		return alarmkind;
	}
	public void setAlarmkind(int alarmkind) {
		this.alarmkind = alarmkind;
	}
	
}
