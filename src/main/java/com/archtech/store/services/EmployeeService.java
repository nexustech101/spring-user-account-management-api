package com.archtech.store.services;

import com.archtech.store.model.*;
import com.archtech.store.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
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
        return this.repository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee updatedData) {
        return repository.findById(id)
                .map(existing -> {

                    // Apply updates
                    existing.setEmployeeName(updatedData.getEmployeeName());
                    existing.setEmail(updatedData.getEmail());
                    existing.setDept(updatedData.getDept());
                    existing.setSalary(updatedData.getSalary());
                    existing.setManager(updatedData.getManager());
                    existing.setIsManager(updatedData.getIsManager());

                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Employee not found with id " + id));
    }

    public void deleteManager(Long managerId) {
        Employee manager = this.repository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        List<Employee> subs = this.repository.findSubordinates(managerId);

        if (!subs.isEmpty()) {
            Employee replacement = this.findReplacementManager(managerId);

            for (Employee s : subs) {
                s.setManager(replacement);
                repository.save(s);
            }

            if (replacement != null && replacement.getIsManager()) {
                replacement.setNumSubordinates(repository.findSubordinates(replacement.getId()).size());
                repository.save(replacement);
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
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (employee.getIsManager()) {
            throw new IllegalStateException("Cannot delete a manager. Reassign or downgrade first.");
        }

        repository.deleteById(id);
    }
}
