package com.archtech.store.services;

import com.archtech.store.model.*;
import com.archtech.store.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public class EmployeeService {
    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<Employee> getAllEmployees() {
        return this.repository.findAll();
    }

    public List<Employee> getAllManagers() {
        return this.repository.findByIsManagerTrue();
    }

    public Optional<Employee> getEmployee(Long id) {
        return this.repository.findById(id);
    }

    public Optional<Employee> getEmployee(String email) {
        return this.repository.findByEmail(email);
    }

    public Employee createEmployee(Employee employee) {
        return this.repository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee employee) {
        employee.setId(id);
        return this.repository.save(employee);
    }

    public void deleteById(Long id) {
        this.repository.deleteById(id);
    }
}
