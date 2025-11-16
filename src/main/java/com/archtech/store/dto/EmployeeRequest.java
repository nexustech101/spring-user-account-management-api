package com.archtech.store.dto;

import com.archtech.store.model.Employee;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EmployeeRequest {

    @NotBlank
    private String name;

    @Email
    private String email;

    @NotNull
    private String dept;

    private Employee manager;

    // getters + setters

    public Employee toEntity() {
        Employee e = new Employee();
        e.setEmployeeName(name);
        e.setEmail(email);
        e.setDept(dept);
        e.setManager(manager);
        return e;
    }
}
