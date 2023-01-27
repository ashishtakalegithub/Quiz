package com.miniproject.quiz;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

public class Quiz implements QuizInterf {
	Connection con = null;
	Statement st = null;
	ResultSet rs = null;
	Student student = new Student();
	Scanner sc = new Scanner(System.in);

	// to get user details
	public Student getUserDetails() {
		System.out.println("Enter your First Name");
		student.setfName(sc.nextLine());
		System.out.println("Enter your Last Name");
		student.setlName(sc.nextLine());
		System.out.println("Enter your Mobile Number");
		student.setMobileNumber(sc.nextLong());
		return student;
	}

	// get connection and statement object
	public Statement getstatement() {
		ConnectionTest connection = new ConnectionTest();
		con = connection.getConnectionDetails();

		try {
			st = con.createStatement();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return st;

	}

	// select services from the list
	public void selectService(Student details) {
		System.out.printf("To attempt quiz  ::Press 1%nTo get Result    ::Press 2%nTo get Merit List::Press 3%n");
		int service = sc.nextInt();
		switch (service) {
		case 1:
			checkEntry(details);
			break;
		case 2:
			displayResult(details);
			break;
		case 3:
			getMeritList();
			break;

		default:
			break;
		}
	}

	// restrict student to give exam only once
	public Student checkEntry(Student details) {
		getstatement();
		// check whether table is empty or not
		String SqlQueryCheck = "select exists(select 1 from student.result);";
		int empty = 0;
		try {
			rs = st.executeQuery(SqlQueryCheck);
			while (rs.next()) {
				empty = rs.getInt(1);
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		switch (empty) {
		case 0:
			attemptQuiz(details);
			break;
		// check whether Quiz attempted or not by using full name
		case 1:
			int attempted = 0;
			String sqlQueryStudentEntry = "select exists(select fname,lName from student.result where fName='"
					+ details.getfName() + "' and lName='" + details.getlName() + "');";
			try {
				rs = st.executeQuery(sqlQueryStudentEntry);
				while (rs.next()) {
					attempted = rs.getInt(1);
				}
				switch (attempted) {
				case 0:
					attemptQuiz(details);
					break;
				case 1:
					System.out.println("You have already attempted Quiz");
					displayResult(details);
					break;
				default:
					break;
				}
			} catch (SQLException e) {

				e.printStackTrace();
			}
			break;
		default:
			break;
		}

		return student;

	}

	// store questions with random order from database to list

	public List<String> storeQuestions() {
		getstatement();
		ArrayList<String> questions = new ArrayList<>();
		String sqlQuery = "SELECT questions,option1,option2,option3,option4 FROM quebank ORDER BY RAND();";
		try {
			rs = st.executeQuery(sqlQuery);
			while (rs.next()) {
				String que = rs.getString(1) + "\n" + rs.getString(2) + "\n" + rs.getString(3) + "\n" + rs.getString(4)
						+ "\n" + rs.getString(5) + "\n";
				questions.add(que);
			}
			student.setQuestions(questions);
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return questions;

	}

	// store questions and answers for validation purpose

	public TreeMap<String, String> storeQueAns() {
		TreeMap<String, String> queAndans = new TreeMap<String, String>();
		ArrayList<String> queSeq = new ArrayList<>();
		ArrayList<String> ansSeq = new ArrayList<>();
		String sqlQuery1 = "SELECT questions,option1,option2,option3,option4 FROM quebank ;";
		try {
			rs = st.executeQuery(sqlQuery1);
			while (rs.next()) {
				String que = rs.getString(1) + "\n" + rs.getString(2) + "\n" + rs.getString(3) + "\n" + rs.getString(4)
						+ "\n" + rs.getString(5) + "\n";
				queSeq.add(que);
			}
			student.setQueSeq(queSeq);
		} catch (SQLException e) {

			e.printStackTrace();
		}
		String sqlQuery2 = "SELECT answer FROM quebank ;";
		try {
			rs = st.executeQuery(sqlQuery2);
			while (rs.next()) {
				String ans = rs.getString(1) + "\n";
				ansSeq.add(ans);
			}
			student.setQueSeq(ansSeq);
		} catch (SQLException e) {

			e.printStackTrace();
		}

		int i = 0;
		while (i < queSeq.size()) {
			queAndans.put(queSeq.get(i), ansSeq.get(i));
			i++;
		}
		student.setQueAndans(queAndans);

		return queAndans;

	}

	// give test and save data to database
	public Student attemptQuiz(Student details) {
		getstatement();
		// iterate over all questions
		int count = 0;
		try {

			storeQuestions();
			storeQueAns();
			String currentQue = null;
			String currentAns = null;
			for (int i = 0; i < student.getQuestions().size(); i++) {
				System.out.println(student.getQuestions().get(i));
				currentQue = student.getQuestions().get(i);
				currentAns = student.getQueAndans().get(currentQue);

				// To restrict input to option availability
				int ans = 0;
				while (true) {
					System.out.println("Option 1:Press 1	Option 2:Press 2	Option 3:Press 3	Option 4:Press 4");
					ans = sc.nextInt();
					if (ans > 0 && ans < 5) {
						break;
					}
				}

				// check answer given by student with the right answer
				String input = "select option" + ans + " from student.quebank where answer=" + "'" + currentAns.trim()
						+ "'";
				rs = st.executeQuery(input);
				String stdAns = null;
				while (rs.next()) {

					stdAns = rs.getString(1) + "\n";
				}

				// calculate marks
				if (currentAns.equals(stdAns)) {
					count++;
				}
			}
			// calculate grade and save to database
			String fname = student.getfName();
			String lname = student.getlName();
			String grade = null;

			if (count >= 8) {
				grade = "A";
			} else if (count >= 6 && count <= 7) {
				grade = "B";
			} else if (count == 5) {
				grade = "C";
			} else {
				grade = "Fail";
			}
			String sqlQuery = "insert into result(fName,lName,score,grade)" + " values(" + "'" + fname + "'" + "," + "'"
					+ lname + "'" + "," + count + "," + "'" + grade + "'" + ")";

			st.executeUpdate(sqlQuery);

		} catch (

		SQLException e) {
			e.printStackTrace();
		}

		// display marks
		displayResult(details);
		return student;

	}

	// display result of individual
	@Override

	public void displayResult(Student details) {
		System.out.println("Result of " + details.getfName() + " " + details.getlName());
		getstatement();
		String sqlQuery = "select concat(fName,'  ',lName) as 'Full Name',score,grade \r\n" + "from result\r\n"
				+ "where fName='" + details.getfName() + "' && lName='" + details.getlName() + "';";
		try {
			rs = st.executeQuery(sqlQuery);
			while (rs.next()) {
				System.out.println("Name of Student::" + rs.getString(1) + "\r\n" + "Marks Obtained::" + rs.getInt(2)
						+ "\t" + "Grade::" + rs.getString(3));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// to display all students based on rank
	@Override
	public void getMeritList() {
		getstatement();
		String sqlQuery = "select rank() over (order by score desc) as 'Rank' ,\r\n"
				+ "concat(fName,' ',lName) as 'Student Name',score,grade\r\n"
				+ "from student.result order by score desc\r\n" + "; ";
		System.out.println("Rank\t Name Of Student\t MarksObtained\t Grade");
		try {
			rs = st.executeQuery(sqlQuery);
			while (rs.next()) {
				System.out.printf(rs.getInt(1) + "\t " + rs.getString(2) + "\t\t" + rs.getInt(3) + "\t\t "
						+ rs.getString(4) + "%n");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	// to close resources
	public void closeResource() {
		try {
			st.close();
			con.close();
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

}
