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

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

@WebListener
public class DBConnection implements ServletContextListener {
	private static Connection conn = null; // DB연결 객체 저장

	public DBConnection() {
	}

	public void contextDestroyed(ServletContextEvent sce) {
		Connection conn = (Connection) sce.getServletContext().getAttribute("DBconnection");
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Quartz 스케줄러 종료 추가
		try {
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			if (scheduler != null && scheduler.isStarted()) {
				scheduler.shutdown(); // 스케줄러 종료
				System.out.println("Quartz 스케줄러가 종료되었습니다.");
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public void contextInitialized(ServletContextEvent sce) {
		// Connection conn = null;
		ServletContext context = sce.getServletContext();
		Context initContext;
		DataSource dataSource = null;
		// DBCP 연결
		try {
			initContext = new InitialContext();
			// JNDI 이용을 위한 객체 생성
			Context envContext = (Context) initContext.lookup("java:comp/env");
			// lookup() 등록된 naming서비스로부터 자원을 찾고자 할 때 사용하는 메소드
			// context 객체를 통해 이름으로 Resource 획득
			dataSource = (DataSource) envContext.lookup("jdbc/SSK");
			System.out.println("Load dbcp Driver");

		} catch (NamingException e1) {
			e1.printStackTrace();
		}

		try {
			conn = dataSource.getConnection();
			if (conn != null) {
				context.setAttribute("DBconnection", conn);
				System.out.println("Succeed DB connection");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Fail DB connection");
		}
	}

	public static Connection getConnection() throws SQLException {
		// pool에 사용한 커넥션 있다면 반환, 없으면 새로운 커넥션 반환
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:comp/env");
			DataSource dataSource = (DataSource) envContext.lookup("jdbc/SSK");
			Connection conn = dataSource.getConnection();

			return conn;
		} catch (NamingException | SQLException e) {
			System.err.println("DB 연결 실패: " + e.getMessage());
			throw new SQLException("DB 연결 실패", e);
		}
	}

}
