package com.terryyessfung.whatsins.Model;

public class User {
    private String email;
    private String name;
    private String password;

    public User() {
    }

    public User( String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean verifyField() {
        return !email.isEmpty() && !password.isEmpty() && !name.isEmpty();
    }

    public boolean comparePassword(String password2) {
        return password == password2;
    }
}
