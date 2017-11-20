package cn.edu.nju.se.lawcase.database.service;

import org.bson.Document;

import cn.edu.nju.se.lawcase.database.MongodbHelper;
import cn.edu.nju.se.lawcase.entities.LawCase;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

public class LawCaseService {

	private static MongoCollection<Document> lawCaseCollection = MongodbHelper
			.getMongoDataBase().getCollection("lawcase");

	public static void writeFullText(LawCase lawCase) {
		Document document = new Document("text", lawCase.getFullText());
		lawCaseCollection.insertOne(document);
		lawCase.setFullTextId(document.get("_id").toString());
	}
	
	public static FindIterable<Document> findALL(){
		return lawCaseCollection.find();
	}
}
