package com.wbw.birthday.data;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;







import com.wbw.birthday.info.BirthdayInfo;
import com.wbw.birthday.util.Util;

public class BirthdayInfoXml {
	private static BirthdayInfoXml birthinfoXml;
	public static BirthdayInfoXml instance(){
		if(birthinfoXml == null)
			birthinfoXml = new BirthdayInfoXml();
		return birthinfoXml;
	}

	public void getBirthdayInfoXml(String path) throws XmlPullParserException, IOException{
		BirthdayInfo info = null;
		InputStream in = Util.init().getInputStream(path);
		XmlPullParser parser = Xml.newPullParser();
		List<BirthdayInfo> binfo_list = BirthdayInfo.binfo_list;
		binfo_list.clear();
		parser.setInput(in, "UTF-8");		
		int event = parser.getEventType();		
		while(event!=XmlPullParser.END_DOCUMENT){
			switch(event){
				case XmlPullParser.START_DOCUMENT:
					
					break;
				case XmlPullParser.START_TAG:
					String name = parser.getName();
					if("birthdayinfo".equals(name)){
						
						info = new BirthdayInfo();
						info.setId(parser.getAttributeValue(null, "id"));
						
					}if(info != null){

						if("name".equals(name))
							info.setName(parser.nextText());
						else if("remark".equals(name))
							info.setRemark(parser.nextText());
						else if("kind".equals(name))
							info.setKind(Integer.valueOf(parser.nextText()));
						else if("year".equals(name))
							info.setYear(Integer.valueOf(parser.nextText()));
						else if("month".equals(name))
							info.setMonth(Integer.valueOf(parser.nextText()));
						else if("day".equals(name))
							info.setDay(Integer.valueOf(parser.nextText()));
						else if("timeofday".equals(name))
							info.setTimeofday(parser.nextText());
						else if("alarmkind".equals(name))
							info.setAlarmkind(Integer.valueOf(parser.nextText()));
						
					}
					break;
				case XmlPullParser.END_TAG:
					String end_name = parser.getName();
					//System.out.println("end_name:"+end_name);
					if("birthdayinfo".equals(end_name)){
						binfo_list.add(info);
						info = null;
					}
					break;
			}
			event = parser.next();
		}
		in.close();	
	}
	
	public void saveBirthdayInfoXml(List<BirthdayInfo> infolist,String path) throws IllegalArgumentException, IllegalStateException, IOException{
		XmlSerializer seria = Xml.newSerializer();
		BufferedWriter out = Util.init().getWriter(path);
		seria.setOutput(out);
		seria.startDocument("UTF-8", true);
		seria.startTag(null, "birthdayinfos");
		
		Iterator<BirthdayInfo> buttoninfo = infolist.iterator();
		
		while(buttoninfo.hasNext()){
			BirthdayInfo info_n = buttoninfo.next();
			
			seria.startTag(null, "birthdayinfo");
			
			seria.attribute(null, "id", info_n.getId());
			
			
			seria.startTag(null, "name");
			seria.text(info_n.getName());	
			seria.endTag(null, "name");
			
			seria.startTag(null, "remark");
			seria.text(info_n.getRemark());	
			seria.endTag(null, "remark");
			
			seria.startTag(null, "kind");
			seria.text(String.valueOf(info_n.getKind()));	
			seria.endTag(null, "kind");
			
			seria.startTag(null, "year");
			seria.text(String.valueOf(info_n.getYear()));	
			seria.endTag(null, "year");
			
			seria.startTag(null, "month");
			seria.text(String.valueOf(info_n.getMonth()));	
			seria.endTag(null, "month");
			
			seria.startTag(null, "day");
			seria.text(String.valueOf(info_n.getDay()));	
			seria.endTag(null, "day");
			
			seria.startTag(null, "timeofday");
			seria.text(info_n.getTimeofday());	
			seria.endTag(null, "timeofday");
			
			seria.startTag(null, "alarmkind");
			seria.text(String.valueOf(info_n.getAlarmkind()));	
			seria.endTag(null, "alarmkind");
				
				
				
			seria.endTag(null, "birthdayinfo");		
		}
		 seria.endTag(null, "birthdayinfos"); //标签都是成对的
		 seria.endDocument();
		 
		 out.flush();
		 out.close(); //关闭输出流
	}
}
