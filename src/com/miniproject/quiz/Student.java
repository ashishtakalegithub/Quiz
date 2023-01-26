package com.miniproject.quiz;

//POJO
public class Student {
	private String fName;
	private String lName;
	private long mobileNumber;

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
