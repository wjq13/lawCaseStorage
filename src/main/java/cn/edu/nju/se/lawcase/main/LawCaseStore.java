package cn.edu.nju.se.lawcase.main;


import java.io.File;
import java.util.List;

import cn.edu.nju.se.lawcase.database.service.LawCaseService;
import cn.edu.nju.se.lawcase.database.service.ParagraphService;
import cn.edu.nju.se.lawcase.entities.LawCase;
import cn.edu.nju.se.lawcase.service.InitializeCodeOfCA;
import cn.edu.nju.se.lawcase.util.Segment;
import cn.edu.nju.se.lawcase.util.XmlToLawCase;


public class LawCaseStore {
	public static void main(String args[]){
		//用于初始化案由代码表
		//InitializeCodeOfCA.main(args);
		
		String filepath = "E://研究生//项目组//huayu//test";//文件夹路径
		
		List<File> files = XmlToLawCase.getFileList(filepath);
		Segment.init();
		
		for(File file:files){
			LawCase lawCase = XmlToLawCase.transOneXml(file);
			lawCase.setParagraphs();
			
			LawCaseService.writeFullText(lawCase);
			ParagraphService.writeLawCase(lawCase);
		}
	}
	
}
