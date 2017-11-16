package cn.edu.nju.se.lawcase.util.test;

import Util.Segment;

public class SegmentTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String testSentence = "点击eclipse菜单栏Help->Eclipse Marketplace搜索到插件Maven Integration for Eclipse 并点击安装即可，如下图";
		try {
			String[] result = Segment.processSentenceNLPIR(testSentence);
			System.out.println(result[0] + "\n" + result[1]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
