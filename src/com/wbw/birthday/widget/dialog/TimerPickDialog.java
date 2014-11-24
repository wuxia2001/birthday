package com.wbw.birthday.widget.dialog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;



import com.wbw.birthday.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TimerPickDialog {
	private static TimerPickDialog dialog;
	public static TimerPickDialog init(){
		if(dialog == null)
			dialog = new TimerPickDialog();
		return dialog;
	}
	
	private Context mContext = null;  
	public  Dialog creatTimePickDialog(Context context,int year,int month,int day,
			View.OnClickListener clickListener){
		mContext = context;
		final Dialog dialog = new Dialog(mContext, R.style.MyDialog);
		return dialog;
	}
	
	
}
