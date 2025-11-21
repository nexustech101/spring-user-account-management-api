package com.archtech.store.repository;

import com.archtech.store.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // Spring Data JPA automatically gives you CRUD methods

    // Find by ID and email
    Optional<Employee> findById(Long employeeId);
    Optional<Employee> findByEmail(String employeeEmail);

    // Manager queries
    @Query("SELECT e FROM Employee e WHERE e.isManager = true")
    List<Employee> findAllManagers();

    @Query("SELECT e FROM Employee e WHERE e.manager.id = :managerId")
    List<Employee> findSubordinates(@Param("managerId") Long managerId);

    // Department queries
    List<Employee> findByDept(String dept);

    @Query("SELECT DISTINCT e.dept FROM Employee e WHERE e.dept IS NOT NULL")
    List<String> findAllDepartments();

    // Search queries
    @Query("SELECT e FROM Employee e WHERE " +
           "LOWER(e.employeeName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Employee> searchByNameOrEmail(@Param("searchTerm") String searchTerm);

    @Query("SELECT e FROM Employee e WHERE e.salary BETWEEN :minSalary AND :maxSalary")
    List<Employee> findBySalaryRange(@Param("minSalary") Double minSalary, 
                                      @Param("maxSalary") Double maxSalary);

    // Complex search with multiple filters
    @Query("SELECT e FROM Employee e WHERE " +
           "(:name IS NULL OR LOWER(e.employeeName) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:dept IS NULL OR e.dept = :dept) AND " +
           "(:minSalary IS NULL OR e.salary >= :minSalary) AND " +
           "(:maxSalary IS NULL OR e.salary <= :maxSalary) AND " +
           "(:isManager IS NULL OR e.isManager = :isManager)")
    List<Employee> searchEmployees(@Param("name") String name,
                                   @Param("dept") String dept,
                                   @Param("minSalary") Double minSalary,
                                   @Param("maxSalary") Double maxSalary,
                                   @Param("isManager") Boolean isManager);

    // Validation queries
    boolean existsByEmail(String email);
}
