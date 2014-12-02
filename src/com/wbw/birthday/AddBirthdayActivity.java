package com.wbw.birthday;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;



import com.wbw.birthday.calender.LunarCalendar;
import com.wbw.birthday.data.BirthdayInfoXml;
import com.wbw.birthday.data.SharedPreferencesXml;
import com.wbw.birthday.info.BirthdayInfo;
import com.wbw.birthday.util.Comments;
import com.wbw.birthday.util.Util;
import com.wbw.birthday.widget.CalendarView;
import com.wbw.birthday.widget.dialog.TimerPickDialog;

import android.R.integer;
import android.R.interpolator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddBirthdayActivity extends Activity{
	
    private AnimationSet mModalInAnim;
	
	private int rilikind = 0;  //0为公历，1为农历
	private int tixingkind = 0;  //0 1 2 3 4 5
	private int duplicatekind = 0; //0一次     1每年
	
	ArrayList<String> scheduleDate = new ArrayList<String>();
	String scheduleDay ;  //这一天的阳历
	String scheduleYear;
    String scheduleMonth;
    String week;
  //  String chinese_year;
    private int hh,mm;
    
    private int chinese_year;
    private int chinese_month,chinese_day;
    
    private String dateInfo_yangli,dateInfo_yinli;
    private LunarCalendar lcCalendar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addbirthday);
		Bundle bundle = getIntent().getExtras();
		scheduleDate = bundle.getStringArrayList("info");
		scheduleYear =  scheduleDate.get(0);
		scheduleMonth =  scheduleDate.get(1);
		scheduleDay =  scheduleDate.get(2);
		lcCalendar=new LunarCalendar();
		
		dateInfo_yangli=scheduleYear+"年"+scheduleMonth+"月"+scheduleDay+"日";
		int[] chinesedate = lcCalendar.getLunarDateAll(Integer.valueOf(scheduleYear), 
				Integer.valueOf(scheduleMonth),  Integer.valueOf(scheduleDay));
		
		chinese_year = chinesedate[0];
		chinese_month = chinesedate[1];
		chinese_day = chinesedate[2];
		
		dateInfo_yinli = chinese_year+"年"+lcCalendar.chineseNumber[chinese_month - 1]+"月"+lcCalendar.getChinaDayString(chinese_day);
       	
		   mModalInAnim = (AnimationSet) Util.init().loadAnimation(this, R.anim.modal_in);
		      
		
		//先处理公历，再处理农历
		findAllViews();
		setDefaults();
