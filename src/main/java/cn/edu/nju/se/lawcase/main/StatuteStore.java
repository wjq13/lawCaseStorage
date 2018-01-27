package cn.edu.nju.se.lawcase.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.se.lawcase.entities.Statute;
import cn.edu.nju.se.lawcase.database.service.StatuteService;
import cn.edu.nju.se.lawcase.util.TxtToStatute;


public class StatuteStore {

	public static void main(String args[]){
  	   String filepath = "E://研究生//项目组//法条库1//重复法条//";// 文件夹路径
 	   File dir = new File(filepath);
		   File[] folders = dir.listFiles();
		   Statute statute = null;
		   List<Statute> statutes;
		   int processedFiles = 0;
		   List<Statute> statuteList = new ArrayList<>();
		   for(File folder:folders){
			File[] files = folder.listFiles();
			statuteList = new ArrayList<>();
			for (File file : files) {
				processedFiles++;
				//TxtToStatute.transOneTxt(file);
				
				System.out.println("-------"+file.getName().substring(0,file.getName().length()-4)+"--------");
				//TxtToStatute.transOneTxt(file);
				statute = TxtToStatute.transOneTxt(file);
				//statute = TxtToStatute2.transOneTxt2(file);
				statuteList.add(statute);
			}
			if(statuteList.size()>0){
				
				StatuteService.writeMany(statuteList);
				System.out.println(processedFiles);
			}
		}
	
	}
}
