package com.wbw.birthday;

import java.util.ArrayList;

import com.wbw.birthday.calender.LunarCalendar;






import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ListBirthdayActivity  extends Activity{
	private LunarCalendar lcCalendar = null;
	ArrayList<String> scheduleDate = new ArrayList<String>();
	String scheduleDay ;  //这一天的阳历
	String scheduleYear;
    String scheduleMonth;
    String week;
    String chinese_year;
    private String dateInfo_yangli,dateInfo_yinli;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listbirthday);
		Bundle bundle = getIntent().getExtras();
		scheduleDate = bundle.getStringArrayList("info");
		scheduleYear =  scheduleDate.get(0);
		scheduleMonth =  scheduleDate.get(1);
		scheduleDay =  scheduleDate.get(2);
		week =  scheduleDate.get(3);
		chinese_year = scheduleDate.get(4);
		dateInfo_yangli=scheduleYear+"年"+scheduleMonth+"月"+scheduleDay+"日";
       	//添加农历信息	
		dateInfo_yinli = getLunarDay(Integer.parseInt(scheduleYear),
 				Integer.parseInt(scheduleMonth), Integer.parseInt(scheduleDay));
		findAllViews();
		setStrings();
		createAction();
	}
	
	private ImageView iv_return,iv_add;
	private TextView tv_title;
	private TextView tv_dayandmonth,tv_dayofweek,tv_lunaryear,tv_list_title;
	private ListView lv_list;
	private void findAllViews(){
		iv_return = (ImageView) findViewById(R.id.iv_return);
		iv_add = (ImageView) findViewById(R.id.iv_add);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_dayandmonth = (TextView) findViewById(R.id.tv_dayandmonth);
		tv_dayofweek = (TextView) findViewById(R.id.tv_dayofweek);
		tv_lunaryear = (TextView) findViewById(R.id.tv_lunaryear);
		tv_list_title = (TextView) findViewById(R.id.tv_list_title);
		lv_list = (ListView) findViewById(R.id.lv_list);
	}
	
	private void setStrings(){
		tv_title.setText(dateInfo_yangli);
		tv_dayandmonth.setText(dateInfo_yinli);
		tv_lunaryear.setText(chinese_year);
		tv_dayofweek.setText(week);
	}
	
	private void createAction(){
		iv_return.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			 overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
				ListBirthdayActivity.this.finish();
			}
		});
	}
	
	/**
	 * 根据日期的年月日返回阴历日期
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public String getLunarDay(int year, int month, int day) {
		lcCalendar=new LunarCalendar();
		String lunar = lcCalendar.getLunarDate(year, month, day);
		return lunar;
	}
	
	

}
