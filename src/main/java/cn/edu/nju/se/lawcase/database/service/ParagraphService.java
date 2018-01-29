package cn.edu.nju.se.lawcase.database.service;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bson.Document;

import cn.edu.nju.se.lawcase.database.MongodbHelper;
import cn.edu.nju.se.lawcase.entities.LawCase;
import cn.edu.nju.se.lawcase.util.Segment;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class ParagraphService {

	private static MongoCollection<Document> paragraphCollection = MongodbHelper.getMongoDataBase()
			.getCollection("paragraph");

	public static void writeLawCase(LawCase lawCase) {
		paragraphCollection.insertOne(parseLawCase(lawCase));
	}

	public static FindIterable<Document> findALL() {
		return paragraphCollection.find();
	}

	public static Document findOneForTest() {
		return paragraphCollection.find().first();
	}

	public static int countAll() {

		return (int) paragraphCollection.count();

	}

	public static void writeLawCaseMany(List<LawCase> lawCaseList) {
		List<Document> docList = new ArrayList<>();
		List<Document> segmentDocList = new ArrayList<>();
		List<Document> segmentDocListNotNULL = new ArrayList<>();

		for (LawCase lawCase : lawCaseList) {
			for (int i = 0; i < lawCase.getParagraphSize(); i++) {
				String paragraph = lawCase.getByPName(LawCase.pNames[i]);
				if (!paragraph.isEmpty()) {
					String[] segmentations = Segment.getSegmentation(paragraph);
					Document document = new Document("words", segmentations[0]);
					document.append("wordpostags", segmentations[1]);
					document.append("keywords", segmentations[2]);
					document.append("keywordpostags", segmentations[3]);
					segmentDocList.add(document);
					segmentDocListNotNULL.add(document);
				} else {
					segmentDocList.add(null);
				}
			}
		}

		segmentDocListNotNULL = SegmentService.insertMany(segmentDocListNotNULL);

		int segmentDocStartIndex = 0, segmentDocStartIndexNotNULL = 0;
		for (LawCase lawCase : lawCaseList) {
			List<Document> singleParaSegmentDocList = new ArrayList<Document>();
			for (int segmentIndex = 0; segmentIndex < lawCase.getParagraphSize(); segmentIndex++) {
				if (segmentDocList.get(segmentDocStartIndex + segmentIndex) != null) {
					singleParaSegmentDocList.add(segmentDocListNotNULL.get(segmentDocStartIndexNotNULL));
					segmentDocStartIndexNotNULL++;
				} else {
					singleParaSegmentDocList.add(null);
				}
			}
			segmentDocStartIndex += lawCase.getParagraphSize();

			Document document = new Document(LawCase.FullTextId, lawCase.getFullTextId());
			document.append(LawCase.CauseOfAction, lawCase.getCauseOfAction());
			document.append(LawCase.CodeOfCA, lawCase.getCodeOfCauseOfAction());
			if (lawCase.getCodeOfCauseOfAction() == "") {
				document.append("codeofcatree", "");
			} else {
				document.append("codeofcatree", CodeOfCAService.readTreeOfCodeOfCA(lawCase.getCodeOfCauseOfAction()));
			}

			for (int i = 0; i < lawCase.getParagraphSize(); i++) {
				String paragraph = lawCase.getByPName(LawCase.pNames[i]);
				if (paragraph.isEmpty()) {
					document.append(LawCase.pNames[i], "");
				} else {
					Document paraTextDoc = new Document("text", paragraph);
					paraTextDoc.append("segmentid", singleParaSegmentDocList.get(i).get("_id").toString());
					document.append(LawCase.pNames[i], paraTextDoc);
				}
			}
			singleParaSegmentDocList = null;
			document.append("title", lawCase.getTitle());

			docList.add(document);
		}
		paragraphCollection.insertMany(docList);

		docList = null;
		segmentDocList = null;
		segmentDocListNotNULL = null;
	}

	public static Document parseLawCase(LawCase lawCase) {
		Document document = new Document(LawCase.FullTextId, lawCase.getFullTextId());
		document.append(LawCase.CauseOfAction, lawCase.getCauseOfAction());
		document.append(LawCase.CodeOfCA, lawCase.getCodeOfCauseOfAction());
		if (lawCase.getCodeOfCauseOfAction() == "") {
			document.append("codeofcatree", "");
		} else {
			document.append("codeofcatree", CodeOfCAService.readTreeOfCodeOfCA(lawCase.getCodeOfCauseOfAction()));
		}

		List<String> paragraphs = new ArrayList<>();
		// 调segment中的方法获取长字段分词，调写分词库方法
		for (int i = 0; i < lawCase.getParagraphSize(); i++) {
			String paragraph = lawCase.getByPName(LawCase.pNames[i]);
			if (!paragraph.isEmpty()) {
				paragraphs.add(paragraph);
			}
		}

		List<Document> paraList = SegmentService.writeSegmentations(paragraphs);
		int j = 0;
		for (int i = 0; i < lawCase.getParagraphSize(); i++) {
			String paragraph = lawCase.getByPName(LawCase.pNames[i]);
			if (paragraph.isEmpty()) {
				document.append(LawCase.pNames[i], "");
			} else {
				document.append(LawCase.pNames[i], paraList.get(j));
				j++;
			}
		}
		document.append("title", lawCase.getTitle());
		return document;
	}

	public static int getCountOfCA(String code) {
		long countAll = paragraphCollection.count(and(eq("codeOfCauseOfAction", code), ne("analysisProcess", ""),
				ne("plaintiffAlleges", ""), ne("defendantArgued", ""), ne("factFound", "")));
		return (int) countAll;
	}

	public static AggregateIterable<Document> getsDoc(String code, int num, int skip) {
		AggregateIterable<Document> iterable = paragraphCollection
				.aggregate(
						Arrays.asList(
								match(and(eq("codeOfCauseOfAction", code), ne("analysisProcess", ""),
										ne("plaintiffAlleges", ""), ne("defendantArgued", ""), ne("factFound", ""))),
								skip(skip), limit(num),
								project(fields(
										include("fullTextId", "plaintiffAlleges", "defendantArgued", "factFound",
												"analysisProcess", "codeOfCauseOfAction", "causeOfAction"),
										excludeId()))));
		return iterable;
	}

	public static AggregateIterable<Document> getsDoc(String code) {
		AggregateIterable<Document> iterable = paragraphCollection
				.aggregate(
						Arrays.asList(
								match(and(eq("codeOfCauseOfAction", code), ne("analysisProcess", ""),
										ne("plaintiffAlleges", ""), ne("defendantArgued", ""), ne("factFound", ""))),
								project(fields(
										include("fullTextId", "plaintiffAlleges", "defendantArgued", "factFound",
												"analysisProcess", "codeOfCauseOfAction", "causeOfAction"),
										excludeId()))));
		return iterable;
	}
}
