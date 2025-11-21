package com.archtech.store.dto;

import com.archtech.store.model.Employee;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class EmployeeRequest {

    @NotBlank(message = "Employee name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotNull(message = "Department is required")
    private String dept;

    @PositiveOrZero(message = "Salary must be zero or positive")
    private Double salary;

    private Long managerId; // Changed from Employee to managerId for cleaner API

    private Boolean isManager = false;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public Boolean getIsManager() {
        return isManager;
    }

    public void setIsManager(Boolean isManager) {
        this.isManager = isManager;
    }

    public Employee toEntity() {
        Employee e = new Employee();
        e.setEmployeeName(name);
        e.setEmail(email);
        e.setDept(dept);
        e.setSalary(salary);
        e.setIsManager(isManager != null ? isManager : false);
        return e;
    }
}
