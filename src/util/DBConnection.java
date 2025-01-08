package util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

@WebListener
public class DBConnection implements ServletContextListener {

    public DBConnection() {
    }

    public void contextDestroyed(ServletContextEvent sce)  { 
    	Connection conn = (Connection) sce.getServletContext().getAttribute("DBconnection");
    	try {
    		conn.close();
    	} 
    	catch (SQLException e) {
    		e.printStackTrace();
    	}
    }

    public void contextInitialized(ServletContextEvent sce)  { 
    	Connection conn = null;
    	/*
    	Properties connectionProps = new Properties();
    	ServletContext sc = sce.getServletContext();
    	
    	String DBUrl = sc.getInitParameter("JDBCUrl");
    	String DBuser = sc.getInitParameter("DBuser");
    	String DBpasswd = sc.getInitParameter("DBpasswd");
    	String DBTimeZone = sc.getInitParameter("DBTimeZone");
    	
    	connectionProps.put("user", DBuser);
    	connectionProps.put("password", DBpasswd);
    	connectionProps.put("serverTimezone", DBTimeZone);
    	
    	try {
	    	conn = DriverManager.getConnection(DBUrl, connectionProps);
    	}  
    	catch (SQLException e) {
    		e.printStackTrace();
    	}
    	
    	sc.setAttribute("DBconnection", conn);
    */
    	ServletContext context = sce.getServletContext();
    	Context initContext;
    	DataSource dataSource = null;
    	// DBCP 연결
    	try {
	    	initContext = new InitialContext();
	    	// JNDI 이용을 위한 객체 생성
	    	Context envContext = (Context)initContext.lookup("java:comp/env");
	    	// lookup() 등록된 naming서비스로부터 자원을 찾고자 할 때 사용하는 메소드
	    	// context 객체를 통해 이름으로 Resource 획득
	    	dataSource = (DataSource)envContext.lookup("jdbc/SSK");
	    	System.out.println("Load dbcp Driver");

    	} catch (NamingException e1) {
    		e1.printStackTrace();
    	}
    	
    	try {
    		conn = dataSource.getConnection();
    		if(conn != null) {
	    		context.setAttribute("DBconnection", conn);
	    		System.out.println("Succeed DB connection");
    		}
    		} catch (SQLException e) {
	    		e.printStackTrace();
	    		System.out.println("Fail DB connection");
    		}
    }
	
}
