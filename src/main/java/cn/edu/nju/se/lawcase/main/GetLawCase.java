package cn.edu.nju.se.lawcase.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import org.apache.hadoop.classification.InterfaceAudience.Public;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCursor;

import cn.edu.nju.se.lawcase.database.service.ParagraphService;
import cn.edu.nju.se.lawcase.database.service.SegmentService;

public class GetLawCase {
	public final static int LowTrainNum = 1000;
	public final static int LowTestNum = 500;
	public final static int HighTrainNum = 5000;
	public final static int HighTestNum = 1000;

	public static void main(String args[]) {
//		String codes[] = new String[] { "9038", "9036", "9024", "9027", "9020", "9018", "9016", "9017", "9014", "9015",
//				"9022", "9013", "9019" };
		String codes[] = new String[] { "9023", "9033","9037"};
		for(String code:codes){
			GetLawCase.getAll(code);
		}
	}

	public static void getAll(String code) {
		int count = ParagraphService.getCountOfCA(code);
		System.out.println(code+"_count:"+count);
		writeOne(code);
	}
	
	
	public static void getOne(String code) {
		int count = ParagraphService.getCountOfCA(code);
		System.out.println(code+"_count:"+count);
		int trainnum = LowTrainNum;
		int testnum = LowTestNum;
		if (count > 10000) {
			trainnum = HighTrainNum;
			testnum = HighTestNum;
		}
		if(count<1500&&count>1000){
			trainnum = 800;
			testnum = 200;
			writeOne("train", code, trainnum, 0);
			writeOne("test", code, testnum, 801);
			return;
		}
		if(count<1000){
			writeOne(code);
		}
		int skip = 0;
		Random random = new Random();
		if (count > trainnum) {
			skip = random.nextInt(count - trainnum);
		} else {
			System.out.println(code + ":数据量太小");
			return;
		}
		writeOne("train", code, trainnum, skip);
		if (count - trainnum - skip > testnum) {
			skip = random.nextInt(count - trainnum - skip - testnum);
		}
		writeOne("test", code, testnum, skip);
	}

	private static void writeOne(String code) {
		String[] pNames = new String[] { "plaintiffAlleges", "defendantArgued", "factFound", "analysisProcess" };
		try {
			AggregateIterable<Document> iterable = ParagraphService.getsDoc(code);
			File file = null;
			FileWriter writer = null;
			Document paragraphDoc = null;
			file = new File("otherCase" + "/" + code + ".txt");
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			writer = new FileWriter(file, true);
			MongoCursor<Document> cursor = iterable.iterator();
			while (cursor.hasNext()) {
				paragraphDoc = cursor.next();
				writer.write(paragraphDoc.get("fullTextId").toString() + "----");
				for (String pName : pNames) {
					String segId = ((Document) paragraphDoc.get(pName)).get("segmentid").toString();
					String words = SegmentService.getWordsStringByID(segId);
					writer.write(((Document) paragraphDoc.get(pName)).get("text").toString() + "----");
					writer.write(words + "----");
				}
				writer.write(paragraphDoc.get("causeOfAction").toString() + "----");
				writer.write(paragraphDoc.get("codeOfCauseOfAction").toString() + "\r\n");

			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}
	
	private static void writeOne(String type, String code, int num, int skip) {
		String[] pNames = new String[] { "plaintiffAlleges", "defendantArgued", "factFound", "analysisProcess" };
		try {
			AggregateIterable<Document> iterable = ParagraphService.getsDoc(code, num, skip);
			File file = null;
			FileWriter writer = null;
			Document paragraphDoc = null;
			file = new File(type + "/" + code + ".txt");
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			writer = new FileWriter(file, true);
			MongoCursor<Document> cursor = iterable.iterator();
			while (cursor.hasNext()) {
				paragraphDoc = cursor.next();
				writer.write(paragraphDoc.get("fullTextId").toString() + "----");
				for (String pName : pNames) {
					String segId = ((Document) paragraphDoc.get(pName)).get("segmentid").toString();
					String words = SegmentService.getWordsStringByID(segId);
					writer.write(((Document) paragraphDoc.get(pName)).get("text").toString() + "----");
					writer.write(words + "----");
				}
				writer.write(paragraphDoc.get("causeOfAction").toString() + "----");
				writer.write(paragraphDoc.get("codeOfCauseOfAction").toString() + "\r\n");

			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}
}
