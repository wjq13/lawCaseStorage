package Util;

import service.CodeOfCAService;

public class CauseOfCaseOp {
	
	private static CodeOfCAService ccas = new CodeOfCAService();
	/**
	 * @param codeOfCA 案由代码
	 * @return 相应案由树
	 */
	public static String getLevelTree(String codeOfCA){
		return ccas.readTreeOfCodeOfCA(codeOfCA);
	}
	
}
