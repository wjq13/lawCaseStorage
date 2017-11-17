package service;

import org.bson.Document;

import Util.CauseOfCaseOp;
import Util.Segment;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dao.MongodbHelper;
import entities.LawCase;

public class WriteToMongo {
	
	MongoCollection<Document> lawCaseCollection;
	MongoCollection<Document> paragraphCollection;
	MongoCollection<Document> segmentCollection;
	
	Segment segment;

	public WriteToMongo() {
		lawCaseCollection = MongodbHelper.getMongoDataBase().getCollection("lawcase");
		paragraphCollection = MongodbHelper.getMongoDataBase().getCollection("paragraph");
		segmentCollection = MongodbHelper.getMongoDataBase().getCollection("segment");
		
		segment = new Segment();
		segment.init();
	}

	/**
	 * 写全文库
	 * 
	 * @param lawCase
	 *            要插入的案例
	 * @return 对应全文表的_id
	 */
	public void writeFullText(LawCase lawCase) {
		Document document = new Document("text", lawCase.getFullText());
		lawCaseCollection.insertOne(document);
		lawCase.setFullTextId(document.get("_id").toString());
		// 把生成的全文表id,set进lawcase里
	}

	/**
	 * 写分段库
	 * 
	 * @param lawCase
	 */
	public void writeLawCase(LawCase lawCase) {
		Document document = new Document(LawCase.FullTextId, lawCase.getFullTextId());
		document.append(LawCase.CauseOfAction, lawCase.getCauseOfAction());
		document.append(LawCase.CodeOfCA, lawCase.getCodeOfCauseOfAction());
		document.append("codeofcatree", CauseOfCaseOp.getLevelTree(lawCase.getCodeOfCauseOfAction()));
		// 调segment中的方法获取长字段分词，调写分词库方法
		for(int i = 0; i < lawCase.getParagraphSize(); i ++){
			String paragraph = lawCase.getByPName(LawCase.pNames[i]);
			Document paraDoc = new Document("text", paragraph);
			String[] segmentations = segment.getSegmentation(paragraph);
			String segId = writeSegmentation(segmentations);
			paraDoc.append("segmentid", segId);
			
			document.append(LawCase.pNames[i], paraDoc);
		}
		paragraphCollection.insertOne(document);
	}

	// 写分词库，返回对应id
	public String writeSegmentation(String[] segmentations) {
		Document document = new Document("words", segmentations[0]);
		document.append("wordpostags", segmentations[1]);
		document.append("keywords", segmentations[2]);
		document.append("keywordpostags", segmentations[3]);
		
		segmentCollection.insertOne(document);
		return document.get("_id").toString();
	}
}
