package model.sevice;

import java.sql.Connection;
import java.util.ArrayList;

import model.dao.UserDAO;
import model.dto.SearchUserType;
import model.dto.User;

public class UserListService {
	/**
	 * select search filter
	 * parameter - connection, userRole, searchType, keyword, startIndex, endIndex
	 * */
	public static ArrayList<User> getUserListByFilter(Connection conn, String userRole, SearchUserType searchType, String keyword, int startIndex, int length){
		ArrayList<User> userList = null;
		if(searchType==SearchUserType.LOGIN_ID) {/*미구현*/
			userList = UserDAO.getUserListByUserRoleOrderByRegistrationDateLimit(conn, "CHILD", startIndex, length);
		}else if(searchType==SearchUserType.NAME) {
			userList = searchUserListByName(conn, userRole, keyword, startIndex, length);
		}else {
			userList = getAllUserList(conn, userRole, startIndex, length);
		}
		
		return userList;
	}
	
	/*get all user list*/
	public static ArrayList<User> getAllUserList(Connection conn, String userRole, int startIndex, int length){
		return UserDAO.getUserListByUserRoleOrderByRegistrationDateLimit(conn, userRole, startIndex, length);
	}
	/*search user by name*/
	public static ArrayList<User> searchUserListByName(Connection con, String userRole, String searchName, int startIndex, int length){
		return UserDAO.getUserListLikeUserNameLimit(con, userRole, searchName, startIndex, length);
	}
	/*search user by id*/
	
	/*search user by birth*/

}
