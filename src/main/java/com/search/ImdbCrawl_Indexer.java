package com.search;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;

import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class ImdbCrawl_Indexer {
	
	

   @Override
   
   public void finalize() throws IOException
   {
	   
   }
		
	public Document _Indexer(ArrayList<String> crawlData) throws IOException
	{
		      
			   Document doc = new Document();
			   
			   doc.add(new Field("Movie_Name", crawlData.get(0).toLowerCase().trim(), TextField.TYPE_STORED));
			   doc.add(new Field("release_date", crawlData.get(1).toLowerCase().trim(), TextField.TYPE_STORED));
			   doc.add(new Field("Rating", crawlData.get(2).toLowerCase().trim(), TextField.TYPE_STORED));
			   doc.add(new Field("Genre_1", crawlData.get(3).toLowerCase().trim(), TextField.TYPE_STORED));
			   System.out.println(crawlData.get(3).toLowerCase().trim());
			   System.out.println(crawlData.get(4).toLowerCase().trim());
			   doc.add(new Field("Genre_2", crawlData.get(4).toLowerCase().trim(), TextField.TYPE_STORED));
			   doc.add(new Field("Director", crawlData.get(5).toLowerCase().trim(), TextField.TYPE_STORED));
			   doc.add(new Field("Writer", crawlData.get(6).toLowerCase().trim(), TextField.TYPE_STORED));
			   doc.add(new Field("Cast1", crawlData.get(7).toLowerCase().trim(), TextField.TYPE_STORED));
			   doc.add(new Field("Cast2", crawlData.get(8).toLowerCase().trim(), TextField.TYPE_STORED));
			   
		   	
			   System.out.println("Success");
			   
		return doc;
	}
	
	public void _writeIndex(String systemPath,List<Document> Hyperdoc) throws IOException
	{
		IndexWriter writer;
		
		 File inDir = new File(systemPath + "/index/");
		    
		    Directory dir = FSDirectory.open(inDir);
		  
		   
		   Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_9);
		   IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_4_9, analyzer);
		   writer = new IndexWriter(dir,conf);
		   
		   try{
			   writer.deleteAll();
			   writer.commit();
			   System.out.println("Deleted existing index");
	   }catch(IOException e){
		   e.printStackTrace();
	   }
		   System.out.println(Hyperdoc.size());
		for ( Document doc : Hyperdoc)
		{
			System.out.println(doc);
			writer.addDocument(doc);
		}
		writer.close();
		System.out.println("Sucess");

	}
}
	