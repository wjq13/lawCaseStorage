package service;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import dao.MongodbHelper;
import entities.CodeOfCA;
import entities.LawCase;

public class CodeOfCAService {

	MongodbHelper helper;
	MongoClient client;
	MongoDatabase database;
	MongoCollection<Document> codeOfCACollection;
	
	public CodeOfCAService(){
		helper = new MongodbHelper();
		client = helper.getMongoClient();
		database = client.getDatabase("lawCase");
		codeOfCACollection = database.getCollection("codeofca");
	}

	public void writeEachCode(CodeOfCA code) {
		Document document = new Document("currentcode", code.getCurrentCode());
		document.append("causeofaction", code.getCurrentCauseofAction());
		document.append("tree", code.getTreeString());
		
		codeOfCACollection.insertOne(document);
	}
}
