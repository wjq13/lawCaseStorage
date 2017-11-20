package cn.edu.nju.se.lawcase.main;

import cn.edu.nju.se.lawcase.database.service.TfIdfService;

public class TFIDF {

	public static void main(String args[]) {
		// MongoCollection<Document> Collection = MongodbHelper
		// .getMongoDataBase().getCollection("paragraph");
		// FindIterable<Document> docs = Collection.find();
		// for(Document doc:docs){
		// TfIdfService.writeTfAndIdf(doc);
		// }

		int filenum = 47;
		TfIdfService.writeTf_Idf(filenum);
	}
}
