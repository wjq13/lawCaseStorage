package cn.edu.nju.se.lawcase.entities;

import java.io.Serializable;




public class Chapter implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String chapternumber = "";
	private String chaptervalue = "";
	
	public String getChapternumber() {
		return chapternumber;
	}
	public void setChapternumber(String chapternumber) {
		this.chapternumber = chapternumber;
	}
	public String getChaptervalue() {
		return chaptervalue;
	}
	public void setChaptervalue(String chaptervalue) {
		this.chaptervalue = chaptervalue;
	}
	
	public final static String StatuteName = "statutename";
	public final static String SourceFile = "sourceFileName";

	
}
