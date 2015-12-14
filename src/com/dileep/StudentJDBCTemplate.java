package com.dileep;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class StudentJDBCTemplate implements StudentDAO {
	private JdbcTemplate jdbcTemplateObject;

	@Override
	public void setDataSource(DataSource ds) {
		this.jdbcTemplateObject = new JdbcTemplate(ds);
	}

	@Override
	public void create(String name, Integer age, Integer marks, Integer year) {
		try {
			String SQL1 = "insert into STUDENT (name, age) values (?, ?)";
			jdbcTemplateObject.update(SQL1, name, age);

			String SQL2 = "select max(id) from STUDENT";
			int sid = jdbcTemplateObject.queryForObject(SQL2, Integer.class);

			String SQL3 = "insert into MARKS (sid, marks, year) values (?, ?, ?)";
			jdbcTemplateObject.update(SQL3, sid, marks, year);

			System.out.println("Created Name = " + name + ", Age = " + age);
//			throw new RuntimeException("simulate Error condition");

		} catch (DataAccessException e) {
			System.out.println("Error in creating record, rolling back");
			throw e;
		}
	}

	@Override
	public List<StudentMarks> listStudents() {
		String SQL = "select * from STUDENT, MARKS where STUDENT.id = MARKS.sid";
		List<StudentMarks> studentMarks = jdbcTemplateObject.query(SQL, new StudentMarksMapper());
		return studentMarks;
	}

}
