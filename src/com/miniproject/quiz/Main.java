package com.miniproject.quiz;

//Method Calling
public class Main {
	public static void main(String[] args) {
		Quiz quiz = new Quiz();
		// get user details
		Student details = quiz.getUserDetails();
		// enter into particular service
		quiz.selectService(details);
		// close the resources
		quiz.closeResource();
	}
}
