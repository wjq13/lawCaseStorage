package cn.edu.nju.se.lawcase.database.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import cn.edu.nju.se.lawcase.database.MongodbHelper;
import cn.edu.nju.se.lawcase.entities.LawCaseWords;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class LawCaseWordsService {

	/*
	 * 用于操作案件中相关段落词语
	 */
	
	private static MongoCollection<Document> lawcasewordsCollection = MongodbHelper.getMongoDataBase()
			.getCollection("lawcasewords");

	public static void writeLawCaseWords(LawCaseWords lawcaseWords) {
		Document lawcaseWordsDoc = new Document("lawcaseid", lawcaseWords.getLawcaseID());
		lawcaseWordsDoc.append("allwords", lawcaseWords.getAllWords());

		List<Document> wordsList = new ArrayList<Document>();
		int sumWordCount = 0;
		for (String key : lawcaseWords.getWordCountMap().keySet()) {
			Document tmpWord = new Document("word", key);
			tmpWord.append("count", lawcaseWords.getWordCountMap().get(key) + "");
			wordsList.add(tmpWord);
			sumWordCount += lawcaseWords.getWordCountMap().get(key);
		}
		lawcaseWordsDoc.append("wordslist", wordsList);
		lawcaseWordsDoc.append("sum_word_count", sumWordCount);
		lawcasewordsCollection.insertOne(lawcaseWordsDoc);
	}

	@SuppressWarnings("unchecked")
	public static LawCaseWords getByLawCaseId(String lawcaseID) {
		Document lawcaseWordsDoc = lawcasewordsCollection.find(Filters.eq("lawcaseid", lawcaseID)).first();
		LawCaseWords lawcaseWords = new LawCaseWords();
		lawcaseWords.setLawcaseID(lawcaseID);
		lawcaseWords.setAllWords(lawcaseWordsDoc.getString("allwords"));

		Map<String, Integer> wordcountMap = new HashMap<String, Integer>();
		List<Document> wordsList = (List<Document>) lawcaseWordsDoc.get("wordslist");
		for (Document wordCountDoc : wordsList) {
			wordcountMap.put(wordCountDoc.getString("word"), Integer.parseInt((String) wordCountDoc.get("count")));
		}
		lawcaseWords.setWordCountMap(wordcountMap);

		return lawcaseWords;
	}
	
	public static FindIterable<Document> findALL(){
		return lawcasewordsCollection.find();
	}
}
