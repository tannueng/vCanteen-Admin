package com.one.vcanteen_admin;

public class LoginAdmin {

    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public LoginAdmin() {
    }

    @Override
    public String toString() {
        return "LoginAdmin{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LoginAdmin(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
