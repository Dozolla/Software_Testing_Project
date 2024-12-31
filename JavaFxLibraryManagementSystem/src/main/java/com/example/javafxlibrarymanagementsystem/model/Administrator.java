package com.example.javafxlibrarymanagementsystem.model;
import java.util.ArrayList;
import java.util.List;

public class Administrator extends User {
    // Additional attributes specific to Administrator
    private List<User> employees;

    // Constructor
    public Administrator(String name, String username, String password, String birthday,
                         String phone, String email, double salary,Boolean CanAddBooksPermission) {
        super(name, username, password, birthday, phone, email, salary, AccessLevel.ADMINISTRATOR, CanAddBooksPermission);
        this.employees = new ArrayList<>();
    }

    // Getters and Setters
    public List<User> getEmployees() {
        return employees;
    }

    public void setEmployees(List<User> employees) {
        this.employees = employees;
    }

    // Additional Methods
    public void registerEmployee(User employee) {
        employees.add(employee);
    }

    public void modifyEmployee(User employee, String newName, String newUsername, String newPassword,
                               String newBirthday, String newPhone, String newEmail, double newSalary) {
        employee.setName(newName);
        employee.setUsername(newUsername);
        employee.setPassword(newPassword);
        employee.setBirthday(newBirthday);
        employee.setPhone(newPhone);
        employee.setEmail(newEmail);
        employee.setSalary(newSalary);
    }

    public void deleteEmployee(User employee) {
        employees.remove(employee);
    }

    @Override
    public String toString() {
        return "Administrator{" +
                "name='" + getName() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", birthday='" + getBirthday() + '\'' +
                ", phone='" + getPhone() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", salary=" + getSalary() +
                ", accessLevel=" + getAccessLevel() +
                ", employees=" + employees +
                '}';
    }
}
