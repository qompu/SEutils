/*
 * SEARCH ENGINE MAIN FUNCTIONS
 * To be used as part of the main Search Engine project.
 * Lucene main search and index functions. Updated for Lucene 8.8.1
 * Adapted from original source code: 
 * Lucene - Index and Search Text Files - HowToDoInJava.com
 * https://howtodoinjava.com/lucene/lucene-index-and-search-text-files/
 * Modifying the index requires the Luke utility. 
 * Update: Luke is bundled with Lucene on version 8.8.1
 * and can be downloaded from https://lucene.apache.org/core/downloads.html
 * "Apache Lucene is a high-performance, full-featured text search engine library."
 * Lucene features  a NoSQL database which can be accessed and modified with Luke.
 * The functions included in this java class include a delete document form from the index method
 * deleteEntriesFromIndexUsingQuery(). However, due to the limitations in Lucene, an add document 
 * to index method could not be created. Luke can be used to modifying the index.
 * Reference: Tutorial On Using Lucene With Text search 
 * (Brian Will) (21:00) https://www.youtube.com/watch?v=OHX24mNw2Jg
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
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;



public class MainFunctions {
    
    // Directory that contains the Lucene index
    private static final String INDEX_DIR = "c:/temp/indexedFiles";
    
    // Directory that contains the text files to be indexed
    private static final String DOC_DIR = "c:/temp/readFiles";

       protected static void indexDoc(IndexWriter writer, Path file, long lastModified) throws IOException 
    {
        try (InputStream stream = Files.newInputStream(file)) 
        {
            // Create lucene Document
            Document doc = new Document();
             
            doc.add(new StringField("path", file.toString(), Field.Store.YES));
            doc.add(new LongPoint("modified", lastModified));
            doc.add(new TextField("contents", new String(Files.readAllBytes(file)), Store.YES));
             
            //Updates a document - first deletes the document(s) 
            //containing term and then adds the new document. 
            writer.updateDocument(new Term("path", file.toString()), doc);
        }
    }
       
       
protected static void indexDocs(final IndexWriter writer, Path path) throws IOException 
    {
        //Is Directory?
        if (Files.isDirectory(path)) 
        {
            //Iterate directory
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() 
            {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException 
                {
                    try
                    {
                        //Index this file
                       indexDoc(writer, file, attrs.lastModifiedTime().toMillis());
                    } 
                    catch (IOException ioe) 
                    {
                        ioe.printStackTrace();
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } 
        else
        {
            //Index this file
           MainFunctions.indexDoc(writer, path, Files.getLastModifiedTime(path).toMillis());
        }
    }
        
        
        
  protected static IndexSearcher createSearcher() throws IOException  {
         
        Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
         
        // Interface for accessing a point-in-time view of a lucene index
        IndexReader reader = DirectoryReader.open(dir);
         
        //Index searcher
        IndexSearcher searcher = new IndexSearcher(reader);
        return searcher;
    }
    
protected static TopDocs searchInContent(String textToFind, IndexSearcher searcher) throws Exception
    {
        //Create search query
        QueryParser qp = new QueryParser("contents", new StandardAnalyzer());
        Query query = qp.parse(textToFind);
         
        //search the index
        TopDocs hits = searcher.search(query, 10);
        return hits;
    }

        // deleteEntriesFromIndexUsingTerm() not fully functional.
        // Requires debugging and testing
public static void deleteEntriesFromIndexUsingQuery() throws IOException, ParseException, Exception {

 //System.out.println("Deleting documents with field '" + term.field() + "' with text '" + term.text() + "'");
	    System.out.println("Deleting index entries with defined term" );
                
            Directory directory = FSDirectory.open( Paths.get(INDEX_DIR) );
             
            //analyzer with the default stop words
            Analyzer analyzer = new StandardAnalyzer();
                
            //IndexWriter Configuration
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
             
            // new IndexWriter object
            IndexWriter indexWriter = new IndexWriter(directory, iwc);
            
           //Create search query
            String textToFind = "ipsum";
            QueryParser qp = new QueryParser("contents", new StandardAnalyzer());
            Query query = qp.parse(textToFind);
            
            // DELETE DOCUMENT(S) FROM INDEX THAT CONTAIN QUERY
            indexWriter.deleteDocuments(query);
            indexWriter.commit();
            indexWriter.close();
            
            // VERIFY DOCUMENT(S) HAVE BEEN DELETED
            //Create lucene searcher
            IndexSearcher searcher = MainFunctions.createSearcher();
         
            //Search indexed contents using query
            TopDocs foundDocs = MainFunctions.searchInContent("ipsum", searcher);  // test
            //Total found documents
            System.out.println("Total Results :: " + foundDocs.totalHits);
         
           //prints out the path of files with the searched term
           for (ScoreDoc sd : foundDocs.scoreDocs) 
           {
               Document d = searcher.doc(sd.doc);
               System.out.println("Path : "+ d.get("path") + ", Score : " + sd.score);
           }
  
	}

}
