package service;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

import dao.MongodbHelper;
import entities.CodeOfCA;

public class CodeOfCAService {

	MongoCollection<Document> codeOfCACollection;
	
	public CodeOfCAService(){
		codeOfCACollection = MongodbHelper.getMongoDataBase().getCollection("codeofca");
	}

	public void writeEachCode(CodeOfCA code) {
		Document document = new Document("currentcode", code.getCurrentCode());
		document.append("causeofaction", code.getCurrentCauseofAction());
		document.append("tree", code.getTreeString());
		
		codeOfCACollection.insertOne(document);
	}
	
	public CodeOfCA readCodeOfCA(String currentCode){
		FindIterable<Document> find = codeOfCACollection.find(new BasicDBObject("currentcode", currentCode));
		Document codeOfCADoc = find.first();
		CodeOfCA codeOfCA = new CodeOfCA();
		codeOfCA.setCurrentCode(codeOfCADoc.getString("currentcode"));
		codeOfCA.setCodeTree(codeOfCADoc.getString("tree"));
		codeOfCA.setCurrentCauseofAction(codeOfCADoc.getString("causeofaction"));
		
		return codeOfCA;
	}
	
	public String readTreeOfCodeOfCA(String currentCode){
		FindIterable<Document> find = codeOfCACollection.find(new BasicDBObject("currentcode", currentCode));
		Document codeOfCADoc = find.first();
		
		return codeOfCADoc.getString("tree");
	}
}
