package com.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.select.Elements;
import com.search.*;

public class Imdb_Crawl {


	public static void createHtmlFile(String review, String i) throws IOException
	{
		   String systemPath = System.getProperty("user.dir");

		   File metaContentFile = new File(systemPath +"/data/metadata/html.txt");
		   
		   BufferedReader br = null;
		   String line1 = "";
		   
		   String htmlData1 = "";
		   
		   
		   try {
			br = new BufferedReader(new FileReader(metaContentFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   while((line1 = br.readLine()) != null){
			   htmlData1 = line1 + review + "</h:body>" + "</html>"; 
		   }
		   PrintWriter out = new PrintWriter(systemPath+ "/src/main/webapp/html/"+ i.replaceAll("\\s","").toLowerCase() + ".html");
		   out.println(htmlData1);
		   out.close();
	}
	public static String getCrtiqueReview(String rURL) throws IOException
	{
		
		URL eURL  = new URL(rURL);
		Document edoc = Jsoup.parse(eURL,100000);
		
		String externalReview = "";
		Elements rlinks= edoc.select("a"); 
		
		for (Element rlink : rlinks)
		{
			String rText = rlink.text();
			int rlength = rText.length();
			if (rlength >= 14)
			{
				String rsubString = rText.substring(0, 14);  
				if ( rsubString.equals("New York Times"))
				{
				    String erURL = rlink.absUrl("href");
					Document erdoc = Jsoup.parse(new URL(erURL),100000);
					Elements ers = erdoc.getElementsByTag("p");
					for (Element er : ers)
						externalReview = externalReview +  er.text() +"\n";
				}
			}
			
			
		}
		
		//System.out.println(externalReview);
		return externalReview;
	}
	
	
	public static ArrayList<String> parseURL(String systemPath, String movieURL, String i) throws IOException
		{
		   ArrayList<String> htmlData = new ArrayList<String>();
			System.out.println(movieURL);
			URL mURL = new URL(movieURL);
			Document mdoc = Jsoup.parse(mURL,100000);//parse(mURL.openStream(),"ISO-8859-2",movieURL);/
	
			Element movie_Genre_rDate = mdoc.getElementsByClass("title_wrapper").first();
				
				String originalTitle = (mdoc.getElementsByClass("originalTitle").text().split("\\("))[0];
				String[] movieYear =  (movie_Genre_rDate.select("h1").first().text()).split(" ");
				String rating = (mdoc.select("span[itemprop=ratingValue]")).text();
				
				System.out.println(mdoc);
				
				String tf = originalTitle.length() !=0 ?  originalTitle : movieYear[0] + " ";
				htmlData.add(tf.replaceAll("[^\\w\\s]", " "));
				htmlData.add(movieYear[1]+ " ");
				htmlData.add(rating+ " ");
				System.out.println(tf.replaceAll("[^\\w\\s]", " "));
				//System.out.println(movieYear[0]+movieYear[1]);
				//System.out.println(movie_Genre_rDate.select("h1").first().text());
				Elements genreElm = movie_Genre_rDate.select("span[itemprop=genre]");
				int g = 0;
				for (Element genre : genreElm)
				{	
					if (g ==2)break;
					htmlData.add(genre.text());
					g = g+1;
				}
				if (g == 1)
				{
					htmlData.add(" ");
				}
				Elements dir_wri_stars = mdoc.getElementsByClass("credit_summary_item");	
				
				
				
				String director =	dir_wri_stars.select("span[itemprop=director]").text();
				String writer =	dir_wri_stars.select("span[itemprop=creator]").text();
				
				htmlData.add(director + " ");
				htmlData.add(writer + " ");
				
				Elements actors = dir_wri_stars.select("span[itemprop=actors]");
				
				g = 0;
				for (Element actor : actors)
				{
					if (g == 2)break;
					htmlData.add(actor.select("span[itemprop=name]").text());
					g = g+1;
				}
				String it = originalTitle.length() !=0 ?  originalTitle : movieYear[0] + " ";
				String externalReview = getCrtiqueReview(movieURL+"externalreviews")  ;
				
				
				createHtmlFile(externalReview.substring(0, 500), htmlData.get(0));
				
				if (!externalReview.isEmpty())
					htmlData.add(externalReview.substring(0, 500));
				else
					htmlData.add(externalReview.substring(0, 5));
				return htmlData;
		
		}
	
	
	
	public static void main(String[] args) throws IOException 
	{	
		   String systemPath = System.getProperty("user.dir");

		   File metaContentFile = new File(systemPath +"/data/metadata/IRMovieList.csv");
		   
		   BufferedReader br = null;
		   String line = "";
		   
		   String htmlURL = "";
		   
		   
		   try {
			br = new BufferedReader(new FileReader(metaContentFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
		   // Reading line by line, one Entry for each HTML page
		   
		   ImdbCrawl_Indexer ici = new ImdbCrawl_Indexer();
		   
		   int htmlfileCOunt = 0;
		   
		   String title_G1_G2 = "";
		   List<org.apache.lucene.document.Document> Hyperdoc = new ArrayList<org.apache.lucene.document.Document>();   

		   while((line = br.readLine()) != null){
			   
			   String[] metaContent = line.split(",");
			   
			   htmlURL = metaContent[1];
			   
			   ArrayList<String> htmlData = new ArrayList<String>();
			   
			   htmlData = parseURL(systemPath,htmlURL, metaContent[0].toLowerCase());
				   
			   htmlfileCOunt = htmlfileCOunt +1; 
			   
			   org.apache.lucene.document.Document doc = ici._Indexer(htmlData);
			   Hyperdoc.add(doc);
			   title_G1_G2 = title_G1_G2 + (htmlData.get(0)).toLowerCase() + "<>" + htmlData.get(3).toLowerCase()+"<>"+htmlData.get(4).toLowerCase() + "\n";
			   
			 }
		   ici._writeIndex(systemPath,Hyperdoc);
		   PrintWriter out = new PrintWriter(systemPath+ "/data/title_genre.csv");
		   out.println(title_G1_G2);
		   out.close();
	}
	
}
