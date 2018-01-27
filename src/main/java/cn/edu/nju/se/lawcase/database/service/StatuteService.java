package cn.edu.nju.se.lawcase.database.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

import cn.edu.nju.se.lawcase.database.MongodbHelper;
import cn.edu.nju.se.lawcase.entities.Chapter;
import cn.edu.nju.se.lawcase.entities.Statute;

public class StatuteService {

	private static MongoCollection<Document> statuteCollection = MongodbHelper
			.getMongoDataBase().getCollection("statute");
	
	public static void writeMany(List<Statute> statuteList) {
		for(Statute statute:statuteList){
			Document document = new Document("statutename",statute.getStatuteName());
			List<Chapter> cpList = statute.getChapters();
			List<Document> cpdoclist = new ArrayList<Document>();
			for(Chapter cp:cpList){
				Document cpdoc = new Document("name",cp.getChapternumber());
				cpdoc.append("content", cp.getChaptervalue());
				cpdoclist.add(cpdoc);
			}
			document.append("statute", cpdoclist);
			statuteCollection.insertOne(document);
		}
	}

}
