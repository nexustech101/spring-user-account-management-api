package com.archtech.store.controller;

import com.archtech.store.dto.EmployeeRequest;
import com.archtech.store.model.Employee;
import com.archtech.store.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = { "http://localhost:5501", "/**" })
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    /**
     * Get all employees
     */
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(service.getAllEmployees());
    }

    /**
     * Create a new employee
     */
    @PostMapping
    public ResponseEntity<Employee> createEmployee(
            @Valid @RequestBody EmployeeRequest request) {

        Employee created = service.createEmployee(request.toEntity());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Get all managers
     */
    @GetMapping("/managers")
    public ResponseEntity<List<Employee>> getAllManagers() {
        return ResponseEntity.ok(service.getAllManagers());
    }

    /**
     * Get employee by ID
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return service.getEmployee(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get employee by email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Employee> getEmployeeByEmail(@PathVariable String email) {
        return service.getEmployee(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update employee by ID
     */
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request) {
        try {
            Employee updated = service.updateEmployee(id, request.toEntity());
            return ResponseEntity.ok(updated);

        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete an employee by ID.
     * Route is separate because behavior differs significantly.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployeeById(@PathVariable Long id) {
        try {
            service.deleteEmployeeById(id);
            return ResponseEntity.noContent().build();

        } catch (IllegalStateException e) {
            // Attempted to delete a manager using this route
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (RuntimeException e) {
            // Employee not found
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete a manager by ID (with reassignment logic).
     * Route is separate because behavior differs significantly.
     */
    @DeleteMapping("/manager/{id}")
    public ResponseEntity<?> deleteManagerById(@PathVariable Long id) {
        try {
            service.deleteManager(id);
            return ResponseEntity.noContent().build();

        } catch (RuntimeException e) {
            // Either not found, or manager not found
            return ResponseEntity.notFound().build();
        }
    }

}
