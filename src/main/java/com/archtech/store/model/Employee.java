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
    private Set<Employee> subordinates;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    // Default constructor required by JPA
    public Employee() {}

    // Constructor for controller
    public Employee(
        String employeeName, 
        String email, 
        String dept, 
        Double salary,
        Employee manager
    ) {
        this.employeeName = employeeName;
        this.email = email;
        this.dept = dept;
        this.salary = salary;
        this.manager = manager;
    }

    // Constructor for modifying database
    public Employee(
        String employeeName, 
        String email, 
        String dept,
        Double salary, 
        Employee manager, 
        LocalDateTime createdDate, 
        LocalDateTime updatedDate
    ) {
        this.employeeName = employeeName;
        this.email = email;
        this.dept = dept;
        this.salary = salary;
        this.manager = manager;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
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

    public void setManager(Employee manager) {
        this.manager = manager;
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