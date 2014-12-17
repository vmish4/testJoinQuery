package tui;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class TestAgain {
	
	static void createDoc() throws ParseException, IOException
	{
    	String cd ="D:/TUI/sandbox/Development/Code/hybris/data/dateCheck";
    	final Directory unitDirectory = FSDirectory.open(new File(cd+"/units.idx/dn"));
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_CURRENT, analyzer);
       // MaxFieldLength mlf = MaxFieldLength.UNLIMITED;
        IndexWriter writer = new IndexWriter(unitDirectory, iwc);
		 Document doc = new Document();
		 String date;
		 String id ;
		 DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		 long time; 
		 id = String.valueOf(1);
		 time=dateFormat.parse("20-01-2014").getTime();
	      date= buildDate(time);
           String formattedDate = dateFormat.format(time);
        doc.add(new Field("date", date, Store.YES, Index.NOT_ANALYZED));
        doc.add(new Field("dateFormatted", formattedDate, Store.YES, Index.NOT_ANALYZED));
        writer.addDocument(doc);
        
        doc=new Document();
  		 id = String.valueOf(4);
  		 time=dateFormat.parse("15-05-2014").getTime();
  	      date= buildDate(time);
           formattedDate = dateFormat.format(time);
       doc.add(new Field("date", date, Store.YES, Index.NOT_ANALYZED));
       doc.add(new Field("dateFormatted", formattedDate, Store.YES, Index.NOT_ANALYZED));
       writer.addDocument(doc);
       
       doc=new Document();
 		 id = String.valueOf(4);
 		 time=dateFormat.parse("15-05-2014").getTime();
 	      date= buildDate(time);
          formattedDate = dateFormat.format(time);
      doc.add(new Field("date", date, Store.YES, Index.NOT_ANALYZED));
      doc.add(new Field("dateFormatted", formattedDate, Store.YES, Index.NOT_ANALYZED));
      writer.addDocument(doc);
      
      doc=new Document();
 	 id = String.valueOf(5);
 	 time=dateFormat.parse("17-07-2014").getTime();
       date= buildDate(time);
         formattedDate = dateFormat.format(time);
     doc.add(new Field("date", date, Store.YES, Index.NOT_ANALYZED));
     doc.add(new Field("dateFormatted", formattedDate, Store.YES, Index.NOT_ANALYZED));
     writer.addDocument(doc);
     
        doc=new Document();
		 id = String.valueOf(2);
		 time=dateFormat.parse("24-02-2014").getTime();
	      date= buildDate(time);
           formattedDate = dateFormat.format(time);
       doc.add(new Field("date", date, Store.YES, Index.NOT_ANALYZED));
       doc.add(new Field("dateFormatted", formattedDate, Store.YES, Index.NOT_ANALYZED));
       writer.addDocument(doc);
       
 
      

     
  
    
    doc=new Document();
	 id = String.valueOf(6);
	 time=dateFormat.parse("12-08-2014").getTime();
     date= buildDate(time);
       formattedDate = dateFormat.format(time);
   doc.add(new Field("date", date, Store.YES, Index.NOT_ANALYZED));
   doc.add(new Field("dateFormatted", formattedDate, Store.YES, Index.NOT_ANALYZED));
   writer.addDocument(doc);
writer.close();   
   
	}
	
    public static void main(String[] args) throws Exception {
        // setup Lucene to use an in-memory index
    	String cd ="D:/TUI/sandbox/Development/Code/hybris/data/dateCheck";
		final Directory dateDirectory = FSDirectory.open(new File(cd+"/date.idx"));
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
        //IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_CURRENT, analyzer);
       // MaxFieldLength mlf = MaxFieldLength.UNLIMITED;
     //   IndexWriter writer = new IndexWriter(dateDirectory, iwc);
 // createDoc();
        // use the current time as the base of dates for this example
        long baseTime = System.currentTimeMillis();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        // index 10 documents with 1 second between dates
/*        for (int i = 0; i < 10; i++) {
            Document doc = new Document();
            String id = String.valueOf(i);
            long time= baseTime + i * 1000;
            String date = buildDate(time);
            String formattedDate = dateFormat.format(time);
            doc.add(new Field("id", id, Store.YES, Index.NOT_ANALYZED));
            doc.add(new Field("date", date, Store.YES, Index.NOT_ANALYZED));
            doc.add(new Field("dateFormatted", formattedDate, Store.YES, Index.NOT_ANALYZED));
            writer.addDocument(doc);
        }
        writer.close();*/
    	//createDoc();
        

        // search for documents from 5 to 8 seconds after base, inclusive
        final IndexReader dateIndexReader=IndexReader.open(dateDirectory);
        IndexSearcher searcher = new IndexSearcher(dateIndexReader);
        //String lowerDate = buildDate(baseTime + 5000);
        //String upperDate = buildDate(baseTime + 8000);
        String lowerDate = buildDate(dateFormat.parse("02-10-2013").getTime());
        String upperDate = buildDate(dateFormat.parse("02-11-2013").getTime());
        boolean includeLower = true;
        boolean includeUpper = true;
/*        TermRangeQuery query = new TermRangeQuery("date",
                lowerDate, upperDate, includeLower, includeUpper);*/
        QueryParser  parser = new QueryParser(Version.LUCENE_CURRENT, "date", new StandardAnalyzer(Version.LUCENE_CURRENT)); 
       // Query q = parser.parse("date:[20-07-2014 19:39:06 TO 20-07-2014 19:39:11]");
        Query q = parser.parse("date:["+lowerDate+ " TO "+upperDate+"]");
        // display search results
        TopDocs topDocs = searcher.search(q, 10);
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println(doc);
        }
        System.out.println("--------------------------------------------------------------------------------------------"+dateIndexReader.numDocs());
        for(int i=0;i<dateIndexReader.numDocs();i++)
    			{
    				Document doc = searcher.doc(i);
    				//System.out.println(i + "comments "+doc.get("id")+" articleid "+doc.get("articleid")+ " content "+ doc.get("content")) ;  + "fromatted date "+dateFormat.parse(doc.get("dep_date"))
    				//System.out.println(i + " child --> dep_code " + doc.get("dep_code") +" dep_date "+ doc.get("dep_date") + " route " + doc.get("route")) ;
    				 System.out.println(doc);
    				//searcher.doc(2);
    			}
    }

    public static String buildDate(long time) {
        return DateTools.dateToString(new Date(time), Resolution.SECOND);
    }
}
