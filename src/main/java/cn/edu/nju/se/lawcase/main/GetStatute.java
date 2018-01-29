package cn.edu.nju.se.lawcase.main;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCursor;

import cn.edu.nju.se.lawcase.database.service.LawReferenceService;
import cn.edu.nju.se.lawcase.database.service.ParagraphService;
import cn.edu.nju.se.lawcase.database.service.SegmentService;
import cn.edu.nju.se.lawcase.database.service.Statute2Service;

public class GetStatute {
	public static void main(String args[]) {
		Document doc = Statute2Service.getById(new ObjectId("5a408af787e59a2ae0ab2a08"));
		try {
			File file = null;
			FileWriter writer = null;
			file = new File("otherCase" + "/" + doc.get("statutename").toString() + ".txt");
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			writer = new FileWriter(file, true);
			List<Document> statutes = (List<Document>) doc.get("statute");
			for(int i =0;i<statutes.size();i++){
				writer.write(statutes.get(i).get("name").toString() + "\t");
				writer.write(statutes.get(i).get("content").toString() + "\r\n");
			}
			writer.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}
}
