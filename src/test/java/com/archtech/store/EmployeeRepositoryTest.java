package com.archtech.store;

import com.archtech.store.model.Employee;
import com.archtech.store.repository.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository repo;

    @Test
    void testSaveAndFindByEmail() {
        Employee emp = new Employee("Alice", "alice@example.com", "HR", 50000.0, null, false);

        repo.save(emp);

        Optional<Employee> result = repo.findByEmail("alice@example.com");

        assertTrue(result.isPresent());
        assertEquals("Alice", result.get().getEmployeeName());
    }

    @Test
    void testExistsByEmail() {
        Employee emp = new Employee("Alice", "alice@example.com", "HR", 50000.0, null, false);

        repo.save(emp);

        assertTrue(repo.existsByEmail("alice@example.com"));
        assertFalse(repo.existsByEmail("bob@example.com"));
    }

    @Test
    void testEmployeeFields() {
        Employee e = new Employee("Alice", "alice@example.com", "HR", 50000.0, null, false);

        assertEquals("Alice", e.getEmployeeName());
        assertEquals("HR", e.getDept());
    }
}
