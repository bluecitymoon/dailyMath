package com.turling.service.dto;

import java.io.Serializable;

/**
 * DTO for wechat login response.
 */
public class WechatLoginResponse implements Serializable {

    private String token;
    private StudentDTO student;
    private boolean isNewUser;

    public WechatLoginResponse() {}

    public WechatLoginResponse(String token, StudentDTO student, boolean isNewUser) {
        this.token = token;
        this.student = student;
        this.isNewUser = isNewUser;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public StudentDTO getStudent() {
        return student;
    }

    public void setStudent(StudentDTO student) {
        this.student = student;
    }

    public boolean isNewUser() {
        return isNewUser;
    }

    public void setNewUser(boolean newUser) {
        isNewUser = newUser;
    }

    @Override
    public String toString() {
        return "WechatLoginResponse{" + "token='" + token + "'" + ", student=" + student + ", isNewUser=" + isNewUser + "}";
    }
}
