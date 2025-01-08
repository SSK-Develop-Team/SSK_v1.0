package model.dto.export.data;

import model.dto.User;

/**
 * xlsx 추출용 DTO - user data
 * user to user data
 */
public class UserExcelDTO {
    private int id;
    private String name;
    private String loginId;
    private String email;
    private String birthStr;
    private String genderStr;

    public UserExcelDTO(User user){
        this.id = user.getUserId();
        this.name = user.getUserName();
        this.loginId = user.getUserLoginId();
        this.email = user.getUserEmail();
        this.birthStr = user.getUserBirth().toString();
        this.genderStr = user.getUserGenderKr();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthStr() {
        return birthStr;
    }

    public void setBirthStr(String birthStr) {
        this.birthStr = birthStr;
    }

    public String getGenderStr() {
        return genderStr;
    }

    public void setGenderStr(String genderStr) {
        this.genderStr = genderStr;
    }
}
