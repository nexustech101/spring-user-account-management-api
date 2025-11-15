package com.archtech.store.repository;

import com.archtech.store.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // Spring Data JPA automatically gives you CRUD methods

    // Custom query methods
    Optional<Employee> findById(Long employeeId);
    Optional<Employee> findByEmail(String employeeEmail);
    boolean existsByEmail(String email);
}
