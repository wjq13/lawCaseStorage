package cn.edu.nju.se.lawcase.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.se.lawcase.entities.Chapter;
import cn.edu.nju.se.lawcase.entities.Statute;

public class TxtToStatute {

	public static List<File> getFileList(String strPath) {
		List<File> filelist = new ArrayList<>();
		File dir = new File(strPath);
		File[] files = dir.listFiles();
		//System.out.println(files.length);
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				String fileName = files[i].getName();
				//System.out.println(fileName);
				if (files[i].isDirectory()) {
					getFileList(files[i].getAbsolutePath());
				} else if (fileName.endsWith("txt")) {
					filelist.add(files[i]);
				} else {
					continue;
				}
			}

		}
		return filelist;
	}
	
	
	public static Statute transOneTxt(File file) {
		Statute statute =new Statute();
		statute.setStatuteName(file.getName().substring(0,file.getName().length()-4));
		//System.out.println("--------"+file.getName());
		StringBuilder result;
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            List<Chapter> chapters = new ArrayList<Chapter>();
            while((s = br.readLine())!=null){

                //result.append(System.lineSeparator()+s);
                while(s!=null&&s.endsWith("条")){
                	Chapter tmpChapter = new Chapter();
                    result = new StringBuilder();
                    tmpChapter.setChapternumber(s);
                	System.out.println(s);
                	System.out.println("-----------");
                    while((s=br.readLine())!=null){
                    	if(s.endsWith("条")) break;
                      	if(s.startsWith("返")||(s.length()>2&&s.charAt(2)=='章')) break;
                    	result.append(s);
                    }
                    tmpChapter.setChaptervalue(result.toString());
                    chapters.add(tmpChapter);
                    System.out.println(result.toString());
                    if(s!=null&&(s.startsWith("返")||(s.length()>2&&s.charAt(2)=='章')))
                		break;
                	}
                
            }
            statute.setChapters(chapters);
            br.close();    
        }catch(Exception e){
            e.printStackTrace();
        }
        return statute;
		
	}
}
