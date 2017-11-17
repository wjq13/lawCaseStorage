package entities;

import java.util.ArrayList;
import java.util.List;

public class CodeOfCA {

	private String currentCode;
	private String currentCauseofAction;
	private String fatherCode;
	
	private List<String> codeTree;

	public CodeOfCA(String currentCode, String currentCA, String fatherCode){
		this.currentCode = currentCode;
		this.currentCauseofAction = currentCA;
		this.fatherCode = fatherCode;
		
		this.codeTree = new ArrayList<String>();
	}

	public String getCurrentCode() {
		return currentCode;
	}

	public void setCurrentCode(String currentCode) {
		this.currentCode = currentCode;
	}

	public String getCurrentCauseofAction() {
		return currentCauseofAction;
	}

	public void setCurrentCauseofAction(String currentCauseofAction) {
		this.currentCauseofAction = currentCauseofAction;
	}

	public String getFatherCode() {
		return fatherCode;
	}

	public void setFatherCode(String fatherCode) {
		this.fatherCode = fatherCode;
	}

	public List<String> getCodeTree() {
		return codeTree;
	}

	public void setCodeTree(List<String> codeTree) {
		this.codeTree = codeTree;
	}

	public String getTreeString(){
		String tree = "";
		for(String father : this.codeTree){
			tree += father + "-";
		}
		return tree;
	}
}
