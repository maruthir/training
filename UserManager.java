package com.mydomain.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mydomain.model.User;

public class UserManager {
	
	public User doesUserExist(String name) throws Exception{
		Class.forName("org.apache.derby.jdbc.ClientDriver");
		Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527//Users/maruthir/Documents/Training/workspace/CRUD/WebContent/WEB-INF/mydb");
		Statement statement = con.createStatement();
		ResultSet rs = statement.executeQuery("select * from users where name='"+name+"'");
		if(rs.next()){
			User u = new User();
			u.setId(rs.getInt("id"));
			u.setAge(rs.getInt("age"));
			u.setEmailId(rs.getString("email_id"));
			u.setJoinDate(rs.getDate("join_date"));
			u.setName(rs.getString("name"));
			u.setPassword(rs.getString("password"));
			u.setState(rs.getString("state"));
			return u;
		}
		return null;
	}

	public List<User> getAllUsers() throws Exception{
		Class.forName("org.apache.derby.jdbc.ClientDriver");
		Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527//Users/maruthir/Documents/Training/workspace/CRUD/WebContent/WEB-INF/mydb");
		Statement statement = con.createStatement();
		ResultSet rs = statement.executeQuery("select * from users");
		List<User> users = new ArrayList<User>();
		while(rs.next()){
			User u = new User();
			u.setId(rs.getInt("id"));
			u.setAge(rs.getInt("age"));
			u.setEmailId(rs.getString("email_id"));
			u.setJoinDate(rs.getDate("join_date"));
			u.setName(rs.getString("name"));
			u.setPassword(rs.getString("password"));
			u.setState(rs.getString("state"));
			users.add(u);
		}
		return users;
	}
	
	public User getUser(Integer id) throws Exception{
		Class.forName("org.apache.derby.jdbc.ClientDriver");
		Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527//Users/maruthir/Documents/Training/workspace/CRUD/WebContent/WEB-INF/mydb");
		Statement statement = con.createStatement();
		ResultSet rs = statement.executeQuery("select * from users where id="+id);
		while(rs.next()){
			User u = new User();
			u.setId(rs.getInt("id"));
			u.setAge(rs.getInt("age"));
			u.setEmailId(rs.getString("email_id"));
			u.setJoinDate(rs.getDate("join_date"));
			u.setName(rs.getString("name"));
			u.setPassword(rs.getString("password"));
			u.setState(rs.getString("state"));
			return u;
		}
		return null;//User not found
	}

	public void addUser(User u) throws Exception{
		Class.forName("org.apache.derby.jdbc.ClientDriver");
		Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527//Users/maruthir/Documents/Training/workspace/CRUD/WebContent/WEB-INF/mydb");
		Statement st = con.createStatement();
		st.execute("insert into users (name,age) values ('"+u.getName()+"',"+u.getAge()+")");
	}

	public void updateUser(User u) throws Exception{
		Class.forName("org.apache.derby.jdbc.ClientDriver");
		Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527//Users/maruthir/Documents/Training/workspace/CRUD/WebContent/WEB-INF/mydb");
		Statement st = con.createStatement();
		st.execute("update users set name='"+u.getName()+"', age="+u.getAge()+" where id="+u.getId());
	}

	public void deleteUser(Integer id) throws Exception{
		Class.forName("org.apache.derby.jdbc.ClientDriver");
		Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527//Users/maruthir/Documents/Training/workspace/CRUD/WebContent/WEB-INF/mydb");
		Statement st = con.createStatement();
		st.execute("delete from users where id="+id);
	}
}
