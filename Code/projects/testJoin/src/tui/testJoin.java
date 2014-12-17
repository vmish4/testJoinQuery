package tui;

import java.io.File;
import java.io.IOException;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.lucene.analysis.Analyzer;
//import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.document.Document;
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
//import org.apache.lucene.search.join.JoinUtil;
//import org.apache.lucene.search.join.ScoreMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.queryParser.ParseException;
//import org.apache.lucene.queryparser.classic.ParseException;
//import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryParser.QueryParser;

public class testJoin {
	
	public static void fetchDateRange() throws IOException, ParseException, java.text.ParseException 
	{
		String cd ="D:/TUI/sandbox/Development/Code/hybris/data/lucene/UpdatedCode";
		//final Directory airportDirectory = FSDirectory.open(new File(cd+"/airport.idx"));
		final Directory routesDirectory = FSDirectory.open(new File(cd+"/routes.idx/dn"));
		final Directory unitDirectory = FSDirectory.open(new File(cd+"/units.idx/dn"));
		//final IndexReader airportIndexReader=IndexReader.open(airportDirectory) ;
		final IndexReader routeIndexReader=IndexReader.open(routesDirectory);
		final IndexReader unitsIndexReader=IndexReader.open(unitDirectory);
		//final IndexSearcher airportSearcher = new IndexSearcher(airportIndexReader);
		final IndexSearcher flightRouteSearcher = new IndexSearcher(routeIndexReader);
		final IndexSearcher unitSearcher = new IndexSearcher(unitsIndexReader);
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		Date startDate;
		
		startDate = dateFormat.parse("02-10-2013");
		final Date endDate =  dateFormat.parse("02-11-2013");
		final String duration = "5";
		Long startime = startDate.getTime();
        String lowerDate = buildDate(startDate);
        String upperDate = buildDate(endDate);
		
		   QueryParser  parser = new QueryParser(Version.LUCENE_CURRENT, "dep_date", new StandardAnalyzer(Version.LUCENE_CURRENT)); 
	        Query q = parser.parse("dep_date:["+lowerDate+ " TO "+upperDate+"]");
	        // display search results
	        TopDocs topDocs = flightRouteSearcher.search(q, 10);
	        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
	            Document doc = flightRouteSearcher.doc(scoreDoc.doc);
	            System.out.println(doc);
	        }
		
	        for(int i=0;i<routeIndexReader.numDocs();i++)
			{
				Document doc = flightRouteSearcher.doc(i);
				//System.out.println(i + "comments "+doc.get("id")+" articleid "+doc.get("articleid")+ " content "+ doc.get("content")) ;  + "fromatted date "+dateFormat.parse(doc.get("dep_date"))
				System.out.println(i + " child --> dep_code " + doc.get("dep_code") +" dep_date "+ doc.get("dep_date") +" formatted" + dateFormat.format(new Date(DateTools.stringToTime(doc.get("dep_date"))))+ " route " + doc.get("route")) ;
				flightRouteSearcher.doc(2);
			}
	        
	        
		
		
		
		
		
	
	}
	
    private static String buildDate(Date date) {
        return DateTools.dateToString(date, Resolution.SECOND);
    }

	public static void main(String[] args)  {
		try {
			fetchDateRange();
		} catch (IOException | ParseException |java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

}
