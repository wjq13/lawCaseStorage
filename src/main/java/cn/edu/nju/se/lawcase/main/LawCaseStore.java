package cn.edu.nju.se.lawcase.main;


import java.io.File;
import java.util.List;

import cn.edu.nju.se.lawcase.database.service.WriteToMongo;
import cn.edu.nju.se.lawcase.entities.LawCase;
import cn.edu.nju.se.lawcase.util.XmlToLawCase;


public class LawCaseStore {
	public static void main(String args[]){
		String filepath = "E:\\17级工程硕士管理\\民事案件\\";//文件夹路径
		XmlToLawCase xmlToLawCase = new XmlToLawCase();
		WriteToMongo writeToMongo = new WriteToMongo();
		List<File> files = xmlToLawCase.getFileList(filepath);
		for(File file:files){
			LawCase lawCase = xmlToLawCase.transOneXml(file);
			lawCase.setParagraphs();
			writeToMongo.writeFullText(lawCase);
			writeToMongo.writeLawCase(lawCase);
		}
	}
	
}
