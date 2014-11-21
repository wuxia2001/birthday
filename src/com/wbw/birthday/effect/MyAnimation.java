package com.wbw.birthday.effect;

import com.wbw.birthday.MainActivity;
import com.wbw.birthday.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;

public class MyAnimation {
	// 图标的动�?(入动�?)
	public static void startAnimationsIn(ViewGroup viewgroup, int durationMillis) {

		viewgroup.setVisibility(0);
		for (int i = 0; i < viewgroup.getChildCount(); i++) {
			viewgroup.getChildAt(i).setVisibility(0);
			viewgroup.getChildAt(i).setClickable(true);
			viewgroup.getChildAt(i).setFocusable(true);
		}
		Animation animation;
		animation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
		animation.setFillAfter(true);
		animation.setDuration(durationMillis);
		viewgroup.startAnimation(animation);

	}
	
	// 图标的动�?(入动�?)
		public static void startAnimationsIn(View viewgroup, int durationMillis) {

			//viewgroup.setVisibility(0);
			//for (int i = 0; i < viewgroup.getChildCount(); i++) {
				viewgroup.setVisibility(0);
				viewgroup.setClickable(true);
				viewgroup.setFocusable(true);
			//}
			Animation animation;
			animation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
					0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
			animation.setFillAfter(true);
			animation.setDuration(durationMillis);
			viewgroup.startAnimation(animation);

		}
		
		public static void startAnimationsShow(View viewgroup, int durationMillis,
				Context context,int resourcedid) {

			//viewgroup.setVisibility(0);
			//for (int i = 0; i < viewgroup.getChildCount(); i++) {
			viewgroup.setVisibility(0);
			viewgroup.setClickable(true);
			viewgroup.setFocusable(true);
			//}
			Animation animation =AnimationUtils.loadAnimation(context, resourcedid);				
			animation.setFillAfter(true);
			animation.setDuration(durationMillis);
			viewgroup.startAnimation(animation);

		}
		
		// 图标的动�?(出动�?)
		public static void startAnimationsHide(final View viewgroup, int durationMillis,
				Context context,int resourcedid) {

			Animation animation =AnimationUtils.loadAnimation(context, resourcedid);				
			
			animation.setFillAfter(true);
			animation.setDuration(durationMillis);
			animation.setStartOffset(0);
			animation.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation arg0) {}
				@Override
				public void onAnimationRepeat(Animation arg0) {}
				@Override
				public void onAnimationEnd(Animation arg0) {
					//viewgroup.setVisibility(8);
					//for (int i = 0; i < viewgroup.getChildCount(); i++) {
						viewgroup.setVisibility(8);
						viewgroup.setClickable(false);
						viewgroup.setFocusable(false);
					//}
				}
			});
			viewgroup.startAnimation(animation);
		}
	

	// 图标的动�?(出动�?)
	public static void startAnimationsOut(final ViewGroup viewgroup,
			int durationMillis, int startOffset) {

		Animation animation;
		animation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
		animation.setFillAfter(true);
		animation.setDuration(durationMillis);
		animation.setStartOffset(startOffset);
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {}
			@Override
			public void onAnimationRepeat(Animation arg0) {}
			@Override
			public void onAnimationEnd(Animation arg0) {
				viewgroup.setVisibility(8);
				for (int i = 0; i < viewgroup.getChildCount(); i++) {
					viewgroup.getChildAt(i).setVisibility(8);
					viewgroup.getChildAt(i).setClickable(false);
					viewgroup.getChildAt(i).setFocusable(false);
				}
			}
		});
		viewgroup.startAnimation(animation);
	}

}







