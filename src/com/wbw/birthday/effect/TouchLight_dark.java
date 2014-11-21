package com.wbw.birthday.effect;


import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

/**
 * …Ó—’…´
 * @author Administrator
 *
 */
public class TouchLight_dark implements OnTouchListener{
	
	private static TouchLight_dark touch = null;
	public static TouchLight_dark init(){
		if(touch == null)
			touch = new TouchLight_dark();
		return touch;
	}

	public final float[] BT_SELECTED = new float[] {
			100, 0, 100, 100, 50,
			0, 100, 100, 100, 50,
			0, 0, 100, 100, 50, 
			0, 0, 0, 1, 0
	};
	public final float[] BT_NOT_SELECTED = new float[] {
			1, 0, 0, 0, 0, 
			0, 1, 0, 0, 0,
			0, 0, 1, 0, 0,
			0, 0, 0, 1, 0
	};

	Drawable d = null;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// d = v.getBackground();
			//v.getd
			if(v.getBackground() != null){
				v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_SELECTED));
				v.setBackgroundDrawable(v.getBackground());
			}else{
				ImageView vv = (ImageView) v;
				vv.getDrawable().setColorFilter(new ColorMatrixColorFilter(BT_SELECTED));
				vv.setImageDrawable(vv.getDrawable());
			}			
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			if(v.getBackground() != null){
				v.getBackground().clearColorFilter();			
				v.setBackgroundDrawable(v.getBackground());				
			}else{
				ImageView vv = (ImageView) v;
				vv.getDrawable().clearColorFilter();
				//vv.getDrawable().setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED));
				vv.setImageDrawable(vv.getDrawable());
			}			
		}
		return false;
	}
	
	public void setBa(View v){
		v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED));
		v.setBackgroundDrawable(v.getBackground());
	}

}
