package cn.edu.nju.se.lawcase.service;

import entities.CodeOfCA;
import service.CodeOfCAService;

public class ReadCodeTree {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CodeOfCAService ccas = new CodeOfCAService();
		
		String resultTree = ccas.readTreeOfCodeOfCA("9194");
		
		CodeOfCA cca = ccas.readCodeOfCA("9194");
		
		System.out.println(resultTree);
		System.out.println(cca.getCurrentCauseofAction());
		System.out.println(cca.getTreeString());
	}

}
