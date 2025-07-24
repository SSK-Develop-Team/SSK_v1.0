package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.dto.EsmAlarm;
import model.dto.User;

public class EsmAlarmDAO {
	private final static String SQLST_SELECT_ESMALARM_BY_LOGIN_ID = "select * from esm_alarm where user_login_id = ?";

	// 사용자의 Esm 알람 정보 불러오기 (Connection con, int userId)
	public static ArrayList<EsmAlarm> getEsmAlarmListByUser(Connection con, int userId) {
		try {
			PreparedStatement pstmt = con.prepareStatement("select * from esm_alarm where user_id= ?");
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();
			ArrayList<EsmAlarm> alarmList = new ArrayList<EsmAlarm>();
			while (rs.next()) {
				EsmAlarm esmTime = new EsmAlarm();
				esmTime.setAlarmId(rs.getInt(1));
				esmTime.setAlarmStartTime(rs.getTime(2));
				esmTime.setAlarmEndTime(rs.getTime(3));
				esmTime.setAlarmInterval(rs.getInt(4));
				esmTime.setAlarmStartDate(rs.getDate(5));
				esmTime.setAlarmEndDate(rs.getDate(6));
				esmTime.setUserId(rs.getInt(7));

				alarmList.add(esmTime);
			}
			return alarmList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 오늘 날짜 기준 사용자의 Esm 알람 정보 불러오기 (Connection con, int userId)
	public static ArrayList<EsmAlarm> getEsmAlarmListByCurDate(Connection con, int userId) {
		try {
			PreparedStatement pstmt = con.prepareStatement("select * from esm_alarm where user_id= ? AND CURDATE() BETWEEN start_date AND end_date;");
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();
			ArrayList<EsmAlarm> alarmList = new ArrayList<EsmAlarm>();
			while (rs.next()) {
				EsmAlarm esmTime = new EsmAlarm();
				esmTime.setAlarmId(rs.getInt(1));
				esmTime.setAlarmStartTime(rs.getTime(2));
				esmTime.setAlarmEndTime(rs.getTime(3));
				esmTime.setAlarmInterval(rs.getInt(4));
				esmTime.setAlarmStartDate(rs.getDate(5));
				esmTime.setAlarmEndDate(rs.getDate(6));
				esmTime.setUserId(rs.getInt(7));

				alarmList.add(esmTime);
			}
			return alarmList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 사용자의 ESm 알람 정보 저장하기 (Connection con, int userId)
	public static boolean insertUserAlarm(Connection con, EsmAlarm esmAlarm) {
		try {
			PreparedStatement pstmt = con
					.prepareStatement("insert esm_alarm(start_time, end_time, interval_time, start_date, end_date, user_id) values(?,?,?,?,?,?)");
			pstmt.setTime(1, esmAlarm.getAlarmStartTime());
			pstmt.setTime(2, esmAlarm.getAlarmEndTime());
			pstmt.setInt(3, esmAlarm.getAlarmInterval());
			pstmt.setDate(4,  esmAlarm.getAlarmStartDate());
			pstmt.setDate(5,  esmAlarm.getAlarmEndDate());
			pstmt.setInt(6, esmAlarm.getUserId());

			int insertCount = pstmt.executeUpdate();
			if (insertCount >= 1) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// 사용자의 ESm 알람 정보 삭제하기 (Connection con, int userId) //하나씩만 삭제하게 코드수정하기
	public static void deleteUserAlarm(Connection con, int userId) {
		try {
			PreparedStatement pstmt = con.prepareStatement("DELETE FROM esm_alarm WHERE user_id = ?");
			pstmt.setInt(1, userId);

			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 모든 알람 시간 받아오기
	public static List<EsmAlarm> getAllAlarms(Connection con) {
		List<EsmAlarm> alarmList = new ArrayList<>();
		String sql = "SELECT user_id, start_time, end_time, interval_time, start_date, end_date FROM esm_alarm";

		try (PreparedStatement pstmt = con.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				EsmAlarm alarm = new EsmAlarm();
				alarm.setUserId(rs.getInt("user_id")); // userId 설정
				alarm.setAlarmStartTime(rs.getTime("start_time"));
				alarm.setAlarmEndTime(rs.getTime("end_time"));
				alarm.setAlarmInterval(rs.getInt("interval_time"));
				alarm.setAlarmStartDate(rs.getDate("start_date"));
				alarm.setAlarmEndDate(rs.getDate("end_date"));
				alarmList.add(alarm);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return alarmList;
	}

	// 현재 시간에 해당하는 사용자 조회하기
	public static List<Integer> getMatchingUsers(Connection con, String alarmTime) throws SQLException {
		String sql = "SELECT user_id " +
				"FROM esm_alarm " +
				"WHERE TIME(?) BETWEEN start_time AND end_time " +
				"  AND MOD(TIMESTAMPDIFF(MINUTE, start_time, TIME(?)) / 60, interval_time) = 0";
		List<Integer> userIds = new ArrayList<>();

		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setString(1, alarmTime);
			pstmt.setString(2, alarmTime);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					userIds.add(rs.getInt("user_id"));
				}
			}
		}

		return userIds;
	}

}
