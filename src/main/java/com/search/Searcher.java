package com.search;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.*;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;

import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;


import java.nio.file.*;

import com.google.gson.Gson;
import com.search.*;


public class Searcher {
	
	
	 public List<searchResponse> qsearch(String searchString, String indexDir ) throws IOException,ParseException {
		 	
		 	
		 	String []m12 = searchString.split(":");
		 	
		 	System.out.println(searchString);
		 	List<searchResponse> responseList = new ArrayList<searchResponse>(); 
		    StandardAnalyzer sa = new StandardAnalyzer((Version.LUCENE_4_9));
		    
		    File inDir = new File(indexDir);
		    
		    Directory dr = FSDirectory.open(inDir);
		    
		    IndexReader indexReader = DirectoryReader.open(dr);
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);
			
			QueryParser queryParser = new QueryParser(Version.LUCENE_4_9,"Movie_Name", sa);
			
			String mSearch = "Movie_Name:" + m12[0] + " OR Genre_1:" + m12[1]+ " OR Genre_2:" + m12[1];
			

			
			TopDocs results;
		
				results = indexSearcher.search(queryParser.parse(mSearch), 10);
			
			ScoreDoc[] hits = results.scoreDocs;
		 
		 long end = System.currentTimeMillis();
		 int totalhitCount = hits.length;
		 System.out.println("Total number of Documents retrieved are : " + totalhitCount);
			System.out.println();
		 Gson gson = new Gson();
		 int  i = 0;

		 
		 for(ScoreDoc scoreD : results.scoreDocs) {
			 ArrayList<String> topContent = new ArrayList<String>();
			Document doc = indexSearcher.doc(scoreD.doc);
			String jsonInString = gson.toJson(doc);
			
			
			String Movie_Name = doc.get("Movie_Name");
			String release_date = doc.get("release_date");
			String Rating = doc.get("Rating");
			String Genre_1 = doc.get("Genre_1");
			String Genre_2 = doc.get("Genre_2");
			String Director = doc.get("Director");
			String Writer = doc.get("Writer");
			String Cast1 = doc.get("Cast1");
			String Cast2 = doc.get("Cast2");
			String Crituque_Review = doc.get("Crituque_Review");

			System.out.println(Movie_Name);
			
			responseList.add(new 
					searchResponse(Movie_Name,
							release_date,
							Rating,
							Genre_1,
							Genre_2,
							Director,
							Writer,
							Cast1,
							Cast2,
							"html/"+Movie_Name.replaceAll("\\s","")+".html",
							"img/thumbnails/"+Movie_Name.replaceAll("\\s","")+".jpg"));
			i = i+1;
		 }
		 return responseList;
	 }
}