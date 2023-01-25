package com.miniproject.quiz;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Quiz implements QuizInterf {
	Connection con = null;
	Statement st = null;
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

	// give test and save data to database
	public Student attemptQuiz(Student details) {
		getstatement();
		// iterate over all questions
		int count = 0;
		try {
			for (int i = 1; i <= 10; i++) {
				String sqlQuery = "select id,questions,option1,option2,option3,option4 from student.quebank "
						+ "where id=" + i;
				ResultSet rs = st.executeQuery(sqlQuery);
				while (rs.next()) {
					System.out.printf(rs.getInt(1) + "%n" + rs.getString(2) + "%n" + rs.getString(3) + "%n"
							+ rs.getString(4) + "%n" + rs.getString(5) + "%n" + rs.getString(6) + "%n");

				}
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
				String input = "select option" + ans + " from student.quebank where id=" + i;
				rs = st.executeQuery(input);
				String option = null;
				while (rs.next()) {

					option = rs.getString(1);
				}

				String sql = "select answer from student.quebank" + " where id=" + i;
				rs = st.executeQuery(sql);
				String check = null;
				while (rs.next()) {
					check = rs.getString(1);
				}
				if (check.equals(option)) {
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

		} catch (SQLException e) {
			e.printStackTrace();
		}
		// display marks
		displayResult(details);
		return student;

	}

	@Override

	public void displayResult(Student details) {
		System.out.println("Result of " + details.getfName() + " " + details.getlName());
		getstatement();
		String sqlQuery = "select concat(fName,'  ',lName) as 'Full Name',score,grade \r\n" + "from result\r\n"
				+ "where fName='" + details.getfName() + "' && lName='" + details.getlName() + "';";
		try {
			ResultSet result = st.executeQuery(sqlQuery);
			while (result.next()) {
				System.out.println("Name of Student::" + result.getString(1) + "\r\n" + "Marks Obtained::"
						+ result.getInt(2) + "\t" + "Grade::" + result.getString(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// to display all students based on rank
	@Override
	public void displayScoreCard() {
		String sqlQuery = "select rank() over (order by score desc) as 'Rank' ,\r\n"
				+ "concat(fName,' ',lName) as 'Student Name',score,grade\r\n"
				+ "from student.result order by score desc\r\n" + "; ";
		System.out.println("Rank\t Name Of Student\t MarksObtained\t Grade");
		try {
			ResultSet sort = st.executeQuery(sqlQuery);
			while (sort.next()) {
				System.out.println(sort.getInt(1) + "\t" + sort.getString(2) + "	\t" + sort.getInt(3) + "\t"
						+ sort.getString(4));
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void particularRecord() {

	}

}
