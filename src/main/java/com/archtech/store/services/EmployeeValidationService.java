package com.archtech.store.services;

import com.archtech.store.exception.CircularManagerReferenceException;
import com.archtech.store.exception.EmployeeNotFoundException;
import com.archtech.store.exception.InvalidManagerAssignmentException;
import com.archtech.store.model.Employee;
import com.archtech.store.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class EmployeeValidationService {
    
    private final EmployeeRepository repository;

    public EmployeeValidationService(EmployeeRepository repository) {
        this.repository = repository;
    }

    /**
     * Validates that a manager assignment is valid (no circular references)
     * @param employeeId The ID of the employee being assigned a manager
     * @param managerId The ID of the proposed manager
     * @throws CircularManagerReferenceException if the assignment creates a cycle
     * @throws EmployeeNotFoundException if manager doesn't exist
     * @throws InvalidManagerAssignmentException if manager is not designated as a manager
     */
    public void validateManagerAssignment(Long employeeId, Long managerId) {
        if (managerId == null) {
            return; // No manager is valid
        }

        // Check if manager exists
        Employee manager = repository.findById(managerId)
                .orElseThrow(() -> new EmployeeNotFoundException(managerId));

        // Check if the manager is actually designated as a manager
        if (!manager.getIsManager()) {
            throw new InvalidManagerAssignmentException(managerId);
        }

        // Check for direct self-reference
        if (employeeId.equals(managerId)) {
            throw new CircularManagerReferenceException(
                "Employee cannot be their own manager");
        }

        // Check for circular reference in the management chain
        if (wouldCreateCycle(employeeId, managerId)) {
            throw new CircularManagerReferenceException(employeeId, managerId);
        }
    }

    /**
     * Checks if assigning managerId to employeeId would create a cycle
     */
    private boolean wouldCreateCycle(Long employeeId, Long managerId) {
        Set<Long> visited = new HashSet<>();
        Long currentId = managerId;

        while (currentId != null) {
            if (currentId.equals(employeeId)) {
                return true; // Cycle detected
            }

            if (visited.contains(currentId)) {
                // Already visited this node, no cycle to the target employee
                break;
            }

            visited.add(currentId);

            Employee current = repository.findById(currentId).orElse(null);
            if (current == null || current.getManager() == null) {
                break;
            }

            currentId = current.getManager().getId();
        }

        return false;
    }

    /**
     * Validates an employee can be promoted to manager
     */
    public void validatePromotion(Long employeeId) {
        Employee employee = repository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        if (employee.getIsManager()) {
            throw new IllegalStateException("Employee " + employeeId + " is already a manager");
        }
    }

    /**
     * Validates an employee can be demoted from manager
     */
    public void validateDemotion(Long employeeId) {
        Employee employee = repository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        if (!employee.getIsManager()) {
            throw new IllegalStateException("Employee " + employeeId + " is not a manager");
        }
    }
}
