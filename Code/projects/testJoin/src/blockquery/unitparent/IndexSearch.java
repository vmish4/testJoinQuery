package blockquery.unitparent;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.CachingWrapperFilter;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.join.FixedBitSetCachingWrapperFilter;
import org.apache.lucene.search.join.JoinUtil;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.lucene.search.join.ToChildBlockJoinQuery;
import org.apache.lucene.search.join.ToParentBlockJoinQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;


public class IndexSearch {
	
    public static String buildDate(Date date) {
        return DateTools.dateToString(date, Resolution.SECOND);
    }
	
	static void blockSearchToParentJoin() throws IOException, ParseException, java.text.ParseException
	{
		final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		//String cd ="D:/TUI/sandbox/Development/Code/hybris/data/lucene/UpdatedCode/unitsParent/SE";
		//String cd = "D:/opt/hybris/data/index/lucene/";
		String cd ="D:/opt/hybris/data/index/lucene/unitsParent/SE";
		final Directory articleDirectory = FSDirectory.open(new File(cd));
		
		final IndexReader articleIndexReader=IndexReader.open(articleDirectory);
		
		
		final IndexSearcher articleSearcher = new IndexSearcher(articleIndexReader);
		
		 QueryParser  parser = new QueryParser(Version.LUCENE_CURRENT, "group", new StandardAnalyzer(Version.LUCENE_CURRENT)); 
		 Query parentQuery = parser.parse("hotel or destination or concept");

		  Filter parentsFilter = new FixedBitSetCachingWrapperFilter(new QueryWrapperFilter(parentQuery)); 
		  		  	  

		  long start = System.currentTimeMillis();
		  BooleanQuery mainQuery = new BooleanQuery(); 
		  mainQuery.add(new TermQuery(new Term("dep_code", "VXO")), Occur.MUST);
		  ToParentBlockJoinQuery productItemQuery = new ToParentBlockJoinQuery(mainQuery, parentsFilter, ScoreMode.None);
		  TopDocs hits_airToDes = articleSearcher.search(productItemQuery, Integer.MAX_VALUE);
		  for(int i=0;i<hits_airToDes.scoreDocs.length;i++)
			{
				Document doc = articleSearcher.doc(hits_airToDes.scoreDocs[i].doc);
			}
		  System.out.println(" hits_airToDes "+ hits_airToDes.totalHits+" INPUT AIRPORT GET PARENTS "+(System.currentTimeMillis()-start));
		  
		  
		   start = System.currentTimeMillis();	  
		  ToParentBlockJoinQuery productItemQuery1 = new ToParentBlockJoinQuery(new TermQuery(new Term("dep_date", "20150406183000")), parentsFilter, ScoreMode.None);
		  TopDocs hits_dateToDes1 = articleSearcher.search(productItemQuery1, Integer.MAX_VALUE);
		  for(int i=0;i<hits_dateToDes1.scoreDocs.length;i++)
			{
				Document doc = articleSearcher.doc(hits_dateToDes1.scoreDocs[i].doc);
			}
		  System.out.println(" hits_dateToDes1  "+ hits_dateToDes1.totalHits+" INPUT AIRPORT GET PARENTS "+(System.currentTimeMillis()-start));

		  
		  
		  
		  start = System.currentTimeMillis();	  
		  QueryParser  parser1 = new QueryParser(Version.LUCENE_45, "group", new WhitespaceAnalyzer(Version.LUCENE_45));
		  Query query1 = parser1.parse("dep_date:20150406183000");
		  final TopDocs hits_date_single = articleSearcher.search(query1, Integer.MAX_VALUE);
		  	  for(int i=0;i<hits_date_single.scoreDocs.length;i++)
		  		{
		  			Document doc = articleSearcher.doc(hits_date_single.scoreDocs[i].doc);
		  		}
		  System.out.println("hits_date_single "+ hits_date_single.totalHits+" JOIN  Time taken in getting all dates or airports "+(System.currentTimeMillis()-start));
		  
		  
		  
		  start = System.currentTimeMillis();
		   mainQuery = new BooleanQuery(); 
		   mainQuery.add(new TermQuery(new Term("dep_code", "VXO")),Occur.MUST);
		   mainQuery.add(new TermQuery(new Term("dep_date", "20150406183000")),Occur.MUST);
		    productItemQuery = new ToParentBlockJoinQuery(mainQuery, parentsFilter, ScoreMode.None); 
		  TopDocs hits_airport_date_To_des = articleSearcher.search(productItemQuery, Integer.MAX_VALUE);
		  for(int i=0;i<hits_airport_date_To_des.scoreDocs.length;i++)
			{
				Document doc = articleSearcher.doc(hits_airport_date_To_des.scoreDocs[i].doc);
			}
		  System.out.println(" hits_airport_date_To_des "+ hits_airport_date_To_des.totalHits+" Destination name : Time taken in getting all dates or airports "+(System.currentTimeMillis()-start));
		  	

		  
	}
	
	
	static void blockSearchToChildJoin() throws IOException, ParseException, java.text.ParseException
	{

		String cd ="D:/opt/hybris/data/index/lucene/unitsParent/SE"; 
		final Directory articleDirectory = FSDirectory.open(new File(cd));
		
		final IndexReader articleIndexReader=IndexReader.open(articleDirectory);
		final IndexSearcher articleSearcher = new IndexSearcher(articleIndexReader);
		 QueryParser  parser = new QueryParser(Version.LUCENE_CURRENT, "group", new StandardAnalyzer(Version.LUCENE_CURRENT)); 
	        Query parentQuery = parser.parse("hotel or destination or concept");

			  Filter parentsFilter = new FixedBitSetCachingWrapperFilter(new QueryWrapperFilter(parentQuery)); 
				final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");		  		  	  
	
		

		  
		  
		  Query query; 
		  long start = System.currentTimeMillis();
		  Query parentBlockJoinQuery =  new TermQuery(new Term("code", "G-000073"));
		  ToChildBlockJoinQuery productItemQuery = new ToChildBlockJoinQuery(parentBlockJoinQuery, parentsFilter,false); 
		  TopDocs hits_destToAir = articleSearcher.search(productItemQuery, Integer.MAX_VALUE);
		  for(int i=0;i<hits_destToAir.scoreDocs.length;i++)
			{
				Document doc = articleSearcher.doc(hits_destToAir.scoreDocs[i].doc);
			}
		  System.out.println(" hits_destToAir "+ hits_destToAir.totalHits+" JOIN  Time taken in getting all dates or airports "+(System.currentTimeMillis()-start));
		  
		  
		  
		  QueryParser  parser1 = new QueryParser(Version.LUCENE_45, "group", new WhitespaceAnalyzer(Version.LUCENE_45)); 
		  start = System.currentTimeMillis();
		  query = parser1.parse("type:P"); 
		  TopDocs hits_all_airport =  articleSearcher.search(query, Integer.MAX_VALUE);
		  for(int i=0;i<hits_all_airport.scoreDocs.length;i++)
			{
				Document doc = articleSearcher.doc(hits_all_airport.scoreDocs[i].doc);
			}
		  System.out.println(" hits_all_airport "+ hits_all_airport.totalHits+" ALL ROUTES (DATE AIRPORT) : Time taken in getting all dates or airports "+(System.currentTimeMillis()-start));
		  
		  
		  start = System.currentTimeMillis();
		  query =parser1.parse("code:G-000073"); 
		  TopDocs hits_single_destination = articleSearcher.search(query, Integer.MAX_VALUE);
		  for(int i=0;i<hits_single_destination.scoreDocs.length;i++)
			{
				Document doc = articleSearcher.doc(hits_single_destination.scoreDocs[i].doc);
			}
		  System.out.println(" hits_single_destination " + hits_single_destination.totalHits+" Destination name : Time taken in getting all dates or airports "+(System.currentTimeMillis()-start));
		  
		  
		  
		  start = System.currentTimeMillis();
		  parentQuery = parser.parse("hotel or destination or concept");
		  TopDocs hits_all_destination = articleSearcher.search(parentQuery, Integer.MAX_VALUE);
		  for(int i=0;i<hits_all_destination.scoreDocs.length;i++)
			{
				Document doc = articleSearcher.doc(hits_all_destination.scoreDocs[i].doc);
			}
		  System.out.println(parentQuery + " hits_all_destination "+ hits_all_destination.totalHits+" Destination name : Time taken in getting all dates or airports "+(System.currentTimeMillis()-start));
		  
		  
		  
		  QueryParser  parser2 = new QueryParser(Version.LUCENE_45, "group", new WhitespaceAnalyzer(Version.LUCENE_45));
		  start = System.currentTimeMillis();
		  TopDocs hits_airToDate = articleSearcher.search(new TermQuery(new Term("dep_code", "VXO")), Integer.MAX_VALUE);
		  for(int i=0;i<hits_airToDate.scoreDocs.length;i++)
			{
				Document doc = articleSearcher.doc(hits_airToDate.scoreDocs[i].doc);
			}
		  System.out.println(" hits_airToDate "+ hits_airToDate.totalHits+" Destination name : Time taken in getting all dates or airports "+(System.currentTimeMillis()-start));
		  
		  
		  
		  start = System.currentTimeMillis();
		  BooleanQuery mainQuery = new BooleanQuery(); 
		  parentBlockJoinQuery =  new TermQuery(new Term("code", "G-000073"));
          productItemQuery = new ToChildBlockJoinQuery(parentBlockJoinQuery, parentsFilter,false); 
          mainQuery.add(productItemQuery,Occur.MUST);
          mainQuery.add(new TermQuery(new Term("dep_code", "VXO")),Occur.MUST);
		  TopDocs hits_dest_airport_To_Date = articleSearcher.search(mainQuery, Integer.MAX_VALUE);
		  for(int i=0;i<hits_dest_airport_To_Date.scoreDocs.length;i++)
			{
				Document doc = articleSearcher.doc(hits_dest_airport_To_Date.scoreDocs[i].doc);
			}
		  System.out.println(" hits_dest_airport_To_Date "+ hits_dest_airport_To_Date.totalHits+" Destination name : Time taken in getting all dates or airports "+(System.currentTimeMillis()-start));
		  	  
		  
		  start = System.currentTimeMillis();
		   mainQuery = new BooleanQuery(); 
		   parentBlockJoinQuery =  new TermQuery(new Term("code", "G-000073"));
         productItemQuery = new ToChildBlockJoinQuery(parentBlockJoinQuery, parentsFilter,false); 
         mainQuery.add(productItemQuery,Occur.MUST);
         mainQuery.add(new TermQuery(new Term("dep_date", "20150406183000")),Occur.MUST);
		  TopDocs hits_dest_date_To_Airport = articleSearcher.search(mainQuery, Integer.MAX_VALUE);
		  for(int i=0;i<hits_dest_date_To_Airport.scoreDocs.length;i++)
			{
				Document doc = articleSearcher.doc(hits_dest_date_To_Airport.scoreDocs[i].doc);
			}
		  System.out.println(" hits_dest_date_To_Airport "+ hits_dest_date_To_Airport.totalHits+" Destination name : Time taken in getting all dates or airports "+(System.currentTimeMillis()-start));
		  	  
	
	}
	
