package testJoinQuery;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.StopwordAnalyzerBase;
import org.apache.lucene.util.Version;


/**
 * The Class AsciiFoldingAnalyzer.
 */
public class AsciiFoldingAnalyzer extends StopwordAnalyzerBase
{

	/** The Constant DEFAULT_MAX_TOKEN_LENGTH. */
	public static final int DEFAULT_MAX_TOKEN_LENGTH = 255;

	/** The max token length. */
	private int maxTokenLength;

	/** The Constant STOP_WORDS_SET. */
	public static final CharArraySet STOP_WORDS_SET = StopAnalyzer.ENGLISH_STOP_WORDS_SET;

	/**
	 * Instantiates a new ascii folding analyzer.
	 * 
	 * @param matchVersion
	 *           the match version
	 * @param stopWords
	 *           the stop words
	 */
	public AsciiFoldingAnalyzer(final Version matchVersion, final CharArraySet stopWords)
	{
		super(matchVersion, stopWords);

		this.maxTokenLength = 255;
	}

	/**
	 * Instantiates a new ascii folding analyzer.
	 * 
	 * @param matchVersion
	 *           the match version
	 */
	public AsciiFoldingAnalyzer(final Version matchVersion)
	{
		this(matchVersion, STOP_WORDS_SET);
	}

	/**
	 * Instantiates a new ascii folding analyzer.
	 * 
	 * @param matchVersion
	 *           the match version
	 * @param stopwords
	 *           the stopwords
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 */
	public AsciiFoldingAnalyzer(final Version matchVersion, final Reader stopwords) throws IOException
	{
		this(matchVersion, loadStopwordSet(stopwords, matchVersion));
	}

	/**
	 * Sets the max token length.
	 * 
	 * @param length
	 *           the new max token length
	 */
	public void setMaxTokenLength(final int length)
	{
		this.maxTokenLength = length;
	}

	/**
	 * Gets the max token length.
	 * 
	 * @return the max token length
	 */
	public int getMaxTokenLength()
	{
		return this.maxTokenLength;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.lucene.analysis.Analyzer#createComponents(java.lang.String, java.io.Reader)
	 */
	@Override
	protected Analyzer.TokenStreamComponents createComponents(final String fieldName, final Reader reader)
	{
		final StandardTokenizer src = new StandardTokenizer(this.matchVersion, reader);
		src.setMaxTokenLength(this.maxTokenLength);
		TokenStream tok = new StandardFilter(this.matchVersion, src);
		tok = new LowerCaseFilter(this.matchVersion, tok);
		tok = new StopFilter(this.matchVersion, tok, this.stopwords);
		tok = new ASCIIFoldingFilter(tok);


		return new Analyzer.TokenStreamComponents(src, tok)
		{
			@Override
			protected void setReader(final Reader reader) throws IOException
			{
				src.setMaxTokenLength(AsciiFoldingAnalyzer.this.maxTokenLength);
				super.setReader(reader);
			}
		};
	}
}
