package cn.edu.nju.se.lawcase.database.service;

import org.bson.Document;

import cn.edu.nju.se.lawcase.database.MongodbHelper;
import cn.edu.nju.se.lawcase.entities.LawCase;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

public class LawCaseService {

	/*
	 * 用于操作案件全文表
	 */
	
	private static MongoCollection<Document> lawCaseCollection = MongodbHelper
			.getMongoDataBase().getCollection("lawcase");

	/**
	 * 写入完整的案件文书
	 * @param lawCase
	 */
	public static void writeFullText(LawCase lawCase) {
		Document document = new Document("text", lawCase.getFullText());
		document.append("filepath", lawCase.getFilePath());
		lawCaseCollection.insertOne(document);
		lawCase.setFullTextId(document.get("_id").toString());
	}
	
	/**
	 * 读取所有的案件文书，用于初始化相关索引等内容
	 * @return
	 */
	public static FindIterable<Document> findALL(){
		return lawCaseCollection.find();
	}
}
