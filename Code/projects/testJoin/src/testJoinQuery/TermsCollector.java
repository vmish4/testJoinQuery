package testJoinQuery;

import java.io.IOException;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.BinaryDocValues;
import org.apache.lucene.index.SortedSetDocValues;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.BytesRefHash;

abstract class TermsCollector extends Collector
{
  final String field;
  final BytesRefHash collectorTerms = new BytesRefHash();

  TermsCollector(String field) {
    this.field = field;
  }

  public BytesRefHash getCollectorTerms() {
    return this.collectorTerms;
  }

  public void setScorer(Scorer scorer) throws IOException
  {
  }

  public boolean acceptsDocsOutOfOrder()
  {
    return true;
  }

  static TermsCollector create(String field, boolean multipleValuesPerDocument)
  {
    return multipleValuesPerDocument ? new MV(field) : new SV(field);
  }

  static class SV extends TermsCollector
  {
    final BytesRef spare = new BytesRef();
    private BinaryDocValues fromDocTerms;

    SV(String field)
    {
      super(field);
    }

    public void collect(int doc) throws IOException
    {
    	String abc = this.fromDocTerms.get(doc).toString();
    			
      BytesRef term = this.fromDocTerms.get(doc);
      this.collectorTerms.add(term);
    }

    public void setNextReader(AtomicReaderContext context) throws IOException
    {
      this.fromDocTerms = FieldCache.DEFAULT.getTerms(context.reader(), this.field, false);
    }
  }

  static class MV extends TermsCollector
  {
    final BytesRef scratch = new BytesRef();
    private SortedSetDocValues docTermOrds;

    MV(String field)
    {
      super(field);
    }

    public void collect(int doc) throws IOException
    {
      this.docTermOrds.setDocument(doc);
      long ord;
      while ((ord = this.docTermOrds.nextOrd()) != -1L) {
        BytesRef term = this.docTermOrds.lookupOrd(ord);
        this.collectorTerms.add(term);
      }
    }

    public void setNextReader(AtomicReaderContext context) throws IOException
    {
      this.docTermOrds = FieldCache.DEFAULT.getDocTermOrds(context.reader(), this.field);
    }
  }
}