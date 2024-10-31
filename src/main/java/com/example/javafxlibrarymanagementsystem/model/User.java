package com.example.javafxlibrarymanagementsystem.model;
import java.io.Serializable;

public class User implements Serializable {
    // Attributes
    private String name;
    private String username;
    private String password;
    private String birthday;
    private String phone;
    private String email;
    private double salary;

    private Boolean CanAddBooksPermission;
    private AccessLevel accessLevel;

    // Enum for Access Level
    public enum AccessLevel {
        LIBRARIAN, MANAGER, ADMINISTRATOR
    }

    // Constructors
    public User(String name, String username, String password, String birthday,
                String phone, String email, double salary, AccessLevel accessLevel, Boolean CanAddBooksPermission) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.birthday = birthday;
        this.phone = phone;
        this.email = email;
        this.salary = salary;
        this.accessLevel = accessLevel;
        this.CanAddBooksPermission= CanAddBooksPermission;
    }


    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public  void setCanAddBooksPermission(Boolean CanAddBooksPermission){
        this.CanAddBooksPermission = CanAddBooksPermission;
    }

    public  Boolean getCanAddBooksPermission(){
        return  CanAddBooksPermission;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    // Additional Methods
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", birthday='" + birthday + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", salary=" + salary +
                ", accessLevel=" + accessLevel +
                '}';
    }
}
