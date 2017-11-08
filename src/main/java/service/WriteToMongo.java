package service;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dao.MongodbHelper;
import entities.LawCase;

public class WriteToMongo {
	MongodbHelper helper;
	MongoClient client;
	MongoDatabase database;
	MongoCollection<Document> lawCaseCollection;
	MongoCollection<Document> fullTextCollection;

	public WriteToMongo() {
		helper = new MongodbHelper();
		client = helper.getMongoClient();
		database = client.getDatabase("lawCase");
		lawCaseCollection = database.getCollection("lawcase");
		fullTextCollection = database.getCollection("fullText");
	}

	/**
	 * 写全文库
	 * 
	 * @param lawCase
	 *            要插入的案例
	 * @return 对应全文表的_id
	 */
	public void writeFullText(LawCase lawCase) {
		Document document = new Document("text", lawCase.getFullText());
		lawCaseCollection.insertOne(document);
		lawCase.setFullTextId(document.get("_id").toString());
		// 把生成的全文表id,set进lawcase里
	}

	/**
	 * 写分段库
	 * 
	 * @param lawCase
	 */
	public void writeLawCase(LawCase lawCase) {
		Document document = new Document("text", lawCase.getFullText());
		//

		// 调segment中的方法获取长字段分词，调写分词库方法

		//
	}

	// 写分词库，返回对应id
	public String writeSegmentation(String text) {
		return null;
	}
}
