package com.archtech.store.controller;

import com.archtech.store.dto.EmployeeRequest;
import com.archtech.store.dto.EmployeeResponse;
import com.archtech.store.model.Employee;
import com.archtech.store.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = { "http://localhost:5501", "/**" })
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    /**
     * Get all employees with pagination
     */
    @GetMapping
    public ResponseEntity<Page<EmployeeResponse>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EmployeeResponse> employees = service.getAllEmployeesPaged(pageable)
                .map(EmployeeResponse::new);
        return ResponseEntity.ok(employees);
    }

    /**
     * Create a new employee
     */
    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid @RequestBody EmployeeRequest request) {
        
        Employee employee = request.toEntity();
        
        // Handle manager assignment
        if (request.getManagerId() != null) {
            Employee manager = service.getEmployee(request.getManagerId())
                    .orElseThrow(() -> new RuntimeException("Manager not found"));
            employee.setManager(manager);
        }
        
        Employee created = service.createEmployee(employee);
        return new ResponseEntity<>(new EmployeeResponse(created), HttpStatus.CREATED);
    }

    /**
     * Get all managers
     */
    @GetMapping("/managers")
    public ResponseEntity<List<EmployeeResponse>> getAllManagers() {
        List<EmployeeResponse> managers = service.getAllManagers().stream()
                .map(EmployeeResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(managers);
    }

    /**
     * Get employee by ID
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id) {
        return service.getEmployee(id)
                .map(EmployeeResponse::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get employee by email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<EmployeeResponse> getEmployeeByEmail(@PathVariable String email) {
        return service.getEmployee(email)
                .map(EmployeeResponse::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update employee by ID
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request) {
        
        Employee updatedData = request.toEntity();
        
        // Handle manager assignment
        if (request.getManagerId() != null) {
            Employee manager = service.getEmployee(request.getManagerId())
                    .orElseThrow(() -> new RuntimeException("Manager not found"));
            updatedData.setManager(manager);
        }
        
        Employee updated = service.updateEmployee(id, updatedData);
        return ResponseEntity.ok(new EmployeeResponse(updated));
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

    /**
     * Search employees with filters
     */
    @GetMapping("/search")
    public ResponseEntity<List<EmployeeResponse>> searchEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String dept,
            @RequestParam(required = false) Double minSalary,
            @RequestParam(required = false) Double maxSalary,
            @RequestParam(required = false) Boolean isManager) {
        
        List<EmployeeResponse> results = service.searchEmployees(name, dept, minSalary, maxSalary, isManager)
                .stream()
                .map(EmployeeResponse::new)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(results);
    }

    /**
     * Get all employees in a department
     */
    @GetMapping("/department/{dept}")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesByDepartment(@PathVariable String dept) {
        List<EmployeeResponse> employees = service.getEmployeesByDepartment(dept)
                .stream()
                .map(EmployeeResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(employees);
    }

    /**
     * Get all departments
     */
    @GetMapping("/departments")
    public ResponseEntity<List<String>> getAllDepartments() {
        return ResponseEntity.ok(service.getAllDepartments());
    }

    /**
     * Get all subordinates for a manager
     */
    @GetMapping("/manager/{id}/subordinates")
    public ResponseEntity<List<EmployeeResponse>> getSubordinates(@PathVariable Long id) {
        List<EmployeeResponse> subordinates = service.getSubordinates(id)
                .stream()
                .map(EmployeeResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(subordinates);
    }

    /**
     * Promote an employee to manager
     */
    @PostMapping("/{id}/promote")
    public ResponseEntity<EmployeeResponse> promoteToManager(@PathVariable Long id) {
        Employee promoted = service.promoteToManager(id);
        return ResponseEntity.ok(new EmployeeResponse(promoted));
    }

    /**
     * Demote a manager to regular employee
     */
    @PostMapping("/{id}/demote")
    public ResponseEntity<EmployeeResponse> demoteFromManager(@PathVariable Long id) {
        Employee demoted = service.demoteFromManager(id);
        return ResponseEntity.ok(new EmployeeResponse(demoted));
    }

    /**
     * Transfer employee to a different manager
     */
    @PutMapping("/{id}/transfer")
    public ResponseEntity<EmployeeResponse> transferEmployee(
            @PathVariable Long id,
            @RequestParam(required = false) Long newManagerId) {
        Employee transferred = service.transferEmployee(id, newManagerId);
        return ResponseEntity.ok(new EmployeeResponse(transferred));
    }

    /**
     * Get reporting hierarchy (chain of command) for an employee
     */
    @GetMapping("/{id}/hierarchy")
    public ResponseEntity<List<EmployeeResponse>> getReportingHierarchy(@PathVariable Long id) {
        List<EmployeeResponse> hierarchy = service.getReportingHierarchy(id)
                .stream()
                .map(EmployeeResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(hierarchy);
    }

}
