package com.swcs.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by rahul on 8/8/14.
 */
public class LuceneSearchUtility {

    public final String FILES_TO_INDEX_DIRECTORY = "corpus";


    private  Directory ramDirectory;

    private  HashMap<String, Long> wordFrequencyMap = new HashMap<String, Long>();


    public boolean createIndex() throws IOException {

        ramDirectory = new RAMDirectory();
        wordFrequencyMap.clear();

        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_9);
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_9, analyzer);
        IndexWriter indexWriter = new IndexWriter(ramDirectory, indexWriterConfig);

        File dir = new File(FILES_TO_INDEX_DIRECTORY);
        File[] files = dir.listFiles();
        if(dir.isDirectory())
            files = dir.listFiles();
        else {
            files = new File[1];
            files[0] = dir;
        }


        System.out.println("total files : "+files.length);
        for (File file : files) {
            System.out.println("File : "+file.getAbsolutePath());
            Document document = new Document();
            FileReader reader = new FileReader(file);
            document.add(new TextField("contents", reader));

            indexWriter.addDocument(document);
        }

        indexWriter.close();

        /*
            populating frequency map
        */
        IndexReader indexReader = DirectoryReader.open(ramDirectory);
        Fields fields = MultiFields.getFields(indexReader);

        for (String field: fields){
        //    System.out.println("Field: "+field);
            Terms terms = fields.terms(field);
            TermsEnum termsEnum = terms.iterator(null);
            Term term;
            BytesRef text;
            while ((text = termsEnum.next())!=null){
                String termString = text.utf8ToString();
                term = new Term(field,termString);
                long termFrequency = indexReader.totalTermFreq(term);
                //System.out.println(termString+" : "+termFrequency);
                wordFrequencyMap.put(termString.toLowerCase(), termFrequency);
            }
        }

        //System.out.println("Map : "+wordFrequencyMap);

        return true;
    }

    public  long getWordFrequency(String word){
        word = word.toLowerCase();
        return (wordFrequencyMap.containsKey(word))?wordFrequencyMap.get(word):0;
    }


}
