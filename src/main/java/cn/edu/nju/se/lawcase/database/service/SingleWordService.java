package cn.edu.nju.se.lawcase.database.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import cn.edu.nju.se.lawcase.database.MongodbHelper;
import cn.edu.nju.se.lawcase.entities.SingleWord;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public class SingleWordService {

	private static MongoCollection<Document> singlewordCollection = MongodbHelper
			.getMongoDataBase().getCollection("singleword");

	public static void writeSingleWord(SingleWord singleWord) {
		Document singlewordDoc = new Document("word", singleWord.getWord());
		singlewordDoc.append("doccount", singleWord.getDocCount() + "");

		List<Document> docWithCounts = new ArrayList<Document>();
		Document docWithCount = new Document();
		docWithCount.append("docid", singleWord.getCurrentDocID());
		docWithCount.append("count", singleWord.getCurrentDocCount() + "");
		docWithCounts.add(docWithCount);

		singlewordDoc.append("docwithcount", docWithCounts);

		singlewordCollection.insertOne(singlewordDoc);
	}

	public static void updateSingleWord(SingleWord singleWord) {
		Document existWordDoc = singlewordCollection.find(
				Filters.eq("word", singleWord.getWord())).first();
		if (existWordDoc == null) {
			SingleWordService.writeSingleWord(singleWord);
		} else {
			int docCount = Integer.parseInt((String) existWordDoc.get("doccount")) + 1;
			Document docWithCount = new Document();
			docWithCount.append("docid", singleWord.getCurrentDocID());
			docWithCount.append("count", singleWord.getCurrentDocCount() + "");

			singlewordCollection.updateOne(
					Filters.eq("word", singleWord.getWord()),
					Updates.combine(Updates.set("doccount", docCount + ""),
							Updates.addToSet("docwithcount", docWithCount)));
		}
	}

	@SuppressWarnings("unchecked")
	public static SingleWord findByWord(String word) {
		Document existWordDoc = singlewordCollection.find(
				Filters.eq("word", word)).first();
		if (existWordDoc == null) {
			return null;
		} else {
			SingleWord singleWord = new SingleWord();
			singleWord.setWord(word);
			singleWord.setDocCount(Integer.parseInt((String) existWordDoc
					.get("doccount")));

			Map<String, Integer> docWithCounts = new HashMap<String, Integer>();
			List<Document> docWithCountDocs = (List<Document>) existWordDoc
					.get("docwithcount");
			for (Document docWithCountDoc : docWithCountDocs) {
				docWithCounts.put(docWithCountDoc.getString("docid"),
						Integer.parseInt((String) docWithCountDoc.get("count")));
			}
			singleWord.setDocWithCount(docWithCounts);
			
			return singleWord;
		}
	}
}
