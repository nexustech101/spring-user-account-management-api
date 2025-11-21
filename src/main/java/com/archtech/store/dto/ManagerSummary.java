package com.archtech.store.dto;

import com.archtech.store.model.Employee;

/**
 * Lightweight DTO for manager references to avoid circular dependencies
 */
public class ManagerSummary {
    private Long id;
    private String name;
    private String email;
    private String dept;

    public ManagerSummary() {}

    public ManagerSummary(Employee employee) {
        if (employee != null) {
            this.id = employee.getId();
            this.name = employee.getEmployeeName();
            this.email = employee.getEmail();
            this.dept = employee.getDept();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
