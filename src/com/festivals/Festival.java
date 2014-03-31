package com.festivals;


public class Festival {
	public String name;
	public String img;
	public String date;
	public String url;
	public String content;
	public String place;
	public String start_date;
	public String end_date;
	public String lat = "null";
	public String lng = "null";
	public Festival(String name, String img, String date, String url,
			String content,String place,String start_date,String end_date) {
		this.name = name;
		this.img = img;
		this.date = date;
		this.url = url;
		this.content = content;
		this.place = place;
		this.start_date = start_date;
		this.end_date = end_date;
	}
}
