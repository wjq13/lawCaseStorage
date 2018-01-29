package cn.edu.nju.se.lawcase.main;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import cn.edu.nju.se.lawcase.database.service.LawCaseWordsService;
import cn.edu.nju.se.lawcase.database.service.ParagraphService;
import cn.edu.nju.se.lawcase.database.service.Segment2Service;
import cn.edu.nju.se.lawcase.database.service.SegmentService;
import cn.edu.nju.se.lawcase.database.service.SingleWordService;
import cn.edu.nju.se.lawcase.entities.LawCase;
import cn.edu.nju.se.lawcase.entities.LawCaseWords;
import cn.edu.nju.se.lawcase.entities.SingleWord;

import com.mongodb.client.FindIterable;

public class WordFrequency {

	
	static Map<String,SingleWord> wordCount = new HashMap<String, SingleWord>();
	public static void main(String args[]){
		
		FindIterable<Document> lawcaseParagraphs = ParagraphService.findALL();
		System.out.println("findall ok");
		int count = 0,correctNum= 0;
		for(Document lawcaseParagraph : lawcaseParagraphs){
			LawCaseWords lawcaseWords = generateLawCaseTF(lawcaseParagraph);
			//wx的分词没有分案件基本情况，有可能三段都为空，
			count++;
			if(count%10000==0){
				System.out.println("count:"+count+"----- findNum:"+correctNum);
			}
			if(lawcaseWords.getWordCountMap().size()<1){
				continue;
			}
			correctNum++;
			LawCaseWordsService.writeLawCaseWords(lawcaseWords);
			
			for(String word : lawcaseWords.getWordCountMap().keySet()){
				SingleWord singleWord = new SingleWord();
				singleWord.setWord(word);
				singleWord.setCurrentDocID(lawcaseWords.getLawcaseID());
				singleWord.setCurrentDocCount(lawcaseWords.getWordCountMap().get(word));
				if(wordCount.get(word)!=null){
					singleWord.setDocCount(1+wordCount.get(word).getDocCount());
					wordCount.put(word, singleWord);
				}else{
					singleWord.setDocCount(1);
					wordCount.put(word, singleWord);
				}
				
//				SingleWord singleWord = new SingleWord();
//				singleWord.setWord(word);
//				singleWord.setCurrentDocID(lawcaseWords.getLawcaseID());
//				singleWord.setCurrentDocCount(lawcaseWords.getWordCountMap().get(word));
//				
//				singleWord.setDocCount(1);
//				
//				SingleWordService.updateSingleWord(singleWord);
			}
			
		}
		SingleWordService.updateSingleWords(wordCount);
	}

	public static LawCaseWords generateLawCaseTF(Document lawcaseParagraph) {
		// TODO Auto-generated method stub
		LawCaseWords lawcaseWords = new LawCaseWords();
		
		String allWords = "";
		String lawcaseId = lawcaseParagraph.get("_id").toString();
		lawcaseWords.setLawcaseID(lawcaseId);
		
		
//		//根据NLPIR分词记词频
//		for(String paragraphName : LawCase.pNamesTF){
//			if(!(lawcaseParagraph.get(paragraphName).toString().isEmpty())){
//				String segID = ((Document)lawcaseParagraph.get(paragraphName)).get("segmentid").toString();
//				allWords += SegmentService.getWordsStringByID(segID);
//			}
//		}
		//jieba分词记词频
		for(String paragraphName : LawCase.pNamesJieBaTF){
			if(!(lawcaseParagraph.get(paragraphName).toString().isEmpty())){
				String segID = ((Document)lawcaseParagraph.get(paragraphName)).get("segmentid").toString();
				allWords += Segment2Service.getWordsStringByID(segID);
			}
		}
		lawcaseWords.setAllWords(allWords);
		lawcaseWords.generateWordCountMap();
		
		return lawcaseWords;
	}
	
}
