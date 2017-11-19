package cn.edu.nju.se.lawcase.main;

import org.bson.Document;

import cn.edu.nju.se.lawcase.database.MongodbHelper;
import cn.edu.nju.se.lawcase.database.service.IdfService;
import cn.edu.nju.se.lawcase.database.service.TfService;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

public class TFIDF {

	public static void main(String args[]){
//		MongoCollection<Document> Collection = MongodbHelper
//				.getMongoDataBase().getCollection("paragraph");
//		FindIterable<Document> docs = Collection.find();
//		for(Document doc:docs){
//			TfService.writeTf(doc);
//		}
		
		MongoCollection<Document> tfCollection = MongodbHelper
				.getMongoDataBase().getCollection("tf");
		FindIterable<Document> docs = tfCollection.find();
		for(Document doc:docs){
			IdfService.writeIdf(doc);
		}
	}
}