//		setImages();
		createAction();
	}
	
	private ImageView iv_return,iv_complete;
	private TextView tv_title,tv_selectrili,tv_select_year,tv_select_hour,tv_tixingkind,tv_duplicate;
	private EditText et_inputname,et_inputremark;
	private Button bt_option_gongli,bt_option_nongli;
	private Button bt_option_0,bt_option_1,bt_option_2,bt_option_3,bt_option_4,bt_option_5;
	private Button bt_option_once,bt_option_everyyear;
	private Button[] bt_options ;
	private void findAllViews(){
		iv_return = (ImageView) findViewById(R.id.iv_return);
		iv_complete = (ImageView) findViewById(R.id.iv_complete);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_selectrili = (TextView) findViewById(R.id.tv_selectrili);
		tv_select_year = (TextView) findViewById(R.id.tv_select_year);
		tv_select_hour = (TextView) findViewById(R.id.tv_select_hour);
		tv_tixingkind = (TextView) findViewById(R.id.tv_tixingkind);
		tv_duplicate = (TextView) findViewById(R.id.tv_duplicate);
		et_inputname = (EditText) findViewById(R.id.et_inputname);
		et_inputremark = (EditText) findViewById(R.id.et_inputremark);
		bt_option_gongli = (Button) findViewById(R.id.bt_option_gongli);
		bt_option_nongli = (Button) findViewById(R.id.bt_option_nongli);
		bt_option_0 = (Button) findViewById(R.id.bt_option_0);
		bt_option_1 = (Button) findViewById(R.id.bt_option_1);
		bt_option_2 = (Button) findViewById(R.id.bt_option_2);
		bt_option_3 = (Button) findViewById(R.id.bt_option_3);
		bt_option_4 = (Button) findViewById(R.id.bt_option_4);
		bt_option_5 = (Button) findViewById(R.id.bt_option_5);
		Button[] tButtons = {bt_option_0,bt_option_1,bt_option_2,bt_option_3,bt_option_4,bt_option_5};
		bt_options = tButtons;
		bt_option_once = (Button) findViewById(R.id.bt_option_once);
		bt_option_everyyear = (Button) findViewById(R.id.bt_option_everyyear);
	}
	
	private void setDefaults(){
		tv_title.setText(R.string.add_birthday);
		tv_select_year.setText(dateInfo_yangli);
		setOptionSelectRiLi(rilikind);
		setOptionTiXingKind(tixingkind);
		setOptionDuplicate(duplicatekind);
		final Calendar calendar = Calendar.getInstance();   
        final int hour   = calendar.get(Calendar.HOUR_OF_DAY);  
        final int minute = calendar.get(Calendar.MINUTE);
        hh = hour;
        mm = minute;
        tv_select_hour.setText(hour+"时"+minute+"分");
	}
	
	
	private void createAction(){
		iv_complete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String nameString = et_inputname.getText().toString();
				if(nameString == null || nameString.equals("")){
					Toast.makeText(AddBirthdayActivity.this, "名称不能为空", 
							Toast.LENGTH_SHORT).show();
					return;
				}
				int id =Integer.valueOf(SharedPreferencesXml.init().getConfigSharedPreferences("id_max", "0"));
				id++;
				SharedPreferencesXml.init().setConfigSharedPreferences("id_max", String.valueOf(id));
				BirthdayInfo birthdayInfo = new BirthdayInfo();
				birthdayInfo.setAlarmkind(tixingkind);
				birthdayInfo.setDuplicatekind(duplicatekind);
				birthdayInfo.setName(et_inputname.getText().toString());
				birthdayInfo.setRemark(et_inputremark.getText().toString());
				birthdayInfo.setId(String.valueOf(id));
				birthdayInfo.setKind(rilikind);
				birthdayInfo.setTimeofday(hh+":"+mm);
				if(rilikind == 1){
					birthdayInfo.setYear(chinese_year);
					birthdayInfo.setMonth(chinese_month);
					
					birthdayInfo.setDay(chinese_day);
				}else
				{				
					birthdayInfo.setYear(Integer.valueOf(scheduleYear));
					birthdayInfo.setMonth(Integer.valueOf(scheduleMonth));
					
					birthdayInfo.setDay(Integer.valueOf(scheduleDay));
				}
				BirthdayInfo.binfo_list.add(birthdayInfo);
				try {
					BirthdayInfoXml.instance().saveBirthdayInfoXml(BirthdayInfo.binfo_list,
							Comments.BasePath+Comments.xml_name);
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
				Toast.makeText(AddBirthdayActivity.this, "添加成功", 
						Toast.LENGTH_SHORT).show();
				AddBirthdayActivity.this.finish();
					
			}
		});
		
		iv_return.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AddBirthdayActivity.this.finish();
			}
		});
		
		bt_option_gongli.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				setOptionSelectRiLi(0);
			}
		});
		bt_option_nongli.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO 自动生成的方法存根
						setOptionSelectRiLi(1);
					}
		});
		for(int i=0;i<bt_options.length;i++){
			final int tmp = i;
			bt_options[i].setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					setOptionTiXingKind(tmp);
				}
			});
		}
		bt_option_once.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setOptionDuplicate(0);
			}
		});
		bt_option_everyyear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setOptionDuplicate(1);
			}
		});
		
		tv_select_hour.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				timePickDialog();
			}
		});
		
		tv_select_year.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setOptionSelectYear();
			}
		});
	}
	
	
	private void setOptionSelectYear(){
		if(rilikind == 1){
			chineseyearPickDialog();
		}else{
			//公历
			yearPickDialog();
		}
	}
	
	//日历各类的选择
	private void setOptionSelectRiLi(int num){
		//1为9宫格  2为12宫格 3为15宫格
		rilikind = num;
		bt_option_gongli.setBackgroundResource(R.drawable.option);
		bt_option_nongli.setBackgroundResource(R.drawable.option);		
		String meitu = getString(R.string.select_rili);
		String every = getString(R.string.everty_year);
	//	String yeartString = tv_select_year.getText().toString();
		if(num == 0 ){  //公历
			bt_option_gongli.setBackgroundResource(R.drawable.option_select);
			tv_select_year.setText(dateInfo_yangli);
			String yeartString = tv_select_year.getText().toString();
			meitu = String.format(meitu, bt_option_gongli.getText().toString());
			every = String.format(every, bt_option_gongli.getText().toString(),yeartString);
		}else
		if(num == 1 ){
			bt_option_nongli.setBackgroundResource(R.drawable.option_select);
			//农历
			tv_select_year.setText(dateInfo_yinli);
			String yeartString = tv_select_year.getText().toString();
			meitu = String.format(meitu, bt_option_nongli.getText().toString());
			every = String.format(every, bt_option_nongli.getText().toString(),yeartString);
		}
		tv_selectrili.setText(meitu);
		//最后选择是是阴历多少
		
		bt_option_everyyear.setText(every);
	}
	
	//提醒方式的选择
	private void setOptionTiXingKind(int num){
		setBtOptionNormal();
		tixingkind = num;
		bt_options[num].setBackgroundResource(R.drawable.option_select);
		String meitu = getString(R.string.tixingkind);
		meitu = String.format(meitu, bt_options[num].getText().toString());	
		tv_tixingkind.setText(meitu);
	}
	
	private void setBtOptionNormal(){
		for(int i=0;i<bt_options.length;i++){
			bt_options[i].setBackgroundResource(R.drawable.option);
		}
	}
	
	private void setOptionDuplicate(int num){
		duplicatekind = num;
		bt_option_once.setBackgroundResource(R.drawable.option);
		bt_option_everyyear.setBackgroundResource(R.drawable.option);		
		String meitu = getString(R.string.duplicate);
		if(num == 0 ){
			bt_option_once.setBackgroundResource(R.drawable.option_select);
			meitu = String.format(meitu, bt_option_once.getText().toString());
			
		}else
		if(num == 1 ){
			bt_option_everyyear.setBackgroundResource(R.drawable.option_select);
			meitu = String.format(meitu, bt_option_everyyear.getText().toString());		
		}
		tv_duplicate.setText(meitu);
	}
	
	
