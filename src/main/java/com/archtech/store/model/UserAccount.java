package com.archtech.store.model;

import java.sql.Date;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class UserAccount {

    // Data fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @Column(nullable = false, unique = true, length = 50)
    private String userName;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false)
    private String password;

    // Default constructor required by JPA
    public UserAccount() {
    }

    public UserAccount(
        String name, 
        String userName, 
        String email, 
        String password
    ) {
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public UserAccount(
        String name, 
        String userName, 
        String email, 
        String password, 
        LocalDateTime createdDate, 
        LocalDateTime updatedDate
    ) {
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setName(String fName, String lName) {
        StringBuilder name = new StringBuilder();
        name.append(fName);
        name.append(" ");
        name.append(lName);
        this.name = name.toString();
    }

    public void setName(String fName, String mName, String lName) {
        StringBuilder name = new StringBuilder();
        name.append(fName);
        name.append(" ");
        name.append(mName);
        name.append(" ");
        name.append(lName);
        this.name = name.toString();
    }

    public void setName(String fName, String mName, String lName, String suffix) {
        StringBuilder name = new StringBuilder();
        name.append(fName);
        name.append(" ");
        name.append(mName);
        name.append(" ");
        name.append(lName);
        name.append(" ");
        name.append(suffix);
        this.name = name.toString();
    }

    public void setName(String[] fullName) {
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < fullName.length - 1; i++) {
            name.append(fullName[i] + "");
        }
        name.append(fullName[fullName.length - 1]);
        this.name = name.toString();
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getName() {
        return this.name;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public LocalDateTime getCreatedDate() {
        return this.createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return this.updatedDate;
    }
}