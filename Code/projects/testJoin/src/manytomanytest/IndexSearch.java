package manytomanytest;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.join.JoinUtil;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class IndexSearch {
	
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
			
			for(int i=0;i<articleIndexReader.numDocs();i++)
			{
				Document doc = articleSearcher.doc(i);
				//System.out.println(i + "comments "+doc.get("id")+" articleid "+doc.get("articleid")+ " content "+ doc.get("content")) ;
				System.out.println(i +  "Article --> "+doc.get("id")+" title "+doc.get("title")+ " content "+ doc.get("content")) ;
				//articleSearcher.doc(2);
			}
			
			
			for(int i=0;i<commentsIndexReader.numDocs();i++)
			{
				Document doc = commentsSearcher.doc(i);
				//System.out.println(i + "comments "+doc.get("id")+" articleid "+doc.get("articleid")+ " content "+ doc.get("content")) ;
				System.out.println(i + " child --> child id " + doc.get("id") +" articleid "+ doc.getFields("articleid") + " childContent " + doc.get("content")) ;
				//articleSearcher.doc(2);
			}
			
			System.out.println("hits total length "+hits.totalHits);
			
			for(int i=0;i<hits.scoreDocs.length;i++)
			{
				Document doc = articleSearcher.doc(hits.scoreDocs[i].doc);
				//System.out.println(i + "comments "+doc.get("id")+" articleid "+doc.get("articleid")+ " content "+ doc.get("content")) ;
				System.out.println(i + "Article "+doc.get("id")+" title "+doc.get("title")+ " content "+ doc.get("content")) ;

			}
			
			
		
	}

	public static void main(String[] args) {
	
try {
	search();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
	}

}
