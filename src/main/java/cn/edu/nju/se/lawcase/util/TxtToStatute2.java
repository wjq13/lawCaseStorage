package cn.edu.nju.se.lawcase.util;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.fs.FileUtil;

import cn.edu.nju.se.lawcase.entities.Chapter;
import cn.edu.nju.se.lawcase.entities.Statute;

public class TxtToStatute2 {

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
	
	
	public static Statute transOneTxt2(File file) {
		Statute statute =new Statute();
		statute.setStatuteName(file.getName().substring(0,file.getName().length()-4));
//		System.out.println("--------"+file.getName());
		StringBuilder result;
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            List<Chapter> chapters = new ArrayList<Chapter>();
            while((s = br.readLine())!=null){

                //result.append(System.lineSeparator()+s);
            	
            	
                while(s!=null&&s.length()>1&&((s.charAt(0)=='一')||(s.charAt(0)=='二')||
	                      (s.charAt(0)=='三')||(s.charAt(0)=='四')||(s.charAt(0)=='五')||
	                      (s.charAt(0)=='六')||(s.charAt(0)=='七')||(s.charAt(0)=='八')||
	                      (s.charAt(0)=='九')||(s.charAt(0)=='十'))){
                	Chapter tmpChapter = new Chapter();
                    result = new StringBuilder();
                    int i =s.indexOf("、");
                    if(i>0&&i<8){
                    tmpChapter.setChapternumber(s.substring(0,i));
                    result.append(s.substring(i+1,s.length()));
                    System.out.println(s.substring(0,i));
                    }
                    else break;
                	
                	System.out.println("-----------");
                    while((s=br.readLine())!=null&&s.length()>1){
                    	int j =s.indexOf("、");
                    	if(j<0){
                    		result.append(" "+s);
                    	}
                    	else
                    	{
                    		if(j>0&&s.charAt(j-1)!='三'&&s.charAt(j-1)!='一'&&s.charAt(j-1)!='二'
                      		  &&s.charAt(j-1)!='四'&&s.charAt(j-1)!='五'&&s.charAt(j-1)!='六'
                      		  &&s.charAt(j-1)!='七'&&s.charAt(j-1)!='八'&&s.charAt(j-1)!='九'
                      		  &&s.charAt(j-1)!='十'&&s.charAt(j-1)!='百'&&s.charAt(j-1)!='千')
                    			result.append(" "+s);
                    		else{
                    			break;
                      			}
                    	}
                    	
                    		
    
                    }
                    tmpChapter.setChaptervalue(result.toString());
                    chapters.add(tmpChapter);
                    System.out.println(result.toString());

                    File file2 = new File("E:\\研究生\\项目组\\法条库1\\已处理法条\\法条库第二批\\"+file.getName());
                    FileUtils.copyFile(file, file2);
                    file.deleteOnExit(); 
                    
                    
                    
                    
                    
//                    try {  
//                        //创建文件2  
//                        file2.createNewFile();  
//                    } catch (IOException e) {  
//                        e.printStackTrace();  
//                    }  
                  //  cutFile(file, file2);
                	}
                
            }
            statute.setChapters(chapters);
            br.close();    
        }catch(Exception e){
            e.printStackTrace();
        }
        return statute;
		
	}
	
    public static void cutFile(File file, File file2){  
        FileOutputStream fileOutputStream = null;  
        InputStream inputStream = null;  
        byte[] bytes = new byte[1024];  
        int temp = 0;  
        try {  
            inputStream = new FileInputStream(file);  
            fileOutputStream = new FileOutputStream(file2);  
            while((temp = inputStream.read(bytes)) != -1){  
                fileOutputStream.write(bytes, 0, temp);  
                fileOutputStream.flush();  
            }  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        }catch (IOException e) {  
            e.printStackTrace();  
        }finally{  
            if (inputStream != null) {  
                try {  
                    inputStream.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
            if (fileOutputStream != null) {  
                try {  
                    fileOutputStream.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
          
    }  
}

