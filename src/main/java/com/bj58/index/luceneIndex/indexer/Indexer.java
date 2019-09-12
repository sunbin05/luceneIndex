package com.bj58.index.luceneIndex.indexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.SimpleFSLockFactory;
import org.apache.lucene.util.Version;

import com.bj58.index.luceneIndex.constants.LuceneConstants;

public class Indexer {
	
	public IndexWriter writer;
	
	public Indexer(String indexDirectoryPath) throws IOException{
		FSDirectory directory = FSDirectory.open(new File(indexDirectoryPath), new SimpleFSLockFactory());
		IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_45, new StandardAnalyzer(Version.LUCENE_45));
		writer = new IndexWriter(directory, conf);
	}
	
	public void close() throws IOException{
		writer.close();
	}
	
	public Document getDocument(File file) throws FileNotFoundException{
		Document doc = new Document();
		Field contents = new Field(LuceneConstants.CONTENTS, new FileReader(file));
		Field filename = new Field(LuceneConstants.FILENAME, file.getName(), Store.YES, Index.NOT_ANALYZED);
		Field filepath = new Field(LuceneConstants.FILEPATH, file.getPath(), Store.YES, Index.ANALYZED);
		doc.add(contents);
		doc.add(filename);
		doc.add(filepath);
		return doc;
	}
	
	public void indexFile(File file) throws IOException{
		Document doc = getDocument(file);
		writer.addDocument(doc);
	}

}
