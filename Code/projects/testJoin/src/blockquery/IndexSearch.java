package blockquery;

import java.io.File;
import java.io.IOException;

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
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.join.FixedBitSetCachingWrapperFilter;
import org.apache.lucene.search.join.JoinUtil;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.lucene.search.join.ToChildBlockJoinQuery;
import org.apache.lucene.search.join.ToParentBlockJoinQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class IndexSearch {
	
	
	
	static void blockSearchToParentJoin() throws IOException
	{
		
		String cd ="D:/TUI/PhoenixCodeBase/hybris/data/articleExampleBlockSingleIns";
		
		final Directory articleDirectory = FSDirectory.open(new File(cd+"/article.idx"));
		//final Directory commentsDirectory = FSDirectory.open(new File(cd+"/comments.idx"));
		
		final IndexReader articleIndexReader=IndexReader.open(articleDirectory);
		//final IndexReader commentsIndexReader=IndexReader.open(commentsDirectory) ;
		
		
		final IndexSearcher articleSearcher = new IndexSearcher(articleIndexReader);
		//final IndexSearcher commentsSearcher = new IndexSearcher(commentsIndexReader);
		
		 // Query parentQuery = new TermQuery(new Term("title", "lucene"));
		Query parentQuery = new TermQuery(new Term("type", "article"));
		//Query parentQuery = new TermQuery(new Term("title", "norms"));
		  Filter parentsFilter = new FixedBitSetCachingWrapperFilter(new QueryWrapperFilter(parentQuery)); 
		  
		  Query childQuery =  new TermQuery(new Term("articleid", "1")); 
		  
		  		  	  
		  BooleanQuery mainQuery = new BooleanQuery(); 
		  //mainQuery.add(new TermQuery(new Term("id", "1")), Occur.MUST);
		  mainQuery.add(new TermQuery(new Term("title", "norms")), Occur.MUST);
		  
		  ToParentBlockJoinQuery productItemQuery = new ToParentBlockJoinQuery(childQuery, parentsFilter, ScoreMode.None); 
		  mainQuery.add(productItemQuery, Occur.MUST);
		  TopDocs hits = articleSearcher.search(mainQuery, 10);
		  TopDocs hits1 = articleSearcher.search(productItemQuery, 10);
		  System.out.println("Total hits "+hits.totalHits);
		  System.out.println("Total hits "+hits1.totalHits);
		  
		  Query childQueryNoParent =  new TermQuery(new Term("articleid", "5")); 
		  
		  ToParentBlockJoinQuery productItemQueryNoParent = new ToParentBlockJoinQuery(childQueryNoParent, parentsFilter, ScoreMode.None); 
		 // mainQuery.add(productItemQuery, Occur.MUST);
		 // TopDocs hitsNoParent = articleSearcher.search(mainQuery, 10);
		  TopDocs hits1NoParent = articleSearcher.search(productItemQueryNoParent, 10);
		  //System.out.println("Total hitsNoParent "+hitsNoParent.totalHits);
		  System.out.println("Total hitsNoParent1 "+hits1NoParent.totalHits);
		  
			for(int i=0;i<hits1NoParent.scoreDocs.length;i++)
			{
				Document doc = articleSearcher.doc(hits1NoParent.scoreDocs[i].doc);
				//System.out.println(i + "comments "+doc.get("id")+" articleid "+doc.get("articleid")+ " content "+ doc.get("content")) ;
				System.out.println("No Parent"+i + "Article "+doc.get("id")+" title "+doc.get("title")+ " content "+ doc.get("content")) ;
				//articleSearcher.doc(2);
				
				
				
			}
			
			for(int i=0;i<hits1.scoreDocs.length;i++)
			{
				Document doc = articleSearcher.doc(hits1.scoreDocs[i].doc);
				//System.out.println(i + "comments "+doc.get("id")+" articleid "+doc.get("articleid")+ " content "+ doc.get("content")) ;
				System.out.println(i + "Article "+doc.get("id")+" title "+doc.get("title")+ " content "+ doc.get("content")) ;
				articleSearcher.doc(2);
			}
		  
/*			for(int i=0;i<articleIndexReader.numDocs();i++)
			{
				Document doc = articleSearcher.doc(i);
				//System.out.println(i + "comments "+doc.get("id")+" articleid "+doc.get("articleid")+ " content "+ doc.get("content")) ;
				System.out.println(i + " child --> child id " + doc.get("childId") +" articleid "+ doc.get("articleid") + " childContent " + doc.get("childContent")+  "Article --> "+doc.get("id")+" title "+doc.get("title")+ " content "+ doc.get("content")) ;
				articleSearcher.doc(2);
			}*/
	}
	
	
	static void blockSearchToChildJoin() throws IOException
	{
		
		//String cd ="D:/TUI/PhoenixCodeBase/hybris/data/articleExampleBlock";
		String cd ="D:/TUI/PhoenixCodeBase/hybris/data/articleExampleBlockSingleIns";
		final Directory articleDirectory = FSDirectory.open(new File(cd+"/article.idx"));
		//final Directory commentsDirectory = FSDirectory.open(new File(cd+"/comments.idx"));
		
		final IndexReader articleIndexReader=IndexReader.open(articleDirectory);
		//final IndexReader commentsIndexReader=IndexReader.open(commentsDirectory) ;
		
		
		final IndexSearcher articleSearcher = new IndexSearcher(articleIndexReader);
		//final IndexSearcher commentsSearcher = new IndexSearcher(commentsIndexReader);
		
		 // Query parentQuery = new TermQuery(new Term("title", "lucene"));
		Query parentQuery = new TermQuery(new Term("type", "article"));
		// Query parentQuery = new TermQuery(new Term("title", "norms"));
		  Filter parentsFilter = new FixedBitSetCachingWrapperFilter(new QueryWrapperFilter(parentQuery)); 
		  
		  //Query parentBlockJoinQuery =  new TermQuery(new Term("articleid", "1")); 
		  //Query parentBlockJoinQuery =  new TermQuery(new Term("content", "REC"));
		  Query parentBlockJoinQuery = new TermQuery(new Term("id", "3"));
		  
		  		  	  
		  BooleanQuery mainQuery = new BooleanQuery(); 
		  //mainQuery.add(new TermQuery(new Term("id", "1")), Occur.MUST);
		 // mainQuery.add(new TermQuery(new Term("title", "norms")), Occur.MUST);
		  //mainQuery.add(new TermQuery(new Term("childId", "4")), Occur.MUST);
		  mainQuery.add(new TermQuery(new Term("articleid", "5")), Occur.MUST);
		  
		  
		  ToChildBlockJoinQuery productItemQuery = new ToChildBlockJoinQuery(parentBlockJoinQuery, parentsFilter,false); 
		  mainQuery.add(productItemQuery, Occur.MUST);
		  TopDocs hits = articleSearcher.search(mainQuery, 10);
		  TopDocs hits1 = articleSearcher.search(productItemQuery, 10);
		  System.out.println("Total hits "+hits.totalHits);
		  System.out.println("Total hits "+hits1.totalHits);
		  
			for(int i=0;i<hits.scoreDocs.length;i++)
			{
				Document doc = articleSearcher.doc(hits.scoreDocs[i].doc);
				//System.out.println(i + "comments "+doc.get("id")+" articleid "+doc.get("articleid")+ " content "+ doc.get("content")) ;
				//System.out.println(i + "Article "+doc.get("id")+" title "+doc.get("title")+ " content "+ doc.get("content")) ;
				System.out.println(i + "Child "+doc.get("childId")+" articleid "+doc.get("articleid")+ " childContent "+ doc.get("childContent")) ;
				//articleSearcher.doc(2);
				
				
				
			}
			
			for(int i=0;i<hits1.scoreDocs.length;i++)
			{
				Document doc = articleSearcher.doc(hits1.scoreDocs[i].doc);
				//System.out.println(i + "comments "+doc.get("id")+" articleid "+doc.get("articleid")+ " content "+ doc.get("content")) ;
				//System.out.println(i + "Article "+doc.get("id")+" title "+doc.get("title")+ " content "+ doc.get("content")) ;
				//articleSearcher.doc(2);
				System.out.println(i + "Child "+doc.get("childId")+" articleid "+doc.get("articleid")+ " childContent "+ doc.get("childContent")) ;
				
				
				
			}
		  
/*			for(int i=0;i<articleIndexReader.numDocs();i++)
			{
				Document doc = articleSearcher.doc(i);
				//System.out.println(i + "comments "+doc.get("id")+" articleid "+doc.get("articleid")+ " content "+ doc.get("content")) ;
				System.out.println(i + " child --> child id " + doc.get("childId") +" articleid "+ doc.get("articleid") + " childContent " + doc.get("childContent")+  "Article --> "+doc.get("id")+ " type  "+doc.get("type")+" title "+doc.get("title")+ " content "+ doc.get("content")) ;
				articleSearcher.doc(2);
			}*/
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
	blockSearchToChildJoin();
	
	blockSearchToParentJoin();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
	}

}
