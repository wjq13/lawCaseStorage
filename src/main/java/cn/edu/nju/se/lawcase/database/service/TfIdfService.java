package cn.edu.nju.se.lawcase.database.service;

import java.util.HashMap;

import org.bson.Document;
import org.bson.types.ObjectId;

import cn.edu.nju.se.lawcase.database.MongodbHelper;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

public class TfIdfService {

	private static MongoCollection<Document> tfCollection = MongodbHelper
			.getMongoDataBase().getCollection("tf");
	private static MongoCollection<Document> segmentCollection = MongodbHelper
			.getMongoDataBase().getCollection("segment");
	private static MongoCollection<Document> idfCollection = MongodbHelper
			.getMongoDataBase().getCollection("idf");
	private static MongoCollection<Document> tfidfCollection = MongodbHelper
			.getMongoDataBase().getCollection("tf_idf");
	
	
	public static String writeTfAndIdf(Document lawcase) {
		String toSplit = "";
		String lawcaseId = lawcase.get("_id").toString();
		String litigationRecord = ((Document)lawcase.get("litigationRecord")).get("segmentid").toString();
		String defendantArgued = ((Document)lawcase.get("defendantArgued")).get("segmentid").toString();
		String factFound = ((Document)lawcase.get("factFound")).get("segmentid").toString();
		
		Document doc = null;
		if(!litigationRecord.isEmpty()){
			doc =segmentCollection.find(Filters.eq("_id",new ObjectId(litigationRecord))).first();
			toSplit += doc.get("words");
		}
		
		if(!defendantArgued.isEmpty()){
			doc =segmentCollection.find(Filters.eq("_id",new ObjectId(defendantArgued))).first();
			toSplit += doc.get("words");
		}
		
		if(!factFound.isEmpty()){
			doc =segmentCollection.find(Filters.eq("_id",new ObjectId(factFound))).first();
			toSplit += doc.get("words");
		}
		
		
		if(toSplit.length()>0){
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
			int countSum = 0;
			int i=1;
			for(String wordvalue:stat.keySet()){
				Document childDocument = new Document("word",wordvalue);
				childDocument.append("count", stat.get(wordvalue));
				document.append("word"+i, childDocument);
				
				countSum+=stat.get(wordvalue);
				
				//文件数
				FindIterable<Document> ret = idfCollection.find(Filters.eq("word",wordvalue));
				MongoCursor<Document> cursor = ret.iterator();
				if(cursor.hasNext()){
					
					Document firstDocument = cursor.next();
					int newcount = (int)firstDocument.get("count")+1;
					Document update = new Document("$set",new Document("count", newcount));
					idfCollection.updateOne(Filters.eq("word",wordvalue), update);
				}
				else{
					Document newDoc  = new Document("word",wordvalue);
					newDoc.append("count", 1);
					idfCollection.insertOne(newDoc);
				}
				
				
				i++;
			}
			document.append("wordcount", --i);
			document.append("countSum", countSum);
			tfCollection.insertOne(document);
			return document.get("_id").toString();
		}
		return "";
		
	}
	
	public static void writeTf_Idf(int num){
		FindIterable<Document> docs = tfCollection.find();
		for(Document doc:docs){
			String lawcaseId = doc.get("lawcaseId").toString();
			Document tfidfDoc = new Document("lawcaseId",lawcaseId);
			int count = (int) doc.get("wordcount");
			int countSum = (int) doc.get("countSum");
			for(int i=1;i<=count;i++){
				Document wordDoc  = (Document) doc.get("word"+i);
				String wvalue = (String) wordDoc.get("word");
				int wordcount = (int) wordDoc.get("count");
				double tf = wordcount/(double)countSum;
				double idf = 0.0;
				FindIterable<Document> ret = idfCollection.find(Filters.eq("word",wvalue));
				MongoCursor<Document> cursor = ret.iterator();
				if(cursor.hasNext()){
					
					Document firstDocument = cursor.next();
					int filecount = (int)firstDocument.get("count");
					idf = Math.log10((double)num/(1+filecount));
				}
				else{
					idf = Math.log10(num/1);
				}
				Document childDoc =new Document("word",wvalue);
				childDoc.append("tfidf", tf*idf);
				tfidfDoc.append("word"+i, childDoc);
			}
			tfidfDoc.append("wordnum", count);
			tfidfCollection.insertOne(tfidfDoc);
		}
	}
	
}
