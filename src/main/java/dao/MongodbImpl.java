package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class MongodbImpl implements MongodbDao {
	public Map<String, Integer> queryByID(MongoDatabase db, String table, Object Id) throws Exception {
		MongoCollection<Document> collection = db.getCollection(table);
		BasicDBObject query = new BasicDBObject("_id", Id);
		// DBObject�ӿں�BasicDBObject���󣺱�ʾһ������ļ�¼��BasicDBObjectʵ����DBObject����key-value�����ݽṹ����������HashMap�ǻ���һ�µġ�
		FindIterable<Document> iterable = collection.find(query);

		// for (Document dd : iterable) {
		// int dudu = dd.getInteger("�Ϻ�"); // ��ȡ��Ӧ������
		// System.out.println("dudududu:"+dudu);
		// }

		Map<String, Integer> jsonStrToMap = null;
		MongoCursor<Document> cursor = iterable.iterator();
		while (cursor.hasNext()) {
			Document user = cursor.next();
			String jsonString = user.toJson();
			jsonStrToMap = JsonStrToMap.jsonStrToMap(jsonString);
		}
		System.out.println("����ID���");

		return jsonStrToMap;
	}

	/**
	 * ����һ��doc������������doc�ǿյ�ʱ�����ȫ��
	 * 
	 * @param db
	 * @param table
	 * @param doc
	 */
	public List<Map<String, Integer>> queryByDoc(MongoDatabase db, String table, BasicDBObject doc) {
		MongoCollection<Document> collection = db.getCollection(table);
		FindIterable<Document> iterable = collection.find();
		/**
		 * 1. ��ȡ������FindIterable<Document> 2. ��ȡ�α�MongoCursor<Document> 3.
		 * ͨ���α�������������ĵ�����
		 */

		List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
		MongoCursor<Document> cursor = iterable.iterator();
		while (cursor.hasNext()) {
			Document user = cursor.next();
			String jsonString = user.toJson();
			Map<String, Integer> jsonStrToMap = JsonStrToMap.jsonStrToMap(jsonString);
			list.add(jsonStrToMap);
		}
		System.out.println("����doc���");
		return list;
	}

	/**
	 * ����ȫ�������ص�����
	 * 
	 * @param db
	 * @param table
	 */
	public List<Map<String, Integer>> queryAll(MongoDatabase db, String table) {
		MongoCollection<Document> collection = db.getCollection(table);
		FindIterable<Document> iterable = collection.find();

		List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
		MongoCursor<Document> cursor = iterable.iterator();
		while (cursor.hasNext()) {
			Document user = cursor.next();
			String jsonString = user.toJson();
			Map<String, Integer> jsonStrToMap = JsonStrToMap.jsonStrToMap(jsonString);
			list.add(jsonStrToMap);
		}
		System.out.println("����ȫ�����");
		return list;
	}

	/**
	 * ����������FindIterable<Document>
	 */
	public void printFindIterable(FindIterable<Document> iterable) {
		MongoCursor<Document> cursor = iterable.iterator();
		while (cursor.hasNext()) {
			Document user = cursor.next();
			System.out.println(user.toJson());
		}
		cursor.close();
	}

	public boolean insert(MongoDatabase db, String table, Document document) {
		MongoCollection<Document> collection = db.getCollection(table);
		collection.insertOne(document);
		long count = collection.count(document);
		// System.out.println(collection.getNamespace());//weibo.area
		// System.out.println(collection.getClass());//class
		// com.mongodb.MongoCollectionImpl
		// System.out.println(collection.getDocumentClass());//class
		// org.bson.Document
		// System.out.println(collection.getWriteConcern());//WriteConcern{w=1,
		// wtimeout=0, fsync=false, j=false
		// System.out.println(collection.getWriteConcern().getW());//1

		System.out.println("count: " + count);
		if (count == 1) {
			System.out.println("�ĵ�����ɹ�");
			return true;
		} else {
			System.out.println("�ĵ�����ɹ�");
			return false;
		}

	}

	/**
	 * insert many
	 * 
	 * @param db
	 * @param table
	 * @param document
	 */
	public boolean insertMany(MongoDatabase db, String table, List<Document> documents) {

		MongoCollection<Document> collection = db.getCollection(table);
		long preCount = collection.count();
		collection.insertMany(documents);
		long nowCount = collection.count();
		System.out.println("���������: " + (nowCount - preCount));
		if ((nowCount - preCount) == documents.size()) {
			System.out.println("�ĵ��������ɹ�");
			return true;
		} else {
			System.out.println("�ĵ�������ʧ��");
			return false;
		}

	}

	@Override
	public boolean delete(MongoDatabase db, String table, BasicDBObject document) {
		MongoCollection<Document> collection = db.getCollection(table);
		DeleteResult deleteManyResult = collection.deleteMany(document);
		long deletedCount = deleteManyResult.getDeletedCount();
		System.out.println("ɾ��������: " + deletedCount);
		if (deletedCount > 0) {
			System.out.println("�ĵ�ɾ������ɹ�");
			return true;
		} else {
			System.out.println("�ĵ�ɾ�����ʧ��");
			return false;
		}
	}

	/**
	 * ɾ��һ��
	 * 
	 * @param db
	 * @param table
	 * @param document
	 */
	public boolean deleteOne(MongoDatabase db, String table, BasicDBObject document) {
		MongoCollection<Document> collection = db.getCollection(table);
		DeleteResult deleteOneResult = collection.deleteOne(document);
		long deletedCount = deleteOneResult.getDeletedCount();
		System.out.println("ɾ��������: " + deletedCount);
		if (deletedCount == 1) {
			System.out.println("�ĵ�ɾ��һ���ɹ�");
			return true;
		} else {
			System.out.println("�ĵ�ɾ��һ��ʧ��");
			return false;
		}
	}

	@Override
	public boolean update(MongoDatabase db, String table, BasicDBObject whereDoc, BasicDBObject updateDoc) {
		try {
			MongoCollection<Document> collection = db.getCollection(table);
			UpdateResult updateManyResult = collection.updateMany(whereDoc, new Document("$set", updateDoc));
			System.out.println("�ĵ����¶���ɹ�");
			return true;
		} catch (Exception e) {
			System.out.println("�ĵ�����ʧ��");
			return false;
		}
		
	}

	/**
	 * update one Data
	 * 
	 * @param db
	 * @param table
	 * @param whereDoc
	 * @param updateDoc
	 */
	public boolean updateOne(MongoDatabase db, String table, BasicDBObject whereDoc, BasicDBObject updateDoc) {
		try {
			MongoCollection<Document> collection = db.getCollection(table);
			UpdateResult updateOneResult = collection.updateOne(whereDoc, new Document("$set", updateDoc));
			System.out.println("�ĵ�����һ���ɹ�");
			System.out.println(updateOneResult.getMatchedCount());
			return true;
		} catch (Exception e) {
			System.out.println("�ĵ�����ʧ��");
			return false;
		}
		
	}
	
	public boolean updateUnset(MongoDatabase db, String table, BasicDBObject whereDoc, BasicDBObject updateDoc) {
		try {
			MongoCollection<Document> collection = db.getCollection(table);
			UpdateResult updateManyResult = collection.updateMany(whereDoc, new Document("$unset", updateDoc));
			System.out.println(updateManyResult.getMatchedCount());
			System.out.println("�ĵ����¶���ɹ�");
			return true;
		} catch (Exception e) {
			System.out.println("�ĵ�����ʧ��");
			return false;
		}
		
	}
	
	public boolean updateOneUnset(MongoDatabase db, String table, BasicDBObject whereDoc, BasicDBObject updateDoc) {
		try {
			MongoCollection<Document> collection = db.getCollection(table);
			UpdateResult updateOneResult = collection.updateOne(whereDoc, new Document("$unset", updateDoc));
			System.out.println("�ĵ�����һ���ɹ�");
			return true;
		} catch (Exception e) {
			System.out.println("�ĵ�����ʧ��");
			return false;
		}
		
	}

	/**
	 * create collection
	 * 
	 * @param db
	 * @param table
	 */
	public void createCol(MongoDatabase db, String table) {
		db.createCollection(table);
		System.out.println("���ϴ����ɹ�");
	}

	/**
	 * drop a collection
	 * 
	 * @param db
	 * @param table
	 */
	public void dropCol(MongoDatabase db, String table) {
		db.getCollection(table).drop();
		System.out.println("����ɾ���ɹ�");

	}
}
