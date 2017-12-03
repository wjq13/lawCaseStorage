package cn.edu.nju.se.lawcase.database.service;

import java.util.ArrayList;
import java.util.List;

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

	public static void writeFullTextMany(List<LawCase> lawCaseList) {
		// TODO Auto-generated method stub
		List<Document> lawcaseDocList = new ArrayList<Document>();
		for(LawCase lawcase : lawCaseList){
			Document document = new Document("text", lawcase.getFullText());
			document.append("filepath", lawcase.getFilePath());
			lawcaseDocList.add(document);
		}
		lawCaseCollection.insertMany(lawcaseDocList);
		for(int caseindex = 0; caseindex < lawCaseList.size(); caseindex ++){
			lawCaseList.get(caseindex).setFullTextId(lawcaseDocList.get(caseindex).get("_id").toString());
		}
	}
}
