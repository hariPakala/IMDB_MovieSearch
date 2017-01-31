package com.ir.sample3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.queryparser.classic.ParseException;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

import com.search.*;
import com.search.searchResponse;

import com.sun.faces.util.*;

@ManagedBean(name = "searchMovieBean", eager = true)
@RequestScoped

public class searchMovie {
			
		private List<searchResponse> queryresponse = new ArrayList<searchResponse>();
	    private String searchMovie ;
	    
	    public String getsearchMovie() {
	        return searchMovie;
	    }

	    public void setsearchMovie(String searchMovie) {
	        this.searchMovie = searchMovie;
	    }
	    
	    public void executeQuery() throws IOException, ParseException{
	    	
			 String path = System.getProperty("user.dir");

		     String systemPath = path + "/index/";
		     System.out.println(path); 
		    String genere = getGenere(path, searchMovie); 
			Searcher s = new Searcher();
			System.out.println(genere + "hh");

			//
			queryresponse = s.qsearch(searchMovie+":"+genere, systemPath);
			System.out.println(queryresponse.get(0).moviename);
	    }
	    
	    public String gettestSearch() {
	        return searchMovie;
	    }
	    
	  public List<searchResponse> getqueryresponse(){
	    	return queryresponse;
	    } 
	 
	  public String getGenere (String systemPath, String moviename) throws IOException
	  {
		   File metaContentFile = new File(systemPath +"/data/title_genre.csv");
		   
		   BufferedReader br = null;
		   
		   try {
			br = new BufferedReader(new FileReader(metaContentFile));
		   		} catch (FileNotFoundException e) {
		   			// TODO Auto-generated catch block
		   			e.printStackTrace();
		   		}
		    
		    String line = "";
			String genere = "";
		    while((line = br.readLine()) != null){
			   
			   String[] metaContent = line.split("<>");
			   
			   if ((metaContent[0].trim()).contains(searchMovie.toLowerCase()))
					{
						genere = metaContent[1] + ":" + metaContent[2];
						System.out.println(metaContent[0]+metaContent[1]+metaContent[2]);
						break;
					}
			   
			 }
		

		  return genere;
	  }
	  
}
