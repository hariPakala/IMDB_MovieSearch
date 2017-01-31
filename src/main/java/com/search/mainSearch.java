package com.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.queryparser.classic.ParseException;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;
import com.search.*;


public class mainSearch {

	public static void search(String[] args) throws IOException, ParseException {
		
		List<searchResponse> response = new ArrayList<searchResponse>();
        ArrayList<String> stemmedQueryList = new ArrayList<String>();
		 System.out.print("Please enter the query: ");
	        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	        String inputQuery = br.readLine();
	      
	     String path = mainSearch.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	     String decodedPath = (URLDecoder.decode(path,"UTF-8").substring(1));
	     String systemPath =  "/"+decodedPath.substring(0, decodedPath.length() - 6);
	     System.out.println(systemPath);
		 
		 
		String[] queryParts = inputQuery.split(" ");
        
		SnowballStemmer snowballStemmer = new englishStemmer();

		for (String pWord : queryParts)
		{
			snowballStemmer.setCurrent(pWord.toLowerCase());
			snowballStemmer.stem();
			String stemmedWord = snowballStemmer.getCurrent();
			stemmedQueryList.add(stemmedWord.toUpperCase());
		}
        String stemmedQuery = StringUtils.join(stemmedQueryList, " ");
        System.out.print("The query you have entered is: " + stemmedQuery);
		Searcher s = new Searcher();
		response = s.qsearch(stemmedQuery,systemPath + "/index/");

	}

}
