package com.search;

public class searchResponse {

	public String moviename;
	public String releasedate;
	public String rating;
	public String genre1;
	public String genre2;
	public String director;
	public String writer;
	public String cast1;
	public String cast2;
	public String htmlfile;
	public String imgfile;
	
	searchResponse(String a, String b, String c, String d,
				   String e, String f, String g, String h,
				   String i, String j, String k)
	{
		moviename = a;
		releasedate = b;
		rating = c;
		genre1 = d;
		genre2 = e;
		director = f;
		writer = g;
		cast1 = h;
		cast2 = i;
		htmlfile = j;
		imgfile = k;

	}
	
	public String gethtmlfile()
	{
		return htmlfile;
	}
	
    public String getmoviename() {
        return moviename;
    }
    
    public String getreleasedate() {
        return releasedate;
    }
    
    public String getrating() {
        return rating;
    }
    
    public String getgenre1() {
        return genre1;
    }
    public String getgenre2() {
        return genre2;
    }
    
    public String getdirector() {
        return director;
    }
    
    public String getwriter() {
        return writer;
    }
    
    public String getcast1() {
        return cast1;
    }
    public String getcast2() {
        return cast2;
    }
    public String getimgfile() {
        return imgfile;
    }
}
