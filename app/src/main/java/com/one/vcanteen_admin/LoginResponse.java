package com.one.vcanteen_admin;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("token")
    private String token;

    @SerializedName("adminId")
    private int adminId;

    public LoginResponse() {
    }

    public LoginResponse(String status, String token, int adminId) {
        this.status = status;
        this.token = token;
        this.adminId = adminId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }
}
