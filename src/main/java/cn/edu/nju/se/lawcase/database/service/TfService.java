package cn.edu.nju.se.lawcase.database.service;

import java.util.HashMap;

import org.bson.Document;
import org.bson.types.ObjectId;

import cn.edu.nju.se.lawcase.database.MongodbHelper;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class TfService {

	private static MongoCollection<Document> tfCollection = MongodbHelper
			.getMongoDataBase().getCollection("tf");
	private static MongoCollection<Document> segmentCollection = MongodbHelper
			.getMongoDataBase().getCollection("segment");
	
	
	public static String writeTf(Document lawcase) {
		String toSplit = "";
		String lawcaseId = lawcase.get("_id").toString();
		String litigationRecord = ((Document)lawcase.get("litigationRecord")).get("segmentid").toString();
		String defendantArgued = ((Document)lawcase.get("defendantArgued")).get("segmentid").toString();
		String factFound = ((Document)lawcase.get("factFound")).get("segmentid").toString();
		
		Document doc =segmentCollection.find(Filters.eq("_id",new ObjectId(litigationRecord))).first();
		toSplit += doc.get("words");
		
		doc =segmentCollection.find(Filters.eq("_id",new ObjectId(defendantArgued))).first();
		toSplit += doc.get("words");
		
		doc =segmentCollection.find(Filters.eq("_id",new ObjectId(factFound))).first();
		toSplit += doc.get("words");
		
		String[] splitted = toSplit.split(" ");
		HashMap<String, Integer> stat = new HashMap<>();
		for(int i = 0; i < splitted.length; ++ i) {
			String toMatch = splitted[i];
			int currentTotal = 0;
			for(String current : splitted)
				if(current.equals(toMatch))
					currentTotal ++;
			stat.put(toMatch, currentTotal);
		}
		for(String k : stat.keySet()) {
			int v = stat.get(k);
			System.out.println(k + "    "  + v);
		}
		
		Document document = new Document("words", toSplit);
		document.append("lawcaseId", lawcaseId);
		int i=1;
		for(String temp:stat.keySet()){
			Document childDocument = new Document("word",temp);
			childDocument.append("count", stat.get(temp));
			document.append("word"+i, childDocument);
			i++;
		}
		document.append("wordcount", --i);
		tfCollection.insertOne(document);
		return document.get("_id").toString();
	}
	
}