	static void search() throws IOException
	{
		String cd ="D:/TUI/PhoenixCodeBase/hybris/data/articleExample";
		final Directory articleDirectory = FSDirectory.open(new File(cd+"/article.idx"));
		final Directory commentsDirectory = FSDirectory.open(new File(cd+"/comments.idx"));
		
		final IndexReader articleIndexReader=IndexReader.open(articleDirectory);
		final IndexReader commentsIndexReader=IndexReader.open(commentsDirectory) ;
		
		
		final IndexSearcher articleSearcher = new IndexSearcher(articleIndexReader);
		final IndexSearcher commentsSearcher = new IndexSearcher(commentsIndexReader);
		
		
		
		final String fromField = "articleid";
		final boolean multipleValuesPerDocument = true;
		final String toField = "id";
		
		 final TermQuery termDate =new TermQuery(new Term("title","byted"));
		 final TermQuery termComments =new TermQuery(new Term("content","second"));
		 final TermQuery termArticleId =new TermQuery(new Term("articleid","2"));
		 final TermQuery termId =new TermQuery(new Term("id","2"));
		// Query t  = new 
		 
		 PhraseQuery joinQuery12 = new PhraseQuery();
		joinQuery12.add(new Term("locationcode", "id1_MBA"));
		
		TopDocs hit1 = commentsSearcher.search(termId, 10);
		
		Document doc1 = commentsSearcher.doc(hit1.scoreDocs[0].doc); 
		
		
		Query joinQuery; 

			joinQuery = JoinUtil.createJoinQuery(fromField, multipleValuesPerDocument, toField, termId, commentsSearcher,
					ScoreMode.None);
			
			
			TopDocs hits = articleSearcher.search(joinQuery, 10);
			
			for(int i=0;i<hits.scoreDocs.length;i++)
			{
				Document doc = articleSearcher.doc(hits.scoreDocs[i].doc);
				//System.out.println(i + "comments "+doc.get("id")+" articleid "+doc.get("articleid")+ " content "+ doc.get("content")) ;
				System.out.println(i + "Article "+doc.get("id")+" title "+doc.get("title")+ " content "+ doc.get("content")) ;

			}
			
			
		
	}

	public static void main(String[] args) {
	
try {
	try {
		try {
			blockSearchToChildJoin();
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} catch (ParseException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	try {
		blockSearchToParentJoin();
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (java.text.ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
	}

}
