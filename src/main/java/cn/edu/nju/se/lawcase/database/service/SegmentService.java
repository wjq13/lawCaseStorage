package cn.edu.nju.se.lawcase.database.service;

import org.bson.Document;
import org.bson.types.ObjectId;

import cn.edu.nju.se.lawcase.database.MongodbHelper;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class SegmentService {

	private static MongoCollection<Document> segmentCollection = MongodbHelper
			.getMongoDataBase().getCollection("segment");

	public static String writeSegmentation(String[] segmentations) {
		Document document = new Document("words", segmentations[0]);
		document.append("wordpostags", segmentations[1]);
		document.append("keywords", segmentations[2]);
		document.append("keywordpostags", segmentations[3]);

		segmentCollection.insertOne(document);
		return document.get("_id").toString();
	}
	
	public static String getWordsStringByID(String segID){
		return (String) segmentCollection.find(Filters.eq("_id",new ObjectId(segID))).first().get("words");
	}
}
