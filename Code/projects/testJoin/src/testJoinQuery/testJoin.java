package testJoinQuery;

import java.io.File;
import java.io.IOException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.ja.Token;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.join.JoinUtil;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.lucene.search.join.ToChildBlockJoinQuery;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.spans.SpanMultiTermQueryWrapper;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.queryparser.analyzing.AnalyzingQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.complexPhrase.ComplexPhraseQueryParser;
public class testJoin {
	

		 
	public static void fetch() throws IOException, ParseException 
	{
		String cd ="D:/TUI/PhoenixCodeBase/hybris/data/myNewIndexCODEROUTE";
		 String unitsPath="D:/opt/hybris/data/index/lucene/units/SE";
		 String routesPath="D:/opt/hybris/data/index/lucene/routes/SE";
		final Directory airportDirectory = FSDirectory.open(new File(cd+"/airport.idx"));
		final Directory routesDirectory = FSDirectory.open(new File(routesPath));
		final Directory unitDirectory = FSDirectory.open(new File(unitsPath));
		final IndexReader airportIndexReader=IndexReader.open(airportDirectory) ;
		final IndexReader routeIndexReader=IndexReader.open(routesDirectory);
		final IndexReader unitsIndexReader=IndexReader.open(unitDirectory);
		final IndexSearcher airportSearcher = new IndexSearcher(airportIndexReader);
		final IndexSearcher flightRouteSearcher = new IndexSearcher(routeIndexReader);
		final IndexSearcher unitSearcher = new IndexSearcher(unitsIndexReader);
		Similarity sim = new DefaultSimilarity() {

/*			@Override
			public float queryNorm(float sumOfSquaredWeights) {
				// TODO Auto-generated method stub
				System.out.println("elseCalled");
				return (float) (0.1);
			}*/
			
			@Override
			public float idf(long docFreq, long numDocs) {
				// TODO Auto-generated method stub
				//System.out.println("IDF");
				return super.idf(docFreq, numDocs);
			}
			
			@Override
						public float sloppyFreq(int distance) {
							// TODO Auto-generated method stub
				System.out.println("SLOP FEQ"+distance);
							return super.sloppyFreq(distance);
						}
			
@Override
public float lengthNorm(FieldInvertState arg0) {
	// TODO Auto-generated method stub
	return (float)(0.01);
}

			  
			};
		unitSearcher.setSimilarity(sim);
		final String fromField = "route";
		final boolean multipleValuesPerDocument = false;
		final String toField = "route";

		
		Query joinQuery;	
		Query query1;
		try
		{
			
			final AsciiFoldingAnalyzer analyzer = new AsciiFoldingAnalyzer(Version.LUCENE_45);
			final AnalyzingQueryParser parser = new AnalyzingQueryParser(Version.LUCENE_45, "name", analyzer);
			SpanQuery termQuery = new SpanTermQuery(new Term("name","blue")); 
			
			 WildcardQuery wildcard = new WildcardQuery(new Term("name", "vill*"));
			 SpanQuery spanWildcard = new SpanMultiTermQueryWrapper<WildcardQuery>(wildcard);
			 
			 SpanNearQuery spanNearQuery = new SpanNearQuery(new SpanQuery[]{termQuery,spanWildcard}, 1,false); 
			 
			TopDocs docsSpan = unitSearcher.search(spanNearQuery, Integer.MAX_VALUE);
			System.out.println("length of span is " + docsSpan.totalHits);
			for(int i=0;i<docsSpan.scoreDocs.length;i++)
			{
				Document doc = unitSearcher.doc(docsSpan.scoreDocs[i].doc);
//				System.out.println( unitSearcher.explain(spanNearQuery, docsSpan.scoreDocs[i].doc)  + "Doc id " +docsSpan.scoreDocs[i].doc + " score  "+ docsSpan.scoreDocs[i].score + " doc -->"+  doc.getField("name"));
				//System.out.println("Doc score  "+ docsSpan.scoreDocs[i].score + " doc name -->"+  doc.getField("name"));
			}
			
			List<SpanQuery> spans= new ArrayList<SpanQuery>();
			//Analyzer standardAnayzer = new StandardAnalyzer(Version.LUCENE_45); 
			ComplexPhraseQueryParser complexPhraseQueryParser = new ComplexPhraseQueryParser(Version.LUCENE_45, "name", new StandardAnalyzer(Version.LUCENE_45));
			Analyzer standardAnayzer = new StandardAnalyzer(Version.LUCENE_45); 
org.apache.lucene.analysis.Token token = new org.apache.lucene.analysis.Token();

//TokenStream source=standardAnayzer.tokenStream(fieldName,new StringReader("name:Blue"),AnalysisMode.TOKENISE);

//TokenStream tokenStream = analyzer.tokenStream(fieldName, reader);



		
/*			    while (ts.incrementToken()) {
			    	
			      String term=((CharTermAttribute)ts.getAttribute(CharTermAttribute.class)).toString();
			      System.out.println("Term "+term);
			      spans.add(new SpanTermQuery(new Term("name",term)));
			    }
*/			    	 TokenStream ts=analyzer.tokenStream("name","name:√‚z-Ay*");
			    OffsetAttribute offsetAttribute = ts.addAttribute(OffsetAttribute.class);
			    CharTermAttribute charTermAttribute = ts.addAttribute(CharTermAttribute.class);

			    ts.reset();
			    while (ts.incrementToken()) {
			        int startOffset = offsetAttribute.startOffset();
			        int endOffset = offsetAttribute.endOffset();
			        String term = charTermAttribute.toString();
			        System.out.println("Tesm 1w "+ term);
			    }
			
		
		//	complexPhraseQueryParser.parse("((name:blue AND name:vill*)~1)"); 
			
			  QueryParser  parser12 = new QueryParser(Version.LUCENE_45, "group", new WhitespaceAnalyzer(Version.LUCENE_45));
			 // Query q =parser12.parse("name:Blue*");
			  Query q= new WildcardQuery(new Term("name", "vill*")); 
			TopDocs docsWild = unitSearcher.search(q, Integer.MAX_VALUE);
			System.out.println("length of span is " + docsWild.totalHits);

		  QueryParser  parser1 = new QueryParser(Version.LUCENE_45, "group", new WhitespaceAnalyzer(Version.LUCENE_45)); 
		  long start = System.currentTimeMillis();
		  query1 = parser1.parse("type:P"); 
		  TopDocs hits_all_airport = flightRouteSearcher.search(query1, Integer.MAX_VALUE); 
		  for(int i=0;i<hits_all_airport.scoreDocs.length;i++)
			{
				Document doc = flightRouteSearcher.doc(hits_all_airport.scoreDocs[i].doc);
			}
			  System.out.println(" hits_all_airport "+ hits_all_airport.totalHits+" JOIN  Time taken in getting all dates or airports "+(System.currentTimeMillis()-start));
			  
			
		start = System.currentTimeMillis();
		query1 = parser1.parse("hotel or destination or concept");
		final TopDocs hits_all_destination = unitSearcher.search(query1, Integer.MAX_VALUE);
		  for(int i=0;i<hits_all_destination.totalHits;i++)
			  for(i=0;i<hits_all_destination.scoreDocs.length;i++)
				{
					Document doc = flightRouteSearcher.doc(hits_all_destination.scoreDocs[i].doc);
				}
		  System.out.println(" hits_all_destination "+ hits_all_destination.totalHits+" JOIN  All Destinations"+(System.currentTimeMillis()-start));
		
	
		  
		   start = System.currentTimeMillis();
		   QueryParser  parser2 = new QueryParser(Version.LUCENE_45, "group", new WhitespaceAnalyzer(Version.LUCENE_45)); 
		   query1 = parser2.parse("code:G-000073");
		   final TopDocs hits_single_destination = unitSearcher.search(query1, Integer.MAX_VALUE);
	  for(int i=0;i<hits_single_destination.scoreDocs.length;i++)
		{
			Document doc = unitSearcher.doc(hits_single_destination.scoreDocs[i].doc);
		}
	  System.out.println(query1+ " hits_single_destination "+ hits_single_destination.totalHits+" JOIN  All Destinations"+ (System.currentTimeMillis()-start));
		  
		  
		  
		  
		  start = System.currentTimeMillis();
		  query1 = parser1.parse("code:G-000073");
		joinQuery = JoinUtil.createJoinQuery(fromField, true, toField, query1, unitSearcher,
				ScoreMode.None);
	final TopDocs hits_destToAir = flightRouteSearcher.search(joinQuery, Integer.MAX_VALUE);
	  for(int i=0;i<hits_destToAir.scoreDocs.length;i++)
		{
			Document doc = flightRouteSearcher.doc(hits_destToAir.scoreDocs[i].doc);
		}
	  System.out.println(" hits_destToAir "+ hits_destToAir.totalHits+" Destination --> Airport "+(System.currentTimeMillis()-start));
	
	
	  
	  start = System.currentTimeMillis();
	  query1 = parser1.parse("dep_code:VXO");
	  joinQuery = JoinUtil.createJoinQuery(fromField, multipleValuesPerDocument, toField, query1, flightRouteSearcher,
			ScoreMode.None);
	final TopDocs hits_airToDes = unitSearcher.search(joinQuery, Integer.MAX_VALUE);
	for(int i=0;i<hits_airToDes.scoreDocs.length;i++)
		{
		Document doc = unitSearcher.doc(hits_airToDes.scoreDocs[i].doc);
		}
	System.out.println(" hits_airToDes "+ hits_airToDes.totalHits+" JOIN  Time taken in getting all dates or airports "+(System.currentTimeMillis()-start));


	  start = System.currentTimeMillis();
	  TopDocs hits_airToDate = flightRouteSearcher.search(new TermQuery(new Term("dep_code", "VXO")), Integer.MAX_VALUE);
	  for(int i=0;i<hits_airToDate.scoreDocs.length;i++)
		{
			Document doc = flightRouteSearcher.doc(hits_airToDate.scoreDocs[i].doc);
		}
	  System.out.println(" hits_airToDate "+ hits_airToDate.totalHits+" Destination name : Time taken in getting all dates or airports "+(System.currentTimeMillis()-start));

	
	
	start = System.currentTimeMillis();
	query1 = parser1.parse("dep_date:20150406183000");
joinQuery = JoinUtil.createJoinQuery(fromField, multipleValuesPerDocument, toField, query1, flightRouteSearcher,
		ScoreMode.None);
final TopDocs hits_dateToDes = unitSearcher.search(joinQuery, Integer.MAX_VALUE);
 
for(int i=0;i<hits_dateToDes.scoreDocs.length;i++)
{
	Document doc = unitSearcher.doc(hits_dateToDes.scoreDocs[i].doc);
}
System.out.println(" hits_dateToDes"+ hits_dateToDes.totalHits+" JOIN  Time taken in getting all dates or airports "+(System.currentTimeMillis()-start));




start = System.currentTimeMillis();
query1 = parser1.parse("dep_date:20150406183000");
joinQuery = JoinUtil.createJoinQuery(fromField, multipleValuesPerDocument, toField, query1, flightRouteSearcher,
		ScoreMode.None);
final TopDocs hits_dateToDes1 = unitSearcher.search(joinQuery, Integer.MAX_VALUE);
for(int i=0;i<hits_dateToDes1.scoreDocs.length;i++)
	{
		Document doc = unitSearcher.doc(hits_dateToDes1.scoreDocs[i].doc);
	}
System.out.println(" hits_dateToDes1 "+ hits_dateToDes1.totalHits+" JOIN  Time taken in getting all dates or airports "+(System.currentTimeMillis()-start));






start = System.currentTimeMillis();

final TopDocs hits_date_single = flightRouteSearcher.search(query1, Integer.MAX_VALUE);
	  for(int i=0;i<hits_date_single.scoreDocs.length;i++)
		{
			Document doc = flightRouteSearcher.doc(hits_date_single.scoreDocs[i].doc);
		}
System.out.println(" hits_date_single "+ hits_date_single.totalHits+" JOIN  Time taken in getting all dates or airports "+(System.currentTimeMillis()-start));



start = System.currentTimeMillis();
BooleanQuery mainQuery = new BooleanQuery(); 
Query dest =  new TermQuery(new Term("code", "G-000073"));
joinQuery = JoinUtil.createJoinQuery(fromField, true, toField, dest,unitSearcher ,
		ScoreMode.None);
mainQuery.add(joinQuery,Occur.MUST);
mainQuery.add(new TermQuery(new Term("dep_code", "VXO")),Occur.MUST);
TopDocs hits_dest_airport_To_Date = flightRouteSearcher.search(mainQuery, Integer.MAX_VALUE);
for(int i=0;i<hits_dest_airport_To_Date.scoreDocs.length;i++)
	{
		Document doc = flightRouteSearcher.doc(hits_dest_airport_To_Date.scoreDocs[i].doc);
	}
System.out.println(" hits_dest_airport_To_Date "+ hits_dest_airport_To_Date.totalHits+" Destination name : Time taken in getting all dates or airports "+(System.currentTimeMillis()-start));
	  


start = System.currentTimeMillis();
mainQuery = new BooleanQuery(); 
joinQuery = JoinUtil.createJoinQuery(fromField, true, toField, dest,unitSearcher ,
		ScoreMode.None);
mainQuery.add(joinQuery,Occur.MUST);
mainQuery.add(new TermQuery(new Term("dep_date","20150406183000")),Occur.MUST);
TopDocs hits_dest_date_To_Airport = flightRouteSearcher.search(mainQuery, Integer.MAX_VALUE);
for(int i=0;i<hits_dest_date_To_Airport.scoreDocs.length;i++)
	{
		Document doc = flightRouteSearcher.doc(hits_dest_date_To_Airport.scoreDocs[i].doc);
	}
System.out.println(" hits_dest_date_To_Airport "+ hits_dest_date_To_Airport.totalHits+" Destination name : Time taken in getting all dates or airports "+(System.currentTimeMillis()-start));
	  

start = System.currentTimeMillis();
mainQuery = new BooleanQuery(); 

mainQuery.add(new TermQuery(new Term("dep_date","20150406183000")),Occur.MUST);
mainQuery.add(new TermQuery(new Term("dep_code", "VXO")),Occur.MUST);
joinQuery = JoinUtil.createJoinQuery(fromField, true, toField, mainQuery,flightRouteSearcher  ,
		ScoreMode.None);
mainQuery.add(joinQuery,Occur.MUST);
TopDocs hits_airport_date_To_des = unitSearcher.search(joinQuery, Integer.MAX_VALUE);

System.out.println("flightRouteSearcher.search(mainQuery, Integer.MAX_VALUE).totalHits;" + flightRouteSearcher.search(mainQuery, Integer.MAX_VALUE).totalHits);
for(int i=0;i<hits_airport_date_To_des.scoreDocs.length;i++)
	{
		Document doc = unitSearcher.doc(hits_airport_date_To_des.scoreDocs[i].doc);
	}
System.out.println(" hits_airport_date_To_des "+ hits_airport_date_To_des.totalHits+" Destination name : Time taken in getting all dates or airports "+(System.currentTimeMillis()-start));
	
		}
		catch (final IOException e)
		{
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
		//final String fromField = "dep_arr_code";
		//final boolean multipleValuesPerDocument = false;
		//final String toField = "dep_arr_code";
		
		final String fromField = "route";
		final boolean multipleValuesPerDocument = false;
		final String toField = "route";

		//final String query1 = "locationcode: (id1_MBA or id1_DEL) and departurecode:(LGW or LHR)";
		final String query = "locationcode:id1_MBA";
		Analyzer analyzer = new WhitespaceAnalyzer(Version.LUCENE_45);
		QueryParser parser = new QueryParser(Version.LUCENE_45, "locationcode", analyzer);
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
			  TermsCollector termsCollector = TermsCollector.create(fromField, multipleValuesPerDocument);
		      unitSearcher.search(joinQuery12, termsCollector);
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

	public static void main(String[] args)  {
		try {
			fetch();
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

}
