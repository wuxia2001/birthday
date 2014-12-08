package com.wbw.birthday;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;




















import org.xmlpull.v1.XmlPullParserException;

import com.wbw.birthday.data.BirthdayInfoXml;
import com.wbw.birthday.data.SharedPreferencesXml;
import com.wbw.birthday.effect.MyAnimation;
import com.wbw.birthday.effect.TouchLight_dark;
import com.wbw.birthday.effect.TouchLight_light;
import com.wbw.birthday.info.BirthdayInfo;
import com.wbw.birthday.util.Comments;
import com.wbw.birthday.util.Util;
import com.wbw.birthday.widget.BorderText;
import com.wbw.birthday.widget.BorderTextView;
import com.wbw.birthday.widget.CalendarView;
import com.wbw.birthday.widget.dialog.ExitDialog;
import com.wbw.birthday.widget.dialog.TimerPickDialog;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.R.bool;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity implements OnGestureListener,OnClickListener,OnLongClickListener {
    private AnimationSet mModalInAnim;
	private static final String Tag="CalendarActivity";
	private static int jumpMonth = 0;      //每次滑动，增加或减去一个月,默认为0（即显示当前月）
	private static int jumpYear = 0;       //滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private String currentDate = "";
	private ViewFlipper flipper = null;
	private GestureDetector gestureDetector = null;
	private CalendarView calV = null,left_calView = null;
	private GridView gridView = null;
	private TextView topText = null;
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
	private Context mContext;
	private  ArrayList<String> scheduleDate;
	
	LayoutAnimationController animation_s = null ;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = MainActivity.this;
		Comments.defaultContext = getApplicationContext();
		
		Date date = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    	currentDate = sdf.format(date);  //当期日期
    	year_c = Integer.parseInt(currentDate.split("-")[0]);
    	month_c = Integer.parseInt(currentDate.split("-")[1]);
    	day_c = Integer.parseInt(currentDate.split("-")[2]);
    	gestureDetector = new GestureDetector(this);
    	
    	   mModalInAnim = (AnimationSet) Util.init().loadAnimation(this, R.anim.modal_in);
		      
    	   animation_s = AnimationUtils.loadLayoutAnimation(mContext, R.anim.layout_random_fade);
    		
    	
    	 //处理外部存储
		 boolean sdCardExist = Environment.getExternalStorageState()
				  .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
		 if (sdCardExist)
		 {
			 String file_path = Environment.getExternalStorageDirectory().toString();
			 System.out.println("filepa:"+file_path);
			 Comments.BasePath = file_path+"/birthday/";
		 }
		 Util.init().creatFileIfNotExist(Comments.BasePath);
		 firstRun();
		 getBirthdayInfo();
    	createView();
	}
	
	private void firstRun(){
		boolean isfirst = Boolean.valueOf(SharedPreferencesXml.init().getConfigSharedPreferences("isfirst", "true"));
		if(isfirst){
			//是第一次
			Util.init().creatFileIfNotExist(Comments.BasePath+Comments.xml_name);
			SharedPreferencesXml.init().setConfigSharedPreferences("isfirst", "false");
			try {
				BirthdayInfoXml.instance().saveBirthdayInfoXml(BirthdayInfo.binfo_list, Comments.BasePath+Comments.xml_name);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	private void getBirthdayInfo(){
		try {
			BirthdayInfoXml.instance().getBirthdayInfoXml(Comments.BasePath+Comments.xml_name);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected int getScreenWith() {
        return getResources().getDisplayMetrics().widthPixels;
    }

	
	private void createView(){
		  flipper = (ViewFlipper) findViewById(R.id.flipper);
	        flipper.removeAllViews();
	        int w = getScreenWith();
	        calV = new CalendarView(this, getResources(),
	        		jumpMonth,jumpYear,year_c,month_c,day_c,w);
	        
	        left_calView = new CalendarView(this, getResources(),
	        		jumpMonth+1,jumpYear,year_c,month_c,day_c,getScreenWith());
	        
	        
	        addGridView();
	        gridView.setAdapter(calV);
	        //flipper.addView(gridView);
	        flipper.addView(gridView,0);
	        
	        
			topText = (TextView) findViewById(R.id.schedule_toptext);
			addTextToTopTextView(topText);
			
			addMenu();
			
			
	}
	
	private RelativeLayout relate_level2;
	private TextView birthday_goin,config_goin;
	private boolean areLevel2Showing = false;
	private boolean isleftshowing = false,isrightshowing = false;
	private ImageView iv_goto,iv_today,iv_convert;
	private ImageView home;
	private void addMenu(){
		relate_level2 = (RelativeLayout) findViewById(R.id.relate_level2);
		//relate_level3 = (RelativeLayout) findViewById(R.id.relate_level3);
		home = (ImageView) findViewById(R.id.home);
		birthday_goin = (TextView) findViewById(R.id.birthday_goin);
		birthday_goin.setVisibility(View.GONE);
		config_goin = (TextView) findViewById(R.id.config_goin);
		config_goin.setVisibility(View.GONE);
		iv_goto = (ImageView) findViewById(R.id.iv_goto);
		iv_today = (ImageView) findViewById(R.id.iv_today);
		iv_convert = (ImageView) findViewById(R.id.iv_convert);
		home.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!areLevel2Showing) {
					//要显示
					MyAnimation.startAnimationsIn(relate_level2, 500);
					MyAnimation.startAnimationsShow(birthday_goin,500,
							MainActivity.this,R.anim.push_right_in);
					isleftshowing = true;
					MyAnimation.startAnimationsShow(config_goin,500,
							MainActivity.this,R.anim.push_left_in);
					isrightshowing = true;
				} else {	
					//显示中，要隐藏
					MyAnimation.startAnimationsOut(relate_level2, 500, 0);	
					MyAnimation.startAnimationsHide(birthday_goin,500,
							MainActivity.this,R.anim.push_right_out);
					isleftshowing = false;
					MyAnimation.startAnimationsHide(config_goin,500,
							MainActivity.this,R.anim.push_left_out);
					isrightshowing = false;
				}
				areLevel2Showing = !areLevel2Showing;
//				if(isleftshowing){
//					//显示中，要隐藏
//					MyAnimation.startAnimationsHide(birthday_goin,500,
//							MainActivity.this,R.anim.push_right_out);
//				}else{
//					MyAnimation.startAnimationsShow(birthday_goin,500,
//							MainActivity.this,R.anim.push_right_in);
//				}
//				isleftshowing = !isleftshowing;
//				if(isrightshowing){
//					//显示中，要隐藏
//					MyAnimation.startAnimationsHide(config_goin,500,
//							MainActivity.this,R.anim.push_left_out);
//				}else{
//					MyAnimation.startAnimationsShow(config_goin,500,
//							MainActivity.this,R.anim.push_left_in);
//				}
//				isrightshowing = !isrightshowing;
			}
		});
		iv_goto.setOnTouchListener(TouchLight_light.init());
		iv_today.setOnTouchListener(TouchLight_light.init());
		iv_convert.setOnTouchListener(TouchLight_light.init());
		iv_today.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				actionMenu(type_today);
				 hideAllMenu();
			}
		});
		iv_goto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				actionMenu(type_goto);
				 hideAllMenu();
			}
		});
		iv_convert.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				actionMenu(type_convert);
				 hideAllMenu();
			}
		});
	}
	
	//菜单里的当有一个按钮按下时，代表己选择了子项，所有的全部隐藏
	private void hideAllMenu(){
		areLevel2Showing = false;
		MyAnimation.startAnimationsOut(relate_level2, 500, 0);	
		MyAnimation.startAnimationsHide(birthday_goin,500,
				MainActivity.this,R.anim.push_right_out);
		isleftshowing = false;
		MyAnimation.startAnimationsHide(config_goin,500,
				MainActivity.this,R.anim.push_left_out);
		isrightshowing = false;
	}
	
	private final int type_today = 1,type_goto=2,type_convert = 3;
	private void actionMenu(int type){
        switch (type){
        case type_today:
        	//跳转到今天
        	int xMonth = jumpMonth;
        	int xYear = jumpYear;
        	int gvFlag =0;
        	jumpMonth = 0;
        	jumpYear = 0;
        	addGridView();   //添加一个gridView
        	year_c = Integer.parseInt(currentDate.split("-")[0]);
        	month_c = Integer.parseInt(currentDate.split("-")[1]);
        	day_c = Integer.parseInt(currentDate.split("-")[2]);
        	calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c,getScreenWith());
	        gridView.setAdapter(calV);
	        addTextToTopTextView(topText);
	        gvFlag++;
	        flipper.addView(gridView,gvFlag);
	        if(xMonth == 0 && xYear == 0){
	        	//nothing to do
	        }else if((xYear == 0 && xMonth >0) || xYear >0){
	        	this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_in));
				this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_out));
				this.flipper.showNext();
	        }else{
	        	this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_in));
				this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_out));
				this.flipper.showPrevious();
	        }
			flipper.removeViewAt(0);
        	break;
        case type_goto:
        	
        	timePickDialog();
        	break;
        	
        case type_convert:
        //	Intent mIntent=new Intent(CalendarActivity.this, CalendarConvertTrans.class);
       // startActivity(mIntent);
        	
        	break;
        	
        }
	
	}
	
	private Dialog timerPickDialog;
	private void timePickDialog(){
		LayoutInflater inf = LayoutInflater.from(mContext); 		
		final View view = inf.inflate(R.layout.dialog_timepick, null);  		
		Button ok = (Button) view.findViewById(R.id.wifi_dialog_ok);
		Button cancle = (Button) view.findViewById(R.id.wifi_dialog_cancle);	
		final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker);
		datePicker.updateDate(year_c, month_c-1, day_c);
		final Dialog dialog = new Dialog(mContext, R.style.MyDialog);
		//dialog.setView(view , 0, 0, 0, 0 );
		dialog.setContentView(view);		
		cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if(dialog.isShowing())
					dialog.dismiss();
			}
		});
		
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				 int year = datePicker.getYear();
				 int month = datePicker.getMonth() ; 
				 int dayOfMonth = datePicker.getDayOfMonth();
				 if(year < 1901 || year > 2049){
						//不在查询范围内
						new AlertDialog.Builder(mContext).setTitle("错误日期").setMessage("跳转日期范围(1901/1/1-2049/12/31)").setPositiveButton("确认", null).show();
					}else{
						int gvFlag = 0;
						addGridView();   //添加一个gridView
			        	calV = new CalendarView(mContext, mContext.getResources(),year,month+1,dayOfMonth);
				        gridView.setAdapter(calV);
				        addTextToTopTextView(topText);
				        gvFlag++;
				        flipper.addView(gridView,gvFlag);
				        if(year == year_c && month+1 == month_c){
				        	//nothing to do
				        }
				        if((year == year_c && month+1 > month_c) || year > year_c ){
				        	MainActivity.this.flipper.setInAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.push_left_in));
				        	MainActivity.this.flipper.setOutAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.push_left_out));
				        	MainActivity.this.flipper.showNext();
				        }else{
				        	MainActivity.this.flipper.setInAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.push_right_in));
				        	MainActivity.this.flipper.setOutAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.push_right_out));
				        	MainActivity.this.flipper.showPrevious();
				        }
				        flipper.removeViewAt(0);
				        //跳转之后将跳转之后的日期设置为当期日期
				        year_c = year;
						month_c = month+1;
						day_c = dayOfMonth;
						jumpMonth = 0;
						jumpYear = 0;
					}
				if(dialog.isShowing())
					dialog.dismiss();
			}
		});
		View mDialogView = dialog.getWindow().getDecorView().findViewById(android.R.id.content);
		 mDialogView.startAnimation(mModalInAnim);
		dialog.show();
		
	}
	

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		int gvFlag = 0;         //每次添加gridview到viewflipper中时给的标记
		if (e1.getX() - e2.getX() > 50) {
            //像左滑动
			gotoLeft(gvFlag);
			return true;
		} else if (e1.getX() - e2.getX() < -50) {
            //向右滑动
			gotoRight(gvFlag);
			return true;
		}
		return false;
	}
	
	private final int changeleft = 1;
	private final int changeright = 2;
	 Handler handler = new Handler() {
	        public void handleMessage(Message msg) {
	            switch (msg.what) {
	                case changeleft:
	                    left_calView = new CalendarView(MainActivity.this, getResources(),jumpMonth+1,jumpYear,year_c,month_c,day_c,getScreenWith());
	                	break;
	            }
	        }
	 };
	//向左滑动
	private void gotoLeft(int gvFlag){
		//addGridView();   //添加一个gridView
		gridView.setLayoutAnimation(animation_s);
		jumpMonth++;     //下一个月
		calV = (CalendarView) left_calView.clone();  //对象复制，为了加快速
		  addTextToTopTextView(topText);
//        flipper.removeAllViews();
//        flipper.addView(gridView, 0);
        gridView.setAdapter(calV);
		handler.sendEmptyMessage(changeleft);
	}
	
	private void gotoRight(int gvFlag){
//		addGridView();   //添加一个gridView
		jumpMonth--;     //上一个月
		
		calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c,getScreenWith());
        gridView.setAdapter(calV);
        gvFlag++;
        addTextToTopTextView(topText);
        //flipper.addView(gridView);
        flipper.addView(gridView,gvFlag);
        
		this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_in));
		this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_out));
		this.flipper.showPrevious();
		flipper.removeViewAt(0);
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
		//draw = getResources().getDrawable(R.drawable.schedule_title_bg);
		//view.setBackgroundDrawable(draw);
		textDate.append(calV.getShowYear()).append("年").append(
				calV.getShowMonth()).append("月").append("\t");
		if (!calV.getLeapMonth().equals("") && calV.getLeapMonth() != null) {
			textDate.append("闰").append(calV.getLeapMonth()).append("月")
					.append("\t");
		}
		textDate.append(calV.getAnimalsYear()).append("年").append("(").append(
				calV.getCyclical()).append("年)");
		view.setText(textDate);
		//view.setTextColor(Color.WHITE);
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
	
	//添加农历信息
		public String getLunarDayInfo(){
			StringBuffer textDate = new StringBuffer();
			if (!calV.getLeapMonth().equals("") && calV.getLeapMonth() != null) {
				textDate.append("闰").append(calV.getLeapMonth()).append("月")
						.append("\t");
			}
			textDate.append(calV.getAnimalsYear()).append("年").append("(").append(
					calV.getCyclical()).append("年)");
			return textDate.toString();
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

        
		gridView = new GridView(this);
		gridView.setNumColumns(7);
		gridView.getHeight();
		gridView.setLayoutAnimation(animation_s);
		//gridView.
//		gridView.setColumnWidth(46);
//	//	gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
//		if(Width == 480 && Height == 800){
//			gridView.setColumnWidth(69);
//		}else if(Width==800&&Height==1280){
//			gridView.setColumnWidth(69);
//		}
		gridView.setColumnWidth(Width/7);
		
		
		gridView.setGravity(Gravity.CENTER_VERTICAL);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT)); // 去除gridView边框
		gridView.setVerticalSpacing(1);
		gridView.setHorizontalSpacing(1);
        //gridView.setBackgroundResource(R.drawable.gridview_bk);
		gridView.setBackgroundResource(R.drawable.bg);
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
					   String scheduleYear = calV.getShowYear();
	                  String scheduleMonth = calV.getShowMonth();
	                  String week = "";
	                 
	                  Log.i("日程历史浏览", scheduleDay);	               
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
	                  //前面是得到 了所有的信息
	                  scheduleDate = new ArrayList<String>();
	                  scheduleDate.add(scheduleYear);
	                  scheduleDate.add(scheduleMonth);
	                  scheduleDate.add(scheduleDay);
	                  scheduleDate.add(week);
	                  String chinese_year = getLunarDayInfo();
	                  scheduleDate.add(chinese_year);
	                  
	                  Intent intent = new Intent(MainActivity.this,ListBirthdayActivity.class);
	                  Bundle b = new Bundle();
	                  b.putStringArrayList("info",scheduleDate);
	                  intent.putExtras(b);
	                  MainActivity.this.startActivity(intent);
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
		    	calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c,getScreenWith());
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
			
			
			@Override
			public boolean onKeyDown(int keyCode, KeyEvent event) {
				// TODO 自动生成的方法存根
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					Dialog dialog = ExitDialog.init().creatExitDialog(MainActivity.this);
					View mDialogView = dialog.getWindow().getDecorView().findViewById(android.R.id.content);
					 mDialogView.startAnimation(mModalInAnim);
					dialog.show();
					return true;

				} else {
					return super.onKeyDown(keyCode, event);
				}
				
			}
	
}
