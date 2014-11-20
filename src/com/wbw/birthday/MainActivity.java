package com.wbw.birthday;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;






import com.wbw.birthday.widget.BorderText;
import com.wbw.birthday.widget.BorderTextView;
import com.wbw.birthday.widget.CalendarView;
import com.wbw.birthday.widget.MyAnimation;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity implements OnGestureListener,OnClickListener,OnLongClickListener {
	private static final String Tag="CalendarActivity";
	private static int jumpMonth = 0;      //每次滑动，增加或减去一个月,默认为0（即显示当前月）
	private static int jumpYear = 0;       //滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private String currentDate = "";
	private ViewFlipper flipper = null;
	private GestureDetector gestureDetector = null;
	private CalendarView calV = null;
	private GridView gridView = null;
	private BorderText topText = null;
	private Drawable draw = null;
	private BorderTextView schdule_tip;
	private Button add;
	private Button quit;
	private TextView day_tv;
	private TextView launarDay;
	private ListView listView;
	private TextView weekday;
	private TextView lunarTime;
	private ListView list;
	private String dateInfo;//点击gridview的日期信息
	private LayoutInflater inflater;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Date date = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    	currentDate = sdf.format(date);  //当期日期
    	year_c = Integer.parseInt(currentDate.split("-")[0]);
    	month_c = Integer.parseInt(currentDate.split("-")[1]);
    	day_c = Integer.parseInt(currentDate.split("-")[2]);
    	gestureDetector = new GestureDetector(this);
        flipper = (ViewFlipper) findViewById(R.id.flipper);
        flipper.removeAllViews();
        calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
        
        addGridView();
        gridView.setAdapter(calV);
        //flipper.addView(gridView);
        flipper.addView(gridView,0);
        
		topText = (BorderText) findViewById(R.id.schedule_toptext);
		addTextToTopTextView(topText);
		
		addMenu();
	}
	
	private RelativeLayout relate_level2;
	private boolean areLevel2Showing = true;
	private ImageButton home;
	private void addMenu(){
		relate_level2 = (RelativeLayout) findViewById(R.id.relate_level2);
		//relate_level3 = (RelativeLayout) findViewById(R.id.relate_level3);
		home = (ImageButton) findViewById(R.id.home);
		home.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!areLevel2Showing) {
					MyAnimation.startAnimationsIn(relate_level2, 500);
				} else {				
					MyAnimation.startAnimationsOut(relate_level2, 500, 0);					
				}
				areLevel2Showing = !areLevel2Showing;
			}
		});
	}
	

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		int gvFlag = 0;         //每次添加gridview到viewflipper中时给的标记
		if (e1.getX() - e2.getX() > 50) {
            //像左滑动
			addGridView();   //添加一个gridView
			jumpMonth++;     //下一个月
			
			calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
	        gridView.setAdapter(calV);
	        //flipper.addView(gridView);
	        addTextToTopTextView(topText);
	        gvFlag++;
	        flipper.addView(gridView, gvFlag);
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_out));
			this.flipper.showNext();
			flipper.removeViewAt(0);
			return true;
		} else if (e1.getX() - e2.getX() < -50) {
            //向右滑动
			addGridView();   //添加一个gridView
			jumpMonth--;     //上一个月
			
			calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
	        gridView.setAdapter(calV);
	        gvFlag++;
	        addTextToTopTextView(topText);
	        //flipper.addView(gridView);
	        flipper.addView(gridView,gvFlag);
	        
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_out));
			this.flipper.showPrevious();
			flipper.removeViewAt(0);
			return true;
		}
		return false;
	}

	public boolean onTouchEvent(MotionEvent event) {

		return this.gestureDetector.onTouchEvent(event);
	}

	
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * 添加画板头部的年份 闰哪月等信息
	 * */
	public void addTextToTopTextView(TextView view){
		StringBuffer textDate = new StringBuffer();
		draw = getResources().getDrawable(R.drawable.schedule_title_bg);
		view.setBackgroundDrawable(draw);
		textDate.append(calV.getShowYear()).append("年").append(
				calV.getShowMonth()).append("月").append("\t");
		if (!calV.getLeapMonth().equals("") && calV.getLeapMonth() != null) {
			textDate.append("闰").append(calV.getLeapMonth()).append("月")
					.append("\t");
		}
		textDate.append(calV.getAnimalsYear()).append("年").append("(").append(
				calV.getCyclical()).append("年)");
		view.setText(textDate);
		view.setTextColor(Color.WHITE);
		view.setTextSize(15.0f);
		view.setTypeface(Typeface.DEFAULT_BOLD);
	}
	
	
	//添加农历信息
	public void addLunarDayInfo(TextView text){
		StringBuffer textDate = new StringBuffer();
		if (!calV.getLeapMonth().equals("") && calV.getLeapMonth() != null) {
			textDate.append("闰").append(calV.getLeapMonth()).append("月")
					.append("\t");
		}
		textDate.append(calV.getAnimalsYear()).append("年").append("(").append(
				calV.getCyclical()).append("年)");
		text.setText(textDate);
	}
	
	//添加gridview,显示具体的日期
	@SuppressLint("ResourceAsColor")
	private void addGridView() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		//取得屏幕的宽度和高度
		WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth(); 
        int Height = display.getHeight();
        
        Log.d(Tag, "屏幕分辨率=="+"height*weight"+Height+Width);
        
		gridView = new GridView(this);
		gridView.setNumColumns(7);
		gridView.setColumnWidth(46);
	//	gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		if(Width == 480 && Height == 800){
			gridView.setColumnWidth(69);
		}else if(Width==800&&Height==1280){
			gridView.setColumnWidth(69);
		}
		
		
		gridView.setGravity(Gravity.CENTER_VERTICAL);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT)); // 去除gridView边框
		gridView.setVerticalSpacing(1);
		gridView.setHorizontalSpacing(1);
        gridView.setBackgroundResource(R.drawable.gridview_bk);
		gridView.setOnTouchListener(new OnTouchListener() {
            //将gridview中的触摸事件回传给gestureDetector
			
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return MainActivity.this.gestureDetector
						.onTouchEvent(event);
			}
		});

		
		gridView.setOnItemClickListener(new OnItemClickListener() {
            //gridView中的每一个item的点击事件
			
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				  //点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
				  int startPosition = calV.getStartPositon();
				  int endPosition = calV.getEndPosition();
				  if(startPosition <= position  && position <= endPosition){
					  String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0];  //这一天的阳历
					  //String scheduleLunarDay = calV.getDateByClickItem(position).split("\\.")[1];  //这一天的阴历
	                  String scheduleYear = calV.getShowYear();
	                  String scheduleMonth = calV.getShowMonth();
	                  String week = "";
	                 
	                  Log.i("日程历史浏览", scheduleDay);
	                  
	                  //通过日期查询这一天是否被标记，如果标记了日程就查询出这天的所有日程信息
	                 // scheduleIDs = dao.getScheduleByTagDate(Integer.parseInt(scheduleYear), Integer.parseInt(scheduleMonth), Integer.parseInt(scheduleDay));
	                  
	                  //得到这一天是星期几
	                  switch(position%7){
	                  case 0:
	                	  week = "星期日";
	                	  break;
	                  case 1:
	                	  week = "星期一";
	                	  break;
	                  case 2:
	                	  week = "星期二";
	                	  break;
	                  case 3:
	                	  week = "星期三";
	                	  break;
	                  case 4:
	                	  week = "星期四";
	                	  break;
	                  case 5:
	                	  week = "星期五";
	                	  break;
	                  case 6:
	                	  week = "星期六";
	                	  break;
	                  }
					 
	            
	                	  
	                  }else{ //如果没有标记位直接则跟换为“暂无安排”
	                 
	                	  
	                	  schdule_tip.setText("暂无安排");
	                	  listView.setVisibility(View.INVISIBLE);
	                	
	                  }	                  
	               
			}
		});
		gridView.setLayoutParams(params);
	}
	
	/**
	 * 
	 * 被标记有相应的日程安排*/
		
		
		
		
		
	
		 @Override
			protected void onRestart() {
				int xMonth = jumpMonth;
		    	int xYear = jumpYear;
		    	int gvFlag =0;
		    	jumpMonth = 0;
		    	jumpYear = 0;
		    	addGridView();   //添加一个gridView
		    	year_c = Integer.parseInt(currentDate.split("-")[0]);
		    	month_c = Integer.parseInt(currentDate.split("-")[1]);
		    	day_c = Integer.parseInt(currentDate.split("-")[2]);
		    	calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
		        gridView.setAdapter(calV);
		        addTextToTopTextView(topText);
		        gvFlag++;
		        flipper.addView(gridView,gvFlag);
				flipper.removeViewAt(0);
				super.onRestart();
			}

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}



			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				return false;
			}
	
}
