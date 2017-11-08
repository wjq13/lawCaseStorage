package Main;


import java.io.File;
import java.util.List;

import Util.XmlToLawCase;
import entities.LawCase;
import service.WriteToMongo;

public class LawCaseStore {
	public static void main(String args[]){
		String filepath = "";//文件夹路径
		XmlToLawCase xmlToLawCase = new XmlToLawCase();
		WriteToMongo writeToMongo = new WriteToMongo();
		List<File> files = xmlToLawCase.getFileList(filepath);
		for(File file:files){
			LawCase lawCase = xmlToLawCase.transOneXml(file);
			writeToMongo.writeFullText(lawCase);
			writeToMongo.writeLawCase(lawCase);
		}
	}
	
}
