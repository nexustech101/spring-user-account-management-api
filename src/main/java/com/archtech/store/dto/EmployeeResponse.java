package com.archtech.store.dto;

import com.archtech.store.model.Employee;
import java.time.LocalDateTime;

/**
 * Response DTO for Employee with manager summary to avoid circular references
 */
public class EmployeeResponse {
    private Long id;
    private String employeeName;
    private String email;
    private String dept;
    private Double salary;
    private boolean isManager;
    private Integer numSubordinates;
    private ManagerSummary manager;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public EmployeeResponse() {}

    public EmployeeResponse(Employee employee) {
        this.id = employee.getId();
        this.employeeName = employee.getEmployeeName();
        this.email = employee.getEmail();
        this.dept = employee.getDept();
        this.salary = employee.getSalary();
        this.isManager = employee.getIsManager();
        this.numSubordinates = employee.getNumSubordinates();
        this.manager = employee.getManager() != null ? new ManagerSummary(employee.getManager()) : null;
        this.createdDate = employee.getCreatedDate();
        this.updatedDate = employee.getUpdatedDate();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
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

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }

    public Integer getNumSubordinates() {
        return numSubordinates;
    }

    public void setNumSubordinates(Integer numSubordinates) {
        this.numSubordinates = numSubordinates;
    }

    public ManagerSummary getManager() {
        return manager;
    }

    public void setManager(ManagerSummary manager) {
        this.manager = manager;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }
}
