package com.miniproject.quiz;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Quiz implements QuizInterf {
	Connection con = null;
	Student student = new Student();
	Scanner sc = new Scanner(System.in);

	public Student getUserDetails() {
		System.out.println("Enter your First Name");
		student.setfName(sc.nextLine());
		System.out.println("Enter your Last Name");
		student.setlName(sc.nextLine());
		System.out.println("Enter your Mobile Number");
		student.setMobileNumber(sc.nextLong());
		return student;
	}

	public Student attemptQuiz(Student details) {
		ConnectionTest connection = new ConnectionTest();
		con = connection.getConnectionDetails();
		try {
			int count = 0;
			Statement st = con.createStatement();
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

			String fname = student.getfName();
			String lname = student.getlName();

			String sqlQuery = "insert into result(fName,lName,score)" + " values(" + "'" + fname + "'" + "," + "'"
					+ lname + "'" + "," + count + ")";

			st.executeUpdate(sqlQuery);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return student;

	}

	@Override
	public void displayResult() {

	}

	@Override
	public void displayScoreCard() {

	}

	@Override
	public void particularRecord() {

	}

}
