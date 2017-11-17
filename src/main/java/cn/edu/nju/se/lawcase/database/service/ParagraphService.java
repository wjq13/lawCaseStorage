package cn.edu.nju.se.lawcase.database.service;

import org.bson.Document;

import cn.edu.nju.se.lawcase.database.MongodbHelper;
import cn.edu.nju.se.lawcase.entities.LawCase;
import cn.edu.nju.se.lawcase.util.Segment;

import com.mongodb.client.MongoCollection;

public class ParagraphService {

	private static MongoCollection<Document> paragraphCollection = MongodbHelper
			.getMongoDataBase().getCollection("paragraph");

	public static void writeLawCase(LawCase lawCase) {
		Document document = new Document(LawCase.FullTextId, lawCase.getFullTextId());
		document.append(LawCase.CauseOfAction, lawCase.getCauseOfAction());
		document.append(LawCase.CodeOfCA, lawCase.getCodeOfCauseOfAction());
		document.append("codeofcatree", CodeOfCAService.readTreeOfCodeOfCA(lawCase.getCodeOfCauseOfAction()));
		// 调segment中的方法获取长字段分词，调写分词库方法
		for(int i = 0; i < lawCase.getParagraphSize(); i ++){
			String paragraph = lawCase.getByPName(LawCase.pNames[i]);
			Document paraDoc = new Document("text", paragraph);
			String[] segmentations = Segment.getSegmentation(paragraph);
			String segId = SegmentService.writeSegmentation(segmentations);
			paraDoc.append("segmentid", segId);
			
			document.append(LawCase.pNames[i], paraDoc);
		}
		paragraphCollection.insertOne(document);
	}
}
