package com.wbw.birthday.widget;

import java.util.List;

import com.wbw.birthday.R;
import com.wbw.birthday.info.BirthdayInfo;
import com.wbw.birthday.util.Comments;

import android.content.Context;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListBirthdayListAdapter extends BaseAdapter{
	private LayoutInflater mInflater;
	private Context mContext;
	private List<BirthdayInfo> list;
	public ListBirthdayListAdapter(Context context,List<BirthdayInfo> list){
		this.mContext=context;
		this.list = list;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		if(list!= null)
			return list.size();
		else return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		
		final ViewHolder holder;		
		if(convertView==null){
			convertView=mInflater.inflate(R.layout.listbirthday_item, null);
			holder=new ViewHolder();
			
			holder.list_item_text1 =(TextView) convertView.findViewById(R.id.list_item_text1);
			holder.list_item_text2 =(TextView) convertView.findViewById(R.id.list_item_text2);
			holder.list_item_text3 =(TextView) convertView.findViewById(R.id.list_item_text3);
					
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		BirthdayInfo info = list.get(position);
		String name = info.getName();
		String remark = info.getRemark();
		SpannableString sp = new SpannableString(name+" "+remark+"");
	
		sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new RelativeSizeSpan(1.2f) , 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new TypefaceSpan("monospace"), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		//sp.setSpan(new BackgroundColorSpan(Color.RED), 0, d.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		if(remark != null && remark != "" && !remark.equals(" ") && !remark.trim().equals("")){
			//农历显示的样式
            sp.setSpan(new RelativeSizeSpan(0.75f), name.length()+1, name.length()+remark.length()+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //sp.setSpan(new BackgroundColorSpan(Color.RED), d.length()+1, dayNumber[position].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		holder.list_item_text1.setText(sp);
		StringBuilder sb2 = new StringBuilder();
		if(info.getKind() == Comments.nongli){
			sb2.append(mContext.getString(R.string.nongli));
		}else{
			sb2.append(mContext.getString(R.string.gongli));
		}
		
		if(info.getDuplicatekind() == Comments.everyyear){
			sb2.append("每年");
		}else{
			sb2.append(info.getYear()+"年");
		}
		sb2.append(info.getMonth()+"月"+info.getDay()+"日"+info.getTimeofday());
		holder.list_item_text2.setText(sb2.toString());
		
		
		
		
		return convertView;
	}
	
	class ViewHolder{
		
		TextView list_item_text1;
		TextView list_item_text2;
		TextView list_item_text3;
	}


}
