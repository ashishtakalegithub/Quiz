package com.miniproject.quiz;

import java.util.ArrayList;
import java.util.TreeMap;

//POJO
public class Student {
	private String fName;
	private String lName;
	private long mobileNumber;
	private ArrayList<String> questions;
	private ArrayList<String> queSeq;
	private ArrayList<String> ansSeq;
	private TreeMap<String, String> queAndans;

	public TreeMap<String, String> getQueAndans() {
		return queAndans;
	}

	public void setQueAndans(TreeMap<String, String> queAndans) {
		this.queAndans = queAndans;
	}

	public ArrayList<String> getQueSeq() {
		return queSeq;
	}

	public void setQueSeq(ArrayList<String> queSeq) {
		this.queSeq = queSeq;
	}

	public ArrayList<String> getAnsSeq() {
		return ansSeq;
	}

	public void setAnsSeq(ArrayList<String> ansSeq) {
		this.ansSeq = ansSeq;
	}

	public ArrayList<String> getQuestions() {
		return questions;
	}

	public void setQuestions(ArrayList<String> questions) {
		this.questions = questions;
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName.toUpperCase().trim();
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName = lName.toUpperCase().trim();
	}

	public long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
}
