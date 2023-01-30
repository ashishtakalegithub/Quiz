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
// Point 1 : when new user selects option 2 console is empty ?? project is terminated
// Point 2 : In options if user give input as 5 then no error occurs ??
// point 3 : project gets terminated after the quiz no options to check the other options 
// points 4 : exceptions are not handled properly if user gives wrong input
// point 5 : No provision to add new questions.
// point 6 : overall good efforts to solve all the critical problems keep it up.