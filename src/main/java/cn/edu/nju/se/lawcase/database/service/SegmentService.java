package cn.edu.nju.se.lawcase.database.service;

import org.bson.Document;

import cn.edu.nju.se.lawcase.database.MongodbHelper;

import com.mongodb.client.MongoCollection;

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
}