//	private Dialog timerPickDialog;
	private void timePickDialog(){
		LayoutInflater inf = LayoutInflater.from(AddBirthdayActivity.this); 		
		final View view = inf.inflate(R.layout.dialog_timepick, null);  		
		Button ok = (Button) view.findViewById(R.id.wifi_dialog_ok);
		Button cancle = (Button) view.findViewById(R.id.wifi_dialog_cancle);	
		final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker);
		datePicker.setVisibility(View.GONE);
		final TimePicker timerPicker = (TimePicker) view.findViewById(R.id.timerPicker);
		timerPicker.setVisibility(View.VISIBLE);
		timerPicker.setIs24HourView(true);  
		TextView wifi_title_tv = (TextView) view.findViewById(R.id.wifi_title_tv);
		wifi_title_tv.setText(R.string.set_time);
		
		final Calendar calendar = Calendar.getInstance();   
        final int hour   = calendar.get(Calendar.HOUR_OF_DAY);  
        final int minute = calendar.get(Calendar.MINUTE);
        
        timerPicker.setCurrentHour(hour);
        timerPicker.setCurrentMinute(minute);
		
		
		final Dialog dialog = new Dialog(AddBirthdayActivity.this, R.style.MyDialog);
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
				int h = timerPicker.getCurrentHour();
				int m = timerPicker.getCurrentMinute();
				tv_select_hour.setText(h+"时"+m+"分");
				hh = h;
				mm = m;
				if(dialog.isShowing())
					dialog.dismiss();
			}
		});
		dialog.show();
		
	}
	
	
	private void yearPickDialog(){
		LayoutInflater inf = LayoutInflater.from(AddBirthdayActivity.this); 		
		final View view = inf.inflate(R.layout.dialog_timepick, null);  		
		Button ok = (Button) view.findViewById(R.id.wifi_dialog_ok);
		Button cancle = (Button) view.findViewById(R.id.wifi_dialog_cancle);	
		final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker);
		datePicker.updateDate(Integer.valueOf(scheduleYear), 
				Integer.valueOf(scheduleMonth)-1, Integer.valueOf(scheduleDay));
		final Dialog dialog = new Dialog(AddBirthdayActivity.this, R.style.MyDialog);
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
						new AlertDialog.Builder(AddBirthdayActivity.this).setTitle("错误日期").setMessage("请选择日期范围(1901/1/1-2049/12/31)").setPositiveButton("确认", null).show();
				}else{
					
				        scheduleYear = String.valueOf(year);
						scheduleMonth = String.valueOf(month+1);
						scheduleDay = String.valueOf(dayOfMonth);
						dateInfo_yangli=scheduleYear+"年"+scheduleMonth+"月"+scheduleDay+"日";
						int[] chinesedate = lcCalendar.getLunarDateAll(Integer.valueOf(scheduleYear), 
								Integer.valueOf(scheduleMonth),  Integer.valueOf(scheduleDay));
						
						chinese_year = chinesedate[0];
						chinese_month = chinesedate[1];
						chinese_day = chinesedate[2];
						
						dateInfo_yinli = chinese_year+"年"+lcCalendar.chineseNumber[chinese_month-1]+"月"+lcCalendar.getChinaDayString(chinese_day);
				       	
						tv_select_year.setText(dateInfo_yangli);
					}
				if(dialog.isShowing())
					dialog.dismiss();
			}
		});
