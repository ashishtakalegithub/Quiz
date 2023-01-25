package com.miniproject.quiz;

//Method Calling
public class Main {
	public static void main(String[] args) {
		Quiz quiz = new Quiz();
		Student details = quiz.getUserDetails();
		quiz.attemptQuiz(details);
	}
}
