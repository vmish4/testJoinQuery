/**
 * 
 */
package tui;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * @author vmish4
 *
 */
public class LuceneSimpleSearch {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws IOException, ParseException {
		
		String cd ="D:/TUI/sandbox/Development/Code/hybris/data/lucene/UpdatedCode/unitsParent/SE";
		
		final Directory articleDirectory = FSDirectory.open(new File(cd));

		
		final IndexReader articleIndexReader=IndexReader.open(articleDirectory);

		
		
		final IndexSearcher articleSearcher = new IndexSearcher(articleIndexReader);
		
		 QueryParser  parser = new QueryParser(Version.LUCENE_CURRENT, "type", new StandardAnalyzer(Version.LUCENE_CURRENT)); 
	       // Query q = parser.parse("date:[20-07-2014 19:39:06 TO 20-07-2014 19:39:11]");
	        Query parentQuery = parser.parse("hotel and destination");
//hotel and destination
		Query q = new TermQuery(new Term("type","destination")); 
		
		TopDocs hits = articleSearcher.search(parentQuery, 10); 
		
		  System.out.println("Total hitsNoParent1 "+hits.totalHits);
		  
			for(int i=0;i<hits.scoreDocs.length;i++)
			{
				Document doc = articleSearcher.doc(hits.scoreDocs[i].doc);
				//System.out.println(i + "comments "+doc.get("id")+" articleid "+doc.get("articleid")+ " content "+ doc.get("content")) ;
				System.out.println(doc) ;
				//articleSearcher.doc(2);
				
				
				
			}

	}

}
