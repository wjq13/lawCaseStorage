package cn.edu.nju.se.lawcase.database.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import cn.edu.nju.se.lawcase.database.MongodbHelper;
import cn.edu.nju.se.lawcase.entities.LawCaseWordsTfIdf;

import com.mongodb.client.MongoCollection;

public class TfIdfService {
	
	private static MongoCollection<Document> tfidfCollection = MongodbHelper
			.getMongoDataBase().getCollection("tf_idf");

	public static void InsertOne(LawCaseWordsTfIdf tfIdf) {
		Document tfidfDoc = new Document("lawcaseid",tfIdf.getLawcaseID());
		List<Document> wordsList = new ArrayList<Document>();
		for (String key : tfIdf.getWordTfIdfMap().keySet()) {
			Document tmpWord = new Document("word", key);
			tmpWord.append("tfidf", tfIdf.getWordTfIdfMap().get(key));
			wordsList.add(tmpWord);
		}
		tfidfDoc.append("wordslist", wordsList);
		tfidfCollection.insertOne(tfidfDoc);
	}
	
}
