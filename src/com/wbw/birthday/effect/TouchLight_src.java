package com.wbw.birthday.effect;

import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class TouchLight_src implements OnTouchListener{
	
	private static TouchLight_src touch = null;
	public static TouchLight_src init(){
		if(touch == null)
			touch = new TouchLight_src();
		return touch;
	}

	public final float[] BT_SELECTED = new float[] {
			1, 0, 0, 0, 50, 0, 1, 0, 0, 50, 0, 0, 1, 0, 50, 0, 0, 0, 1, 0
	};
	public final float[] BT_NOT_SELECTED = new float[] {
			1, 0, 0, 0, 0, 
			0, 1, 0, 0, 0,
			0, 0, 1, 0, 0,
			0, 0, 0, 1, 0
	};
//	public final float[] BT_NOT_SELECTED = new float[] {
//			1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0
//	};
	
	Drawable d = null;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// d = v.getBackground();
			//v.getd
			ImageView vv = (ImageView) v;
			vv.getDrawable().setColorFilter(new ColorMatrixColorFilter(BT_SELECTED));
			vv.setImageDrawable(vv.getDrawable());
		
		} 
		else if (event.getAction() == MotionEvent.ACTION_UP) {
			ImageView vv = (ImageView) v;
			vv.getDrawable().clearColorFilter();
			//vv.getDrawable().setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED));
			vv.setImageDrawable(vv.getDrawable());
			
		}
		else if(event.getAction() == MotionEvent.ACTION_MOVE){
			//v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED));
			//v.setBackgroundDrawable(v.getBackground());
			ImageView vv = (ImageView) v;
			vv.getDrawable().clearColorFilter();
			//vv.getDrawable().setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED));
			vv.setImageDrawable(vv.getDrawable());
			System.out.println("move");
		}
		return false;
	}
	
	public void setBa(View v){
		v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED));
		v.setBackgroundDrawable(v.getBackground());
	}

}
