package cn.edu.nju.se.lawcase.entities;

import java.io.Serializable;
import java.util.List;

public class Statute implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String statuteId;
	private String statuteName;
	private List<Chapter> chapters;
	public String getStatuteId() {
		return statuteId;
	}
	public void setStatuteId(String statuteId) {
		this.statuteId = statuteId;
	}
	public String getStatuteName() {
		return statuteName;
	}
	public void setStatuteName(String statuteName) {
		this.statuteName = statuteName;
	}
	public List<Chapter> getChapters() {
		return chapters;
	}
	public void setChapters(List<Chapter> chapters) {
		this.chapters = chapters;
	}
}
