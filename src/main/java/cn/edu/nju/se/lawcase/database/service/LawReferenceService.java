package cn.edu.nju.se.lawcase.database.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import cn.edu.nju.se.lawcase.database.MongodbHelper;
import cn.edu.nju.se.lawcase.entities.LawCase;
import cn.edu.nju.se.lawcase.entities.LawReference;

import com.mongodb.client.MongoCollection;

public class LawReferenceService {

	private static MongoCollection<Document> lawReferenceCollection = MongodbHelper
			.getMongoDataBase().getCollection("lawreference");
	
	public static void write(String fullTextID, List<LawReference> lawReferences){
		Document document = new Document(LawCase.FullTextId, fullTextID);
		
		List<Document> lawReferencesDoc = new ArrayList<Document>();
		for(LawReference lawReference : lawReferences){
			Document lawReferenceDoc = new Document("name", lawReference.getLawName());
			lawReferenceDoc.append("levelone", lawReference.getLevelOneTiao());
			lawReferenceDoc.append("leveltwo", lawReference.getLevelTwoKuan());
			
			lawReferencesDoc.add(lawReferenceDoc);
		}
		
		document.append("references", lawReferencesDoc);
		
		lawReferenceCollection.insertOne(document);
	}
}
