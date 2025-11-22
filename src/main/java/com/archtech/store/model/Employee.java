package com.archtech.store.model;

import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
// @Table(name = "Employee")
public class Employee {

    // Data fields
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, length = 150)
    private String employeeName;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = true, length = 10)
    private String dept;

    @Column(nullable = true, length = 12)
    private Double salary;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @OneToMany(mappedBy = "manager")
    @JsonIgnore // Prevents circular reference in JSON serialization
    private Set<Employee> subordinates;

    @Column(nullable = false)
    private boolean isManager;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    // Default constructor required by JPA
    public Employee() {}

    // Constructor for creating new employees
    public Employee(
        String employeeName, 
        String email, 
        String dept, 
        Double salary,
        Employee manager,
        boolean isManager
    ) {
        this.employeeName = employeeName;
        this.email = email;
        this.dept = dept;
        this.salary = salary;
        this.manager = manager;
        this.isManager = isManager;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public void setIsManager(boolean isManager) {
        this.isManager = isManager;
    }

    public Integer getNumSubordinates() {
        return (subordinates == null) ? 0 : subordinates.size();
    }

    public void setSubordinates(Set<Employee> subordinates) {
        this.subordinates = subordinates;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Long getId() {
        return this.id;
    }

    public String getEmployeeName() {
        return this.employeeName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getDept() {
        return this.dept;
    }

    public Double getSalary() {
        return this.salary;
    }

    public Employee getManager() {
        return this.manager;
    }

    public boolean getIsManager() {
        return this.isManager;
    }

    public Set<Employee> getSubordinates() {
        return this.subordinates;
    }

    public LocalDateTime getCreatedDate() {
        return this.createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return this.updatedDate;
    }
    
}