package com.wbw.birthday.effect;

import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * Ç³ÑÕÉ«
 * @author Administrator
 *
 */
public class TouchLight_light implements OnTouchListener{
	
	private static TouchLight_light touch = null;
	public static TouchLight_light init(){
		if(touch == null)
			touch = new TouchLight_light();
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
			v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_SELECTED));
			v.setBackgroundDrawable(v.getBackground());
		
		} 
		else if (event.getAction() == MotionEvent.ACTION_UP) {
			//v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED));
			v.getBackground().clearColorFilter();
			v.setBackgroundDrawable(v.getBackground());
			
		}
		else if(event.getAction() == MotionEvent.ACTION_MOVE){
			v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED));
			v.setBackgroundDrawable(v.getBackground());
			System.out.println("move");
		}
		return false;
	}
	
	public void setBa(View v){
		v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED));
		v.setBackgroundDrawable(v.getBackground());
	}

}
