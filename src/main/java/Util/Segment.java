package Util;

public class Segment {

	public interface CLibrary extends Library {
		CLibrary Instance = (CLibrary) Native.loadLibrary("\\SegmentationJars\\file\\NLPIR", CLibrary.class);
		// 定义并初始化接口的静态变量 这一个语句是来加载 dll 的，注意 dll 文件的路径,可以是绝对路径也可以是相对路径，只需要填写 dll 的文件名，不能加后缀。

		// 初始化函数声明
		public int NLPIR_Init(byte[] sDataPath, int encoding, byte[] sLicenceCode);

		//执行分词函数声明
		public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);

		//提取关键词函数声明
		public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit, boolean bWeightOut);

		//退出函数声明
		public void NLPIR_Exit();
	}

	public static String[] processSentence(String content) throws Exception {
		String argu = "";
		String system_charset = "utf8";
		int charset_type = 1;
		int init_flag = CLibrary.Instance.NLPIR_Init(argu.getBytes(system_charset),
				charset_type, "0".getBytes(system_charset));
		if (0 == init_flag) {
			System.out.println("初始化失败！");
			return null;
		}

		String[] wordsandkeys = new String[2];
		try {
			wordsandkeys[0] = CLibrary.Instance.NLPIR_ParagraphProcess(content, 1);
			wordsandkeys[1] = CLibrary.Instance.NLPIR_GetKeyWords(content, 15, true);

			CLibrary.Instance.NLPIR_Exit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return wordsandkeys;
	}

	//
	/**
	 * @param mouduanchangwen 某段长信息
	 * @return 生成的分词信息:
	 */
	public String[] getSegment(String originalText){
		return null;
	}
	
}
