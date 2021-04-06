/*
 * SEARCH ENGINE TEXT SEARCHING 
 * To be used as part of the main Search Engine project.
 * Lucene search class. Updated for Lucene 8.8.1
 * Adapted from original source code: 
 * Lucene - Index and Search Text Files - HowToDoInJava.com
 * https://howtodoinjava.com/lucene/lucene-index-and-search-text-files/
 * Modifying the index requires the Luke utility. 
 * Update: Luke is bundled with Lucene on version 8.8.1
 * and can be downloaded from https://lucene.apache.org/core/downloads.html
 * "Apache Lucene is a high-performance, full-featured text search engine library."
 * Lucene features  a nonSQL database which can be accessed and modified with Luke.
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
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
 
public class SEsearch {
    //directory contains the lucene indexes
    protected static final String INDEX_DIR = "c:/temp/indexedFiles"; // Protected string can be accessed by other classes in package
    
    public static void main(String[] args) throws Exception 
    {
        //Create lucene searcher. It search over a single IndexReader.
        IndexSearcher searcher = MainFunctions.createSearcher();
         
        String myString = "cottage";
        //Search indexed contents using search term
        TopDocs foundDocs = MainFunctions.searchInContent(myString, searcher);  //  search for "cottage" should return file data1.txt and  data2.txt 
           
        //Total found documents
        System.out.println("'" + myString + "' " + "Total Results :: " + foundDocs.totalHits);
         
        //prints out the path of files with the searched term
        for (ScoreDoc sd : foundDocs.scoreDocs) 
        {
            Document d = searcher.doc(sd.doc);
            System.out.println("Path : "+ d.get("path") + ", Score : " + sd.score);
        }
    }

    
}
