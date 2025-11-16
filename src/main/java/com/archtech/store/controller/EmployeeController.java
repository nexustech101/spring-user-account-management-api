package com.archtech.store.controller;

import com.archtech.store.model.*;
import com.archtech.store.services.*;
import com.archtech.store.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin(origins = {"http://localhost:5501", "/**"})
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping("/employees") // Get all employees
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(this.service.getAllEmployees());
    }

    @PostMapping // Create an employee
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee created = this.service.createEmployee(employee);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/managers")
    public ResponseEntity<List<Employee>> getAllManagers() {
        return ResponseEntity.ok(this.service.getAllManagers());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return this.service.getEmployee(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Employee> getEmployeeByEmail(@PathVariable String email) {
        return this.service.getEmployee(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id,
            @RequestBody Employee employee) {
        Employee updated = this.service.updateEmployee(id, employee);
        return ResponseEntity.ok(updated);
    }

    // @TODO: implement company logic somewhere to handle when a manager gets deleted or demoted
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmplyeeById(@PathVariable Long id) {
        this.service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
