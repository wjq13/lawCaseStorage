package cn.edu.nju.se.lawcase.database.service;

import org.bson.Document;

import cn.edu.nju.se.lawcase.database.MongodbHelper;
import cn.edu.nju.se.lawcase.entities.CodeOfCA;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

public class CodeOfCAService {

	private static MongoCollection<Document> codeOfCACollection = MongodbHelper
			.getMongoDataBase().getCollection("codeofca");

	public static void writeEachCode(CodeOfCA code) {
		Document document = new Document("currentcode", code.getCurrentCode());
		document.append("causeofaction", code.getCurrentCauseofAction());
		document.append("tree", code.getTreeString());
		
		codeOfCACollection.insertOne(document);
	}

	public static CodeOfCA readCodeOfCA(String currentCode) {
		FindIterable<Document> find = codeOfCACollection
				.find(new BasicDBObject("currentcode", currentCode));
		Document codeOfCADoc = find.first();
		CodeOfCA codeOfCA = new CodeOfCA();
		codeOfCA.setCurrentCode(codeOfCADoc.getString("currentcode"));
		codeOfCA.setCodeTree(codeOfCADoc.getString("tree"));
		codeOfCA.setCurrentCauseofAction(codeOfCADoc.getString("causeofaction"));

		return codeOfCA;
	}

	public static String readTreeOfCodeOfCA(String currentCode) {
		FindIterable<Document> find = codeOfCACollection
				.find(new BasicDBObject("currentcode", currentCode));
		Document codeOfCADoc = find.first();
		return codeOfCADoc.getString("tree");
	}
}
