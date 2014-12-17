package tui;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class backUp {
	
	public static void fetch() throws IOException, ParseException 
	{
		String cd ="D:/TUI/sandbox/Development/Code/hybris/data/junit";
		//final Directory airportDirectory = FSDirectory.open(new File(cd+"/airport.idx"));
		final Directory routesDirectory = FSDirectory.open(new File(cd+"/routes.idx/dn"));
		final Directory unitDirectory = FSDirectory.open(new File(cd+"/units.idx/dn"));
		//final IndexReader airportIndexReader=IndexReader.open(airportDirectory) ;
		final IndexReader routeIndexReader=IndexReader.open(routesDirectory);
		final IndexReader unitsIndexReader=IndexReader.open(unitDirectory);
		//final IndexSearcher airportSearcher = new IndexSearcher(airportIndexReader);
		final IndexSearcher flightRouteSearcher = new IndexSearcher(routeIndexReader);
		final IndexSearcher unitSearcher = new IndexSearcher(unitsIndexReader);
		
		QueryParser  parser = new QueryParser(Version.LUCENE_CURRENT, "dep_date", new StandardAnalyzer(Version.LUCENE_CURRENT)); 
		
		Query dateRangeQuery = parser.parse("date:[10/1/13 TO 11/2/14]"); 
		Locale loc = parser.getLocale(); 
		DateFormat f = DateFormat.getDateInstance(DateFormat.SHORT, loc);
		String date = f.format(new Date()); 
		//SimpleDateFormat formatter = new SimpleDateFormat(pattern, loc); 
		
		TopDocs topDate = flightRouteSearcher.search(dateRangeQuery, 12); 
		
		//date:[6/1/2005 TO 6/4/2005]
		//System.out.println(" total recods in airport "+airportIndexReader.maxDoc() + " route "+routeIndexReader.maxDoc() + " units "+unitsIndexReader.maxDoc());


	//	final String fromField = "dep_arr_code";
		final String fromField = "route";
		final boolean multipleValuesPerDocument = false;
		final String toField = "route";

		//final String query1 = "locationcode: (id1_MBA or id1_DEL) and departurecode:(LGW or LHR)";
		final String query = "locationcode:id1_MBA";
		Analyzer analyzer = new WhitespaceAnalyzer(Version.LUCENE_47);
		//parser = new QueryParser(Version.LUCENE_47, "locationcode", analyzer);
		//Query q = new TermQuery(new Term("locationcode", "id1_MBA"));
		Query query12 = parser.parse(query);
		System.out.println("unit search "+unitSearcher.search(query12, 20).totalHits);
		// This query should yield article with id 2 as result
		final BooleanQuery fromQuery = new BooleanQuery();
		fromQuery.add(new TermQuery(new Term("locationcode", "id1_MBA")), BooleanClause.Occur.MUST);
		//fromQuery.add(new TermQuery(new Term("locationcode", "id2_MBA")), BooleanClause.Occur.MUST);
		 final TermQuery term =new TermQuery(new Term("locationcode", "id1_MBA"));
		 final TermQuery term1 =new TermQuery(new Term("productBrandType", "FC"));
		 final TermQuery term2 =new TermQuery(new Term("dep_arr_route_code", "LGW_MBA"));
		 
		 final TermQuery termDate =new TermQuery(new Term("departuredate","01-10-2013"));
		// Query t  = new 
		 final TermQuery termDate1 =new TermQuery(new Term("code","id_dummy2"));
		 
		 PhraseQuery joinQuery12 = new PhraseQuery();
		joinQuery12.add(new Term("locationcode", "id1_MBA"));
		
		Query joinQuery; 
		
		try
		{
			joinQuery = JoinUtil.createJoinQuery(fromField, multipleValuesPerDocument, toField, termDate1, unitSearcher,
					ScoreMode.None);
			
			System.out.println("length "+flightRouteSearcher.search(term, 20).totalHits);
	
		     // System.out.println(" collected "+termsCollector.); 
			final TopDocs topDocs = flightRouteSearcher.search(joinQuery, 10);
			final TopDocs topDocsUnits = unitSearcher.search(termDate1, 10);
			System.out.println("length12 "+topDocs.totalHits);
			System.out.println("length123 "+topDocsUnits.totalHits);
			
			for(int i=0;i<routeIndexReader.numDocs();i++)
			{
				Document doc = flightRouteSearcher.doc(i);
				//System.out.println(i + "comments "+doc.get("id")+" articleid "+doc.get("articleid")+ " content "+ doc.get("content")) ;
				//System.out.println(i + " child --> child id " + doc.get("childId") +" articleid "+ doc.get("articleid") + " childContent " + doc.get("childContent")+  "Article --> "+doc.get("id")+" title "+doc.get("title")+ " content "+ doc.get("content")) ;
			System.out.println(i+" dep_code "+ doc.get("dep_code")+" dep_date "+ doc.get("dep_date")+ " duration "+ doc.get("duration")+ " type "+doc.get("type") +" rotue "+doc.get("route"));
				//flightRouteSearcher.doc(2);
			}
			for(int i=0;i<topDocsUnits.scoreDocs.length;i++)
			{
				Document doc = unitSearcher.doc(topDocsUnits.scoreDocs[i].doc);
				System.out.println(i + "Unit flight date  "+doc.get("departuredate")+" departurecode "+doc.get("departurecode")+ " productype "+ doc.get("productBrandType") + " dep_arr_code "+doc.get("dep_arr_code") +" unit id "+doc.get("locationcode") );
				
/*		        ScoreDoc match = topDocsUnits.scoreDocs[i];
		        Explanation explanation = unitSearcher.explain(joinQuery, match.doc);   
		        System.out.println("----------");
		        Document doc1 = unitSearcher.doc(match.doc);
		        System.out.println(doc1.get("title"));
		        System.out.println(explanation.toString());*/
			}	
			for(int i=0;i<topDocs.scoreDocs.length;i++)
			{
				Document doc = flightRouteSearcher.doc(topDocs.scoreDocs[i].doc);
				System.out.println(i+"flightRouteSearcher flight date  "+doc.get("departuredate")+" departurecode "+doc.get("departurecode")+ " productype "+ doc.get("productBrandType") + " dep_arr_code "+doc.get("dep_arr_code"));
				
/*		        ScoreDoc match = topDocsUnits.scoreDocs[i];
		        Explanation explanation = flightRouteSearcher.explain(joinQuery, match.doc);   
		        System.out.println("----------");
		        Document doc1 = flightRouteSearcher.doc(match.doc);
		        System.out.println(doc1.get("title"));
		        System.out.println(explanation.toString());*/
			}
			
			
			System.out.println("topdocs "+topDocs.totalHits + "" +topDocs.scoreDocs.length);
			System.out.println("topdocs1 "+topDocsUnits.totalHits + "" +topDocsUnits.scoreDocs.length);
		}
		catch (final IOException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void fetchBlocking() throws IOException, ParseException 
	{
		String cd ="D:/TUI/PhoenixCodeBase/hybris/data/myNewIndexBLOCKING";
		final Directory airportDirectory = FSDirectory.open(new File(cd+"/airport.idx"));
		final Directory routesDirectory = FSDirectory.open(new File(cd+"/routes.idx"));
		final Directory unitDirectory = FSDirectory.open(new File(cd+"/units.idx"));
		final IndexReader airportIndexReader=IndexReader.open(airportDirectory) ;
		final IndexReader routeIndexReader=IndexReader.open(routesDirectory);
		final IndexReader unitsIndexReader=IndexReader.open(unitDirectory);
		final IndexSearcher airportSearcher = new IndexSearcher(airportIndexReader);
		final IndexSearcher flightRouteSearcher = new IndexSearcher(routeIndexReader);
		final IndexSearcher unitSearcher = new IndexSearcher(unitsIndexReader);
		
		
		
		System.out.println(" total recods in airport "+airportIndexReader.maxDoc() + " route "+routeIndexReader.maxDoc() + " units "+unitsIndexReader.maxDoc());


	//	final String fromField = "dep_arr_code";
		final String fromField = "dep_arr_code";
		final boolean multipleValuesPerDocument = false;
		final String toField = "dep_arr_code";

		//final String query1 = "locationcode: (id1_MBA or id1_DEL) and departurecode:(LGW or LHR)";
		final String query = "locationcode:id1_MBA";
		Analyzer analyzer = new WhitespaceAnalyzer(Version.LUCENE_47);
		QueryParser parser = new QueryParser(Version.LUCENE_47, "locationcode", analyzer);
		//Query q = new TermQuery(new Term("locationcode", "id1_MBA"));
		Query query12 = parser.parse(query);
		System.out.println("unit search "+unitSearcher.search(query12, 20).totalHits);
		// This query should yield article with id 2 as result
		final BooleanQuery fromQuery = new BooleanQuery();
		fromQuery.add(new TermQuery(new Term("locationcode", "id1_MBA")), BooleanClause.Occur.MUST);
		//fromQuery.add(new TermQuery(new Term("locationcode", "id2_MBA")), BooleanClause.Occur.MUST);
		 final TermQuery term =new TermQuery(new Term("locationcode", "id1_MBA"));
		 final TermQuery term1 =new TermQuery(new Term("productBrandType", "FC"));
		 final TermQuery term2 =new TermQuery(new Term("dep_arr_route_code", "LGW_MBA"));
		 
		 final TermQuery termDate =new TermQuery(new Term("departuredate","01-10-2013"));
		// Query t  = new 
		 
		 PhraseQuery joinQuery12 = new PhraseQuery();
		joinQuery12.add(new Term("locationcode", "id1_MBA"));
		
		Query joinQuery; 
		
		try
		{
			joinQuery = JoinUtil.createJoinQuery(fromField, multipleValuesPerDocument, toField, term1, unitSearcher,
					ScoreMode.None);
			
			System.out.println("length "+flightRouteSearcher.search(term, 20).totalHits);
	
		     // System.out.println(" collected "+termsCollector.); 
			final TopDocs topDocs = flightRouteSearcher.search(joinQuery, 10);
			final TopDocs topDocsUnits = unitSearcher.search(joinQuery, 10);
			for(int i=0;i<topDocsUnits.scoreDocs.length;i++)
			{
				Document doc = unitSearcher.doc(topDocsUnits.scoreDocs[i].doc);
				System.out.println(i + "Unit flight date  "+doc.get("departuredate")+" departurecode "+doc.get("departurecode")+ " productype "+ doc.get("productBrandType") + " dep_arr_code "+doc.get("dep_arr_code") +" unit id "+doc.get("locationcode") );
				
/*		        ScoreDoc match = topDocsUnits.scoreDocs[i];
		        Explanation explanation = unitSearcher.explain(joinQuery, match.doc);   
		        System.out.println("----------");
		        Document doc1 = unitSearcher.doc(match.doc);
		        System.out.println(doc1.get("title"));
		        System.out.println(explanation.toString());*/
			}	
			for(int i=0;i<topDocs.scoreDocs.length;i++)
			{
				Document doc = flightRouteSearcher.doc(topDocs.scoreDocs[i].doc);
				System.out.println(i+"flightRouteSearcher flight date  "+doc.get("departuredate")+" departurecode "+doc.get("departurecode")+ " productype "+ doc.get("productBrandType") + " dep_arr_code "+doc.get("dep_arr_code"));
				
/*		        ScoreDoc match = topDocsUnits.scoreDocs[i];
		        Explanation explanation = flightRouteSearcher.explain(joinQuery, match.doc);   
		        System.out.println("----------");
		        Document doc1 = flightRouteSearcher.doc(match.doc);
		        System.out.println(doc1.get("title"));
		        System.out.println(explanation.toString());*/
			}
			
			
			System.out.println("topdocs "+topDocs.totalHits + "" +topDocs.scoreDocs.length);
			System.out.println("topdocs1 "+topDocsUnits.totalHits + "" +topDocsUnits.scoreDocs.length);
		}
		catch (final IOException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
