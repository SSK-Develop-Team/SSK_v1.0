package util.process;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;

public class UserInfoProcessor {
	public static int getUserBirthToCurrAge(Date userBirth) {
		LocalDate now = LocalDate.now();
		LocalDate userLocalBirth = userBirth.toLocalDate();
		
		Period diff = Period.between(userLocalBirth, now);
		int nowAge = diff.getYears() * 12 + diff.getMonths();
		
		return nowAge;
	}

}
