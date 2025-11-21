package com.archtech.store.services;

import com.archtech.store.exception.EmployeeNotFoundException;
import com.archtech.store.model.*;
import com.archtech.store.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository repository;
    private final EmployeeValidationService validationService;

    public EmployeeService(EmployeeRepository repository, EmployeeValidationService validationService) {
        this.repository = repository;
        this.validationService = validationService;
    }

    public List<Employee> getAllEmployees() {
        return this.repository.findAll();
    }

    public List<Employee> getAllManagers() {
        return this.repository.findAllManagers();
    }

    public Optional<Employee> getEmployee(Long id) {
        return this.repository.findById(id);
    }

    public Optional<Employee> getEmployee(String email) {
        return this.repository.findByEmail(email);
    }

    public Employee createEmployee(Employee employee) {
        // Validate manager assignment if provided
        if (employee.getManager() != null) {
            // For new employees, we need to save first to get an ID, then validate
            // Or we can validate with a temporary ID = null
            validationService.validateManagerAssignment(
                0L, // New employee doesn't have ID yet
                employee.getManager().getId()
            );
        }
        return this.repository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee updatedData) {
        return repository.findById(id)
                .map(existing -> {
                    // Validate manager assignment if it's being changed
                    if (updatedData.getManager() != null) {
                        Long newManagerId = updatedData.getManager().getId();
                        Long currentManagerId = existing.getManager() != null ? 
                            existing.getManager().getId() : null;
                        
                        // Only validate if manager is actually changing
                        if (!newManagerId.equals(currentManagerId)) {
                            validationService.validateManagerAssignment(id, newManagerId);
                        }
                    }

                    // Apply updates
                    existing.setEmployeeName(updatedData.getEmployeeName());
                    existing.setEmail(updatedData.getEmail());
                    existing.setDept(updatedData.getDept());
                    existing.setSalary(updatedData.getSalary());
                    existing.setManager(updatedData.getManager());
                    existing.setIsManager(updatedData.getIsManager());

                    return repository.save(existing);
                })
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    public void deleteManager(Long managerId) {
        Employee manager = this.repository.findById(managerId)
                .orElseThrow(() -> new EmployeeNotFoundException(managerId));

        List<Employee> subs = this.repository.findSubordinates(managerId);

        if (!subs.isEmpty()) {
            Employee replacement = this.findReplacementManager(managerId);

            for (Employee s : subs) {
                s.setManager(replacement);
                repository.save(s);
            }
        }

        repository.deleteById(managerId);
    }

    private Employee findReplacementManager(Long removedManagerId) {
        // Find all managers except the one being removed
        List<Employee> managers = repository.findAllManagers().stream()
                .filter(m -> !m.getId().equals(removedManagerId))
                .toList();

        if (managers.isEmpty())
            return null;

        return managers.stream()
                .min((a, b) -> Integer.compare(a.getNumSubordinates(), b.getNumSubordinates()))
                .orElse(null);
    }

    public void deleteEmployeeById(Long id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        if (employee.getIsManager()) {
            throw new IllegalStateException("Cannot delete a manager. Use deleteManager endpoint or demote first.");
        }

        repository.deleteById(id);
    }

    // Department operations
    public List<Employee> getEmployeesByDepartment(String dept) {
        return repository.findByDept(dept);
    }

    public List<String> getAllDepartments() {
        return repository.findAllDepartments();
    }

    // Search operations
    public List<Employee> searchEmployees(String name, String dept, 
                                          Double minSalary, Double maxSalary, 
                                          Boolean isManager) {
        return repository.searchEmployees(name, dept, minSalary, maxSalary, isManager);
    }

    public List<Employee> searchByNameOrEmail(String searchTerm) {
        return repository.searchByNameOrEmail(searchTerm);
    }

    // Subordinate operations
    public List<Employee> getSubordinates(Long managerId) {
        return repository.findSubordinates(managerId);
    }

    // Promotion and demotion
    public Employee promoteToManager(Long employeeId) {
        validationService.validatePromotion(employeeId);
        
        Employee employee = repository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        
        employee.setIsManager(true);
        return repository.save(employee);
    }

    public Employee demoteFromManager(Long employeeId) {
        validationService.validateDemotion(employeeId);
        
        Employee employee = repository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        
        // Check if manager has subordinates
        List<Employee> subordinates = repository.findSubordinates(employeeId);
        if (!subordinates.isEmpty()) {
            throw new IllegalStateException(
                "Cannot demote manager with subordinates. Reassign subordinates first.");
        }
        
        employee.setIsManager(false);
        return repository.save(employee);
    }

    // Transfer employee to different manager
    public Employee transferEmployee(Long employeeId, Long newManagerId) {
        Employee employee = repository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        
        // Validate the new manager assignment
        if (newManagerId != null) {
            validationService.validateManagerAssignment(employeeId, newManagerId);
            Employee newManager = repository.findById(newManagerId)
                    .orElseThrow(() -> new EmployeeNotFoundException(newManagerId));
            employee.setManager(newManager);
        } else {
            employee.setManager(null);
        }
        
        return repository.save(employee);
    }

    // Get reporting hierarchy (chain of command)
    public List<Employee> getReportingHierarchy(Long employeeId) {
        Employee employee = repository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        
        List<Employee> hierarchy = new ArrayList<>();
        hierarchy.add(employee);
        
        Employee current = employee.getManager();
        while (current != null) {
            hierarchy.add(current);
            current = current.getManager();
        }
        
        return hierarchy;
    }

    // Pagination support
    public Page<Employee> getAllEmployeesPaged(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
