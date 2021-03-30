/*
 * SEARCH ENGINE INDEXING 
 * Lucene index class. Updated for Lucene 8.8.1
 * Adapted from original source code: 
 * Lucene - Index and Search Text Files - HowToDoInJava.com
 * https://howtodoinjava.com/lucene/lucene-index-and-search-text-files/
 * Additional debugging and troubleshooting of the deleteEntriesFromIndexUsingTerm() 
 * method using the Luke utility hosted by Google
 * (https://code.google.com/archive/p/luke/) is required.
 * Update: Apparently Luke has not been updated since 2012 and it's not compatible 
 * with Lucene 8. ... Searching for other solutions...
 *
 * Lucene docs: https://lucene.apache.org/core/8_0_0/core/index.html?overview-summary.html
 * "Apache Lucene is a high-performance, full-featured text search engine library."
 * Lucene features  a nonSQL database which can be accessed and modified with java utilities.
 */
package coffee123.seutils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
 
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
 
public class SEindex {
    // Directory that contains the Lucene index
    private static final String INDEX_DIR = "c:/temp/indexedFiles";
    
    // Directory that contains the text files to be indexed
    private static final String DOC_DIR = "c:/temp/readFiles";
    
    public static void main(String[] args) throws IOException, Exception    {
        
        System.out.println( "Indexing files" ); 

         
        //Output folder
       // String indexPath = "c:/temp/indexedFiles";
 
        //Input Path Variable
        final Path docDir = Paths.get(DOC_DIR);
 
        try
        {
            //org.apache.lucene.store.Directory instance
            Directory dir = FSDirectory.open( Paths.get(INDEX_DIR) );
             
            //analyzer with the default stop words
            Analyzer analyzer = new StandardAnalyzer();
             
            //IndexWriter Configuration
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
             
            //IndexWriter writes new index files to the directory
            IndexWriter writer = new IndexWriter(dir, iwc);
             
            //Its recursive method to iterate all files and directories
            MainFunctions.indexDocs(writer, docDir);
 
            writer.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        
        
       //DELETE ENTRIES	
	//	deleteEntriesFromIndexUsingTerm();

        
        
    }
    

  
 
}