//		 Window window = dialog.getWindow();  
//		 window.set
//		 //   window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置  
//		        window.setWindowAnimations(R.anim.modal_in);  //添加动画  
		View mDialogView = dialog.getWindow().getDecorView().findViewById(android.R.id.content);
		 mDialogView.startAnimation(mModalInAnim);
		dialog.show();
		
	}
	
	private void chineseyearPickDialog(){
		LayoutInflater inf = LayoutInflater.from(AddBirthdayActivity.this); 		
		final View view = inf.inflate(R.layout.dialog_chinese_yearpick, null);  		
		Button ok = (Button) view.findViewById(R.id.wifi_dialog_ok);
		Button cancle = (Button) view.findViewById(R.id.wifi_dialog_cancle);	
		final EditText et_chineseday = (EditText) view.findViewById(R.id.et_chineseday);
		final EditText et_chinesemonth = (EditText) view.findViewById(R.id.et_chinesemonth);
		final EditText et_chineseyear = (EditText) view.findViewById(R.id.et_chineseyear);
		final Dialog dialog = new Dialog(AddBirthdayActivity.this, R.style.MyDialog);
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
				try{
					int year = Integer.valueOf(et_chineseyear.getText().toString());
					int month = Integer.valueOf(et_chinesemonth.getText().toString());
					int day = Integer.valueOf(et_chineseday.getText().toString());
					 if(year < 1901 || year > 2049){
							//不在查询范围内
							new AlertDialog.Builder(AddBirthdayActivity.this).setTitle("错误日期").setMessage("请选择日期范围(1901/1/1-2049/12/31)").setPositiveButton("确认", null).show();
							return;
					}
					 if(month<=0 || month>12){
							new AlertDialog.Builder(AddBirthdayActivity.this).setTitle("错误日期").setMessage("请选择日期范围(1901/1/1-2049/12/31)").setPositiveButton("确认", null).show();
							return;
					 }
					 if(day<=0 || day>30){
							new AlertDialog.Builder(AddBirthdayActivity.this).setTitle("错误日期").setMessage("请选择日期范围(1901/1/1-2049/12/31)").setPositiveButton("确认", null).show();
							return;
					 }
					 chinese_year = year;
						chinese_month = month;
						chinese_day = day;
						
						dateInfo_yinli = chinese_year+"年"+lcCalendar.chineseNumber[chinese_month-1]+"月"+lcCalendar.getChinaDayString(chinese_day);
				       	
						tv_select_year.setText(dateInfo_yinli);
				}catch(Exception e){
					
				}
				
				if(dialog.isShowing())
					dialog.dismiss();
			}
		});
		View mDialogView = dialog.getWindow().getDecorView().findViewById(android.R.id.content);
		 mDialogView.startAnimation(mModalInAnim);
		dialog.show();
		
	}
	
}
