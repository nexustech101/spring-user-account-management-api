package com.archtech.store.repository;

import com.archtech.store.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // Spring Data JPA automatically gives you CRUD methods

    // Custom query methods
    Optional<Employee> findById(Long employeeId);
    Optional<Employee> findByEmail(String employeeEmail);

    @Query("SELECT e FROM Employee e WHERE e.isManager = true")
    List<Employee> findAllManagers();

    @Query("SELECT e FROM Employee e WHERE e.manager.id = :managerId")
    List<Employee> findSubordinates(Long managerId);

    boolean existsByEmail(String email);
}
