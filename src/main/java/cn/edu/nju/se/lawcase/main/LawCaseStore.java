package cn.edu.nju.se.lawcase.main;

import java.io.File;
import java.util.List;

import cn.edu.nju.se.lawcase.database.service.LawCaseService;
import cn.edu.nju.se.lawcase.database.service.LawReferenceService;
import cn.edu.nju.se.lawcase.database.service.ParagraphService;
import cn.edu.nju.se.lawcase.entities.LawCase;
import cn.edu.nju.se.lawcase.util.Segment;
import cn.edu.nju.se.lawcase.util.XmlToLawCase;

public class LawCaseStore {
	public static void main(String args[]) {
		// 用于初始化案由代码表
		// InitializeCodeOfCA.main(args);

		String filepath = "F://研一//民事一审测试集";// 文件夹路径

		List<File> files = XmlToLawCase.getFileList(filepath);
		Segment.init();

		for (File file : files) {
			LawCase lawCase = XmlToLawCase.transOneXml(file);
			lawCase.setFilePath(file.getAbsolutePath());
			lawCase.setParagraphs();

			LawCaseService.writeFullText(lawCase);
			ParagraphService.writeLawCase(lawCase);
			if (lawCase.getLawReferences().size() > 0) {
				LawReferenceService.write(lawCase.getFullTextId(),
						lawCase.getLawReferences());
			}
		}
	}

}
