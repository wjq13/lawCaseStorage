package cn.edu.nju.se.lawcase.database.service;

import org.bson.Document;

import cn.edu.nju.se.lawcase.database.MongodbHelper;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.sun.tools.corba.se.idl.constExpr.Equal;

public class IdfService {
	private static MongoCollection<Document> idfCollection = MongodbHelper
			.getMongoDataBase().getCollection("idf");
	
	public static void writeIdf(Document tfDocument){
		int count = (int) tfDocument.get("wordcount");
		for(int i=1;i<=count;i++){
			Document wordDoc  = (Document) tfDocument.get("word"+i);
			String wvalue = (String) wordDoc.get("word");
			int wordcount = (int) wordDoc.get("count");
			FindIterable<Document> ret = idfCollection.find(Filters.eq("word",wvalue));
			if(ret!=null){
				Document firstDocument = ret.iterator().next();
				int newcount = (int)firstDocument.get("count")+wordcount;
				Document update = new Document("$set",new Document("count", newcount));
				idfCollection.updateOne(Filters.eq("word",wvalue), update);
			}
			else{
				Document newDoc  = new Document("word",wvalue);
				newDoc.append("count", wordcount);
				idfCollection.insertOne(newDoc);
			}
		}
	}
}
