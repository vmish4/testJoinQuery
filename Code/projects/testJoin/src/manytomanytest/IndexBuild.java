package manytomanytest;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;


public class IndexBuild {
	
	final static Version LUCENE_VERSION = Version.LUCENE_47;
	
	public static void buildIndex1() throws IOException
	{
		String cd ="D:/TUI/PhoenixCodeBase/hybris/data/articleExample";
		final Directory airportDirectory = FSDirectory.open(new File(cd+"/article.idx"));
		// final Directory routesDirectory = FSDirectory.open(new File(cd+"/comments.idx"));

/*		final IndexReader airportIndexReader=IndexReader.open(airportDirectory) ;
		final IndexReader routeIndexReader=IndexReader.open(routesDirectory);

		final IndexSearcher airportSearcher = new IndexSearcher(airportIndexReader);
		final IndexSearcher flightRouteSearcher = new IndexSearcher(routeIndexReader);*/
		
		final IndexWriter airportLuceneWriter = openWriter(airportDirectory);
//		final IndexWriter routeLuceneWriter = openWriter(routesDirectory);

		
		 
		 Document document = new Document();
		document.add(new Field("id","1" , Field.Store.YES, Field.Index.ANALYZED));
		document.add(new Field("title","Query Time joining in lucene" , Field.Store.YES, Field.Index.ANALYZED));
		document.add(new Field("content", "REC 1 tthis is current what we are able to search ", Field.Store.YES, Field.Index.NOT_ANALYZED));

		
		airportLuceneWriter.addDocument(document);
		
		 document = new Document();
		document.add(new Field("id","2" , Field.Store.YES, Field.Index.ANALYZED));
		document.add(new Field("title","single byted norms are this" , Field.Store.YES, Field.Index.ANALYZED));
		document.add(new Field("content", "REC 2 tthis is current what we are able to search " , Field.Store.YES, Field.Index.NOT_ANALYZED));
		airportLuceneWriter.addDocument(document);
		airportLuceneWriter.close();
		
		
		
	}
	
    public static IndexWriter openWriter(Directory directory) throws IOException {
    	
    	 Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
    	 IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_47, analyzer);
        IndexWriter iw = new IndexWriter(directory,
               iwc);
        return iw;
    }
	
    public static void buildIndex2() throws IOException
	{
		
		String cd ="D:/TUI/PhoenixCodeBase/hybris/data/articleExample";
//		final Directory airportDirectory = FSDirectory.open(new File(cd+"/article.idx"));
		final Directory routesDirectory = FSDirectory.open(new File(cd+"/comments.idx"));

/*		final IndexReader airportIndexReader=IndexReader.open(airportDirectory) ;
		final IndexReader routeIndexReader=IndexReader.open(routesDirectory);

		final IndexSearcher airportSearcher = new IndexSearcher(airportIndexReader);
		final IndexSearcher flightRouteSearcher = new IndexSearcher(routeIndexReader);*/
		
	//	final IndexWriter airportLuceneWriter = openWriter(airportDirectory);
		final IndexWriter routeLuceneWriter = openWriter(routesDirectory);
		

		 Document document = new Document();
		document.add(new Field("id", "1", Field.Store.YES, Field.Index.ANALYZED));
		document.add(new Field("articleid","2" , Field.Store.YES, Field.Index.ANALYZED));
		document.add(new Field("content","comment on first article" , Field.Store.YES, Field.Index.NOT_ANALYZED));

		
		routeLuceneWriter.addDocument(document);
		
		document = new Document();
		document.add(new Field("id","2" , Field.Store.YES, Field.Index.ANALYZED));
		document.add(new Field("articleid","1" , Field.Store.YES, Field.Index.ANALYZED));
		document.add(new Field("articleid","2" , Field.Store.YES, Field.Index.ANALYZED));
		document.add(new Field("content","comment on second article", Field.Store.YES, Field.Index.NOT_ANALYZED));
		routeLuceneWriter.addDocument(document);
		
		routeLuceneWriter.close();
	}
	public static void main(String[] args) {

		try {
			buildIndex1();
			buildIndex2();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
